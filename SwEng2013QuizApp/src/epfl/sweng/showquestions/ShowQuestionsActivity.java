package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.accounts.NetworkErrorException;
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
import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * 
 * @author AlbanMarguet & LorenzoLeon
 * 
 */

public class ShowQuestionsActivity extends Activity implements HttpcommunicationsAdapter {

	private TextView text;
	private TextView tags;
	private ListView answerChoices;
	private ArrayAdapter<String> adapter;
	private QuizQuestion currrentQuestion;
	private int lastChoice = -1;
	private OnItemClickListener answerListener = null;

	/**
	 * Launches fetchNewQuestion() when clicking on the button labeled "Next Question"
	 * 
	 * @param view
	 */
	public void askNextQuestion(View view) {
		answerChoices.setOnItemClickListener(answerListener);
		((Button) findViewById(R.id.next_question)).setEnabled(false);
		fetchNewQuestion();
	}

	/**
	 * Get the tags of the question to display them on the screen
	 * 
	 * @param list
	 *                : set of Strings
	 * @return the tags
	 */
	private String displayTags(List<String> list) {
		if (list.size() > 0) {
			System.out.println("Va afficher les tags");
			String tagsInString = "";
			int counter = 0;

			for (String s : list) {
				counter++;
				if (counter == list.size()) {
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

	/**
	 * Obtains a random question thru an AsyncTask but blocks the thread until the response is received.
	 * 
	 * @return HttpResponse
	 */

	public HttpResponse fetchFirstQuestion() {
		HttpResponse response = null;

		Debug.out("Start fetching");
		try {
			response = new HttpCommsBackgroundTask(this, false).execute().get();
		} catch (InterruptedException e) {
			Log.e(getLocalClassName(), "AsyncTask thread exception");
		} catch (ExecutionException e) {
			Log.e(getLocalClassName(), "AsyncTask thread exception");

		}

		return response;
	}

	/**
	 * Launches the HTTPGET operation to display a new random question
	 */
	public void fetchNewQuestion() {
		new HttpCommsBackgroundTask(this, true).execute();
	}

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
			public void onItemClick(AdapterView<?> listAdapter, View view, int selectedAnswer, long arg3) {

				ListView list = (ListView) listAdapter;
				TextView textListener = (TextView) list.getChildAt(selectedAnswer);

				if (lastChoice != -1) {
					TextView lastChild = (TextView) list.getChildAt(lastChoice);
					if (lastChild != null) {
						String lastAnswer = lastChild.getText().toString();
						lastAnswer = lastAnswer.substring(0, lastAnswer.length() - 1);
						lastChild.setText(lastAnswer);
					}
				}

				String question = getResources().getString(R.string.heavy_ballot_x);

				if (currrentQuestion.checkAnswer(selectedAnswer)) {
					question = getResources().getString(R.string.heavy_check_mark);
					((Button) findViewById(R.id.next_question)).setEnabled(true);
					list.setOnItemClickListener(null);
					// answerChoices.setOnClickListener(null);

				}

				String newText = textListener.getText().toString() + question;
				textListener.setText(newText);
				lastChoice = selectedAnswer;

				TestCoordinator.check(TTChecks.ANSWER_SELECTED);
			}

		};
		answerChoices.setOnItemClickListener(answerListener);
		processHttpReponse(fetchFirstQuestion());
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
	public void processHttpReponse(HttpResponse httpResponse) {
		Debug.out(httpResponse);
		QuizQuestion quizQuestion = null;

		try {
			quizQuestion = JSONParser.parseJsonToQuiz(httpResponse);
			Debug.out(quizQuestion);
		} catch (JSONException e) {
			text.append("/n No question can be obtained !");
			Log.e(getLocalClassName(), e.getMessage());
			return;
		} catch (IOException e) {
			text.append("/n No question can be obtained !");
			Log.e(getLocalClassName(), e.getMessage());
			return;
		}

		if (text == null && quizQuestion != null) {
			Debug.out("null textview");
		} else {
			// We've got a satisfying question => treating it
			currrentQuestion = quizQuestion;

			text.setText(quizQuestion.getQuestion());
			tags.setText(displayTags(quizQuestion.getTags()));
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				quizQuestion.getAnswers());

			answerChoices.setAdapter(adapter);

			adapter.setNotifyOnChange(true);
			TestCoordinator.check(TTChecks.QUESTION_SHOWN);
		}

	}

	@Override
	public HttpResponse requete() {
		HttpResponse response = null;

		try {
			response = HttpComms.getInstance(this).getHttpResponse();
		} catch (NetworkErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}
}
