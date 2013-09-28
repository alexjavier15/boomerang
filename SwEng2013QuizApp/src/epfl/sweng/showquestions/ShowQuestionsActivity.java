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
import android.widget.TextView;
import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.JSONParser;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;

/**
 * 
 * @author AlbanMarguet & LorenzoLeon
 * 
 */
public class ShowQuestionsActivity extends Activity {
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_questions);
		text = (TextView) findViewById(R.id.show_question);

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
			new HttpCommsBackgroundTask().execute(HttpCommunications.URL);
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
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String randomQuestion = null;
			HttpResponse response = null;
			QuizQuestion quizQuestion = null;

			try {
				response = HttpCommunications.getHttpResponse(params[0]);

				if (response != null) {
					quizQuestion = JSONParser.parseJsonToQuiz(response);
				}

				if (quizQuestion != null) {
					randomQuestion = quizQuestion.getQuestion();
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
			return randomQuestion;
		}

		/**
		 * Set the text on the screen with the fetched random question
		 */
		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				text.setText("Aucune question n'a pu etre obtenue.");
			} else {
				text.setText(result);
			}
		}
	}

}
