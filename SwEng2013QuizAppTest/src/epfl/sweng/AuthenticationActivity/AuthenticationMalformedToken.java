package epfl.sweng.AuthenticationActivity;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationMalformedToken extends AuthenticationActivityTemplate {

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.AuthenticationActivity.AuthenticationActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();

        getmMock().pushCannedResponse(
                "GET (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/login\\b",
                HttpStatus.SC_OK,
                "\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
                        + " \"message\": \"Here's your authentication token. Please validate it with Tequila"
                        + " at https://tequila.epfl.ch/cgi-bin/tequila/login\"}", "application/json");

        pushCannedPostTequilaToken(TEQUILA_REQUEST_STATUS);
        pushCannedPostRequestSessionID(HttpStatus.SC_OK);

    }

    public void testResponseMalformedToken() {
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.INTERNAL_ERROR_MSG);
    }

}
