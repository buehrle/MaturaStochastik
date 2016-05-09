package at.leoflo.maturastochastik.networking;

public interface RequestTable {
	public static final int UPDATE_POLL_DATA = 0; // The server tells the client which two selections are coming next
	public static final int SELECTION_COMPLETED = 1; // Is sent to the server when one selection is completed. After that, the server will tell the client the next two possibilities via UPDATE_POLL_DATA
	public static final int SERVER_CLOSED = 2; // When the server is shutting down
	public static final int UNEXPECTED_ERROR = 3; // When an unexpected error occurs on the server
	public static final int ILLEGAL_REQUEST = 4;
	public static final int REGULAR_DISCONNECT = 5; // Client tells the server that it will disconnect
	public static final int HEARTBEAT = 6;
	public static final int TIME_RELAPSE = 7; // Tell the client that the time for one question is UPPPP
}
