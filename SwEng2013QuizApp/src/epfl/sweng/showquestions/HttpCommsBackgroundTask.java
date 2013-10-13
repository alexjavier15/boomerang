package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.os.AsyncTask;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.JSONParser;
import epfl.sweng.servercomm.QuestionReader;
import epfl.sweng.testing.Debug;

/**
 * Handle the AsyncTask before ShowQuestionActivity is created
 * 
 * @author albanMarguet & LorenzoLeon
 * 
 */
public class HttpCommsBackgroundTask extends AsyncTask<Void, Void, QuizQuestion> {
	
	private QuestionReader reader;

	public HttpCommsBackgroundTask(QuestionReader reader) {
		super();
		this.reader = reader;
	}

	/**
	 * Getting the question on the server asynchronously. Called by execute().
	 */
	@Override
	protected QuizQuestion doInBackground(Void... arg) {

		HttpResponse response = null;
		QuizQuestion quizQuestion = null;

		try {

			response = HttpCommunications.getHttpResponse();
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
	 * Send the informations of the fetched random question. Called by execute()
	 * right after doInBackground().
	 */
	@Override
	protected void onPostExecute(QuizQuestion result) {
		Debug.out(result);
		if (result != null) {
			reader.readQuestion(result);
		}

	}
}