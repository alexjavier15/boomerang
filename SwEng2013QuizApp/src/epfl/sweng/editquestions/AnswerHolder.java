package epfl.sweng.editquestions;

import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class AnswerHolder {
    private Button checkButton;
    private EditText answerText;
    private Button removeButton;

    public Button getCheckButton() {
        return checkButton;
    }

    public void setCheckButton(Button button) {
        checkButton = button;
    }

    public EditText getAnswerText() {
        return answerText;
    }

    public void setAnswerText(EditText text) {
        answerText = text;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public void setRemoveButton(Button button) {
        removeButton = button;
    }

}
