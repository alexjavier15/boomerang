/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheHttpComms implements IHttpConnectionHelper {

	private static CacheHttpComms sCache = null;

	public static CacheHttpComms getInstance() {
		if (sCache == null) {
			sCache = new CacheHttpComms();
		}
		return sCache;
	}

	private CacheHttpComms() {

		CacheManager.getInstance();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
	 */
	@Override
	public HttpResponse getHttpResponse(String urlString)
		throws ClientProtocolException, IOException, NetworkErrorException {
		HttpResponse reponse = null;

		if (urlString.equals(HttpComms.URL_SWENG_RANDOM_GET)) {

			try {
				reponse = CacheManager.getInstance().getRandomQuestion();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			throw new UnsupportedOperationException(
					"Unsupported operation in offline mode");
		}
		return reponse;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see epfl.sweng.servercomm.IHttpConnection#postQuestion(java.lang.String,
	 * org.json.JSONObject)
	 */
	@Override
	public HttpResponse postJSONObject(String url, JSONObject question)
		throws ClientProtocolException, IOException, NetworkErrorException {
		if (url.equals(HttpComms.URL_SWENG_PUSH)) {
			return CacheManager.getInstance().addQuestionForSync(
					question.toString());
		} else if (url.equals(HttpComms.URL_SWENG_QUERY_POST)) {
			try {
                return CacheManager.getInstance().getQueriedQuestion(question.getString("query"));
            } catch (JSONException e) {
                
                //TODO
                throw new UnsupportedOperationException(
                        "Unsupported operation in offline mode");
            }
		} else {
			throw new UnsupportedOperationException(
					"Unsupported operation in offline mode");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return false;
	}

	/**
	 * @param reponse
	 */
	public void pushQuestion(HttpResponse reponse) {

		JSONObject quizQuestion;
		try {
			quizQuestion = JSONParser.getParser(reponse);
			CacheManager.getInstance().pushFetchedQuestion(
					quizQuestion.toString());
		} catch (IOException e) {
		    //TODO
			e.printStackTrace();
		}

	}

}
