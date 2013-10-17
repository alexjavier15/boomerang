package epfl.sweng.editquestions;

import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class AnswerHolder {

    private Button mCheckButton;
    private EditText mAnswerText;
    private Button mRemoveButton;

    public EditText getAnswerText() {
        return mAnswerText;
    }

    public Button getCheckButton() {
        return mCheckButton;
    }

    public Button getRemoveButton() {
        return mRemoveButton;
    }

    public void setAnswerText(EditText text) {
        mAnswerText = text;
    }

    public void setCheckButton(Button button) {
        mCheckButton = button;
    }

    public void setRemoveButton(Button button) {
        mRemoveButton = button;
    }
    

}
