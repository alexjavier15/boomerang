package epfl.sweng.editquestionactivity;

import android.widget.Button;
import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class EditQuestionsActivityUITest extends EditQuestionActivityTemplate {

    private Button submit;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void test2ButtonsMustBeDisplayed() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        assertTrue("EditText for the question", getSolo().searchEditText("text body"));
        assertTrue("EditText for an answer", getSolo().searchEditText("answer"));
        assertTrue("Button to remove answer", getSolo().searchButton("-"));
        assertTrue("Button to add an anser", getSolo().searchButton("+"));
        assertTrue("EditText for the tags", getSolo().searchEditText("tags"));
        assertTrue("Button to check correct answer", getSolo().searchButton(getActivity().getResources().getString(R.string.heavy_ballot_x)));
        assertTrue("Button to submit", getSolo().searchButton("Submit"));

        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());
        getActivity().finish();

    }

}
