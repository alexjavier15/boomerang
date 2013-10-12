package epfl.sweng.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import epfl.sweng.testing.Debug;

/**
 * 
 * @author Noortch
 * 
 *         This class represents a question.
 */
public class QuizQuestion implements QuestionProvider {

	private long id;
	private String question;
	private List<String> answers;
	private int solutionIndex;
	private Set<String> tags;
	private int minimumNumberParameter;

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
		this.answers = ans;
		this.solutionIndex = solIndex;
		this.tags = tag;
	}
	/**
	 * Constructor for QuizQuestion.
	 * 
	 * The order must be :
	 * 		1) id - int
	 * 		2) question - String
	 * 		3) number of answers - int
	 * 		4) answers - String
	 * 		5) solutionIndex - int
	 * 		6) number of tags - int
	 * 		7) tags - String
	 * 
	 * @param tabQuestion
	 * 			String tab with right parameters
	 */
	public QuizQuestion(String[] tabQuestion) {
		if (tabQuestion.length >= minimumNumberParameter) {
			int pos = 0;
			try {
				Debug.out("tabQuestion length : " + tabQuestion.length);
				id = Long.parseLong(tabQuestion[pos++]);
				Debug.out(pos);
				question = tabQuestion[pos++];
				Debug.out(question);
				int answersSize = Integer.parseInt(tabQuestion[pos++]);
				Debug.out("answersize : " + answersSize);
				for (int i = 0; i < answersSize; i++) {
					Debug.out(tabQuestion[pos]);
					Debug.out(tabQuestion[pos + 1]);
					answers.add(tabQuestion[pos++]);
					Debug.out("pass pos" + pos);
				}
				solutionIndex = Integer.parseInt(tabQuestion[pos++]);
				int tagsSize = Integer.parseInt(tabQuestion[pos++]);
				for (int i = 0; i < tagsSize; i++) {
					tags.add(tabQuestion[pos++]);
				}
			} catch (NumberFormatException e) {
			} catch (IndexOutOfBoundsException e) {
			}
		} else {
			System.out.println("This is not a valid question!");
		}
	}
	
	/**
	 * Takes a QuizQuestion and changes it into a String array.
	 * 
	 * The elements in the new array are in the following order:
	 * 		1) id - int
	 * 		2) question - String
	 * 		3) number of answers - int
	 * 		4) answers - String
	 * 		5) solutionIndex - int
	 * 		6) number of tags - int
	 * 		7) tags - String
	 * 
	 * @param quizQuestion
	 * 			the one to be transformed
	 * @return
	 */
	public String[] getTabQuestion(QuizQuestion quizQuestion) {
		ArrayList<String> tabQuestion = new ArrayList<String>();
		tabQuestion.add(quizQuestion.getID()+"");
		tabQuestion.add(quizQuestion.getQuestion());
		tabQuestion.add(quizQuestion.getAnswers().size()+"");
		tabQuestion.addAll(quizQuestion.getAnswers());
		tabQuestion.add(quizQuestion.getIndex()+"");
		tabQuestion.add(quizQuestion.getSetOfTags().size()+"");
		tabQuestion.addAll(quizQuestion.getSetOfTags());
		return (String[]) tabQuestion.toArray(new String[tabQuestion.size()]);
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
