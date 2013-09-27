package epfl.sweng.questions;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Noortch
 * 
 */
public class QuizQuestion implements QuestionProvider {

	private int id;
	private String question;
	private List<String> answers;
	private int solutionIndex;
	private Set<String> tags;

	public QuizQuestion(int id, String question, List<String> answers,
			int solIndex, Set<String> tags) {
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

	@Override
	public List<String> getAnswerList() {
		return answers;
	}

	@Override
	public boolean checkAnswer(int sol) {
		return sol == solutionIndex;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public int getIndex() {
		return solutionIndex;
	}

	@Override
	public Set<String> getSetOfTags() {
		return tags;
	}

}
