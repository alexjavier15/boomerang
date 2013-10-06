package epfl.sweng.questions;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Noortch
 * 
 * 			This class represents a question.
 */
public class QuizQuestion implements QuestionProvider {

	private long id;
	private String question;
	private List<String> answers;
	private int solutionIndex;
	private Set<String> tags;

	/**
	 * Constructor of a QuizQuestion : class to modelize a
	 * quiz question at the json format.
	 * @param id
	 * 			of the question
	 * @param question
	 * 				String
	 * @param answers
	 * 				List of String
	 * @param solIndex
	 * 				int
	 * @param tags
	 * 			themas of the question
	 */
	public QuizQuestion(long iD, String quest, List<String> ans,
			int solIndex, Set<String> tag) {
		this.id = iD;
		this.question = quest;
		this.answers = ans;
		this.solutionIndex = solIndex;
		this.tags = tag;
	}

	@Override
	public String getQuestion() {
		return question;
	}

	@Override
	public String getCorrectAnswer() {
		return answers.get(solutionIndex);
	}

	@Override
	public List<String> getAnswers() {
		return answers;
	}

	@Override
	public boolean checkAnswer(int sol) {
		return sol == solutionIndex;
	}

	@Override
	public long getID() {
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
