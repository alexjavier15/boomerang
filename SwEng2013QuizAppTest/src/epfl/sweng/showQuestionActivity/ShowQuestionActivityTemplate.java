package epfl.sweng.showQuestionActivity;

import java.io.File;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.ITTCordinatorHelper;
import epfl.sweng.tools.TTCoordinatorUtility;

public class ShowQuestionActivityTemplate extends ActivityInstrumentationTestCase2<ShowQuestionsActivity> implements
        ITTCordinatorHelper {

    private Solo mSolo;
    private TTCoordinatorUtility mCoordinator;

    private MockHttpClient mMock = new MockHttpClient();
    public final static String CORRECT_ANS = "Forty-two";
    public final static String DEFAULT_TAGS = "\"h2g2\", \"trivia\"";
    public final static String DEFAULT_QUESTION = "What is the answer to life, the universe, and everything?";

    public ShowQuestionActivityTemplate() {
        super(ShowQuestionsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cleanUpData();
        CacheManager.reset();
        SwengHttpClientFactory.setInstance(mMock);
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();
        mSolo = new Solo(getInstrumentation());
        mCoordinator = new TTCoordinatorUtility(this, mSolo);
    }

    private void cleanUpData() {

        File postDB = QuizApp.getContexStatic().getDatabasePath(CacheManager.POST_SYNC_DB_NAME);
        File cacheDB = QuizApp.getContexStatic().getDatabasePath(CacheManager.QUESTION_CACHE_DB_NAME);
        postDB.delete();
        cacheDB.delete();
        QuizApp.getPreferences().edit().clear().commit();

    }

    public Solo getSolo() {

        return mSolo;
    }

    protected void pushCannedResponse(String type, int httpStatus) {
        pushCannedResponse(type, httpStatus, DEFAULT_QUESTION, DEFAULT_TAGS);

    }

    protected void popCannedResponse() {

        mMock.popCannedResponse();
    }

    protected void pushCannedResponse(String type, int httpStatus, String question, String tags) {
        mMock.pushCannedResponse(type + " (?:https?://[^/]+|[^/]+)?/+quizquestions/random HTTP/1.1", httpStatus,
                "{\"question\": \"" + question + "\", "
                        + "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
                        + " 0, \"tags\": [" + tags + "], \"id\": \"1\" }", "application/json");

    }

    protected void pushMalformedCannedResponse(String type, int httpStatus) {
        pushCannedResponse(type, httpStatus, "\"" + "}*****", "h2g2\", \"trivia\"");

    }

    protected void chooseCorrectAnswer() {
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
        clickAndWaitForAnswer(TTChecks.ANSWER_SELECTED, CORRECT_ANS);

    }

    protected void chooseBadAnswer() {
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
        clickAndWaitForAnswer(TTChecks.ANSWER_SELECTED, "Twenty-seven");

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

    public void goBackAndWaitFor(final TestCoordinator.TTChecks expected) {
        mCoordinator.goBackAndWaitFor(expected);
    }

    @Override
    public void clickAndGetToastAndWaitFor(TTChecks expected, final String button, String text) {
        mCoordinator.clickAndGetToastAndWaitFor(expected, button, text);
    }
    @Override
    public void clickAndWaitFor(TTChecks expected, View view) {
        mCoordinator.clickAndWaitFor(expected, view);
    }
}
