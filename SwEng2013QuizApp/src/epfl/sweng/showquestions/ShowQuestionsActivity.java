package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.JSONParser;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;

/**
 * 
 * @author AlbanMarguet & LorenzoLeon
 * 
 */
public class ShowQuestionsActivity extends Activity {
	private TextView text;
	private ListView answerChoices;
	private TextView tags;
	private ArrayAdapter<String> adapter;
	private QuizQuestion currrentQuestion;
	private int lastChoice = -1;
	private OnItemClickListener answerListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		View found = findViewById(R.id.answer_choices);
		answerChoices = (ListView) found;
		// answerChoices = (ListView) findViewById(R.id.answer_choices);

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
					String lastAnswer = lastChild.getText().toString();
					lastAnswer = lastAnswer.substring(0,
							lastAnswer.length() - 1);
					lastChild.setText(lastAnswer);
				}

				String result = getResources().getString(
						R.string.heavy_ballot_x);

				if (currrentQuestion.checkAnswer(selectedAnswer)) {
					result = getResources()
							.getString(R.string.heavy_check_mark);
					((Button) findViewById(R.id.next_question))
							.setClickable(true);
					list.setOnItemClickListener(null);
					// answerChoices.setOnClickListener(null);

				}

				String newText = textListener.getText().toString() + " "
						+ result;
				textListener.setText(newText);
				lastChoice = selectedAnswer;
				TestingTransactions.check(TTChecks.ANSWER_SELECTED);
			}

		};
		answerChoices.setOnItemClickListener(answerListener);

		fetchNewQuestion();
		TestingTransactions.check(TTChecks.QUESTION_SHOWN);
	}

	/**
	 * Launches the HTTPGET operation to display a new random question
	 */
	public void fetchNewQuestion() {
		((Button) findViewById(R.id.next_question)).setClickable(false);
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isConnected()) {
			text.setText("You are currently not connected to a network.");
		} else {
			Debug.out("starting fetching");
			new HttpCommsBackgroundTask(this).execute(HttpCommunications.URL);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_questions, menu);
		return true;
	}

	/**
	 * Launches fetchNewQuestion() when clicking on the button labeled
	 * "Next Question"
	 * 
	 * @param view
	 */
	public void askNextQuestion(View view) {
		answerChoices.setOnItemClickListener(answerListener);
		fetchNewQuestion();
		TestingTransactions.check(TTChecks.QUESTION_SHOWN);
	}

	private class HttpCommsBackgroundTask extends
			AsyncTask<String, Void, QuizQuestion> {
		private ShowQuestionsActivity activity;

		public HttpCommsBackgroundTask(ShowQuestionsActivity activity) {
			super();
			this.activity = activity;
		}

		/**
		 * Getting the question on the server asynchronously. Called by
		 * execute().
		 */
		@Override
		protected QuizQuestion doInBackground(String... params) {

			HttpResponse response = null;
			QuizQuestion quizQuestion = null;

			try {
				response = HttpCommunications.getHttpResponse(params[0]);

				if (response != null) {
					quizQuestion = JSONParser.parseJsonToQuiz(response);
				} else {
					Debug.out("can't get an answer from the server");
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return quizQuestion;
		}

		/**
		 * Set the text on the screen with the fetched random question. Called
		 * by execute() right after doInBackground().
		 */
		@Override
		protected void onPostExecute(QuizQuestion result) {
			Debug.out(result);

			if (result == null) {
				text.setText("No question can be obtained !");
			} else if (text == null) {
				Debug.out("null textview");
			} else {
				if (text == null) {
					Debug.out("null textview");
				} else {
					// We've got a satisfying result => treating it
					currrentQuestion = result;

					text.setText(result.getQuestion());
					tags.setText(displayTags(result.getSetOfTags()));
					adapter = new ArrayAdapter<String>(activity,
							android.R.layout.simple_list_item_1,
							result.getAnswers());

					answerChoices.setAdapter(adapter);

					adapter.setNotifyOnChange(true);

				}
			}
		}

		private String displayTags(Set<String> setTags) {
			if (setTags.size() > 0) {
				System.out.println("Va afficher les tags");
				String tagsInString = "";
				int counter = 0;
				for (String s : setTags) {
					counter++;
					if (counter == setTags.size()) {
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

	}
}
