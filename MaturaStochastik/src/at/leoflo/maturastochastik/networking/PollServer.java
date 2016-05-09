package at.leoflo.maturastochastik.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class PollServer extends Thread {
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private ArrayList<Client> clients;
	private HashMap<Integer, String> topics;
	private PollCoordinator coordinator;
	private final int relapseTime;
	
	public PollServer(int port, HashMap<Integer, String> topics, PollCoordinator coordinator, int relapseTime) {
		try {
			serverSocket = new ServerSocket(port);
			
			executor = Executors.newCachedThreadPool();
			clients = new ArrayList<Client>();
			
			this.topics = topics;
			this.relapseTime = relapseTime;
		} catch (IOException e) {
			throw new RuntimeException("Failed to open port.");
		}
	}
	
	@Override
	public void run() { // Waits for new clients
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Socket clientSocket = serverSocket.accept();
				Client client = new Client(clientSocket, topics, coordinator, relapseTime);
				
				clients.add(client);
				executor.execute(client);
			} catch (IOException e) {
				System.err.println("Error while accepting client.");
			}
		}
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		
		// Disconnect all clients
	}
	
}
