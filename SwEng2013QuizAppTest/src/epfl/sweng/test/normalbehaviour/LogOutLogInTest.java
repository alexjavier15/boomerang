package epfl.sweng.test.normalbehaviour;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class LogOutLogInTest extends TestTemplate<MainActivity> {

	public LogOutLogInTest() {
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
						+ " \"message\": \"Here's your session id. Please include the following HTTP" +
						" header in your subsequent requests:\n Authorization: Tequila test\"}",
				"application/json");
		SwengHttpClientFactory.setInstance(mock);
	}

	public void testLogInLogOut() {
		getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
		if (getSolo().searchButton("Log in using Tequila")) {
			getSolo().clickOnButton("Log in using Tequila");
			waitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
			EditText username = getSolo().getEditText("GASPAR Username");
			getSolo().enterText(username, "test");
			EditText password = getSolo().getEditText("GASPAR Password");
			getSolo().enterText(password, "password");
			getSolo().clickOnButton("Log in using Tequila");
			getSolo().clickOnButton("Log out");
		} else {
			getSolo().clickOnButton("Log out");
		}

	}

}
