package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.JSONParser;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;

public class HttpCommsBackgroundTask extends
AsyncTask<String, Void, QuizQuestion> {
private Activity activity;

public HttpCommsBackgroundTask(Activity act) {
	super();
	this.activity = act;
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
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
} catch (JSONException e) {
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

ArrayList<String> questionComponents = new ArrayList<String>();
questionComponents.add(Long.toString(result.getID()));
questionComponents.add(result.getQuestion());
questionComponents.add(Integer.toString(result.getAnswers().size()));
questionComponents.addAll(result.getAnswers());
questionComponents.add(Integer.toString(result.getIndex()));
questionComponents.add(Integer.toString(result.getSetOfTags().size()));
questionComponents.addAll(result.getSetOfTags());


Intent showQuestionActivityIntent = new Intent(activity,
		ShowQuestionsActivity.class);
showQuestionActivityIntent.putExtra(name, questionComponents);
activity.startActivity(showQuestionActivityIntent);




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
		TestingTransactions.check(TTChecks.QUESTION_SHOWN);

	}
}
}