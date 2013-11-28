package epfl.sweng.EditQuestionActivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SubmitCorrectQuestionTest extends EditQuestionActivityTemplate {

    private Button submit;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pushCannedResponse(HttpStatus.SC_CREATED);
        SwengHttpClientFactory.setInstance(getMock());
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();

    }

    public void test1SubmitSingleQuestion() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());

        fillCorrectQuizQuestion();

        clickAndWaitForButton(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");

    }
}
