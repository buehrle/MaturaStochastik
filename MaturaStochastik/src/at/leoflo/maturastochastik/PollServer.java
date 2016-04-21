package at.leoflo.maturastochastik;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PollServer {
	private ServerSocket serverSocket;
	private ExecutorService executor;
	private ArrayList<Client> clients;
	private Thread serverThread;
	
	public PollServer() {
		try {
			serverSocket = new ServerSocket(69691);
		} catch (IOException e) {
			throw new RuntimeException("Failed to open port.");
		}
		
		executor = Executors.newCachedThreadPool();
		clients = new ArrayList<Client>();
	}
	
	public void startServer() {
		if(serverThread == null ? false : serverThread.isAlive()) {
			throw new RuntimeException("Server already running!");
		}
		
		serverThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Socket clientSocket = serverSocket.accept(); //waits for a connection
					Client client = new Client(clientSocket);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			}
		});
		
		serverThread.start();
	}
	
	public void requestShutdown() {
		if (serverThread != null) serverThread.interrupt();
	}
	
}
