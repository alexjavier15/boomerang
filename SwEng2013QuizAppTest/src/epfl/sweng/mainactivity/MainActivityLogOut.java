package epfl.sweng.mainactivity;

import android.widget.Button;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class MainActivityLogOut extends MainActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();
    }

    public void testalreadyLogInAndLogOut() {
        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
        Button logOut = getSolo().getButton("Log out");
        assertTrue("Log out button is enabled ", logOut.isEnabled());
        clickAndWaitForButton(TTChecks.LOGGED_OUT, "Log out");
    }

}
