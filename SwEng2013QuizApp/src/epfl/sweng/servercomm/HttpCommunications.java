package epfl.sweng.servercomm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
 *         This class is used to communicate with the server
 * 
 */
public class HttpCommunications {

	public final static String URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	public final static String URLPUSH = "https://sweng-quiz.appspot.com/quizquestions/";
	public final static String URL_TEQUILA = "https://sweng-quiz.appspot.com/login";
	public final static int RESPONSE_CODE = 201;
	public final static int STRING_ENTITY = 1;

	/**
	 * Gets an HttpResponse from the server in parameter
	 * 
	 * @param urlString
	 *            The URL of the server on which we want to connect to.
	 * @return The HttpResponse from the server.
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
	 * Gets an HttpResponse from the quiz server.
	 * 
	 * @return The HttpResponse from the server.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse getHttpResponse()
			throws ClientProtocolException, IOException {
		return HttpCommunications.getHttpResponse(URL);
	}

	/**
	 * Posts a JSONObject question on the server in parameter Returns true if
	 * the question is valid, false if not
	 * 
	 * @param url
	 *            URL of the server on which we want to post the question.
	 * @param question
	 *            The question that we want to post on the server.
	 * @return boolean true if the server has received the Question
	 * @throws JSONException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static HttpResponse postQuestion(String url, JSONObject question)
			throws ClientProtocolException,IOException, JSONException  {

		if (question == null) {
			throw new JSONException("This is not a valid question");
		}

		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(question.toString(STRING_ENTITY)));
		post.setHeader("Content-type", "application/json");

		Debug.out(post); // TODO post out

		HttpResponse response = SwengHttpClientFactory.getInstance().execute(
				post);

		return response;
	}

}
