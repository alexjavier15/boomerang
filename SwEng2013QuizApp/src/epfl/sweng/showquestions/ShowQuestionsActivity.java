package epfl.sweng.showquestions;

import java.io.IOException;

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
	private ArrayAdapter<String> adapter;
	private QuizQuestion currrentQuestion;
	private int lastChoice = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);

		answerChoices = (ListView) findViewById(R.id.answer_choices);

		text = (TextView) findViewById(R.id.show_question);
		Debug.out(text);

		answerChoices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listAdapter, View view,
					int selectedAnswer, long arg3) {
				ListView list = (ListView) listAdapter;
				TextView text = (TextView) list.getChildAt(selectedAnswer);
				if (lastChoice != -1) {
					TextView lastChild = ((TextView) list
							.getChildAt(lastChoice));
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

				}

				String newText = text.getText().toString() + " " + result;
				text.setText(newText);
				lastChoice = selectedAnswer;

			}

		});

		fetchNewQuestion();
		TestingTransactions.check(TTChecks.QUESTION_SHOWN);
	}

	/**
	 * Launches the HTTPGET operation to display a new random question
	 */

	public void fetchNewQuestion() {
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
		fetchNewQuestion();
	}

	private class HttpCommsBackgroundTask extends
			AsyncTask<String, Void, QuizQuestion> {
		private ShowQuestionsActivity activity;

		public HttpCommsBackgroundTask(ShowQuestionsActivity activity) {
			super();
			this.activity = activity;
		}

		@Override
		protected QuizQuestion doInBackground(String... params) {

			HttpResponse response = null;
			QuizQuestion quizQuestion = null;

			try {
				response = HttpCommunications.getHttpResponse(params[0]);

				if (response != null) {
					quizQuestion = JSONParser.parseJsonToQuiz(response);
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
		 * Set the text on the screen with the fetched random question
		 */
		@Override
		protected void onPostExecute(QuizQuestion result) {
			Debug.out(result);

			if (result == null) {
				text.setText("Aucune question n'a pu etre obtenue.");
			} else {
				if (text == null) {
					Debug.out("null textview");
				}

				else {

					currrentQuestion = result;

					text.setText(result.getQuestion());
					adapter = new ArrayAdapter<String>(activity,
							android.R.layout.simple_list_item_1,
							result.getAnswers());

					answerChoices.setAdapter(adapter);
					adapter.setNotifyOnChange(true);

				}
			}
		}
	}

}
