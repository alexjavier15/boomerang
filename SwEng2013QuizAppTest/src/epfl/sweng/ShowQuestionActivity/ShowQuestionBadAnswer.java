package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import android.widget.Button;
import epfl.sweng.R;

public class ShowQuestionBadAnswer extends ShowQuestionActivityTemplate {

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.ShowQuestionActivity.ShowQuestionActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        pushCannedResponse("GET", HttpStatus.SC_OK);

    }

    public void testBadAnswerNextDisable() {
        chooseBadAnswer();
        Button next = getSolo().getButton(getActivity().getResources().getString(R.string.next_question));

        assertFalse("Next Button is Enable", next.isEnabled());

    }

}
