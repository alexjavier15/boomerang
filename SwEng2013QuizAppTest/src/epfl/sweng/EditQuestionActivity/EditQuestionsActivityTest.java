package epfl.sweng.EditQuestionActivity;

import android.widget.Button;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class EditQuestionsActivityTest extends EditQuestionActivityTemplate {

    private Button submit;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void test2ButtonsMustBeDisplayed() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        assertTrue("EditText for the question", getSolo().searchText("Type in the question's text body"));
        assertTrue("EditText for an answer", getSolo().searchText("Type in the answer"));
        assertTrue("Button to remove answer", getSolo().searchButton("-"));
        assertTrue("Button to add an anser", getSolo().searchButton("+"));
        assertTrue("EditText for the tags", getSolo().searchText("Type in the question's tags"));
        assertTrue("Button to submit", getSolo().searchButton("Submit"));

        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());

    }

}
