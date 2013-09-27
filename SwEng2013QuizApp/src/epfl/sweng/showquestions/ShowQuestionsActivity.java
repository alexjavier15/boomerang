package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_questions);

		fetchNewQuestion();

		TestingTransactions.check(TTChecks.QUESTION_SHOWN);
	}

	/**
	 * Launches the HTTPGET operation to display a new random question
	 */
	public void fetchNewQuestion() {
		Intent startingIntent = getIntent();

		String quizQuestion = null;

		HttpResponse response = null;

		try {
			response = new HttpCommsBackgroundTask().execute(
					HttpCommunications.URL).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (response == null) {

			quizQuestion = "No question to show";

		} else {

			QuizQuestion randomNewQuestion = null;

			try {
				randomNewQuestion = JSONParser.parseJsonToQuiz(response);
			} catch (HttpResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
			AsyncTask<String, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(String... params) {
			try {
				HttpResponse response = HttpCommunications
						.getHttpResponse(params[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

}
