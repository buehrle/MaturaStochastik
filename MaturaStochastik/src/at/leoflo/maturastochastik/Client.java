package at.leoflo.maturastochastik;

import java.net.Socket;

public class Client implements Runnable {
	private Socket clientSocket; 
	
	public Client(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		
		
		while(!Thread.currentThread().isInterrupted()) {
			
		}
	}

}
