package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import android.os.AsyncTask;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.QuestionReader;
import epfl.sweng.testing.Debug;

/**
 * Handle the AsyncTask before ShowQuestionActivity is created
 * 
 * @author albanMarguet & LorenzoLeon
 * 
 */
public class HttpCommsBackgroundTask extends
		AsyncTask<Void, Void, HttpResponse> {

	//private QuestionReader reader; Don't delete

	public HttpCommsBackgroundTask(QuestionReader qreader) {
		super();
		//this.reader = qreader;
	}

	/**
	 * Getting the question on the server asynchronously. Called by execute().
	 */
	@Override
	protected HttpResponse doInBackground(Void... arg) {

		HttpResponse response = null;
		
		try {

			response = HttpComms.getInstance().getHttpResponse();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {

			return response;
			// quizQuestion = JS;ONParser.parseJsonToQuiz(response);
		} else {
			Debug.out("can't get an answer from the server");

			return null;
		}
	}

	/**
	 * Send the informations of the fetched random question. Called by execute()
	 * right after doInBackground().
	 */
	@Override
	protected void onPostExecute(HttpResponse result) {
		/*
		 * Debug.out(result); if (result != null) { reader.readQuestion(result);
		 * }
		 */

	}
}