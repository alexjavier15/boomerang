package epfl.sweng.test;

import android.widget.Button;
import epfl.sweng.editquestions.EditQuestionActivity;

public class EditQuestionsActivityTest extends TestTemplate<EditQuestionActivity> {

	Button submit;
	
	public EditQuestionsActivityTest() {
		super(EditQuestionActivity.class);
	}
	
	public void testButtonsMustBeDisplayed() {
		getActivity();
		assertTrue("EditText for the question", solo.searchText("question"));
		assertTrue("EditText for an answer", solo.searchText("answer"));
		assertTrue("Button to remove answer", solo.searchButton("-"));
		assertTrue("Button to add an answer", solo.searchButton("+"));
		assertTrue("EditText for the tags", solo.searchText("tags"));
		assertTrue("Button to submit", solo.searchButton("Submit"));
		
		submit = solo.getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());
	}
}
