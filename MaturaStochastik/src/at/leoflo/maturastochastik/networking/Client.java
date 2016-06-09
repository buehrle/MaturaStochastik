package at.leoflo.maturastochastik.networking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import at.leoflo.maturastochastik.networking.streams.CompositeInputStream;
import at.leoflo.maturastochastik.networking.streams.CompositeOutputStream;

public class Client extends Thread implements RequestTable {
	
	private Socket clientSocket;
	
	private CompositeInputStream input;
	private CompositeOutputStream output;
	
	private int[] receivedID;
	private String[] receivedTopicString;
	
	private int relapseTime;

	private ClientCommunicator cc;
	
	public Client(String address, ClientCommunicator cc) throws UnknownHostException, IOException {

		System.out.println("Connecting");
		this.clientSocket = new Socket(address, 10000);
		
		receivedID = new int[2];
		receivedTopicString = new String[2];

		this.cc = cc;
		
	}
	
	//TODO: Send Selection Completed, Heartbeats, 
	
	
	public void run() {
		
		this.init();
		try {
			
			while(!Thread.currentThread().isInterrupted()) {
				if (input.available() > 0) {
					int request = input.readInt();
				
					switch (request) {
						case UPDATE_POLL_DATA:
							//Receive the data
							receivedID[0] = input.readInt();
							receivedTopicString[0] = input.readString();
							
							receivedID[1] = input.readInt();
							receivedTopicString[1] = input.readString();

							cc.updateQuestions(receivedID, receivedTopicString);
							cc.resetTimer();
							break;
						
						case SERVER_CLOSED:
							this.interrupt();
							System.err.println("Server Closed - Disconnecting");
							break;
						case TIME_RELAPSE:
							cc.resetTimer();
							break;
						case UNEXPECTED_ERROR:
							this.interrupt();
							System.err.println("Unexpected Error - Disconnecting");
							break;
						case ILLEGAL_REQUEST:
							this.interrupt();
							System.err.println("Illegal Request - Disconnecting");
							break;
					}
				}
				
				Thread.sleep(50);
				
				synchronized (this) {
					output.writeInt(HEARTBEAT);
					output.flush();
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}
	
	public synchronized void sendResult(int selectedID) {
		try {
			output.writeInt(SELECTION_COMPLETED);
			output.flush();
			output.writeInt(selectedID);
			output.flush();
			
			System.out.println("SELECTION");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void init() {
		try {
			input = new CompositeInputStream(clientSocket.getInputStream(), StandardCharsets.UTF_8);
			output = new CompositeOutputStream(clientSocket.getOutputStream(), StandardCharsets.UTF_8);
			
			relapseTime = input.readInt();
			System.out.println(relapseTime);
			cc.connected();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			input.close();
			output.close();
			
			cc.disconnected();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getRelapseTime() {
		return relapseTime;
	}

	public void setRelapseTime(int relapseTime) {
		this.relapseTime = relapseTime;
	}
}
