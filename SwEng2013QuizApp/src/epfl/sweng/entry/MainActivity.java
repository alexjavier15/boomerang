package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.showquestions.HttpCommsBackgroundTask;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author AlbanMarguet, CanGuzelhan
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TestingTransactions.check(TTChecks.MAIN_ACTIVITY_SHOWN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Launches the new Activity ShowQuestionsActivity to display a random
	 * question
	 * 
	 * @param view
	 *            The view that was clicked.
	 */
	public void askQuestion(View view) {
		Toast.makeText(this, "You are on the page to show a random question!",
				Toast.LENGTH_SHORT).show();
		new HttpCommsBackgroundTask(this).execute(HttpCommunications.URL);
	}

	/**
	 * Launches the new Activity EditQuestionActivity to permit the user to
	 * submit a new question to the server
	 * 
	 * @param view
	 *            The view that was clicked.
	 */
	public void submitQuestion(View view) {
		Toast.makeText(this, "You are on the page to submit a question!",
				Toast.LENGTH_SHORT).show();
		Intent submitActivityIntent = new Intent(this,
				EditQuestionActivity.class);
		startActivity(submitActivityIntent);
	}
}
