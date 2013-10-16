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
	private String checked;
	private String answer;
	private String removed;

	/**
	 * Each answer the user types is represented by an Object of type Answer.
	 * The user can modify the text and choose whether this answer is the
	 * correct one or not.
	 * 
	 * @param check
	 *            The property of the answer: it might be marked as correct (✔)
	 *            or incorrect (✘).
	 * @param answerText
	 *            The text of the answer that the user can enter.
	 * @param remove
	 *            The String value of the hyphen minus symbol (-) the button to
	 *            remove this particular answer has.
	 */
	public Answer(String check, String answerText, String remove) {
		super();
		this.checked = check;
		this.answer = answerText;
		this.removed = remove;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String receivedChecked) {
		this.checked = receivedChecked;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String receivedAnswer) {
		if (!receivedAnswer.equals(this.answer)) {
			TestCoordinator.check(TTChecks.QUESTION_EDITED);
			this.answer = receivedAnswer;
		}
	}

	public String getRemoved() {
		return removed;
	}

	public void setRemoved(String receivedRemoved) {
		this.removed = receivedRemoved;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "checked : " + checked + " answer : " + answer; 
	}
	
}
