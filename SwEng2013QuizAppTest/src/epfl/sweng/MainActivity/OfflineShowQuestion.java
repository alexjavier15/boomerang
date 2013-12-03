package epfl.sweng.MainActivity;

import android.view.View;
import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class OfflineShowQuestion extends MainActivityTemplate {
    

    /* (non-Javadoc)
     * @see epfl.sweng.MainActivity.MainActivityTemplate#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
    }

    public void goOfflineAndShowQuestion() {
        
        getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);

        View check = getActivity().findViewById(R.id.offline_mode);
        clickAndWaitFor(TTChecks.OFFLINE_CHECKBOX_ENABLED, check);
        View showq = getActivity().findViewById(R.id.show_question);
        clickAndWaitFor(TTChecks.QUESTION_SHOWN, showq);
        getSolo().goBack();
        getActivity().finish();

    }

}
