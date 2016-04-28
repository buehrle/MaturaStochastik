package at.leoflo.maturastochastik;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class PollServer extends Thread {
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private ArrayList<Client> clients;
	
	public PollServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("Failed to open port.");
		}
		
		executor = Executors.newCachedThreadPool();
		clients = new ArrayList<Client>();
	}
	
	@Override
	public void run() { // Waits for new clients
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Socket clientSocket = serverSocket.accept();
				Client client = new Client(clientSocket);
				
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
