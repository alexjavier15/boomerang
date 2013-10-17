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
    /**Return the the checkButton
     *
     * @return the checkButton
     */
    public Button getCheckButton() {
        return mCheckButton;
    }
    /**Set the checkButton
     * 
     *@param checkButton the checkButton to set
     */
    public void setCheckButton(Button checkButton) {
        mCheckButton = checkButton;
    }
    /**Return the the answerText
     *
     * @return the answerText
     */
    public EditText getAnswerText() {
        return mAnswerText;
    }
    /**Set the answerText
     * 
     *@param answerText the answerText to set
     */
    public void setAnswerText(EditText answerText) {
        mAnswerText = answerText;
    }
    /**Return the the removeButton
     *
     * @return the removeButton
     */
    public Button getRemoveButton() {
        return mRemoveButton;
    }
    /**Set the removeButton
     * 
     *@param removeButton the removeButton to set
     */
    public void setRemoveButton(Button removeButton) {
        mRemoveButton = removeButton;
    }
   

    

}
