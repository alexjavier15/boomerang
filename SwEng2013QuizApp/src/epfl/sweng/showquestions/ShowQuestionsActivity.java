package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;

import epfl.sweng.R;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

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

		String quizzQuestion = null;

		HttpResponse response = new HttpCommsBackgroundTask().execute(
				HttpCommunications.URL).get();

		if (response == null) {

			quizQuestion = "No question to show";

		} else {

			QuizQuestion randomNewQuestion = JSONParser
					.parseJsonToQuiz(response);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_questions, menu);
		return true;
	}

	/**
	 * Launches fetchNewQuestion() when clikcing on the button labeled "Next Question"
	 * @param view
	 */
	public void askNextQuestion(View view) {
		fetchNewQuestion();
	}

	private class HttpCommsBackgroundTask extends
			AsyncTask<String, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(String... params) {
			HttpResponse response = HttpCommunications
					.getHttpResponse(params[0]);
			return null;
		}
	}

}
