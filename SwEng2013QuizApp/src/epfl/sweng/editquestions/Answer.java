package epfl.sweng.editquestions;

public class Answer {
	private String checked;
	private String answer;
	private String removed;

	public Answer(String checked, String answer, String removed) {
		this.checked = checked;
		this.answer = answer;
		this.removed = removed;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getRemoved() {
		return removed;
	}

	public void setRemoved(String removed) {
		this.removed = removed;
	}
}
