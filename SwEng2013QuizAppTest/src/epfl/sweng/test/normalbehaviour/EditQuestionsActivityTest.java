package epfl.sweng.test.normalbehaviour;

import org.apache.http.HttpStatus;

import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class EditQuestionsActivityTest extends TestTemplate<EditQuestionActivity> {

    private final static int NUM_ANSWERS = 5;
    private final static int NUMBER = 10;
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
        mock.pushCannedResponse("POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
                HttpStatus.SC_CREATED, "{\"question\": \"Pourquoi je suis si con?\","
                        + " \"answers\": [\"A cause de la cigarette\", \"Je ne suis pas con\", \"De naissance\"],"
                        + " \"solutionIndex\": 1, \"tags\": [\"stupid\", \"alex\"], \"id\": \"-1\" }",
                "application/json");
        SwengHttpClientFactory.setInstance(mock);
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
    }

    @UiThreadTest
    public void testAddMultipleanswers1() {
        getActivity();

        getSolo().getButton(0).setText(R.string.heavy_check_mark);
        Button plus = (Button) getSolo().getButton("+");
      
//        EditText answerT = getSolo().getEditText(1);
//        answerT.setText(answer2);
//        plus.callOnClick();
//        answerT = getSolo().getEditText(2);
//        answerT.setText(answer1);
//        getSolo().getEditText("question").setText(question);
//        EditText tags = getSolo().getEditText("tags");
//        tags.setText("debile, alex");
//
//        getSolo().getButton(2).setText(R.string.heavy_check_mark);
        submit = getSolo().getButton("Submit");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertFalse("Submit button should be disabled", submit.isEnabled());
        assertFalse("multiple checks, condition", 0 != ((EditQuestionActivity) getActivity()).auditErrors());
        getActivity().finish();
    }

//    public void testAddMultipleanswers2() {
//
//        // Click on the first response to be true
//        getSolo().clickOnButton(0);
//        for (int i = 0; i < NUM_ANSWERS; i++) {
//
//            EditText answerT = getSolo().getEditText("answer");
//            if (i % 2 == 0) {
//                getSolo().enterText(answerT, answer2);
//            } else {
//                getSolo().enterText(answerT, answer1);
//
//            }
//            getSolo().clickOnButton("+");
//            // waitFor(TTChecks.QUESTION_EDITED);
//        }
//
//        EditText answerT = getSolo().getEditText("answer");
//        getSolo().enterText(answerT, answer1);
//        submit = getSolo().getButton("Submit");
//        assertFalse("Submit button should be disabled", submit.isEnabled());
//
//        getSolo().enterText(getSolo().getEditText("question"), question);
//
//        EditText tags = getSolo().getEditText("tags");
//
//        getSolo().enterText(tags, "debile, alex");
//
//        assertTrue("Submit button should be disabled", submit.isEnabled());
//        getSolo().clickOnButton("Submit");
//
//        waitFor(TTChecks.NEW_QUESTION_SUBMITTED);
//        assertTrue("Succes", getSolo().searchText("Your submission was successful"));
//    }
    // public void test1ButtonsMustBeDisplayed() {
    // getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
    // assertTrue("EditText for the question",
    // getSolo().searchText("Type in the question"));
    // assertTrue("EditText for an answer",
    // getSolo().searchText("Type in the answer"));
    // assertTrue("Button to remove answer", getSolo().searchButton("-"));
    // assertTrue("Button to add an anser", getSolo().searchButton("+"));
    // assertTrue("EditText for the tags", getSolo().searchText("tags"));
    // assertTrue("Button to submit", getSolo().searchButton("Submit"));
    //
    // submit = getSolo().getButton("Submit");
    // assertFalse("Submit button should be disabled", submit.isEnabled());
    // }
    //
    // @UiThreadTest
    // public void testButtonStayDissabled() {
    // //getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
    // EditText questionT = getSolo().getEditText("question");
    // questionT.setText("test question");
    // assertFalse("question body must be displayed", questionT.getText()
    // .toString().equals(question));
    // questionT.setText(question);
    // assertTrue("question body must be displayed", questionT.getText()
    // .toString().equals(question));
    //
    // submit = getSolo().getButton("Submit");
    // assertFalse("Submit button should be disabled", submit.isEnabled());
    // EditText answerT = getSolo().getEditText("answer");
    // answerT.setText(answer1);
    // assertTrue("answer must be displayed", answerT.getText().toString()
    // .equals(answer1));
    // submit = getSolo().getButton("Submit");
    // assertFalse("Submit button should be disabled", submit.isEnabled());
    // EditText tagsT = getSolo().getEditText("tag");
    // tagsT.setText("test");
    // assertFalse("answer must be displayed", tagsT.getText().toString()
    // .equals(" test"));
    // assertTrue("answer must be displayed", tagsT.getText().toString()
    // .equals("test"));
    // submit = getSolo().getButton("Submit");
    // assertFalse("Submit button should be disabled", submit.isEnabled());
    //
    // getActivity().finish();
    // }
}
