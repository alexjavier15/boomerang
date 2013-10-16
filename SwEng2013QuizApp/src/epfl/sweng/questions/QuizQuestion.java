package epfl.sweng.questions;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Noortch
 * 
 *         This class represents a question.
 */
public class QuizQuestion implements QuestionProvider {

	private long id;
	private String question;
	private ArrayList<String> answers = new ArrayList<String>();
	private int solutionIndex;
	private List<String> tags = new ArrayList<String>();

	/**
	 * Constructor of a QuizQuestion : class to modelize a quiz question at the json format.
	 * 
	 * @param iD
	 *                of the question
	 * @param quest
	 *                String
	 * @param ans
	 *                List of String
	 * @param solIndex
	 *                int
	 * @param tags
	 *                themas of the question
	 */
	public QuizQuestion(long iD, String quest, List<String> ans, int solIndex, List<String> tag) {
		id = iD;
		question = quest;
		answers = new ArrayList<String>(ans);
		solutionIndex = solIndex;
		tags = tag;
	}

	@Override
	public boolean checkAnswer(int sol) {
		return sol == solutionIndex;
	}

	public List<String> getAnswers() {
		return answers;
	}

	@Override
	public String getCorrectAnswer() {
		return answers.get(solutionIndex);
	}

	public long getID() {
		return id;
	}

	public int getIndex() {
		return solutionIndex;
	}

	public String getQuestion() {
		return question;
	}

	public List<String> getTags() {
		return tags;
	}

}
