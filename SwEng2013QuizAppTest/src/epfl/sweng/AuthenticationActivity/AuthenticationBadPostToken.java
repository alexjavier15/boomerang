package epfl.sweng.AuthenticationActivity;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationBadPostToken extends AuthenticationActivityTemplate {

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.AuthenticationActivity.AuthenticationActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        
        pushCannedPostTequilaToken(HttpStatus.SC_FORBIDDEN);
        pushCannedGetSwengtoken(OTHER_STATUS);
        pushCannedPostRequestSessionID(OTHER_STATUS);

    }

    public void testBadReponseFromPostTequilaServer() {
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        getSolo().clickOnButton("Log in using Tequila");
        boolean isErrorShown = getSolo().waitForText(AuthenticationActivity.TEQUILA_ERROR_MSG);
        assertTrue("request to sweng failed", isErrorShown);

    }

}
