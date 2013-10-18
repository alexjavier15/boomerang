package epfl.sweng.test;

import java.util.ArrayList;

import android.test.UiThreadTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.testing.Debug;

public class EditQuestionsActivityTest extends TestTemplate<EditQuestionActivity> {

	Button submit;
	String question = "Pourquoi suis je si con?";
	String answer1 = "à cause de la cigarrette";
	String answer2 = "de naissance";
	String answer3 = "pourquoi pas?";
	ArrayList<EditText> answers = new ArrayList<EditText>();

	public EditQuestionsActivityTest() {
		super(EditQuestionActivity.class);

	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

//	public void testButtonsMustBeDisplayed() {
//		assertTrue("EditText for the question", solo.searchText("Type in the question"));
//		assertTrue("EditText for an answer", solo.searchText("Type in the answer"));
//		assertTrue("Button to remove answer", solo.searchButton("-"));
//		assertTrue("Button to add an answer", solo.searchButton("+"));
//		assertTrue("EditText for the tags", solo.searchText("tags"));
//		assertTrue("Button to submit", solo.searchButton("Submit"));
//
//		submit = solo.getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//	}
//
//	@UiThreadTest
//	public void testButtonStayDissabled() {
//		EditText questionT = solo.getEditText("question");
//		questionT.setText("test question");
//		assertFalse("question body must be displayed", questionT.getText().toString().equals(question));
//		questionT.setText(question);
//		assertTrue("question body must be displayed", questionT.getText().toString().equals(question));
//
//		submit = solo.getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//		EditText answerT = solo.getEditText("answer");
//		answerT.setText(answer1);
//		assertTrue("answer must be displayed", answerT.getText().toString().equals(answer1));
//		submit = solo.getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//		EditText tagsT = solo.getEditText("tag");
//		tagsT.setText("test");
//		assertFalse("answer must be displayed", tagsT.getText().toString().equals(" test"));
//		assertTrue("answer must be displayed", tagsT.getText().toString().equals("test"));
//		submit = solo.getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//
//	}

	public void testAddMultipleanswers() {
		for (int i = 0; i < 10; i++) {
			solo.clickOnButton("+");
			EditText answerT = solo.getEditText("answer");
			if (i % 2 == 0) {
				solo.enterText(answerT, answer2);
			} else {
				solo.enterText(answerT, answer1);
			}
			//answers.add(answerT);
		}
		submit = solo.getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());

		//Button cross = solo.getButton(R.id.edit_buttonProperty);
		solo.clickOnButton(0);
		//for (int i=0; i<1000000000; i++){};
		submit = solo.getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());

	}
}
