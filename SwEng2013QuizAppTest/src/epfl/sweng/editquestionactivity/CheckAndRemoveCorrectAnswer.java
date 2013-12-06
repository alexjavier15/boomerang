package epfl.sweng.editquestionactivity;

import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class CheckAndRemoveCorrectAnswer extends EditQuestionActivityTemplate {

    private String answer1 = "a cause de la cigarrette";
    private String answer2 = "de naissance";
    private String question = "Pourquoi suis je si con?";
    private Button submit;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCheckAndRemoveCorrectSubmitDisabled() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());
        
        clickAndWaitForButton(TTChecks.QUESTION_EDITED, getActivity().getResources().getString(R.string.heavy_ballot_x));
        
        EditText q = getSolo().getEditText("text body");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, q, question);
        
        
        EditText et1 = getSolo().getEditText("answer");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et1, answer2);

        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        
        EditText et2 = getSolo().getEditText("answer");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et2, answer1);

        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        
        EditText et3 = getSolo().getEditText("answer");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et3, answer1);
        
        EditText tags = getSolo().getEditText("tags");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, tags, "debile, alex");

        assertTrue("submit button must be enabled ", submit.isEnabled());
        
        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "-");
        
        assertFalse("submit button must be disabled ", submit.isEnabled());
        getActivity().finish();
    }
}
