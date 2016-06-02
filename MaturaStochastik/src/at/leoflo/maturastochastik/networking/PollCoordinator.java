package at.leoflo.maturastochastik.networking;

public interface PollCoordinator {
	public void topicIncreased(int topic);
	public void clientCountUpdate(int count);
}
