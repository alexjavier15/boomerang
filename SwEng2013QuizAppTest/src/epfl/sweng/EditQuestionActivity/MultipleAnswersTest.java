package epfl.sweng.EditQuestionActivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.R;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class MultipleAnswersTest extends EditQuestionActivityTemplate {

    private final static int NUM_ANSWERS = 2;
    private String answer1 = "a cause de la cigarrette";
    private String answer2 = "de naissance";
    private String question = "Pourquoi suis je si con?";
    private Button submit;

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
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "test").apply();
    }

    public void test1AddMultipleanswers2() {
        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        // Click on the first response to be true
        clickAndWaitForButton(TTChecks.QUESTION_EDITED,
                getActivity().getResources().getString(R.string.heavy_ballot_x));
        submit = getSolo().getButton("Submit");
        for (int i = 0; i <= NUM_ANSWERS; i++) {
            EditText answerT = getSolo().getEditText("answer");
            if (i % 2 == 0) {

                getSolo().clearEditText(answerT);

                enterTextAndWaitFor(TTChecks.QUESTION_EDITED, answerT, answer1);
                assertFalse("Submit button should be disabled", submit.isEnabled());

            } else {
                getSolo().clearEditText(answerT);
                enterTextAndWaitFor(TTChecks.QUESTION_EDITED, answerT, answer2);
                assertFalse("Submit button should be disabled", submit.isEnabled());

            }
            if(i!=NUM_ANSWERS){
            clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");}
        }
      
     
     

        submit = getSolo().getButton("Submit");
        assertFalse("Submit button should be disabled", submit.isEnabled());

        EditText q = getSolo().getEditText(getActivity().getResources().getString(R.string.edit_question));
        getSolo().clearEditText(q);
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, q, question);

        EditText tags = getSolo().getEditText("tags");
        getSolo().clearEditText(tags);
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, tags, "stupid, alex");

        clickAndWaitForButton(TTChecks.NEW_QUESTION_SUBMITTED, "Submit");
        

    }

}
