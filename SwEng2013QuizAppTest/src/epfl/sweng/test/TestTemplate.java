package epfl.sweng.test;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.minimalmock.MockHttpClient;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestingTransaction;

@SuppressWarnings("rawtypes")
public class TestTemplate<T> extends ActivityInstrumentationTestCase2 {

    private MockHttpClient httpClient;
    private Activity myActivity;
    private Solo mSolo;

    @SuppressWarnings("unchecked")
    public TestTemplate(Class activityClass) {
        super(activityClass);
    }

    protected void getActivityAndWaitFor(final TestCoordinator.TTChecks expected) {
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
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                    expected, notification);
            }
        });
    }
    protected void waitFor(final TestCoordinator.TTChecks expected) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                
            }

            @Override
            public String toString() {
                return String.format("getActivityAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                    expected, notification);
            }
        });
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        httpClient = new MockHttpClient();
        httpClient.pushCannedResponse("GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random\\b", HttpStatus.SC_OK,
            "{\"question\": \"What is the answer to life, the universe, and everything?\","
                + " \"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\","
                + " \"solutionIndex\": 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }", "application/json");
        SwengHttpClientFactory.setInstance(httpClient);
        setSolo(new Solo(getInstrumentation()));
    }

    /**
     * @return the myActivity
     */
    public Activity getMyActivity() {
        return myActivity;
    }

    /**
     * @return the solo
     */
    public Solo getSolo() {
        return mSolo;
    }
    
    public MockHttpClient getMock() {
    	return httpClient;
    }

    /**
     * @param solo the solo to set
     */
    public void setSolo(Solo solo) {
        this.mSolo = solo;
    }

}
