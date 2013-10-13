package epfl.sweng.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Intent;

/**
 * 
 * @author Noortch
 * 
 *         This class represents a question.
 */
public class QuizQuestion implements QuestionProvider {

	private long id;
	private String question;
	private ArrayList<String> answers;
	private int solutionIndex;
	private Set<String> tags;
	/**
	 * Constructor of a QuizQuestion : class to modelize a quiz question at the
	 * json format.
	 * 
	 * @param iD
	 *            of the question
	 * @param quest
	 *            String
	 * @param ans
	 *            List of String
	 * @param solIndex
	 *            int
	 * @param tag
	 *            themas of the question
	 */
	public QuizQuestion(long iD, String quest, List<String> ans, int solIndex,
			Set<String> tag) {
		this.id = iD;
		this.question = quest;
		this.answers = new ArrayList<String>(ans);
		this.solutionIndex = solIndex;
		this.tags = tag;
	}

	/**
	 * Constructor for QuizQuestion.
	 * 
	 * The order must be : 1) id - int 2) question - String 3) number of answers
	 * - int 4) answers - String 5) solutionIndex - int 6) number of tags - int
	 * 7) tags - String
	 * 
	 * @param tabQuestion
	 *            String tab with right parameters
	 */

	public QuizQuestion(Intent intent) {
		this.id = intent.getLongExtra("id", 0);
		this.question = intent.getStringExtra("question");
		this.answers = intent.getStringArrayListExtra("answers");
		this.solutionIndex = intent.getIntExtra("index", 0);
		this.tags = new HashSet<String>(Arrays.asList(intent
				.getStringArrayExtra("tags")));
	}

	/**
	 * Takes an Intent and adds as Extra data bundle all information related to
	 * this QuizQuestion.
	 * 
	 * The elements in the new bundle are mapped to their names as in the
	 * private fields :
	 * 
	 * @param quizQuestion
	 *            the one to be transformed
	 * @return
	 */
	public void addExtraDatatoIntent(Intent intent) {
		intent.putExtra("id", this.id);
		intent.putExtra("question", this.question);
		intent.putStringArrayListExtra("answers", answers);
		intent.putExtra("index", this.solutionIndex);
		intent.putExtra("tags", this.tags.toArray(new String[0]));
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
