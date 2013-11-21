package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * 
 * @author AlbanMarguet & LorenzoLeon
 * 
 */

public class ShowQuestionsActivity extends Activity {

	private ArrayAdapter<String> adapter;
	private ListView answerChoices;
	private OnItemClickListener answerListener = null;
	private QuizQuestion currrentQuestion;
	private int lastChoice = -1;
	private TextView tags;
	private TextView text;
	private ConnectionManager conManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		((Button) findViewById(R.id.next_question)).setEnabled(false);
		answerChoices = (ListView) findViewById(R.id.answer_choices);

		tags = (TextView) findViewById(R.id.show_tags);

		text = (TextView) findViewById(R.id.show_question);
		Debug.out(text);

		answerListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listAdapter, View view,
					int selectedAnswer, long arg3) {

				ListView list = (ListView) listAdapter;
				TextView textListener = (TextView) list
						.getChildAt(selectedAnswer);

				if (lastChoice != -1) {
					TextView lastChild = (TextView) list.getChildAt(lastChoice);
					if (lastChild != null) {
						String lastAnswer = lastChild.getText().toString();
						if (lastAnswer.contains("\u2718")) {
							lastAnswer = lastAnswer.substring(0,
									lastAnswer.length() - 1);
						}
						lastChild.setText(lastAnswer);
					}
				}

				String question = getResources().getString(
						R.string.heavy_ballot_x);

				if (currrentQuestion.checkAnswer(selectedAnswer)) {
					question = getResources().getString(
							R.string.heavy_check_mark);
					((Button) findViewById(R.id.next_question))
							.setEnabled(true);
					list.setOnItemClickListener(null);
				}

				TestCoordinator.check(TTChecks.ANSWER_SELECTED);

				String newText = textListener.getText().toString() + question;
				textListener.setText(newText);
				lastChoice = selectedAnswer;

			}

		};
		answerChoices.setOnItemClickListener(answerListener);
		if (getIntent().hasExtra("query_mode")) {
			conManager = new QueryConManager(this);
		} else {
			conManager = new RandomManager(this);
		}
	}

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_questions, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		askNextQuestion(this.getCurrentFocus());
	}

	/**
	 * Launches a new async task which will fetch a new question when clicking
	 * on the button labeled "Next Question"
	 * 
	 * @param view
	 */

	public void askNextQuestion(View view) {
		answerChoices.setOnItemClickListener(answerListener);
		((Button) findViewById(R.id.next_question)).setEnabled(false);
		new HttpCommsBackgroundTask(conManager, true).execute();
	}

	public void parseResponse(HttpResponse response) {
		QuizQuestion quizQuestion = null;
		if (response != null) {
			if (response != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				try {
					quizQuestion = new QuizQuestion(JSONParser.getParser(
							response).toString());
					Debug.out(quizQuestion);
				} catch (IOException e) {
					toast(conManager.getErrorMessage());
					HttpCommsProxy.getInstance().setOnlineMode(false);
					Log.e(getLocalClassName(), e.getMessage());
				} catch (JSONException e) {
					toast(conManager.getErrorMessage());
					HttpCommsProxy.getInstance().setOnlineMode(false);
					e.printStackTrace();
				}
				if (quizQuestion != null) {
					setQuestion(quizQuestion);
				} else {
					text.append("No question can be obtained !");
					toast(conManager.getErrorMessage());
				}
			} else {
				if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {

					HttpCommsProxy.getInstance().setOnlineMode(false);

				}
			}
		} else {
			toast(conManager.getErrorMessage());
		}

		TestCoordinator.check(TTChecks.QUESTION_SHOWN);
	}

	/**
	 * Get the tags of the question to display them on the screen
	 * 
	 * @param set
	 *            : set of Strings
	 * @return the tags
	 */
	private String displayTags(Set<String> set) {
		if (set.size() > 0) {
			System.out.println("Va afficher les tags");
			String tagsInString = "";
			int counter = 0;

			for (String s : set) {
				counter++;
				if (counter == set.size()) {
					tagsInString += s;
				} else {
					tagsInString += s + ", ";
				}
			}

			return tagsInString;
		} else {
			return "No tags for this question";
		}
	}

	private void setQuestion(QuizQuestion quizQuestion) {
		currrentQuestion = quizQuestion;

		text.setText(quizQuestion.getQuestion());
		tags.setText(displayTags(quizQuestion.getTags()));
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, quizQuestion.getAnswers());

		answerChoices.setAdapter(adapter);

		adapter.setNotifyOnChange(true);
	}

	public TextView getText() {
		return text;
	}

	public void setText(TextView view) {
		text = view;
	}

	public void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
