package epfl.sweng.editquestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.JSONParser;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;

/**
 * 
 * @author CanGuzelhan & LorenzoLeon
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
			String questionString = ((EditText) findViewById(R.id.edit_questionText))
					.getText().toString();
			List<String> answers = new LinkedList<String>();
			int solIndex = 0;
			boolean check = true;
			for (Answer answer : fetch) {
				answers.add(answer.getAnswer());
				if (check) {
					if (answer.getChecked()
							.equals(getResources().getString(
									R.string.heavy_check_mark))) {
						check = false;
					} else {
						solIndex++;
					}
				}
			}
			String[] arrayStringTags = ((EditText) findViewById(R.id.edit_tagsText))
					.getText().toString().split("\\s*([a-zA-Z]+)[\\s.,]*");
			Set<String> tags = new HashSet<String>(
					Arrays.asList(arrayStringTags));
			QuizQuestion question = new QuizQuestion(-1, questionString,
					answers, solIndex, tags);
			JSONObject jObject;

			try {
				jObject = JSONParser.parseQuiztoJSON(question);
				HttpCommunications.postQuestion(HttpCommunications.URLPUSH,
						jObject);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						this,
						"Your submission was NOT successful. Problem with the connection.",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			Toast.makeText(this, "Your submission was successful!",
					Toast.LENGTH_SHORT).show();
			//send the question TODO
			TestingTransactions.check(TTChecks.NEW_QUESTION_SUBMITTED);
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
