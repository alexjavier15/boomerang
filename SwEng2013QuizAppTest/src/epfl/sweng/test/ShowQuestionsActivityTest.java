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


		
		
		
		
		
		nextQuestionButton = solo.getButton("Next question");
		assertFalse("Next question button is disabled",
				nextQuestionButton.isEnabled());
	}

	
}
