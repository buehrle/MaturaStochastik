package at.leoflo.maturastochastik;

public class Topic {
	private String name;
	private int popularity;
	private int amountChosen;
	private int amountQuestions;
	private int[] chosenPerIteration;
	private double percentage;
	
	public int getAmountQuestions() {
		return amountQuestions;
	}

	public void setAmountQuestions(int amountQuestions) {
		this.amountQuestions = amountQuestions;
	}

	public Topic(String name, int popularity) {
		this.name = name;
		this.popularity = popularity;
		this.amountChosen = 0;
		this.amountQuestions = 0;
		this.percentage = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getAmountChosen() {
		return amountChosen;
	}

	public void setAmountChosen(int amountChosen) {
		this.amountChosen = amountChosen;
	}

	public int[] getChosenPerIteration() {
		return chosenPerIteration;
	}
	
	public void incrementChosenPerIteration(int iteration, int incrementor) {
		chosenPerIteration[iteration] = chosenPerIteration[iteration] + incrementor;
	}

	public void setChosenPerIteration(int[] chosenPerIteration) {
		this.chosenPerIteration = chosenPerIteration;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
