package epfl.sweng.SearchActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.Tools.TTCoordinatorUtility;
import epfl.sweng.searchquestions.SearchActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.testing.TestCoordinator;

public class SearchQuestionActivityTemplate extends ActivityInstrumentationTestCase2<SearchActivity> {

    private Solo mSolo;
    private TTCoordinatorUtility mCoordinator;

    public Solo getSolo() {

        return mSolo;
    }

    public SearchQuestionActivityTemplate() {
        super(SearchActivity.class);
        mCoordinator = new TTCoordinatorUtility(this, getSolo());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.test.ActivityInstrumentationTestCase2#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
        QuizApp.getPreferences().edit().clear().commit();

    }

    protected void getActivityAndWaitFor(final TestCoordinator.TTChecks expected) {
        mCoordinator.getActivityAndWaitFor(expected);
    }

    protected void clickAndWaitForButton(final TestCoordinator.TTChecks expected, final String button) {
        mCoordinator.clickAndWaitForButton(expected, button);
    }

    protected void enterTextAndWaitFor(final TestCoordinator.TTChecks expected, final EditText et, final String text) {
        mCoordinator.enterTextAndWaitFor(expected, et, text);
    }

    protected void clickAndWaitForAnswer(final TestCoordinator.TTChecks expected, final String answer) {
        mCoordinator.clickAndWaitForAnswer(expected, answer);
    }

}
