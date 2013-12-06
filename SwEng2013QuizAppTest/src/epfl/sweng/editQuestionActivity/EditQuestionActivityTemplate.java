package epfl.sweng.editQuestionActivity;

import java.io.File;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.R;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.ITTCordinatorHelper;
import epfl.sweng.tools.TTCoordinatorUtility;

public class EditQuestionActivityTemplate extends ActivityInstrumentationTestCase2<EditQuestionActivity> implements
        ITTCordinatorHelper {

    private Solo mSolo;
    private TTCoordinatorUtility mCoordinator;
    public final static String TAGS_DEF = "debile, alex";
    public final static String ANSWER1_DEF = "a cause de la cigarrette";
    public final static String ANSWER2_DEF = "de naissance";
    public final static String QUESTION_DEF = "Pourquoi suis je si con?";
    private MockHttpClient mMock = new MockHttpClient();

    public EditQuestionActivityTemplate() {
        super(EditQuestionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        cleanUpData();
        CacheManager.reset();
        mSolo = new Solo(getInstrumentation());

        mCoordinator = new TTCoordinatorUtility(this, mSolo);
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true);
    }

    @Override
    protected void tearDown() throws Exception {
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

    /**
     * @return the mSolo
     */
    public Solo getSolo() {
        return mSolo;
    }

    /**
     * @return the mMock
     */
    public MockHttpClient getMock() {
        return mMock;
    }

    protected void popCannedResponse() {

        mMock.popCannedResponse();
    }

    protected void pushCannedResponse(int httpStatus) {

        mMock.pushCannedResponse("POST (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/quizquestions/ HTTP/1.1",
                httpStatus, "{\"question\": \"Pourquoi je suis si con?\","
                        + " \"answers\": [\"A cause de la cigarette\", \"Je ne suis pas con\", \"De naissance\"],"
                        + " \"solutionIndex\": 1, \"tags\": [\"stupid\", \"alex\"], \"id\": \"-1\" }",
                "application/json");

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

    protected void fillCorrectQuizQuestion() {

        clickAndWaitForButton(TTChecks.QUESTION_EDITED,
                getActivity().getResources().getString(R.string.heavy_ballot_x));
        EditText q = getSolo().getEditText("body");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, q, QUESTION_DEF);
        EditText et1 = getSolo().getEditText("answer");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et1, ANSWER2_DEF);
        clickAndWaitForButton(TTChecks.QUESTION_EDITED, "+");
        EditText et2 = getSolo().getEditText("answer");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, et2, ANSWER1_DEF);
        EditText t = getSolo().getEditText("tags");
        enterTextAndWaitFor(TTChecks.QUESTION_EDITED, t, TAGS_DEF);

    }
}
