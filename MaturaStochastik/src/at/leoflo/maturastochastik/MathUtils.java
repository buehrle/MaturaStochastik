package at.leoflo.maturastochastik;

import java.util.ArrayList;
import java.util.Random;

public class MathUtils {
	/**
	 * 
	 * @param topics The ArrayList of topics that will be modified and used for this method
	 * @param participants The amount of participants choosing between topics
	 * @param iterations The amount of iterations in which every participant chooses one of two topics.
	 * @return An ArrayList of numbers for each Topic in the order
	 */
	public static void stochasticate(ArrayList<Topic> topics, int participants, int iterations, int percentage) {
		for (Topic topic : topics) {
			topic.setChosenPerIteration(new int[iterations]);
		}
		
		for (int i = 0; i < iterations; i++) {
			for (int j = 0; j < participants; j++) {
				Random r = new Random();
				Topic topic1 = topics.get(r.nextInt(topics.size()));
				Topic topic2 = topics.get(r.nextInt(topics.size()));
				
				r = new Random(r.nextLong());
				
				int popularitySum = topic1.getPopularity() + topic2.getPopularity();
				int randomChosen = r.nextInt(popularitySum);
				
				if (randomChosen <= topic1.getPopularity()) {
					topic1.setAmountChosen(topic1.getAmountChosen() + 1);
					topic1.incrementChosenPerIteration(i, 1);
				} else {
					topic2.setAmountChosen(topic2.getAmountChosen() + 1);
					topic2.incrementChosenPerIteration(i, 1);
				}
			}
		}
		
		for (Topic t : topics) {
			int[] topicCounter = new int[participants + 1];
			
			for (int i = 0; i < iterations; i++) {
				topicCounter[t.getChosenPerIteration()[i]]++;
			}
			
			double topicIterator = 0;
			
			for (int i = 0; i <= participants; i++) {
				topicIterator += topicCounter[i];
				
				double percentageL = (topicIterator / iterations) * 100;
				if (percentageL >= percentage) {
					t.setAmountQuestions(i);
					t.setPercentage(percentageL);
					break;
				}
			}
			
			//t.setAmountQuestions(participants);
		}
	}
}
