package epfl.sweng.bigtest;

import org.apache.http.HttpStatus;

import android.widget.EditText;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.minimalmock.MockHttpClient;
import epfl.sweng.test.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class BigTest extends TestTemplate<MainActivity> {

	public BigTest() {
		super(MainActivity.class);
	}

	public void testNormalBehaviour() {
		MockHttpClient mock = getMock();
		getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);

		getSolo().clickOnButton("Log in using Tequila");
		waitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);

		EditText username = getSolo().getEditText("GASPAR Username");
		getSolo().enterText(username, "verstege");
		EditText password = getSolo().getEditText("GASPAR Password");
		getSolo().enterText(password, "W54i00ll96");

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
		
		mock.pushCannedResponse("POST https://sweng-quiz.appspot.com/login\\b",
				HttpStatus.SC_OK,
				"{\"session\": \"test\","
						+ " \"message\": \"Here's your session id. Please include the following HTTP header in your subsequent requests:\n Authorization: Tequila test\"}", "application/json");

		getSolo().clickOnButton("Log in using Tequila");

		waitFor(TTChecks.MAIN_ACTIVITY_SHOWN);

		getSolo().clickOnButton("Show a random question");
		waitFor(TTChecks.QUESTION_SHOWN);
		assertFalse("Next question disabled",
				getSolo().getButton("Next question").isEnabled());
		getSolo().goBack();
		waitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
		
		assertTrue("Log out button is here", getSolo().searchButton("Log out"));
		getSolo().clickOnButton("Log out");
		

	}

}
