package epfl.sweng.AuthenticationActivity;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationBadCheckToken extends AuthenticationActivityTemplate {

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.AuthenticationActivity.AuthenticationActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        pushCannedPostTequilaToken(TEQUILA_REQUEST_STATUS);
        pushCannedGetSwengtoken(HttpStatus.SC_BAD_REQUEST);
        pushCannedPostRequestSessionID(HttpStatus.SC_OK);

    }

    public void testGettingBadAuthenticationResponse() {
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.INTERNAL_ERROR_MSG);

    }

}
