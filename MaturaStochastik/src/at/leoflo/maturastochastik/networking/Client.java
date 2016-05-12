package at.leoflo.maturastochastik.networking;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import at.leoflo.maturastochastik.networking.streams.CompositeInputStream;
import at.leoflo.maturastochastik.networking.streams.CompositeOutputStream;

public class Client implements Runnable, RequestTable {
	private static final int MAX_COUNTS_UNTIL_TIMEOUT = 100;
	private static final int SLEEPING_TIME = 10;
	
	private static ArrayList<Client> clients;
	
	private Socket clientSocket;
	private int timeoutCounter;
	
	private CompositeInputStream input;
	private CompositeOutputStream output;
	
	private HashMap<Integer, String> topics;
	private int[] currentlyOpenTopics;
	private PollCoordinator coordinator;
	
	private final int relapseTime;
	private int relapseCounter;
	
	private final Random random;
	
	static {
		clients = new ArrayList<Client>();
	}
	
	public Client(Socket clientSocket, HashMap<Integer, String> topics, PollCoordinator coordinator, int relapseTime) {
		this.clientSocket = clientSocket;
		this.topics = topics;
		this.coordinator = coordinator;
		
		this.relapseTime = relapseTime;
		
		timeoutCounter = 0;
		relapseCounter = 0;
		
		random = new Random();
		currentlyOpenTopics = new int[2];
		
		getClientList().add(this); // Client adds itself to the global list
		
		synchronized (coordinator) { // Thread-safety
			coordinator.clientCountUpdate(getClientList().size());
		}
	}

	@Override
	public void run() {
		try {
			init();
			
			while(!Thread.currentThread().isInterrupted()) {
				if (timeoutCounter < MAX_COUNTS_UNTIL_TIMEOUT) {
					if (relapseCounter > relapseTime / SLEEPING_TIME) { //Time exceeded!
						output.writeInt(TIME_RELAPSE);
						
						send2Topics();
						
						relapseCounter = 0;
					}
					
					if (input.available() > 0) { //Client sends stuff to us
						int request = input.readInt();
						
						switch (request) {
							case SELECTION_COMPLETED:
								int selected = input.readInt();
								// check if selected topic is allowed
								synchronized (coordinator) {
									if (currentlyOpenTopics[0] == selected || currentlyOpenTopics[1] == selected) coordinator.topicIncreased(selected);
								}
								
								send2Topics();
								
								relapseCounter = 0;
								break;
							case REGULAR_DISCONNECT:
								performShutdown();
								break;
							case HEARTBEAT:
								break;
							default:
								output.writeInt(ILLEGAL_REQUEST);
								performShutdown();
								break;
						}
						
						timeoutCounter = 0;
					} else {
						timeoutCounter++;
					}
					
					relapseCounter++;
					
					if (!Thread.currentThread().isInterrupted()) Thread.sleep(SLEEPING_TIME);
				} else {
					performShutdown();
				}
			}
		} catch (Exception e) {
			try {
				output.writeInt(UNEXPECTED_ERROR);
			} catch (Exception e1) {}
		} finally {
			try {
				input.close();
				output.close();
				clientSocket.close();
			} catch (IOException e) {
			} finally {
				getClientList().remove(this);
				
				synchronized (coordinator) {
					coordinator.clientCountUpdate(getClientList().size());
				}
			}
		}
	}
	
	public void send2Topics() throws IOException {
		ArrayList<Integer> keys = new ArrayList<Integer>(topics.keySet());
		
		currentlyOpenTopics[0] = keys.get(random.nextInt(keys.size()));
		currentlyOpenTopics[1] = keys.get(random.nextInt(keys.size()));
		
		output.writeInt(UPDATE_POLL_DATA); // Tell the client that 
		
		output.writeInt(currentlyOpenTopics[0]);
		output.writeString(topics.get(currentlyOpenTopics[0]));
		
		output.writeInt(currentlyOpenTopics[1]);
		output.writeString(topics.get(currentlyOpenTopics[1]));
	}
	
	private void init() throws IOException {
		input = new CompositeInputStream(clientSocket.getInputStream(), StandardCharsets.UTF_8);
		output = new CompositeOutputStream(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
		
		output.writeInt(relapseTime); // Time in seconds until relapse
		
		send2Topics();
	}

	private void performShutdown() {
		Thread.currentThread().interrupt();
	}
	
	public static synchronized ArrayList<Client> getClientList() { // Prevent ugly thread conflicts.
		return clients;
	}
}
