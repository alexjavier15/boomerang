package epfl.sweng.editquestionactivity;

import org.apache.http.HttpStatus;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * 
 * @author LorenzoLeon
 * 
 */
public class SubmitQuestionBadBehaviour extends EditQuestionActivityTemplate {

    private String errorMessage = EditQuestionActivity.ERROR_MESSAGE;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        SwengHttpClientFactory.setInstance(getMock());

    }

    public void testServer500Reponse() {
        pushCannedResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
        fillCorrectQuizQuestion();
        assertTrue(getSolo().getButton("Submit").isEnabled());
        clickAndGetToastAndWaitFor(TTChecks.NEW_QUESTION_SUBMITTED, "Submit", errorMessage);
        popCannedResponse();
        getActivity().finish();

    }
    public void testServer400Reponse() {
        pushCannedResponse(HttpStatus.SC_BAD_REQUEST);

        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
        fillCorrectQuizQuestion();
        assertTrue(getSolo().getButton("Submit").isEnabled());
        clickAndGetToastAndWaitFor(TTChecks.NEW_QUESTION_SUBMITTED, "Submit", errorMessage);
        popCannedResponse();
        getActivity().finish();

    }
}
