package epfl.sweng.showquestions;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import epfl.sweng.servercomm.QuestionReader;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;

/**
 * 
 * @author AlbanMarguet & LorenzoLeon
 * 
 */
public class ShowQuestionsActivity extends Activity implements QuestionReader {
	private TextView text;
	private TextView tags;
	private ListView answerChoices;
	private ArrayAdapter<String> adapter;
	private QuizQuestion currrentQuestion;
	private int lastChoice = -1;
	private OnItemClickListener answerListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_questions);
		
		String[] questionRcvd = getIntent().getStringArrayExtra("questionName");
		Debug.out(questionRcvd);
		QuizQuestion question = new QuizQuestion(questionRcvd);
		readQuestion(question);

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

				// TRY IT
				((Button) findViewById(R.id.next_question)).setEnabled(false);

				if (lastChoice != -1) {
					TextView lastChild = (TextView) list.getChildAt(lastChoice);
					if (lastChild != null) {
						String lastAnswer = lastChild.getText().toString();
						lastAnswer = lastAnswer.substring(0,
								lastAnswer.length() - 1);
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
					// answerChoices.setOnClickListener(null);

				}

				String newText = textListener.getText().toString() + question;
				textListener.setText(newText);
				lastChoice = selectedAnswer;

				TestingTransactions.check(TTChecks.ANSWER_SELECTED);
			}

		};
		answerChoices.setOnItemClickListener(answerListener);

		fetchNewQuestion();

	}

	/**
	 * Launches the HTTPGET operation to display a new random question
	 */
	public void fetchNewQuestion() {
		((Button) findViewById(R.id.next_question)).setEnabled(false);
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

	}

	/**
	 * Get the tags of the question to display them on the screen
	 * 
	 * @param setTags
	 *            : set of Strings
	 * @return the tags
	 */
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

	@Override
	public void readQuestion(QuizQuestion question) {
		Debug.out(question);

		if (question == null) {
			text.setText("No question can be obtained !");
		} else if (text == null) {
			Debug.out("null textview");
		} else {
			if (text == null) {
				Debug.out("null textview");
			} else {
				// We've got a satisfying question => treating it
				currrentQuestion = question;

				text.setText(question.getQuestion());
				tags.setText(displayTags(question.getSetOfTags()));
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,
						question.getAnswers());

				answerChoices.setAdapter(adapter);

				adapter.setNotifyOnChange(true);
				TestingTransactions.check(TTChecks.QUESTION_SHOWN);

			}
		}

	}
}
