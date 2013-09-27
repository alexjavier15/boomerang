package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.questions.QuizQuestion;

/**
 * @author LorenzoLeon
 * 
 */
public class HttpCommunications {

	public final static String URL = "https://sweng-quiz.appspot.com/quizquestions/random";

	public HttpResponse getHttpResponse(String urlString)
			throws ClientProtocolException, IOException {
		HttpClient client = SwengHttpClientFactory.getInstance();

		HttpGet request = new HttpGet(urlString);
		return client.execute(request);
	}

	public QuizQuestion parseJsonToQuiz(HttpResponse response)
			throws HttpResponseException, JSONException, IOException {

		BasicResponseHandler responseHandler = new BasicResponseHandler();
		JSONObject parser = new JSONObject(
				responseHandler.handleResponse(response));
		int id = parser.getInt("id");
		String question = parser.getString("question");
		String[] answers = jsonArrayToStringArray(parser
				.getJSONArray("answers"));
		int solutionIndex = parser.getInt("solutionIndex");
		String[] tags = jsonArrayToStringArray(parser.getJSONArray("tags"));
		Set<String> set = new HashSet<String>(Arrays.asList(tags));
		return new QuizQuestion(id, question, Arrays.asList(answers),
				solutionIndex, set);

	}

	private String[] jsonArrayToStringArray(JSONArray array)
			throws JSONException {
		int numAnswers = array.length();
		String[] newStringArray = new String[numAnswers];
		for (int i = 0; i <= numAnswers; i++) {
			newStringArray[i] = array.getString(i);
		}
		return newStringArray;
	}
}
