package epfl.sweng.test;

import epfl.sweng.entry.MainActivity;

public class MainActivityTest extends TestTemplate<MainActivity> {

    private String editQuestion = "Submit a quiz question";
    private String login = "Log in using Tequila";
    private String showQuestion = "Show a random question";

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testButtonsShouldBeDisplayed() {
        getActivity();
        assertTrue("Log in", getSolo().searchButton(login));
        assertTrue("Show a random question", getSolo().searchButton(showQuestion));
        assertTrue("Submit question", getSolo().searchButton(editQuestion));
        assertFalse("Show question must be disabled", getSolo().getButton(showQuestion).isEnabled());
        assertFalse("Submit question must be disabled", getSolo().getButton(editQuestion).isEnabled());
    }

}
