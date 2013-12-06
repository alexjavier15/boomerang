package epfl.sweng.authenticationactivity;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationBadBehaviour extends AuthenticationActivityTemplate {

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.AuthenticationActivity.AuthenticationActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {

        super.setUp();

    }

    public void testBadReponseFromPostTequilaServer() {
        pushCannedPostTequilaToken(HttpStatus.SC_FORBIDDEN);
        pushCannedGetSwengtoken(HttpStatus.SC_OK);
        pushCannedPostRequestSessionID(HttpStatus.SC_OK);
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.TEQUILA_ERROR_MSG);
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getActivity().finish();

    }

    public void testGettingBadSessionIDResponse() {
        pushCannedPostTequilaToken(TEQUILA_REQUEST_STATUS);
        pushCannedGetSwengtoken(HttpStatus.SC_OK);
        pushCannedPostRequestSessionID(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.SWENG_ERROR_MSG);
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getActivity().finish();
    }

    public void testResponseMalformedToken() {
        getmMock().pushCannedResponse(
                "GET (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/login\\b",
                HttpStatus.SC_OK,
                "\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
                        + " \"message\": \"Here's your authentication token. Please validate it with Tequila"
                        + " at https://tequila.epfl.ch/cgi-bin/tequila/login\"}", "application/json");

        pushCannedPostTequilaToken(TEQUILA_REQUEST_STATUS);
        pushCannedPostRequestSessionID(HttpStatus.SC_OK);

        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.INTERNAL_ERROR_MSG);
        getActivity().finish();
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();

    }

    public void testResponseMalformedSessionID() {
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

        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.UNEXPECTED_ERROR_MSG);
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getActivity().finish();
    }

    public void testGettingBadTokenRespose() {
        pushCannedPostTequilaToken(TEQUILA_REQUEST_STATUS);
        pushCannedGetSwengtoken(HttpStatus.SC_BAD_REQUEST);
        pushCannedPostRequestSessionID(HttpStatus.SC_OK);
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        EditText username = getSolo().getEditText("GASPAR Username");
        getSolo().enterText(username, "test");
        EditText password = getSolo().getEditText("GASPAR Password");
        getSolo().enterText(password, "password");
        clickAndGetToastAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN, "Log in using Tequila",
                AuthenticationActivity.INTERNAL_ERROR_MSG);
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getmMock().popCannedResponse();
        getActivity().finish();
    }
}
