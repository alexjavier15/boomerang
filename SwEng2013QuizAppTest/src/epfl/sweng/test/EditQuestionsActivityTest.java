package epfl.sweng.test;

import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import epfl.sweng.editquestions.EditQuestionActivity;

public class EditQuestionsActivityTest extends TestTemplate<EditQuestionActivity> {

    private final static int NUM_ANSWERS = 10;
    private String answer1 = "à cause de la cigarrette";
    private String answer2 = "de naissance";
    private String question = "Pourquoi suis je si con?";
    private Button submit;

    public EditQuestionsActivityTest() {
        super(EditQuestionActivity.class);

    }

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

//	public void testButtonsMustBeDisplayed() {
//		assertTrue("EditText for the question", getSolo().searchText("Type in the question"));
//		assertTrue("EditText for an answer", getSolo().searchText("Type in the answer"));
//		assertTrue("Button to remove answer", getSolo().searchButton("-"));
//		assertTrue("Button to add an answer", getSolo().searchButton("+"));
//		assertTrue("EditText for the tags", getSolo().searchText("tags"));
//		assertTrue("Button to submit", getSolo().searchButton("Submit"));
//
//		submit = getSolo().getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//	}
//
//	@UiThreadTest
//	public void testButtonStayDissabled() {
//		EditText questionT = getSolo().getEditText("question");
//		questionT.setText("test question");
//		assertFalse("question body must be displayed", questionT.getText().toString().equals(question));
//		questionT.setText(question);
//		assertTrue("question body must be displayed", questionT.getText().toString().equals(question));
//
//		submit = getSolo().getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//		EditText answerT = getSolo().getEditText("answer");
//		answerT.setText(answer1);
//		assertTrue("answer must be displayed", answerT.getText().toString().equals(answer1));
//		submit = getSolo().getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//		EditText tagsT = getSolo().getEditText("tag");
//		tagsT.setText("test");
//		assertFalse("answer must be displayed", tagsT.getText().toString().equals(" test"));
//		assertTrue("answer must be displayed", tagsT.getText().toString().equals("test"));
//		submit = getSolo().getButton("Submit");
//		assertFalse("Submit button should be disabled", submit.isEnabled());
//
//	}

	public void testAddMultipleanswers() {
		for (int i = 0; i < 10; i++) {
			getSolo().clickOnButton("+");
			EditText answerT = getSolo().getEditText("answer");
			if (i % 2 == 0) {
				getSolo().enterText(answerT, answer2);
			} else {
				getSolo().enterText(answerT, answer1);
			}
			//answers.add(answerT);
		}
		submit = getSolo().getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());

		//Button cross = getSolo().getButton(R.id.edit_buttonProperty);
		getSolo().clickOnButton(0);
		//for (int i=0; i<1000000000; i++){};
		submit = getSolo().getButton("Submit");
		assertFalse("Submit button should be disabled", submit.isEnabled());
    }
}
