package epfl.sweng.test;

import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SadQuestionShown extends TestTemplate<ShowQuestionsActivity> {

	private String errorMessage = ShowQuestionsActivity.ERROR_MESSAGE;

	public SadQuestionShown() {
		super(ShowQuestionsActivity.class);
	}

	public void testNoMessageDisplayedIfQuestion() {
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		assertFalse("Question shown : no error message",
				getSolo().searchText(errorMessage));
	}

	public void testMessageDisplayedIfNoServerResponse() {
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		assertTrue("Server down, message displayed",
				getSolo().searchText(errorMessage));
	}

	public void testMessageDisplayedIfQuestionNotDisplayed() {
		ShowQuestionsActivity current = (ShowQuestionsActivity) getActivity();
		current.setText(null);
		assertTrue("Question is not displayed : message displayed", getSolo()
				.searchText(errorMessage));
	}

}
