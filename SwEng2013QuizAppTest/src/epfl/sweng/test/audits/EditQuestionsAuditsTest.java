package epfl.sweng.test.audits;
import android.test.ActivityInstrumentationTestCase2;
import epfl.sweng.editquestions.EditQuestionActivity;

public class EditQuestionsAuditsTest extends ActivityInstrumentationTestCase2<EditQuestionActivity> {

	public EditQuestionsAuditsTest() {
		super(EditQuestionActivity.class);
	}

	public void testAuditError() {
		assertEquals(0, ((EditQuestionActivity) getActivity()).auditErrors());
	}

}
