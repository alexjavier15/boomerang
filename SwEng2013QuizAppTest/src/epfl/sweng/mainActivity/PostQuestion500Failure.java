package epfl.sweng.mainActivity;

import org.apache.http.HttpStatus;

import android.view.KeyEvent;
import android.view.View;
import epfl.sweng.R;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.patterns.CheckProxyHelper;
import epfl.sweng.patterns.ICheckProxyHelper;
import epfl.sweng.servercomm.CacheHttpComms;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class PostQuestion500Failure extends MainActivityTemplate {
    ICheckProxyHelper helper;

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.MainActivity.MainActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        pushCannedResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        SwengHttpClientFactory.setInstance(getMock());
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
        helper = new CheckProxyHelper();

    }

    public void testBackMainOfflineEnable() {

        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
        assertEquals(true, getSolo().getButton("Submit a quiz question").isEnabled());
        clickAndWaitForButton(TTChecks.EDIT_QUESTIONS_SHOWN, "Submit a quiz question");
        fillCorrectQuizQuestion();
        assertEquals(true, getSolo().getButton("Submit").isEnabled());
        clickAndWaitForAnswer(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");
        getSolo().sendKey(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();
        View check = getActivity().findViewById(R.id.offline_mode);

        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_DISABLED, check);
        getInstrumentation().waitForIdleSync();        
        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_ENABLED, check);

        assertTrue("Offline mode enabled", helper.getServerCommunicationClass() == CacheHttpComms.class);
        clickAndWaitForButton(TTChecks.QUESTION_SHOWN, "Show a random question");
        getActivity().finishAffinity();
        getSolo().goBack();
        getActivity().finish();

    }

}
