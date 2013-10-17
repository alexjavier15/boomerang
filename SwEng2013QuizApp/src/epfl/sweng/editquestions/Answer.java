package epfl.sweng.editquestions;

import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * 
 * @author CanGuzelhan
 * 
 *         This class represents an answer slot.
 * 
 */
public class Answer {
	private String checked= null;
	private String answer = null;
	private boolean mCorrect = false;

	/**
	 * Each answer the user types is represented by an Object of type Answer. The user can modify the text and
	 * choose
	 * whether this answer is the correct one or not.
	 * 
	 * @param check
	 *                The property of the answer: it might be marked as correct (✔) or incorrect (✘).
	 * @param answerText
	 *                The text of the answer that the user can enter.
	 * 
	 */

	public Answer() {
		super();

	}

	public Answer(String check, String answerText) {
		super();
		checked = check;
		answer = answerText;
	}

	public String getAnswer() {
		return answer;
	}

	public String getChecked() {
		return checked;
	}

	public void setAnswer(String receivedAnswer) {
		if (!receivedAnswer.equals(answer)) {
			TestCoordinator.check(TTChecks.QUESTION_EDITED);
			answer = receivedAnswer;
		}
	}

	public boolean isCorrect() {

		return mCorrect;
	}

	public void setCorrect(boolean isCorrect) {

		mCorrect = isCorrect;
	}

	public void setChecked(String receivedChecked) {
		checked = receivedChecked;
	}

	// For test only
	@Override
	public String toString() {
		return "Answer: \nchecked -> " + checked + " \nanswer -> " + answer + " \n" + this.hashCode();
	}
}
