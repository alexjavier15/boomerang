package epfl.sweng.test.audits;

import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.template.TestTemplate;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;

public class EditQuestionsAuditsTest extends TestTemplate<EditQuestionActivity> {

    private Solo solo;

    public EditQuestionsAuditsTest() {
        super(EditQuestionActivity.class);
    }

    @UiThreadTest
    public void testAuditError() {

        Button plus = (Button) getActivity().findViewById(R.id.add_answer);
        plus.callOnClick();

        plus.callOnClick();
        plus.callOnClick();
        plus.callOnClick();
        Button bt = (Button) getActivity().findViewById(R.id.edit_buttonProperty);
        bt.setText(R.string.heavy_check_mark);
        Button bt1 = (Button) getSolo().getButton(0);
        bt1.setText(R.string.heavy_check_mark);
       

        Debug.out(((EditQuestionActivity) getActivity()).auditAnswers());

        assertTrue("audit", ((EditQuestionActivity) getActivity()).auditAnswers() == 0);

    }

    // public void testAuditButtons() {
    // assertEquals(0, ((EditQuestionActivity) getActivity()).auditButtons());
    // }
    //
    // public void testAuditSubmitButton() {
    // assertEquals(0, ((EditQuestionActivity) getActivity()).auditSubmitButton());
    // }
    //
    // public void testAuditAnswers() {
    // assertEquals(0, ((EditQuestionActivity) getActivity()).auditAnswers());
    // }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

}
