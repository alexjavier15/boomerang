package epfl.sweng.ShowQuestionActivity;

import java.io.File;

import android.test.ActivityInstrumentationTestCase2;
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
import epfl.sweng.testing.TestingTransaction;

public class ShowQuestionActivityTemplate extends ActivityInstrumentationTestCase2<ShowQuestionsActivity> {

    private Solo mSolo;
    private MockHttpClient mMock = new MockHttpClient();
    public final static String CORRECT_ANS = "Forty-two";
    public final static String DEFAULT_TAGS = "\"h2g2\", \"trivia\"";
    public final static String DEFAULT_QUESTION = "What is the answer to life, the universe, and everything?";

    public Solo getSolo() {

        return mSolo;
    }

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

    }

    protected void pushCannedResponse(
            String type, int httpStatus) {
        pushCannedResponse(type, httpStatus, DEFAULT_QUESTION, DEFAULT_TAGS);

    }

    protected void popCannedResponse() {

        mMock.popCannedResponse();
    }

    protected void pushCannedResponse(
            String type, int httpStatus, String question, String tags) {
        mMock.pushCannedResponse(type + " (?:https?://[^/]+|[^/]+)?/+quizquestions/random HTTP/1.1", httpStatus,
                "{\"question\": \"" + question + "\", "
                        + "\"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\", \"solutionIndex\":"
                        + " 0, \"tags\": [" + tags + "], \"id\": \"1\" }", "application/json");

    }

    protected void pushMalformedCannedResponse(
            String type, int httpStatus) {
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

    protected void getActivityAndWaitFor(
            final TestCoordinator.TTChecks expected) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                getActivity();

            }

            @Override
            public String toString() {
                return String.format("getActivityAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(
                    TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
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

    protected void clickAndWaitForButton(
            final TestCoordinator.TTChecks expected, final String button) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.clickOnButton(button);

            }

            @Override
            public String toString() {
                return String.format("getActivityAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(
                    TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    protected void enterTextAndWaitFor(
            final TestCoordinator.TTChecks expected, final EditText et, final String text) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.enterText(et, text);
            }

            @Override
            public String toString() {
                return String.format("getActivityAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(
                    TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    protected void clickAndWaitForAnswer(
            final TestCoordinator.TTChecks expected, final String answer) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.clickOnText(answer);
            }

            @Override
            public String toString() {
                return String.format("getActivityAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(
                    TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    protected void goBackAndWaitFor(
            final TestCoordinator.TTChecks expected) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.goBack();
            }

            @Override
            public String toString() {
                return String.format("getActivityAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(
                    TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

}
