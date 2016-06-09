package at.leoflo.maturastochastik.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollServer extends Thread {
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private HashMap<Integer, String> topics;
	private PollCoordinator coordinator;
	private final int relapseTime;
	
	public PollServer(int port, HashMap<Integer, String> topics, PollCoordinator coordinator, int relapseTime) {
		try {
			serverSocket = new ServerSocket(port);
			
			executor = Executors.newCachedThreadPool();
			
			this.topics = topics;
			this.relapseTime = relapseTime;
			
			this.coordinator = coordinator;
		} catch (IOException e) {
			throw new RuntimeException("Failed to open port.");
		}
	}
	
	@Override
	public void run() { // Waits for new clients
		while (!Thread.currentThread().isInterrupted() && !serverSocket.isClosed()) {
			try {
				Socket clientSocket = serverSocket.accept();
				ClientManager client = new ClientManager(clientSocket, topics, coordinator, relapseTime);
				
				executor.execute(client);
				
				System.out.println("Accepted");
			} catch (AlreadyLoggedInException alie) {
				System.err.println("CLIENT IST BEREITS EINGELOGGT! ALARM! ALARM!");
			} catch (IOException e) {
				System.err.println("Error while accepting client.");
			}
		}
	}
	
	@Override
	public void interrupt() {
		super.interrupt();
		
		executor.shutdownNow();
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
