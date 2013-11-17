package epfl.sweng.test.audits;

import android.test.UiThreadTest;
import android.widget.Button;
import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.template.TestTemplate;

public class EditQuestionsAuditsTest extends TestTemplate<EditQuestionActivity> {

    private Solo solo;

    public EditQuestionsAuditsTest() {
        super(EditQuestionActivity.class);
    }

    @UiThreadTest
    public void testAuditError() {
        EditQuestionActivity edit = (EditQuestionActivity) getActivity();

        Button plus = (Button) getSolo().getButton("+");
        plus.callOnClick();

        plus.callOnClick();
        plus.callOnClick();
        plus.callOnClick();
        Button bt = (Button) edit.findViewById(R.id.edit_buttonProperty);
        bt.setText(R.string.heavy_check_mark);
        Button bt1 = (Button) getSolo().getButton(0);
        bt1.setText(R.string.heavy_check_mark);

        assertTrue("audit", ((EditQuestionActivity) edit).auditAnswers() == 0);
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
