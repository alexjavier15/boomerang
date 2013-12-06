package epfl.sweng.showquestionactivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionCheckAnswers extends ShowQuestionActivityTemplate {
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

    }

    public void testCorrectAnswerNextIsEnable() {
        pushCannedResponse("GET", HttpStatus.SC_OK);
        chooseCorrectAnswer();
        Button next = getSolo().getButton(getActivity().getResources().getString(R.string.next_question));

        assertTrue("Next Button is Enable", next.isEnabled());
        popCannedResponse();
        getActivity().finish();

    }

    public void testCheckCorrectAnswerClickNext() {
        pushCannedResponse("GET", HttpStatus.SC_OK);
        chooseCorrectAnswer();
        assertFalse("New Question shown", getSolo().searchText(QUESTION));

        popCannedResponse();
        pushCannedResponse("GET", HttpStatus.SC_OK, QUESTION, DEFAULT_TAGS);
        clickAndWaitForButton(TTChecks.QUESTION_SHOWN, getActivity().getResources().getString(R.string.next_question));
        assertTrue("New Question shown", getSolo().searchText(QUESTION));
        popCannedResponse();
        getActivity().finish();
    }

    public void testCheckBadAnswerNextDisable() {
        pushCannedResponse("GET", HttpStatus.SC_OK);
        chooseBadAnswer();
        Button next = getSolo().getButton(getActivity().getResources().getString(R.string.next_question));

        assertFalse("Next Button is Enable", next.isEnabled());
        popCannedResponse();
        getActivity().finish();

    }

    public void testCheckBadAnswerAndCorrectAnswer() {
        pushCannedResponse("GET", HttpStatus.SC_OK);
        chooseBadAnswer();
        Button next = getSolo().getButton(getActivity().getResources().getString(R.string.next_question));

        assertFalse("Next Button is Enable", next.isEnabled());
        clickAndWaitForAnswer(TTChecks.ANSWER_SELECTED, CORRECT_ANS);
        assertTrue("Correct answer selected : ", next.isEnabled());
        popCannedResponse();
        getActivity().finish();
    }

}
