package epfl.sweng.MainActivity;

import java.io.File;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.Tools.ITTCordinatorHelper;
import epfl.sweng.Tools.TTCoordinatorUtility;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.entry.MainActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class MainActivityTemplate extends ActivityInstrumentationTestCase2<MainActivity> implements
        ITTCordinatorHelper {

    public final static String TAGS_DEF = "debile, alex";
    public final static String ANSWER1_DEF = "a cause de la cigarrette";
    public final static String ANSWER2_DEF = "de naissance";
    public final static String QUESTION_DEF = "Pourquoi suis je si con?";
    private Solo mSolo;
    private TTCoordinatorUtility mCoordinator;
    private MockHttpClient mMock = new MockHttpClient();

    public MainActivityTemplate() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cleanUpData();
        CacheManager.reset();
        mSolo = new Solo(getInstrumentation());
        mCoordinator = new TTCoordinatorUtility(this, mSolo);
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
        cleanUpData();

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

    /**
     * @return the mMock
     */
    public MockHttpClient getMock() {
        return mMock;
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

    public void pushCannedResponse(int httpStatus) {

        mMock.pushCannedResponse("POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
                httpStatus, "{\"question\": \"Pourquoi je suis si con?\","
                        + " \"answers\": [\"A cause de la cigarette\", \"Je ne suis pas con\", \"De naissance\"],"
                        + " \"solutionIndex\": 1, \"tags\": [\"stupid\", \"alex\"], \"id\": \"-1\" }",
                "application/json");

    }

    protected void fillCorrectQuizQuestion() {

        getSolo().clickOnButton(0);
        getSolo().enterText(getSolo().getEditText(0), QUESTION_DEF);
        getSolo().enterText(1, ANSWER2_DEF);
        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        EditText et = getSolo().getEditText(2);
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et, ANSWER1_DEF);
        getSolo().enterText(getSolo().getEditText(3), TAGS_DEF);

    }

}
