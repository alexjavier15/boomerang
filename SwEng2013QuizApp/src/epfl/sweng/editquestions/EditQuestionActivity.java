package epfl.sweng.editquestions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

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
	private ListView listView;
	private AnswerAdapter adapter;
	private ArrayList<Answer> fetch = new ArrayList<Answer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);

		/**
		 * add item in arraylist
		 */
		Answer firstAnswer = new Answer(getResources().getString(
				R.string.heavy_ballot_x), "", getResources().getString(
				R.string.hyphen_minus));
		fetch.add(firstAnswer);
		/**
		 * set item into adapter
		 */
		adapter = new AnswerAdapter(EditQuestionActivity.this, R.id.listview,
				fetch);
		adapter.setNotifyOnChange(true);
		listView = (ListView) findViewById(R.id.listview);
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
		adapter.add(temp);
		adapter.notifyDataSetChanged();
	}

	/**
	 * Called when the user clicks on the button labeled "Submit."
	 * 
	 * @param view
	 */
	public void submitQuestion(View view) {
		if (isValid()) {
			String question = " \"question\": \""
					+ ((EditText) listView.findViewById(R.id.edit_questionText))
							.getText().toString() + "\",";
			String answers = " \"answers\":" + fetch.toString() + ",";
			String solutionIndex = " \"solutionIndex\": "
					+ adapter.getWhoIsChecked() + ",";
			String tags = " \"tags\": "
					+ ((EditText) listView.findViewById(R.id.edit_tagsText))
							.getText().toString();

			try {
				StringEntity entity = new StringEntity("{" + question + answers
						+ solutionIndex + tags + " }");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
			System.out
					.println("The question is empty or the list size is smaller than 2!");
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
			if (answer.getAnswer().trim().equals("")) {
				System.out.println("The answer is empty!");
				return false;
			}
			System.out.println("This one has the answer : "
					+ answer.getAnswer());
		}
		System.out.println("All correct!");
		return correctAnswer == 1;
	}
}
