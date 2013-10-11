package epfl.sweng.questions;

import java.util.List;
import java.util.Set;

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
	private int numberParameter;

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

	public QuizQuestion(String[] tabQuestion) {
		int idPlace = 0;
		int questionPlace = 1;
		int answersSizePlace = 2;
		int answersPlace = 3;
		int solutionIndexPlace = 3 + answersSizePlace;
		int tagsSizePlace = 4 + answersSizePlace;
		int tagsPlace = 5 + answersSizePlace;
		if (tabQuestion.length >= numberParameter) {
			try {
				id = Integer.parseInt(tabQuestion[idPlace]);
				question = tabQuestion[questionPlace];
				int answersSize = Integer.parseInt(tabQuestion[answersSizePlace]);
				for (int i = 0; i < answersSize; i++) {
					answers.add(tabQuestion[answersPlace + i]);
				}
				solutionIndex = Integer.parseInt(tabQuestion[solutionIndexPlace]);
				int tagsSize = Integer.parseInt(tabQuestion[tagsSizePlace]);
				for (int i = 0; i < tagsSize; i++) {
					tags.add(tabQuestion[tagsPlace + i]);
				}
			} catch (NumberFormatException e) {
			} catch (IndexOutOfBoundsException e) {
			}
		} else {
			System.out.println("This is not a valid question!");
		}
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
