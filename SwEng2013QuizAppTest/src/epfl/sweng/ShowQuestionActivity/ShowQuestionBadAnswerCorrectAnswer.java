package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionBadAnswerCorrectAnswer extends ShowQuestionActivityTemplate {
    
    @Override
    protected void setUp() throws Exception {

        super.setUp();
        pushCannedResponse("GET", HttpStatus.SC_OK);

    }

    public void testBadAnswerCorrectAnswer() {
        chooseBadAnswer();
        Button next = getSolo().getButton(getActivity().getResources().getString(R.string.next_question));

        assertFalse("Next Button is Enable", next.isEnabled());
        clickAndWaitForAnswer(TTChecks.ANSWER_SELECTED, CORRECT_ANS);
        assertTrue("Correct answer selected : ", next.isEnabled());

    }

}
