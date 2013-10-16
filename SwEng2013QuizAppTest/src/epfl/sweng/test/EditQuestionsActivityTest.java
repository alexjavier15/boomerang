package epfl.sweng.test;

import android.widget.EditText;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.questions.QuizQuestion;

public class EditQuestionsActivityTest extends TestTemplate<EditQuestionActivity> {
	
	QuizQuestion question = createQuestionUniverse();

	public EditQuestionsActivityTest() {
		super(EditQuestionActivity.class);
	}
	
	public void testSubmitQuestion() {
		getActivity();
		solo.enterText((EditText) getActivity().findViewById(R.id.edit_questionText), question.getQuestion());
	}
}
