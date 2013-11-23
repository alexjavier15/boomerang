package epfl.sweng.test.normalbehaviour;

import epfl.sweng.searchquestions.SearchActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SearchActivityTest extends TestTemplate<SearchActivity> {

	public SearchActivityTest() {
		super(SearchActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockHttpClient mock = new MockHttpClient();
		mock.pushCannedResponse(
				"GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random\\b",
				200,
				"{\"question\": \"What is the answer to life, the universe, and everything?\", "
						+ "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
						+ " 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }",
				"application/json");
		SwengHttpClientFactory.setInstance(mock);
	}
	
	public void viewTest() {
		getActivityAndWaitFor(TTChecks.SEARCH_ACTIVITY_SHOWN);
		
		getActivity().finish();
	}

}
