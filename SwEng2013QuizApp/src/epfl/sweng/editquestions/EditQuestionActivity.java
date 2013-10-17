package epfl.sweng.editquestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.JSONParser;

/**
 * 
 * This activity enables the user to submit new quiz questions.
 * 
 * @author CanGuzelhan, JavierRivas, LorenzoLeon
 * 
 */

public class EditQuestionActivity extends Activity implements HttpcommunicationsAdapter, Observer {
	private ListView listView;
	private AnswerAdapter adapter;

	// private ArrayList<Answer> fetch = new ArrayList<Answer>();

	private static boolean reset = false;

	public static boolean isReset() {
		return reset;
	}

	public static void setReset(boolean newStatus) {
		reset = newStatus;
	}

	/**
	 * Whenever the button with the plus sign (+) is clicked, it adds a new possible answer with the hint
	 * "Type in the answer" and it is marked as incorrect.
	 */
	public void addNewSlot(View view) {
		Answer temp = new Answer(getResources().getString(R.string.heavy_ballot_x), "");
		adapter.add(temp);
		Debug.out(temp);
		adapter.notifyDataSetChanged();
		if (!isReset()) {
			TestCoordinator.check(TTChecks.QUESTION_EDITED);
		}
		listView.setSelection(listView.getCount() - 1);
	}

	/**
	 * This method is called when the quiz question is valid and all answers the user typed in are saved in order to
	 * create a quiz question in JSON format for the SwEng quiz server.
	 * 
	 * @return The quiz question is JSON format.
	 */
	private QuizQuestion createQuestion() {
		String questionString = ((EditText) findViewById(R.id.edit_questionText)).getText().toString();
		List<String> answers = new LinkedList<String>();
		int solIndex = 0;
		boolean check = true;

		for (int i = 0; i < adapter.getCount(); i++) {
			Answer answerI = adapter.getItem(i);
			answers.add(answerI.getAnswer());
			if (check) {
				if (answerI.getChecked().equals(getResources().getString(R.string.heavy_check_mark))) {
					check = false;
				} else {
					solIndex++;
				}
			}
		}

		String[] arrayStringTags = ((EditText) findViewById(R.id.edit_tagsText)).getText().toString()
			.replace(",", " ").split("\\s+");
		// split("\\s*([a-zA-Z]+)[\\s.,]*");

		List<String> tags = Arrays.asList(arrayStringTags);
		tags.removeAll(Arrays.asList("", null));
		return new QuizQuestion(-1, questionString, answers, solIndex, tags);
	}

	/**
	 * When the user clicks on the submission button, this method is triggered to verify all the four requirements
	 * defining a valid quiz question : 1) None of the fields of a quiz question may be empty or contain only white
	 * spaces. 2) None of the answers of a quiz question may be empty or contain only white spaces. 3) There must be
	 * at
	 * least 2 answers. 4) One of the answers must be marked as correct.
	 * 
	 * @return True if all requirements defining a valid quiz question are verified, otherwise false.
	 */
	public boolean isValid() {
		EditText questionText = (EditText) findViewById(R.id.edit_questionText);
		EditText tags = (EditText) findViewById(R.id.edit_tagsText);

		return !tags.getText().toString().trim().equals("")
			&& !questionText.getText().toString().trim().equals("") && adapter.getCount() >= 2
			&& !adapter.hasEmptyAnswer() && adapter.hasOneCorrectAnswer();
	}

	/**
	 * Starts the window adding a modified ArrayAdapter to list the answers. Creates the multiple Test Listeners for
	 * when text has been edited. Shows the view.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);

		// Answer firstAnswer = new Answer(getResources().getString(R.string.heavy_ballot_x), "");
		// fetch.add(firstAnswer);

		TextWatcher watcher = new TextWatcher() {

			/**
			 * Validates the whole question after each change to enable submit button.
			 */
			@Override
			public void afterTextChanged(Editable s) {

				if (s.toString().trim().equals("")) {
					updateEmptyText();
				} else {

					updateTextchanged();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isReset()) {
					TestCoordinator.check(TTChecks.QUESTION_EDITED);
				}
			}
		};
		adapter = new AnswerAdapter(this, R.id.listview, new ArrayList<Answer>(), this);
		adapter.setNotifyOnChange(true);

		listView = (ListView) findViewById(R.id.listview);
		listView.setAdapter(adapter);
		addNewSlot(null);
		EditText questionText = (EditText) findViewById(R.id.edit_questionText);
		questionText.addTextChangedListener(watcher);
		questionText.setTag(null);

		EditText tagsText = (EditText) findViewById(R.id.edit_tagsText);
		tagsText.addTextChangedListener(watcher);
		questionText.setTag(null);

		TestCoordinator.check(TTChecks.EDIT_QUESTIONS_SHOWN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_question, menu);
		return true;
	}

	public void printFail() {
		Toast.makeText(this, "Your submission was NOT successful. Please try again later.", Toast.LENGTH_SHORT)
			.show();
	}

	public void printSuccess() {
		Toast.makeText(this, "Your submission was successful!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void processHttpReponse(HttpResponse response) {

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			reset();
			printSuccess();
		} else {
			printFail();
		}
	}

	@Override
	public HttpResponse requete() {
		HttpResponse response = null;
		try {
			response = HttpComms.getInstance(this).postQuestion(HttpComms.URLPUSH,
				JSONParser.parseQuiztoJSON(createQuestion()));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetworkErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * After a successful submission of a quiz question, EditQuestionActivity‘s UI is reinitialized.
	 */
	public void reset() {
		setReset(true);
		((EditText) findViewById(R.id.edit_questionText)).setText("");
		((EditText) findViewById(R.id.edit_tagsText)).setText("");
		adapter.setDefault();
		addNewSlot(null);
		setReset(false);
		TestCoordinator.check(TTChecks.NEW_QUESTION_SUBMITTED);
	}

	/**
	 * When the user clicks on the button labeled "Submit.", this method first verifies that the quiz question is
	 * valid.
	 * If the quiz question is valid, then it saves each answer typed in the answer slots and posts it to the SwEng
	 * quiz
	 * server. After the question is submitted, EditQuestionActivity is brought to the state identical to that when
	 * the
	 * user freshly started it.
	 * 
	 * @param view
	 *                The view that was clicked.
	 */
	public void submitQuestion(View view) {
		new HttpCommsBackgroundTask(this).execute();
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

	public void updateEmptyText() {
		Debug.out("Fired empty update");
		((Button) findViewById(R.id.submit_button)).setEnabled(false);

	}

	public void updateTextchanged() {

		Debug.out("Fired filled update");
		if (isValid()) {
			((Button) findViewById(R.id.submit_button)).setEnabled(true);
		}

	}

}
