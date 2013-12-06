package epfl.sweng.mainActivity;

import org.apache.http.HttpStatus;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import epfl.sweng.R;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class MainAndSearchQuestion extends MainActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockHttpClient mock = new MockHttpClient();

        mock.pushCannedResponse("POST https://sweng-quiz.appspot.com/search HTTP/1.1", HttpStatus.SC_OK,
                "{\"questions\": [{\"question\": \"What is the answer to life?\", "
                        + "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
                        + " 0, \"tags\": [h2g2, trivia], \"id\": \"1\" }]}", "application/json");
        SwengHttpClientFactory.setInstance(mock);
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
    }

    public void testSearchQuestionShown() {
        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
   

        clickAndWaitForButton(TTChecks.SEARCH_ACTIVITY_SHOWN, "Search");
        enterTextAndWaitFor(TTChecks.QUERY_EDITED,getSolo().getEditText(0), "h2g2");
        Button search = getSolo().getButton("Search");
        clickAndWaitFor(TTChecks.QUESTION_SHOWN, search);
        getSolo().sendKey(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
        getSolo().sendKey(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
        View check = getActivity().findViewById(R.id.offline_mode);
        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_ENABLED, check);
        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_DISABLED, check);
        getInstrumentation().waitForIdleSync();
        getActivity().finishAffinity();
        
        
    }

}
