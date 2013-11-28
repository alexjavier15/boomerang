package epfl.sweng.AuthenticationActivity;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationMalformedSessionID extends AuthenticationActivityTemplate {

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.AuthenticationActivity.AuthenticationActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        pushCannedPostTequilaToken(TEQUILA_REQUEST_STATUS);
        pushCannedGetSwengtoken(HttpStatus.SC_OK);
        getmMock()
                .pushCannedResponse(
                        "POST https://sweng-quiz.appspot.com/login\\b",
                        HttpStatus.SC_OK,
                        "\"session\": \"test\","
                                + " \"message\": \"Here's your session id. Please include the following HTTP"
                                + " header in your subsequent requests:\n Authorization: Tequila test\"}",
                        "application/json");

    }

    public void testResponseMalformedSessionID() {
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        getSolo().clickOnButton("Log in using Tequila");
        boolean isErrorShown = getSolo().waitForText(AuthenticationActivity.UNEXPECTED_ERROR_MSG);
        assertTrue("request to sweng failed", isErrorShown);
    }

}
