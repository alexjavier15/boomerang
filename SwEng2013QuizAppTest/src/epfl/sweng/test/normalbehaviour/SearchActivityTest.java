package epfl.sweng.test.normalbehaviour;

import android.widget.Button;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.searchquestions.SearchActivity;
import epfl.sweng.servercomm.QuizApp;
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
		QuizApp.getPreferences().edit()
		.putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
	}
	
	public void testView() {
		//getActivityAndWaitFor(TTChecks.SEARCH_ACTIVITY_SHOWN);
		
		getActivity();
		
		assertTrue("EditText for search query must exist", getSolo().searchEditText("Type in the search query"));
		assertTrue("Button for submitting the query must exist", getSolo().searchButton("Search"));
		
		Button search = getSolo().getButton("Search");
		
		assertFalse("Search button is disabled", search.isEnabled());
		
		enterTextAndWaitFor(TTChecks.QUERY_EDITED, getSolo().getEditText("query"), "h2g2");
		
		assertTrue("Search button is enabled", search.isEnabled());
		
		getActivity().finish();
	}

}
