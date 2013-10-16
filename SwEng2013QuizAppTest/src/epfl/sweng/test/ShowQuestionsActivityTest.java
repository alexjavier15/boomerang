package epfl.sweng.test;

import android.widget.Button;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionsActivityTest extends TestTemplate<ShowQuestionsActivity> {

	public ShowQuestionsActivityTest() {
		super(ShowQuestionsActivity.class);
	}

	public void testShowQuestion() {
		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		assertTrue(
				"Question is displayed :" + solo.getText(0).getText(),
				solo.searchText("What is the answer to life, the universe, and everything?"));
		assertTrue("Correct answer is displayed", solo.searchText("Forty-two"));
		assertTrue("Incorrect answer is displayed",
				solo.searchText("Twenty-seven"));

		assertTrue("Tags are displayed", solo.searchText("h2g2"));
		assertTrue("Tags are displayed", solo.searchText("trivia"));

		assertTrue("Next question button is displayed", solo.searchButton("Next question"));
		Button nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
		
		solo.clickOnText("Forty-two");
		for(int i=0; i<100000000; i++){};
		assertTrue("Next question button is enabled",
				nextQuestionButton.isEnabled());
		
	}

	
}
