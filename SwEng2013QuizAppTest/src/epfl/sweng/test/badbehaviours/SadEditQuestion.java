package epfl.sweng.test.badbehaviours;

import org.apache.http.HttpStatus;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.patterns.CheckProxyHelper;
import epfl.sweng.servercomm.CacheHttpComms;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;
/**
 * 
 * @author LorenzoLeon
 *
 */
public class SadEditQuestion extends TestTemplate<EditQuestionActivity> {

	private String errorMessage = EditQuestionActivity.ERROR_MESSAGE;

	public SadEditQuestion() {
		super(EditQuestionActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockHttpClient mock = new MockHttpClient();
		mock.pushCannedResponse(
				"POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
				HttpStatus.SC_NOT_FOUND,
				"{\"question\": \"What did I do?\","
						+ " \"answers\": [\"A lot, for once!\", \"Nothing, like usually!\"],"
						+ " \"solutionIndex\": 0, \"tags\": [\"stupid\", \"me\"], \"id\": \"-1\" }",
				"application/json");
		SwengHttpClientFactory.setInstance(mock);
	}

	public void testServerNotAccessible() {
	    
	    CheckProxyHelper check = new CheckProxyHelper();
		getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
        assertTrue(check.getServerCommunicationClass() == HttpComms.class);

		getSolo().enterText(getSolo().getEditText("text body"),
				"What did I do?");
		getSolo().enterText(getSolo().getEditText("tags"), "Stupid, me");
		getSolo().enterText(getSolo().getEditText("answer"),
				"A lot, for this time!");
		// Select the right answer
		getSolo().clickOnButton(0);
		getSolo().clickOnButton("+");
		getSolo().enterText(getSolo().getEditText("answer"),
				"Nothing, like usually");

		assertTrue(getSolo().getButton("Submit").isEnabled());

		getSolo().clickOnButton("Submit");
		waitFor(TTChecks.NEW_QUESTION_SUBMITTED);

		assertTrue("Submission not successful",
				getSolo().searchText(errorMessage));
		assertTrue(check.getServerCommunicationClass() == CacheHttpComms.class);
	}
}
