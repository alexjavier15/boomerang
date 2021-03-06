package epfl.sweng.tools;

import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.testing.TestingTransaction;

public class TTCoordinatorUtility extends TestCase implements ITTCordinatorHelper {

    private ActivityInstrumentationTestCase2<?> mTest;
    private Solo mSolo;

    /**
     * @param mTest
     * @param mSolo
     */
    public TTCoordinatorUtility(ActivityInstrumentationTestCase2<?> mTest, Solo mSolo) {
        super();
        this.mTest = mTest;
        this.mSolo = mSolo;
    }

    @Override
    public void clickAndGetToastAndWaitFor(final TestCoordinator.TTChecks expected, final String button,
            final String text) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            boolean isTextShown = false;

            @Override
            public void initiate() {
                mTest.getActivity();
                mSolo.clickOnButton(button);

            }

            @Override
            public String toString() {
                return String.format("clickAndGetToastAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                isTextShown = mSolo.searchText(text);
                assertEquals("Expected a Toast with text : " + text, true, isTextShown);
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    @Override
    public void getActivityAndWaitFor(final TestCoordinator.TTChecks expected) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mTest.getActivity();
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
    public void clickAndWaitForButton(final TestCoordinator.TTChecks expected, final String button) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.clickOnButton(button);
            }

            @Override
            public String toString() {
                return String.format("clickAndWaitForButton(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    @Override
    public void enterTextAndWaitFor(final TestCoordinator.TTChecks expected, final EditText et, final String text) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.enterText(et, text);
            }

            @Override
            public String toString() {
                return String.format("enterTextAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    @Override
    public void clickAndWaitForAnswer(final TestCoordinator.TTChecks expected, final String answer) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.clickLongOnView(mSolo.getText(answer));
            }

            @Override
            public String toString() {
                return String.format("clickAndWaitForAnswer(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    @Override
    public void goBackAndWaitFor(final TestCoordinator.TTChecks expected) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.sendKey(KeyEvent.KEYCODE_BACK);

            }

            @Override
            public String toString() {
                return String.format("goBackAndWaitFor(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    public void clickAndWaitForCheckBox(final TTChecks expected) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.clickOnCheckBox(0);
            }

            @Override
            public String toString() {
                return String.format("clickAndWaitForOnffline(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });
    }

    @Override
    public void clickAndWaitFor(final TTChecks expected, final View view) {
        TestCoordinator.run(mTest.getInstrumentation(), new TestingTransaction() {
            @Override
            public void initiate() {
                mSolo.clickOnView(view);
            }

            @Override
            public String toString() {
                return String.format("clickAndWaitForOnffline(%s)", expected);
            }

            @Override
            public void verify(TestCoordinator.TTChecks notification) {
                assertEquals(String.format("Expected notification %s, but received %s", expected, notification),
                        expected, notification);
            }
        });

    }

}
