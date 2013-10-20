package epfl.sweng.test.badbehaviours;

import org.apache.http.HttpStatus;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;
/**
 * 
 * @author LorenzoLeon
 *
 */
public class SadQuestionShown extends TestTemplate<ShowQuestionsActivity> {

	private String errorMessage = ShowQuestionsActivity.ERROR_MESSAGE;

	public SadQuestionShown() {
		super(ShowQuestionsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockHttpClient mock = new MockHttpClient();
		mock.pushCannedResponse(
				"GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random HTTP/1.1",
				HttpStatus.SC_NOT_FOUND,
				"{\"question\": \"What is the answer to life, the universe, and everything?\", "
						+ "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
						+ " 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }",
				"application/json");
		SwengHttpClientFactory.setInstance(mock);
	}

	public void testMessageDisplayedIfNoServerResponse() {
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		assertTrue("Server down, message displayed",
				getSolo().searchText(errorMessage));
	}

}
