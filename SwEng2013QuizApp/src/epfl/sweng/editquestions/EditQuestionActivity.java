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
	private AnswerAdapter adapter;
	private ArrayList<Answer> fetch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);

		final ListView listView = (ListView) findViewById(R.id.listview);
		Answer firstAnswer = new Answer(getResources().getString(
				R.string.heavy_ballot_x), null, getResources().getString(
				R.string.hyphen_minus));

		fetch = new ArrayList<Answer>();
		fetch.add(firstAnswer);
		adapter = new AnswerAdapter(this, R.id.listview, fetch);
		adapter.notifyDataSetChanged();
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
				R.string.heavy_ballot_x), null, getResources().getString(
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
		int correct = 0;
		if (questionText.getText().toString().trim().equals("")
				|| fetch.size() < 2) {
			return false;
		}
		for (Answer answer : fetch) {
			if (answer.getChecked().equals(
					getResources().getString(R.string.heavy_check_mark))) {
				correct++;
			}
			if (answer.getAnswer().trim().equals("")) {
				return false;
			}
		}
		// removed parenthesis on return
		return correct == 1;
	}
}
