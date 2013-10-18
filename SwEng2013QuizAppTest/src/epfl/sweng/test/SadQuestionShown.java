package epfl.sweng.test;

import org.apache.http.HttpStatus;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.minimalmock.MockHttpClient;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SadQuestionShown extends TestTemplate<ShowQuestionsActivity> {

	private String errorMessage = ShowQuestionsActivity.ERROR_MESSAGE;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockHttpClient httpClient = new MockHttpClient();
		SwengHttpClientFactory.setInstance(httpClient);
		setSolo(new Solo(getInstrumentation()));
	}

	public SadQuestionShown() {
		super(ShowQuestionsActivity.class);
	}

	public void testNormalBehavior() throws Exception {
		super.setUp();
		assertFalse("everything allright", getSolo().searchText(errorMessage));
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
	}

	public void testMessageDisplayedIfNoServerResponse() {
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		assertTrue("Server down, message displayed",
				getSolo().searchText(errorMessage));
	}

	public void testServerDown() { //TODO
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
	}

	//TODO
	public void testMessageDisplayedIfQuestionNotDisplayed() throws Exception {
		MockHttpClient httpClient = new MockHttpClient();
		httpClient
				.pushCannedResponse(
						"GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random\\b",
						HttpStatus.SC_OK,
						"{\"question\": \"What is the answer to life, the universe, and everything?\","
								+ " \"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\","
								+ " \"solutionIndex\": 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }",
						"application/json");
		SwengHttpClientFactory.setInstance(httpClient);
		setSolo(new Solo(getInstrumentation()));
		ShowQuestionsActivity current = (ShowQuestionsActivity) getActivity();
		current.setText(null); //HERE
		assertTrue("Question is not displayed : message displayed", getSolo()
				.searchText(errorMessage));
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
	}

}
