package epfl.sweng.editquestions;

import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class AnswerHolder {

    private EditText mAnswerText;
    private Button mCheckButton;
    private Button mRemoveButton;

    /**
     * Return the the answerText
     * 
     * @return the answerText
     */
    public EditText getAnswerText() {
        return mAnswerText;
    }

    /**
     * Return the the checkButton
     * 
     * @return the checkButton
     */
    public Button getCheckButton() {
        return mCheckButton;
    }

    /**
     * Return the the removeButton
     * 
     * @return the removeButton
     */
    public Button getRemoveButton() {
        return mRemoveButton;
    }

    /**
     * Set the answerText
     * 
     * @param answerText
     *            the answerText to set
     */
    public void setAnswerText(EditText answerText) {
        mAnswerText = answerText;
    }

    /**
     * Set the checkButton
     * 
     * @param checkButton
     *            the checkButton to set
     */
    public void setCheckButton(Button checkButton) {
        mCheckButton = checkButton;
    }

    /**
     * Set the removeButton
     * 
     * @param removeButton
     *            the removeButton to set
     */
    public void setRemoveButton(Button removeButton) {
        mRemoveButton = removeButton;
    }

}
