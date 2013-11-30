package epfl.sweng.AuthenticationActivity;

import org.apache.http.HttpStatus;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.Tools.ITTCordinatorHelper;
import epfl.sweng.Tools.TTCoordinatorUtility;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.test.minimalmock.MockHttpClient;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class AuthenticationActivityTemplate extends ActivityInstrumentationTestCase2<AuthenticationActivity>
        implements ITTCordinatorHelper {

    private Solo mSolo;
    private TTCoordinatorUtility mCoordinator;
    private MockHttpClient mMock = new MockHttpClient();
    public final static int TEQUILA_REQUEST_STATUS = HttpStatus.SC_MOVED_TEMPORARILY;

    public AuthenticationActivityTemplate() {
        super(AuthenticationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSolo = new Solo(getInstrumentation());
        mCoordinator = new TTCoordinatorUtility(this, mSolo);
        SwengHttpClientFactory.setInstance(mMock);
        QuizApp.getPreferences().edit().putString(PreferenceKeys.SESSION_ID, "").apply();

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

    /**
     * @return the mSolo
     */
    public Solo getSolo() {
        return mSolo;
    }

    /**
     * @return the mMock
     */
    public MockHttpClient getmMock() {
        return mMock;
    }

    protected void pushCannedGetSwengtoken(int httpStatus) {
        mMock.pushCannedResponse("GET (?:https?://[^/]+|[^/]+)?/+sweng-quiz.appspot.com/login\\b", httpStatus,
                "{\"token\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\","
                        + " \"message\": \"Here's your authentication token. Please validate it with Tequila"
                        + " at https://tequila.epfl.ch/cgi-bin/tequila/login\"}", "application/json");

    }

    protected void pushCannedPostTequilaToken(int httpStatus) {
        mMock.pushCannedResponse("POST https://tequila.epfl.ch/cgi-bin/tequila/login HTTP/1.1", httpStatus,
                "{\"requestkey\": \"rqtvk5d3za2x6ocak1a41dsmywogrdlv5\"," + " \"username\": \"test\","
                        + " \"password\": \"password\"}", "application/json");

    }

    protected void pushCannedPostRequestSessionID(int httpStatus) {
        mMock.pushCannedResponse("POST https://sweng-quiz.appspot.com/login\\b", httpStatus,
                "{\"session\": \"test\","
                        + " \"message\": \"Here's your session id. Please include the following HTTP"
                        + " header in your subsequent requests:\n Authorization: Tequila test\"}", "application/json");

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
    public void clickAndGetToastAndWaitFor(TTChecks expected, final String button, String text) {
        mCoordinator.clickAndGetToastAndWaitFor(expected, button, text);

    }

}
