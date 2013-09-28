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
 * @author AlbanMarguet
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

		HttpResponse reponse = null;
		try {
			reponse = HttpCommunications
					.getHttpResponse(HttpCommunications.URL);
		} catch (IOException e) {

			e.printStackTrace();
		}
		if (reponse == null) {

			quizzQuestion = "No question to show";

		} else {

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

}
