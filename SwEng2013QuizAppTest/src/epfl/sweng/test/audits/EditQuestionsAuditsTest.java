package epfl.sweng.test.audits;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.test.template.TestTemplate;

public class EditQuestionsAuditsTest extends TestTemplate<EditQuestionActivity> {
	
	private Solo solo;
	
	public EditQuestionsAuditsTest() {
		super(EditQuestionActivity.class);
	}

	public void testAuditError() {
		assertEquals(0, ((EditQuestionActivity) getActivity()).auditErrors());
	}
	
	public void testAuditEditText() {
		assertEquals(0, ((EditQuestionActivity) getActivity()).auditEditTexts());
	}
	
//	public void testAuditButtons() {
//		assertEquals(0, ((EditQuestionActivity) getActivity()).auditButtons());
//	}
//	
//	public void testAuditSubmitButton() {
//		assertEquals(0, ((EditQuestionActivity) getActivity()).auditSubmitButton());
//	}
//	
//	public void testAuditAnswers() {
//		assertEquals(0, ((EditQuestionActivity) getActivity()).auditAnswers());
//	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
	
}
