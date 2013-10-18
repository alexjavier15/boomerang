package epfl.sweng.test;

import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationActivityTest extends TestTemplate<AuthenticationActivity> {
	
    public AuthenticationActivityTest() {
        super(AuthenticationActivity.class);
    }

    public void testAfterLogIn() {
        getActivityAndWaitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
        assertTrue("Sign in", getSolo().searchButton("Log in using Tequila"));
        assertTrue("Username", getSolo().searchEditText("GASPAR Username"));
        assertTrue("Password", getSolo().searchEditText("GASPAR Password"));
    }
}
