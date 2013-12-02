package epfl.sweng.MainActivity;

import org.apache.http.HttpStatus;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

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
        mock.pushCannedResponse("GET (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/login\\b", HttpStatus.SC_OK,
                "{\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
                        + " \"message\": \"Here's your authentication token. Please validate it with Tequila"
                        + " at https://tequila.epfl.ch/cgi-bin/tequila/login\"}", "application/json");

        SwengHttpClientFactory.setInstance(getMock());
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
    }

    public void testSearchQuestionShown() {
        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);

        clickAndWaitForButton(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila");
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndWaitForButton(TTChecks.MAIN_ACTIVITY_SHOWN, "Log in using Tequila");

        clickAndWaitForButton(TTChecks.SEARCH_ACTIVITY_SHOWN, "Search");
        getSolo().sendKey(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
        getSolo().sendKey(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
        View check = getActivity().findViewById(R.id.offline_mode);

        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_DISABLED, check);
        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_ENABLED, check);

        getActivity().finishAffinity();
        getSolo().goBack();
    }

}
