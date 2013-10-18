package epfl.sweng.test;

import epfl.sweng.authentication.AuthenticationActivity;

public class AuthenticationActivityTest extends TestTemplate<AuthenticationActivity> {

    public AuthenticationActivityTest() {
        super(AuthenticationActivity.class);
    }

    public void testAfterLogIn() {
        getActivity();
        assertTrue("Sign in", getSolo().searchButton("Sign in"));
        assertTrue("Username", getSolo().searchEditText("GASPAR Username"));
        assertTrue("Password", getSolo().searchEditText("GASPAR Password"));
    }
}
