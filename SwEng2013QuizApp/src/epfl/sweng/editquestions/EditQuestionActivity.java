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
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This activity enables the user to submit new quiz questions.
 * 
 * @author CanGuzelhan & LorenzoLeon
 * 
 */
public class EditQuestionActivity extends Activity {
	private ListView listView;
	private AnswerAdapter adapter;
	private ArrayList<Answer> fetch = new ArrayList<Answer>();
	private boolean reset = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);

		Answer firstAnswer = new Answer(getResources().getString(
				R.string.heavy_ballot_x), "", getResources().getString(
						R.string.hyphen_minus));

		fetch.add(firstAnswer);
		adapter = new AnswerAdapter(EditQuestionActivity.this, R.id.listview,
				fetch);
		adapter.setNotifyOnChange(true);
		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);

		TextWatcher watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (!reset) {
					System.out.println("Testing hooks");
					TestingTransactions.check(TTChecks.QUESTION_EDITED);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};

		EditText questionText = (EditText) findViewById(R.id.edit_questionText);
		questionText.addTextChangedListener(watcher);

		EditText tagsText = (EditText) findViewById(R.id.edit_tagsText);
		tagsText.addTextChangedListener(watcher);

		TestingTransactions.check(TTChecks.EDIT_QUESTIONS_SHOWN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_question, menu);
		return true;
	}

	/**
	 * Whenever the button with the plus sign (+) is clicked, it adds a new
	 * possible answer with the hint "Type in the answer" and it is marked as
	 * incorrect.
	 * 
	 * @param view
	 *            The view that was clicked.
	 */
	public void addNewSlot(View view) {
		Answer temp = new Answer(getResources().getString(
				R.string.heavy_ballot_x), null, getResources().getString(
						R.string.hyphen_minus));

		adapter.add(temp);
		adapter.notifyDataSetChanged();

	}

	/**
	 * When the user clicks on the button labeled "Submit.", this method first
	 * verifies that the quiz question is valid. If the quiz question is valid,
	 * then it saves each answer typed in the answer slots and posts it to the
	 * SwEng quiz server. After the question is submitted, EditQuestionActivity
	 * is brought to the state identical to that when the user freshly started
	 * it.
	 * 
	 * @param view
	 *            The view that was clicked.
	 */
	public void submitQuestion(View view) {
		// Loop over view to save answers in case of failure.
		for (int i = 0; i < listView.getChildCount(); i++) {

			View view1 = listView.getChildAt(i);

			EditText answer = (EditText) view1
					.findViewById(R.id.edit_answerText);
			adapter.getItem(i).setAnswer(answer.getText().toString());

		}

		if (isValid()) {
			new HttpCommsBackgroundTask(this)
					.execute(HttpCommunications.URLPUSH);

			JSONObject jObject;
			boolean responsecheck = false;
			try {
				jObject = JSONParser.parseQuiztoJSON(createQuestion());
				responsecheck = HttpCommunications.postQuestion(
						HttpCommunications.URLPUSH, jObject);
			} catch (JSONException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						this,
						"Your submission was NOT successful. Problem with the connection.",
						Toast.LENGTH_SHORT).show();
			}

			if (responsecheck) {
				Toast.makeText(this, "Your submission was successful!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this,
						"Your submission was NOT successful. Please check that you filled in all fields.",
						Toast.LENGTH_SHORT).show();
			}

			TestingTransactions.check(TTChecks.NEW_QUESTION_SUBMITTED);
		} else {
			Toast.makeText(
					this,
					"Your submission was NOT successful. Please check that you filled in all fields.",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * This method is called when the quiz question is valid and all answers the
	 * user typed in are saved in order to create a quiz question in JSON format
	 * for the SwEng quiz server.
	 * 
	 * @return The quiz question is JSON format.
	 */
	private QuizQuestion createQuestion() {
		String questionString = ((EditText) findViewById(R.id.edit_questionText))
				.getText().toString();
		List<String> answers = new LinkedList<String>();
		int solIndex = 0;
		boolean check = true;

		for (int i = 0; i < adapter.getCount(); i++) {
			Answer answerI = adapter.getItem(i);
			answers.add(answerI.getAnswer());
			if (check) {
				if (answerI.getChecked().equals(
						getResources().getString(R.string.heavy_check_mark))) {
					check = false;
				} else {
					solIndex++;
				}
			}
		}

		String[] arrayStringTags = ((EditText) findViewById(R.id.edit_tagsText))
				.getText().toString().split("\\s*([a-zA-Z]+)[\\s.,]*");

		Set<String> tags = new HashSet<String>(Arrays.asList(arrayStringTags));

		return new QuizQuestion(-1, questionString, answers, solIndex, tags);
	}

	/**
	 * When the user clicks on the submission button, this method is triggered
	 * to verify all the four requirements defining a valid quiz question : 1)
	 * None of the fields of a quiz question may be empty or contain only white
	 * spaces. 2) None of the answers of a quiz question may be empty or contain
	 * only white spaces. 3) There must be at least 2 answers. 4) One of the
	 * answers must be marked as correct.
	 * 
	 * @return True if all requirements defining a valid quiz question are
	 *         verified, otherwise false.
	 */
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

	private class HttpCommsBackgroundTask extends
			AsyncTask<String, Void, Boolean> {
		private EditQuestionActivity activity;

		public HttpCommsBackgroundTask(EditQuestionActivity activity) {
			super();
			this.activity = activity;
		}

		/**
		 * Getting the question on the server asynchronously. Called by
		 * execute().
		 */
		@Override
		protected Boolean doInBackground(String... params) {

			JSONObject jObject;
			boolean responsecheck = false;
			try {
				jObject = JSONParser.parseQuiztoJSON(createQuestion());
				responsecheck = HttpCommunications.postQuestion(
						HttpCommunications.URLPUSH, jObject);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// Toast.makeText(
				// this.activity,
				// "Your submission was NOT successful. Problem with the connection.",
				// Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			if (responsecheck) {
				// Toast.makeText(this.activity,
				// "Your submission was successful!",
				// Toast.LENGTH_SHORT).show();
			}
			TestingTransactions.check(TTChecks.NEW_QUESTION_SUBMITTED);
			return responsecheck;
		}

		/**
		 * Set the text on the screen with the fetched random question. Called
		 * by execute() right after doInBackground().
		 */
		@Override
		protected void onPostExecute(Boolean result) {

		}

	}
}
