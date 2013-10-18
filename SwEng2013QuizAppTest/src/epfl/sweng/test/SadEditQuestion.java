package epfl.sweng.test;

import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.testing.TestCoordinator.TTChecks;

public class SadEditQuestion extends TestTemplate<EditQuestionActivity> {

	public SadEditQuestion() {
		super(EditQuestionActivity.class);
	}
	
	public void testServerNotAccessible() {
		getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);
		
	}
}
