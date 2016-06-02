package at.leoflo.maturastochastik.networking;

public interface ClientCommunicator {
	public void updateQuestions(int[] ID, String[] questions);
	public void resetTimer();
	public void connected();
	public void disconnected();
}
