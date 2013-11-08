/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.tools.Debug;

/**
 * @author Alex
 * 
 */

public final class HttpCommsProxy implements IHttpConnectionHelper {
	private static IHttpConnectionHelper sRealHttpComms = null;
	private static CacheHttpComms sCacheHttpComms = null;
	private static HttpCommsProxy proxy = null;

	public static HttpCommsProxy getInstance() {

		if (proxy == null) {
			return new HttpCommsProxy();

		} else {
			return proxy;
		}

	}

	private HttpCommsProxy() {

		if (sRealHttpComms == null) {
			sRealHttpComms = HttpComms.getInstance();
		}

		if (sCacheHttpComms == null) {
			sCacheHttpComms = CacheHttpComms.getInstance();
		}

	}

	public Class<?> getServerCommsClass() {

		return getServerCommsInstance().getClass();
	}

	/**
	 * @return
	 */
	private IHttpConnectionHelper getServerCommsInstance() {

		if (isOnlineMode()) {
			return sRealHttpComms;

		} else {
			return sCacheHttpComms;
		}
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

		HttpResponse reponse = getServerCommsInstance().getHttpResponse(
				urlString);
		if (reponse != null) {
			// Httpresponse can't be read twice
			String entity = EntityUtils.toString(reponse.getEntity());
			String entity2 = new String(entity);

			reponse.setEntity(new StringEntity(entity));

			checkReponseStatus(reponse, HttpStatus.SC_OK);
			if (isConnected()) {
				sCacheHttpComms.pushQuestion(reponse);
				reponse.setEntity(new StringEntity(entity2));

			}

		}

		return reponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * epfl.sweng.servercomm.IHttpConnection#postJSONObject(java.lang.String,
	 * org.json.JSONObject)
	 */
	@Override
	public HttpResponse postJSONObject(String url, JSONObject question)
		throws ClientProtocolException, IOException, JSONException,
			NetworkErrorException {
		HttpResponse reponse = getServerCommsInstance().postJSONObject(url,
				question);

		if (!checkReponseStatus(reponse, HttpStatus.SC_CREATED)) {
			sCacheHttpComms.postJSONObject(url, question);
			QuizApp.getPreferences().edit()
					.putBoolean(PreferenceKeys.ONLINE_MODE, false);
		}

		return reponse;
	}

	/**
	 * Check the status the {@link HttpResponse} against an expected status. If
	 * the status is not as expected The proxy switch to the offline state.
	 * 
	 * @param reponse
	 * @param expectedStatus
	 */
	private boolean checkReponseStatus(HttpResponse reponse, int expectedStatus) {
		return reponse.getStatusLine().getStatusCode() != expectedStatus;
	}

	private boolean isOnlineMode() {
		Debug.out("client status : "
				+ QuizApp.getPreferences().getBoolean(
						PreferenceKeys.ONLINE_MODE, false));

		return QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE,
				false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return getServerCommsInstance().isConnected();
	}
}
