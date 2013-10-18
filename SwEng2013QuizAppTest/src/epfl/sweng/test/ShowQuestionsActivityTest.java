package epfl.sweng.test;

import android.widget.Button;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionsActivityTest extends TestTemplate<ShowQuestionsActivity> {

    public final static int CLICKS = 2000000;

    public ShowQuestionsActivityTest() {
        super(ShowQuestionsActivity.class);
    }

    public void testShowQuestion() {
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
        assertTrue("Question is displayed :" + getSolo().getText(0).getText(),
            getSolo().searchText("What is the answer to life, the universe, and everything?"));
        assertTrue("Correct answer is displayed", getSolo().searchText("Forty-two"));
        assertTrue("Incorrect answer is displayed", getSolo().searchText("Twenty-seven"));

        assertTrue("Tags are displayed", getSolo().searchText("h2g2"));
        assertTrue("Tags are displayed", getSolo().searchText("trivia"));
        
        assertTrue("Next question button is displayed", getSolo().searchButton("Next question"));
        Button nextQuestionButton = getSolo().getButton("Next question");
        assertFalse("Next question button is disabled", nextQuestionButton.isEnabled());

        getSolo().clickOnText("Forty-two");
        waitFor(TTChecks.ANSWER_SELECTED);

        assertTrue("NExt question button exists", getSolo().searchButton("Next question"));
        assertTrue("Next question button is enabled", nextQuestionButton.isEnabled());

    }

}
