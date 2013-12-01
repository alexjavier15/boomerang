package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionEmptyTags extends ShowQuestionActivityTemplate {

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		pushCannedResponse("GET", HttpStatus.SC_OK, DEFAULT_QUESTION, "");

	}

	public void testEmptyTags() {

		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		boolean emptyTagMsg = getSolo().searchText(
				ShowQuestionsActivity.EMPTY_TAGS_MSG);
		assertTrue("Error Toast Shown : ", emptyTagMsg);

	}
}
