package epfl.sweng.minimalmock;

import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/** A test that illustrates the use of MockHttpClients */
public class MockHttpClientTest extends TestTemplate<ShowQuestionsActivity> {

    protected static final String RANDOM_QUESTION_BUTTON_LABEL = "Show a random question";

    public MockHttpClientTest() {
        super(ShowQuestionsActivity.class);
    }

    public void testFetchQuestion() {
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
        assertTrue("Question must be displayed",
                solo.searchText("What is the answer to life, the universe, and everything?"));
        assertTrue("Correct answer must be displayed", solo.searchText("Forty-two"));
        assertTrue("Incorrect answer must be displayed", solo.searchText("Twenty-seven"));
    }

}