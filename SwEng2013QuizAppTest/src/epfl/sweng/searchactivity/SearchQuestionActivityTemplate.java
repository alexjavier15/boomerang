package epfl.sweng.searchactivity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.searchquestions.SearchActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.ITTCordinatorHelper;
import epfl.sweng.tools.TTCoordinatorUtility;

public class SearchQuestionActivityTemplate extends ActivityInstrumentationTestCase2<SearchActivity> implements
        ITTCordinatorHelper {

    private Solo mSolo;
    private TTCoordinatorUtility mCoordinator;

    public Solo getSolo() {

        return mSolo;
    }

    public SearchQuestionActivityTemplate() {
        super(SearchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation());
        mCoordinator = new TTCoordinatorUtility(this, getSolo());

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        QuizApp.getPreferences().edit().clear().commit();
    }

    public void getActivityAndWaitFor(final TestCoordinator.TTChecks expected) {
        mCoordinator.getActivityAndWaitFor(expected);
    }

    public void clickAndWaitForButton(final TestCoordinator.TTChecks expected, final String button) {
        mCoordinator.clickAndWaitForButton(expected, button);
    }

    public void enterTextAndWaitFor(final TestCoordinator.TTChecks expected, final EditText et, final String text) {
        mCoordinator.enterTextAndWaitFor(expected, et, text);
    }

    public void clickAndWaitForAnswer(final TestCoordinator.TTChecks expected, final String answer) {
        mCoordinator.clickAndWaitForAnswer(expected, answer);
    }

    @Override
    public void goBackAndWaitFor(TTChecks expected) {
        mCoordinator.goBackAndWaitFor(expected);
    }

    @Override
    public void clickAndGetToastAndWaitFor(TTChecks expected, String button, String text) {
        mCoordinator.clickAndGetToastAndWaitFor(expected, button, text);
    }
    @Override
    public void clickAndWaitFor(TTChecks expected, View view) {
        mCoordinator.clickAndWaitFor(expected, view);
    }

}
