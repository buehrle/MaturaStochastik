package at.leoflo.fuckingmaturafragenshitausrechnungsshitfotzendrecksprogramm;

public class Topic {
	private String name;
	private int popularity;
	private int amountChosen;
	private double percentage;
	private int amountQuestions;
	private int[] chosenPerIteration;
	private int maxChosen;
	
	public int getAmountQuestions() {
		return amountQuestions;
	}

	public void setAmountQuestions(int amountQuestions) {
		this.amountQuestions = amountQuestions;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public Topic(String name, int popularity) {
		this.name = name;
		this.popularity = popularity;
		this.amountChosen = 0;
		this.percentage = 0;
		this.amountQuestions = 0;
		this.maxChosen = 0;
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

	public void setChosenPerIteration(int[] chosenPerIteration) {
		this.chosenPerIteration = chosenPerIteration;
	}

	public int getMaxChosen() {
		return maxChosen;
	}

	public void setMaxChosen(int maxChosen) {
		this.maxChosen = maxChosen;
	}
}
