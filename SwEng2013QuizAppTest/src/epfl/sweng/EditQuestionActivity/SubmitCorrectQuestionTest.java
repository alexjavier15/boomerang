package epfl.sweng.EditQuestionActivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SubmitCorrectQuestionTest extends EditQuestionActivityTemplate {

    private String answer1 = "a cause de la cigarrette";
    private String answer2 = "de naissance";
    private String question = "Pourquoi suis je si con?";
    private Button submit;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockHttpClient mock = new MockHttpClient();
        mock.pushCannedResponse("POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
                HttpStatus.SC_CREATED, "{\"question\": \"Pourquoi je suis si con?\","
                        + " \"answers\": [\"A cause de la cigarette\", \"Je ne suis pas con\", \"De naissance\"],"
                        + " \"solutionIndex\": 1, \"tags\": [\"stupid\", \"alex\"], \"id\": \"-1\" }",
                "application/json");
        SwengHttpClientFactory.setInstance(mock);


    }

    public void test1SubmitSingleQuestion() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());

        getSolo().clickOnButton(0);
        getSolo().enterText(getSolo().getEditText(0), question);

        getSolo().enterText(1, answer2);
        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        EditText et = getSolo().getEditText(2);
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et, answer1);
        getSolo().enterText(getSolo().getEditText(3), "debile, alex");
      
        clickAndWaitForButton(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");

    }
}
