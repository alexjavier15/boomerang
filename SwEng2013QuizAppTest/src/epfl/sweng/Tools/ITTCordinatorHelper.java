package epfl.sweng.Tools;

import android.widget.EditText;
import epfl.sweng.testing.TestCoordinator;

public interface ITTCordinatorHelper {

    void getActivityAndWaitFor(final TestCoordinator.TTChecks expected);

    void clickAndWaitForButton(final TestCoordinator.TTChecks expected, final String button);

    void enterTextAndWaitFor(final TestCoordinator.TTChecks expected, final EditText et, final String text);

    void clickAndWaitForAnswer(final TestCoordinator.TTChecks expected, final String answer);

    void goBackAndWaitFor(final TestCoordinator.TTChecks expected);

    void clickAndGetToastAndWaitFor(final TestCoordinator.TTChecks expected, final String button, final String text );

}