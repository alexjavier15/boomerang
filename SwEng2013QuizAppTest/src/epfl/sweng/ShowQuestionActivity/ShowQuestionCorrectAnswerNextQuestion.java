package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionCorrectAnswerNextQuestion extends
		ShowQuestionActivityTemplate {

	private final static String QUESTION = "new  question";

	/*
	 * (non-Javadoc)
	 * 
	 * @see epfl.sweng.ShowQuestionActivity.ShowQuestionActivityTemplate#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		pushCannedResponse("GET", HttpStatus.SC_OK);

	}

	public void testCorrectAnswerNextEnable() {

		chooseCorrectAnswer();
		assertFalse("New Question shown", getSolo().searchText(QUESTION));

		popCannedResponse();
		pushCannedResponse("GET", HttpStatus.SC_OK, QUESTION, DEFAULT_TAGS);
		clickAndWaitForButton(TTChecks.QUESTION_SHOWN, getActivity()
				.getResources().getString(R.string.next_question));
		assertTrue("New Question shown", getSolo().searchText(QUESTION));
	}

}
