package epfl.sweng.test.template;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestingTransaction;

@SuppressWarnings("rawtypes")
public class TestTemplate<T> extends ActivityInstrumentationTestCase2 {

    //private MockHttpClient httpClient;
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
    
    protected void clickAndWaitForButton(final TestCoordinator.TTChecks expected, final String button) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                getSolo().clickOnButton(button);
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
    
    protected void enterTextAndWaitFor(final TestCoordinator.TTChecks expected, final EditText et, final String text) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                getSolo().enterText(et, text);
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
    
    protected void clickAndWaitForAnswer(final TestCoordinator.TTChecks expected, final String answer) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                getSolo().clickOnText(answer);
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
    
    protected void goBackAndWaitFor(final TestCoordinator.TTChecks expected) {
        TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                getSolo().goBack();
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
        mSolo = new Solo(getInstrumentation());
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

}
