package epfl.sweng.test;

import epfl.sweng.entry.MainActivity;

public class MainActivityTest extends TestTemplate<MainActivity>{
	
	private String login = "Log in using Tequila";
	private String showQuestion = "Show a random question";
	private String editQuestion = "Submit a quiz question";

	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	public void testButtonsShouldBeDisplayed() {
		getActivity();
		assertTrue("Log in", solo.searchButton(login));
		assertTrue("Show a random question", solo.searchButton(showQuestion));
		assertTrue("Submit question", solo.searchButton(editQuestion));
		assertFalse("Show question must be disabled", solo.getButton(showQuestion).isEnabled());
		assertFalse("Submit question must be disabled", solo.getButton(editQuestion).isEnabled());
	}

}
