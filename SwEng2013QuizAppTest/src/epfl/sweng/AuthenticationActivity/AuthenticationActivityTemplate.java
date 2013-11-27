package epfl.sweng.AuthenticationActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestingTransaction;

public class AuthenticationActivityTemplate extends ActivityInstrumentationTestCase2<AuthenticationActivity> {

    private Solo mSolo;

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

    protected void clickAndWaitForButton(final TestCoordinator.TTChecks expected, final String button) {
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
                mSolo.enterText(et, text);
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
                mSolo.clickOnText(answer);
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
                mSolo.goBack();
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

}
