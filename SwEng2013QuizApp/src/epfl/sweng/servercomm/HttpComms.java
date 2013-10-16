package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.testing.Debug;

/**
 * @author LorenzoLeon & Noortch
 * 
 *         This class is used to communicate with the server
 * 
 */
public final class HttpComms {

	public final static String URL = "https://sweng-quiz.appspot.com/quizquestions/random";
	public final static String URLPUSH = "https://sweng-quiz.appspot.com/quizquestions/";
	public final static String URL_SWENG_SWERVER_LOGIN = "https://sweng-quiz.appspot.com/login";
	public final static String URL_TEQUILA = "https://tequila.epfl.ch/cgi-bin/tequila/login";
	public final static int STRING_ENTITY = 1;
	public final static String HEADER = "Authentication";
	private static HttpComms singleHTTPComs = null;

	private String authenticationValue;

	private HttpComms() {
	}

	public static HttpComms getHttpComs() {
		if (singleHTTPComs == null) {
			singleHTTPComs = new HttpComms();
		}
		return singleHTTPComs;
	}

	public void setSessionID(String value) {
		authenticationValue = value;
	}

	/**
	 * Gets an HttpResponse from the server in parameter
	 * 
	 * @param urlString
	 *            The URL of the server on which we want to connect to.
	 * @return The HttpResponse from the server.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse getHttpResponse(String urlString)
		throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(urlString);
		return execute(request);
	}

	/**
	 * Gets an HttpResponse from the quiz server.
	 * 
	 * @return The HttpResponse from the server.
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public HttpResponse getHttpResponse() throws ClientProtocolException,
			IOException {
		return getHttpResponse(URL);
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
	// TODO do so no code is repeated
	public HttpResponse postQuestion(String url, JSONObject question)
		throws ClientProtocolException, IOException, JSONException {

		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(question.toString(STRING_ENTITY)));
		post.setHeader("Content-type", "application/json");
		HttpResponse response = execute(post);

		return response;
	}

	public HttpResponse postEntity(String url, HttpEntity entity)
		throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		return execute(post);

	}

	public HttpResponse execute(HttpUriRequest request)
		throws ClientProtocolException, IOException {
		if (authenticationValue != null) {
			request.addHeader(HEADER, authenticationValue);
		}
		Debug.out(request);
		return SwengHttpClientFactory.getInstance().execute(request);

	}

}
