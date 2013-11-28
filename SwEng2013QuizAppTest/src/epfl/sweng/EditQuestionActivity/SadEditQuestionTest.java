package epfl.sweng.EditQuestionActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * 
 * @author LorenzoLeon
 * 
 */
public class SadEditQuestionTest extends EditQuestionActivityTemplate {

    private String errorMessage = EditQuestionActivity.ERROR_MESSAGE;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockHttpClient mock = new MockHttpClient();
        mock.pushCannedResponse("POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
                HttpStatus.SC_INTERNAL_SERVER_ERROR, "{\"question\": \"What did I do?\","
                        + " \"answers\": [\"A lot, for once!\", \"Nothing, like usually!\"],"
                        + " \"solutionIndex\": 0, \"tags\": [\"stupid\", \"me\"], \"id\": \"-1\" }",
                "application/json");
        SwengHttpClientFactory.setInstance(mock);

    }

    public void testServerNotAccessible() {

        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        getSolo().enterText(getSolo().getEditText("text body"), "What did I do?");
        getSolo().enterText(getSolo().getEditText("tags"), "Stupid, me");
        getSolo().enterText(getSolo().getEditText("answer"), "A lot, for this time!");
        // Select the right answer
        getSolo().clickOnButton(0);
        getSolo().clickOnButton("+");
        getSolo().enterText(getSolo().getEditText("answer"), "Nothing, like usually");

        assertTrue(getSolo().getButton("Submit").isEnabled());

        clickAndWaitForButton(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");

        assertTrue("Submission not successful", getSolo().searchText(errorMessage));

    }
}
