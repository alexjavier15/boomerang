package epfl.sweng.AuthenticationActivity;

import org.apache.http.HttpStatus;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestingTransaction;

public class AuthenticationActivityTemplate extends ActivityInstrumentationTestCase2<AuthenticationActivity> {

    private Solo mSolo;
    private MockHttpClient mMock = new MockHttpClient();
    public final static int TEQUILA_REQUEST_STATUS= HttpStatus.SC_MOVED_TEMPORARILY;
    public final static int OTHER_STATUS= HttpStatus.SC_OK;
    
    

    public AuthenticationActivityTemplate() {
        super(AuthenticationActivity.class);
    }

    /**
     * @return the mSolo
     */
    public Solo getSolo() {
        return mSolo;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation());

        SwengHttpClientFactory.setInstance(mMock);
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "").apply();

    }

    protected void pushCannedPostTequilaToken(
            int httpStatus) {
        mMock.pushCannedResponse("POST https://tequila.epfl.ch/cgi-bin/tequila/login HTTP/1.1", httpStatus,
                "{\"requestkey\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\"," + " \"username\": \"test\","
                        + " \"password\": \"password\"}", "application/json");

    }

  

    protected void pushCannedPostRequestSessionID(
            int httpStatus) {
        mMock.pushCannedResponse("POST https://sweng-quiz.appspot.com/login\\b", httpStatus,
                "{\"session\": \"test\","
                        + " \"message\": \"Here's your session id. Please include the following HTTP"
                        + " header in your subsequent requests:\n Authorization: Tequila test\"}", "application/json");

    }

    protected void pushCannedGetSwengtoken(
            int httpStatus) {
        mMock.pushCannedResponse("GET (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/login\\b", httpStatus,
                "{\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
                        + " \"message\": \"Here's your authentication token. Please validate it with Tequila"
                        + " at https://tequila.epfl.ch/cgi-bin/tequila/login\"}", "application/json");

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
