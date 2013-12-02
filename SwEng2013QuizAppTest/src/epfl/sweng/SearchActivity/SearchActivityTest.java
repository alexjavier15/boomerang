package epfl.sweng.SearchActivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SearchActivityTest extends SearchQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockHttpClient mock = new MockHttpClient();
        mock.pushCannedResponse("POST https://sweng-quiz.appspot.com/search HTTP/1.1", HttpStatus.SC_OK,
                "{\"questions\": [{\"question\": \"What is the answer to life?\", "
                        + "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
                        + " 0, \"tags\": [h2g2, trivia], \"id\": \"1\" }]}", "application/json");
        SwengHttpClientFactory.setInstance(mock);
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
    }

    public void testItems() {
        getActivityAndWaitFor(TTChecks.SEARCH_ACTIVITY_SHOWN);

        assertTrue("EditText for search query must exist", getSolo().searchEditText("Type in the search query"));

        Button search = getSolo().getButton("Search");
        assertTrue("Button for submitting the query must exist", getSolo().searchButton("Search"));

        assertFalse("Search button is disabled", search.isEnabled());
        getSolo().enterText(getSolo().getEditText(0), "h2g2");
        assertTrue("Search button is enabled", search.isEnabled());

        clickAndWaitForButton(TTChecks.QUESTION_SHOWN, "Search");
        getActivity().finishAffinity();
        getSolo().goBack();
        getActivity().finish();
    }

}
