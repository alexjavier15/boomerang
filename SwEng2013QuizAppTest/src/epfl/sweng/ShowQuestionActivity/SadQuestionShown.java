package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * 
 * @author LorenzoLeon
 * 
 */
public class SadQuestionShown extends ShowQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pushCannedResponse("GET", HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    public void testMessageDisplayedIfNoServerResponse() {
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);

    }

}
