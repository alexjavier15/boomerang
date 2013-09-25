package epfl.sweng.servercomm;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Noortch
 *
 */
public class QuizQuestions implements QuizQuestion {

	private int id;
	private String question;
	private List<String> answers;
	private int solutionIndex;
	private Set<String> tags;

	public QuizQuestions(int id, String question, List<String> answers, int solIndex, Set<String> tags) {
		this.id = id;
		this.question = question;
		this.answers = answers;
		this.solutionIndex = solIndex;
		this.tags = tags;
	}

	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public String getAnswer() {
		return answers.get(solutionIndex);
	}

}
