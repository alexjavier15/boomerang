package epfl.sweng.editquestions;

/**
 * 
 * @author CanGuzelhan
 * 
 *         This class represents an answer slot.
 * 
 */
public class Answer {
    private String answer = "";
    private String checked = "";
    private boolean mCorrect = false;

    /**
     * Each answer the user types is represented by an Object of type Answer. The user can modify the text and choose
     * whether this answer is the correct one or not.
     * 
     * @param check
     *            The property of the answer: it might be marked as correct (heavy) or incorrect (less).
     * @param answerText
     *            The text of the answer that the user can enter.
     * 
     */

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

    public boolean isCorrect() {
        return mCorrect;
    }

    public void setAnswer(String receivedAnswer) {
        answer = receivedAnswer;
    }

    public void setChecked(String receivedChecked) {
        checked = receivedChecked;
    }

    public void setCorrect(boolean isCorrect) {
        mCorrect = isCorrect;
    }

    // For test only
    @Override
    public String toString() {
        return "Answer: \nchecked -> " + checked + " \nanswer -> " + answer + " \n" + this.hashCode();
    }
}
