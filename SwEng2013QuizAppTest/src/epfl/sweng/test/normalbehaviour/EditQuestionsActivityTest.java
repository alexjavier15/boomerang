package epfl.sweng.test.normalbehaviour;

import org.apache.http.HttpStatus;

import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class EditQuestionsActivityTest extends
		TestTemplate<EditQuestionActivity> {

	private final static int NUM_ANSWERS = 5;
	private String answer1 = "a cause de la cigarrette";
	private String answer2 = "de naissance";
	private String question = "Pourquoi suis je si con?";
	private Button submit;

	public EditQuestionsActivityTest() {
		super(EditQuestionActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		MockHttpClient mock = new MockHttpClient();
		mock.pushCannedResponse(
				"POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
				HttpStatus.SC_CREATED,
				"{\"question\": \"Pourquoi je suis si con?\","
						+ " \"answers\": [\"A cause de la cigarette\", \"Je ne suis pas con\", \"De naissance\"],"
						+ " \"solutionIndex\": 1, \"tags\": [\"stupid\", \"alex\"], \"id\": \"-1\" }",
				"application/json");
		SwengHttpClientFactory.setInstance(mock);
		QuizApp.getPreferences().edit()
				.putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
	}

	public void testAuditErrors() {
		getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
		
		submit = getSolo().getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());

		getSolo().clickOnButton(0);

		getSolo().enterText(1, answer2);
		clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
		EditText et = getSolo().getEditText(2);
		enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et, answer1);

		getSolo().enterText(getSolo().getEditText(0), question);

		getSolo().enterText(getSolo().getEditText(3), "debile, alex");

		assertTrue("Submit button should be enabled", submit.isEnabled());
		
		getActivity().finish();
	}

	public void testAddMultipleanswers2() {
		getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

		assertEquals(0, ((EditQuestionActivity) getActivity()).auditErrors());
		
		// Click on the first response to be true
		getSolo().clickOnButton(0);
		for (int i = 0; i < NUM_ANSWERS; i++) {
			EditText answerT = getSolo().getEditText("answer");
			if (i % 2 == 0) {
				getSolo().enterText(answerT, answer1);
			} else {
				getSolo().enterText(answerT, answer2);
			}
			clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
		}
		
		EditText et = getSolo().getEditText("answer");
		enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et, "Coucouc");
		
		submit = getSolo().getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());

		EditText q = getSolo().getEditText("text body");
		enterTextAndWaitFor(TTChecks.QUESTION_EDITED, q, question);

		EditText tags = getSolo().getEditText("tags");
		enterTextAndWaitFor(TTChecks.QUESTION_EDITED, tags, "stupid, alex");

		assertTrue("Submit button should be enabled", submit.isEnabled());
		
		assertEquals(0, ((EditQuestionActivity) getActivity()).auditErrors());

		clickAndWaitForButton(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");
		assertTrue("Success",
				getSolo().searchText("Your submission was successful"));
		
		getActivity().finish();
	}

	public void test1ButtonsMustBeDisplayed() {
		getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
		assertTrue("EditText for the question",
				getSolo().searchText("Type in the question's text body"));
		assertTrue("EditText for an answer",
				getSolo().searchText("Type in the answer"));
		assertTrue("Button to remove answer", getSolo().searchButton("-"));
		assertTrue("Button to add an anser", getSolo().searchButton("+"));
		assertTrue("EditText for the tags", getSolo().searchText("Type in the question's tags"));
		assertTrue("Button to submit", getSolo().searchButton("Submit"));

		submit = getSolo().getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());
		
		getActivity().finish();
	}
	
}
