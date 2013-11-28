package epfl.sweng.EditQuestionActivity;

import android.widget.Button;
import android.widget.EditText;

import epfl.sweng.testing.TestCoordinator.TTChecks;

public class CheckAndRemoveCorrectAnswer extends EditQuestionActivityTemplate {

    private String answer1 = "a cause de la cigarrette";
    private String answer2 = "de naissance";
    private String question = "Pourquoi suis je si con?";
    private Button submit;

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.EditQuestionActivity.EditQuestionActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
    }

    public void testCheckAndRemoveCorrectSubmitDisabled() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());
        // Check first answer as correct
        getSolo().clickOnButton(0);
        getSolo().enterText(getSolo().getEditText(0), question);

        getSolo().enterText(1, answer2);

        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        EditText et = getSolo().getEditText(2);
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et, answer1);

        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        EditText et1 = getSolo().getEditText(3);
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et1, answer1);

        getSolo().enterText(getSolo().getEditText(4), "debile, alex");

        assertTrue("submit button must be enabled ", submit.isEnabled());
        getSolo().clickOnButton(1);
        assertFalse("submit button must be disabled ", submit.isEnabled());
    }
}
