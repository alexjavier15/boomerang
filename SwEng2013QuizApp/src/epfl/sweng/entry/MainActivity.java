package epfl.sweng.entry;

import epfl.sweng.R;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.QuestionReader;
import epfl.sweng.showquestions.HttpCommsBackgroundTask;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author AlbanMarguet, CanGuzelhan
 * 
 */
public class MainActivity extends Activity implements QuestionReader {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TestCoordinator.check(TTChecks.MAIN_ACTIVITY_SHOWN);
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

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isConnected()) {
			Debug.out("You are currently not connected to a network.");
		} else {
			Debug.out("starting fetching");
			new HttpCommsBackgroundTask(this).execute();
		}

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

	@Override
	public void readQuestion(QuizQuestion question) {
		if (question != null) {
			Intent showQuestionActivityIntent = new Intent(this,
					ShowQuestionsActivity.class);
			question.addExtraDatatoIntent(showQuestionActivityIntent);
			this.startActivity(showQuestionActivityIntent);
		}

	}
}
