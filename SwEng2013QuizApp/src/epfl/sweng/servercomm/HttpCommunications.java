package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.testing.Debug;

/**
 * @author LorenzoLeon & Noortch
 * 
 */
public class HttpCommunications {

	public final static String URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	public final static String URLPUSH = "https://sweng-quiz.appspot.com/quizquestions/";

	public final static int RESPONSE_CODE = 201;
	public final static int STRING_ENTITY = 4;

	/**
	 * Gets an HttpResponse from the server in parameter
	 * 
	 * @param urlString
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse getHttpResponse(String urlString)
		throws ClientProtocolException, IOException {

		HttpClient client = SwengHttpClientFactory.getInstance();
		HttpGet request = new HttpGet(urlString);

		return client.execute(request);
	}

	/**
	 * Posts a JSONObject question on the server in parameter Returns true if
	 * the question is valid, false if not
	 * 
	 * @param url
	 * @param question
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static boolean postQuestion(String url, JSONObject question)
			throws JSONException, IOException {

		if (question == null) {
			throw new JSONException("This is not a valid question");
		}

		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(question.toString(STRING_ENTITY)));
		post.setHeader("Content-type", "application/json");

		Debug.out(post);

		BasicResponseHandler handler = new BasicResponseHandler();
		String response = SwengHttpClientFactory.getInstance().execute(post,
				handler);

		return response.equals(RESPONSE_CODE);
	}

}
