package epfl.sweng.editquestions;

import java.util.ArrayList;

import epfl.sweng.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class EditQuestionActivity extends Activity {
	private AnswerAdapter adapter;
	private ListView listView;
	private ArrayList<Answer> fetch = new ArrayList<Answer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);
		Answer firstAnswer = new Answer(getResources().getString(
				R.string.heavy_ballot_x), "", getResources().getString(
				R.string.hyphen_minus));
		fetch.add(firstAnswer);

		listView = (ListView) findViewById(R.id.listview);
		adapter = new AnswerAdapter(EditQuestionActivity.this, R.id.listview,
				fetch);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_question, menu);
		return true;
	}

	public void addNewSlot(View view) {
		Answer temp = new Answer(getResources().getString(
				R.string.heavy_ballot_x), "", getResources().getString(
				R.string.hyphen_minus));
		fetch.add(temp);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Called when the user clicks on the button labeled "Submit."
	 * 
	 * @param view
	 */
	public void submitQuestion(View view) {
		if (isValid()) {
			Toast.makeText(this, "Your submission was successful!",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(
					this,
					"Your submission was NOT successful. Please check that you filled in all fields.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public boolean isValid() {
		EditText questionText = (EditText) findViewById(R.id.edit_questionText);
		int correctAnswer = 0;
		System.out.println("Checking the question...");
		if (questionText.getText().toString().trim().equals("")
				|| fetch.size() < 2) {
			System.out.println("The question is empty or not enough slot!");
			return false;
		}
		System.out
				.println("The question is valid!\nChecking the answers with fetch size : "
						+ fetch.size());
		for (Answer answer : fetch) {
			if (answer.getChecked().equals(
					getResources().getString(R.string.heavy_check_mark))) {
				System.out.println("This one has the check mark!");
				correctAnswer++;
			}
			System.out
					.println("We got " + answer.getAnswer() + "as an answer.");
			if (answer.getAnswer().trim().equals("")) {
				System.out.println("The answer is empty!");
				return false;
			}
		}
		System.out.println("All correct!");
		return correctAnswer == 1;
	}
}
