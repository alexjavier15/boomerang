package epfl.sweng.mainactivity;

import org.apache.http.HttpStatus;

import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class MainAndShowQuestion extends MainActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockHttpClient mock = new MockHttpClient();

        mock.pushCannedResponse("GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random HTTP/1.1", HttpStatus.SC_OK,
                "{\"question\": \"What is the answer to life?\", "
                        + "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
                        + " 0, \"tags\": [h2g2, trivia], \"id\": \"1\" }", "application/json");
        SwengHttpClientFactory.setInstance(mock);
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
    }

    public void testShowQuestionShown() {
        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
        clickAndWaitForButton(TTChecks.QUESTION_SHOWN, "Show a random question");
        getActivity().finishAffinity();
        getSolo().goBack();
        getActivity().finish();
    }

}
