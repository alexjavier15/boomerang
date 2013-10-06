package epfl.sweng.editquestions;

/**
 * 
 * @author CanGuzelhan
 *
 */
public class Answer {
	private String checked;
	private String answer;
	private String removed;

	public Answer(String checked, String answer, String removed) {
		super();
		this.checked = checked;
		this.answer = answer;
		this.removed = removed;
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
		this.answer = receivedAnswer;
	}

	public String getRemoved() {
		return removed;
	}

	public void setRemoved(String receivedRemoved) {
		this.removed = receivedRemoved;
	}
}
