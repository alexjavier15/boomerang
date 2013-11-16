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

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.tools.Debug;

/**
 * @author LorenzoLeon & Noortch
 * 
 *         This class is used to communicate with the server
 * 
 */
public final class HttpComms implements IHttpConnectionHelper {

	public final static String HEADER = "Authorization";
	public final static int STRING_ENTITY = 1;
	public final static String URL_SWENG_RANDOM_GET = "https://sweng-quiz.appspot.com/quizquestions/random";
	public final static String URL_SWENG_SWERVER_LOGIN = "https://sweng-quiz.appspot.com/login";
	public final static String URL_TEQUILA_LOGIN = "https://tequila.epfl.ch/cgi-bin/tequila/login";
	public final static String URL_SWENG_PUSH = "https://sweng-quiz.appspot.com/quizquestions/";
	public final static String URL_SWENG_QUERY_POST = "https://sweng-quiz.appspot.com/search";
	private static HttpComms singleHTTPComs = null;
	private String authenticationValue = null;

	public static HttpComms getInstance() {

		if (singleHTTPComs == null) {
			singleHTTPComs = new HttpComms();
		}

		return singleHTTPComs;
	}

	private HttpComms() {

	}

	private HttpResponse execute(HttpUriRequest request)
		throws ClientProtocolException, IOException, NetworkErrorException {

		if (isConnected()) {
			if (checkLoginStatus()) {
				request.addHeader(HEADER, authenticationValue);

			}
			Debug.out("sending to SwengClient");
			return SwengHttpClientFactory.getInstance().execute(request);
		} else {
			throw new NetworkErrorException(
					"A network error has ocurred when trying to contact the server");
		}
	}

	private boolean checkLoginStatus() {
		String value = CredentialManager.getInstance().getUserCredential();
		if (!value.equals("")) {
			authenticationValue = "Tequila " + value;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets an HttpResponse from the server in parameter
	 * 
	 * @param urlString
	 *            The URL of the server on which we want to connect to.
	 * @return The HttpResponse from the server.
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NetworkErrorException
	 */
	@Override
	public HttpResponse getHttpResponse(String urlString)
		throws ClientProtocolException, IOException, NetworkErrorException {
		HttpGet request = new HttpGet(urlString);

		return execute(request);

	}

	@Override
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) QuizApp
				.getContexStatic().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();

	}

	public HttpResponse postEntity(String url, HttpEntity entity)
		throws ClientProtocolException, IOException, NetworkErrorException {

		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		return execute(post);

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
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NetworkErrorException
	 */
	// TODO do so no code is repeated
	@Override
	public HttpResponse postJSONObject(String url, JSONObject question)
		throws ClientProtocolException, IOException, NetworkErrorException {

		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new StringEntity(question.toString(STRING_ENTITY)));
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
		post.setHeader("Content-type", "application/json");
		HttpResponse response = execute(post);

		return response;
	}
}
