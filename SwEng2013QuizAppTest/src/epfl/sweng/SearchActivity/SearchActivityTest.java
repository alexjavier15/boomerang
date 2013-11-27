package epfl.sweng.SearchActivity;

import android.widget.Button;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SearchActivityTest extends SearchQuestionActivityTemplate {

    private static final int GET_NUM = 200;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockHttpClient mock = new MockHttpClient();
        mock.pushCannedResponse("GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random\\b", GET_NUM,
                "{\"question\": \"What is the answer to life, the universe, and everything?\", "
                        + "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
                        + " 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }", "application/json");
        SwengHttpClientFactory.setInstance(mock);
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
    }

    public void testItems() {
        getActivityAndWaitFor(TTChecks.SEARCH_ACTIVITY_SHOWN);

        assertTrue("EditText for search query must exist", getSolo().searchEditText("Type in the search query"));
        assertTrue("Button for submitting the query must exist", getSolo().searchButton("Search"));

        Button search = getSolo().getButton("Search");

        assertFalse("Search button is disabled", search.isEnabled());
        getSolo().enterText(getSolo().getEditText(0), "h2g2");
        assertTrue("Search button is enabled", search.isEnabled());

    }


}
