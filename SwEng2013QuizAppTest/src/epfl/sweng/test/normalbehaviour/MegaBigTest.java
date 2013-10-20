package epfl.sweng.test.normalbehaviour;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class MegaBigTest extends TestTemplate<MainActivity> {

	public MegaBigTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MockHttpClient mock = new MockHttpClient();
		mock.pushCannedResponse(
				"POST https://sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
				HttpStatus.SC_CREATED,
				"{\"requestkey\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
						+ " \"username\": \"test\","
						+ " \"password\": \"password\"}", "application/json");
		mock.pushCannedResponse(
				"GET (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/login\\b",
				HttpStatus.SC_OK,
				"{\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
						+ " \"message\": \"Here's your authentication token. Please validate it with Tequila"
						+ " at https://tequila.epfl.ch/cgi-bin/tequila/login\"}",
				"application/json");

		mock.pushCannedResponse(
				"POST https://tequila.epfl.ch/cgi-bin/tequila/login HTTP/1.1",
				HttpStatus.SC_MOVED_TEMPORARILY,
				"{\"requestkey\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
						+ " \"username\": \"test\","
						+ " \"password\": \"password\"}", "application/json");

		mock.pushCannedResponse(
				"POST https://sweng-quiz.appspot.com/login\\b",
				HttpStatus.SC_OK,
				"{\"session\": \"test\","
						+ " \"message\": \"Here's your session id. Please include the following"
						+ " HTTP header in your subsequent requests:\n Authorization: Tequila test\"}",
				"application/json");
		mock.pushCannedResponse(
				"GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random\\b",
				200,
				"{\"question\": \"What is the answer to life, the universe, and everything?\", " +
				"\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":" +
				" 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }",
				"application/json");
		SwengHttpClientFactory.setInstance(mock);
	}

	public void testNormalBehaviour() {
		getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);

		if (!getSolo().searchButton("Log in using Tequila")) {
			getSolo().clickOnButton("Log out");
		}
		getSolo().clickOnButton("Log in using Tequila");

		waitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);

		EditText username = getSolo().getEditText("GASPAR Username");
		getSolo().enterText(username, "test");
		EditText password = getSolo().getEditText("GASPAR Password");
		getSolo().enterText(password, "password");

		getSolo().clickOnButton("Log in using Tequila");

		getSolo().clickOnButton("Show a random question");
		// waitFor(TTChecks.QUESTION_SHOWN);
		assertFalse("Next question disabled",
				getSolo().getButton("Next question").isEnabled());

		getSolo().clickOnText("Twenty-seven");
		waitFor(TTChecks.ANSWER_SELECTED);

		assertFalse("Next question button is disabled",
				getSolo().getButton("Next question").isEnabled());

		getSolo().clickOnText("Forty-two");
		waitFor(TTChecks.ANSWER_SELECTED);

		assertTrue("Next question button is enabled",
				getSolo().getButton("Next question").isEnabled());
		getSolo().clickOnButton("Next question");

		getActivity().finish();
	}

}
