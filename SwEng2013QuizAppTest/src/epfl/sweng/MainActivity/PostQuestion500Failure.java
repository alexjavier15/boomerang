package epfl.sweng.MainActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class PostQuestion500Failure extends MainActivityTemplate {

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

    }

//    public void testBackMainOfflineEnable() {
//
//        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
//        assertEquals(true, getSolo().getButton("Submit a quiz question").isEnabled());
//        clickAndWaitForButton(TTChecks.EDIT_QUESTIONS_SHOWN, "Submit a quiz question");
//        fillCorrectQuizQuestion();
//        assertEquals(true, getSolo().getButton("Submit").isEnabled());
//        clickAndWaitForAnswer(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");
//        goBackAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
//
//    }

}
