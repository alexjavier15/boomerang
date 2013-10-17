package epfl.sweng.test;

import epfl.sweng.authentication.AuthenticationActivity;

public class AuthenticationActivityTest extends TestTemplate<AuthenticationActivity>{

	public AuthenticationActivityTest() {
		super(AuthenticationActivity.class);
	}
	
	public void testAfterLogIn() {
		getActivity();
		assertTrue("Sign in", solo.searchButton("Log in using Tequila"));
		assertTrue("Username", solo.searchEditText("GASPAR Username"));
		assertTrue("Password", solo.searchEditText("GASPAR Password"));
	}

}
