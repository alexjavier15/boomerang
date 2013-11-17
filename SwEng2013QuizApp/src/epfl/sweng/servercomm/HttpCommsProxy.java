/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */

public final class HttpCommsProxy implements IHttpConnectionHelper {
    private static HttpCommsProxy proxy = null;
    private static CacheHttpComms sCacheHttpComms = null;
    private static CacheManager sCacheManager = null;
    private static IHttpConnectionHelper sRealHttpComms = null;

    public static HttpCommsProxy getInstance() {

        if (proxy == null) {
            return new HttpCommsProxy();

        } else {
            return proxy;
        }

    }

    private QueryHelper mQueryHelper = null;

    private HttpCommsProxy() {

        if (sRealHttpComms == null) {
            sRealHttpComms = HttpComms.getInstance();
        }

        if (sCacheHttpComms == null) {
            sCacheHttpComms = CacheHttpComms.getInstance();
        }
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, sRealHttpComms.isConnected());
        sCacheManager = CacheManager.getInstance();
    }

    /**
     * Check the status the {@link HttpResponse} against an expected status. If the status is not as expected The proxy
     * switch to the offline state.
     * 
     * @param response
     * @param expectedStatus
     */
    private boolean checkResponseStatus(HttpResponse response, int expectedStatus) {
        return response.getStatusLine().getStatusCode() == expectedStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String )
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws NetworkErrorException, NullPointerException,
            JSONException, ParseException, IOException {

        HttpResponse response = getServerCommsInstance().getHttpResponse(urlString);
        if (response != null) {

            if (isOnlineMode() && checkResponseStatus(response, HttpStatus.SC_OK)) {
                Debug.out(" pushing question");
                String entity = EntityUtils.toString(response.getEntity());

                response.setEntity(new StringEntity(entity));
                sCacheHttpComms.pushQuestion(response);
                response.setEntity(new StringEntity(entity));

            }

        }

        return response;
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
     * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        return getServerCommsInstance().isConnected();
    }

    public boolean isOnlineMode() {
        Debug.out("client status : " + QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true));

        return QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true);
    }

    public HttpResponse poll(boolean isQueryMode, String mQuery) throws ClientProtocolException, IOException,
            NetworkErrorException, NullPointerException, ParseException, JSONException {
        if (isQueryMode && mQuery != null) {
            if (mQueryHelper != null) {
                return mQueryHelper.processQuery(mQuery);
            }
        }

        return getHttpResponse(HttpComms.URL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#postJSONObject(java.lang.String , org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) throws ClientProtocolException, IOException,
            JSONException, NetworkErrorException {
        HttpResponse response = getServerCommsInstance().postJSONObject(url, question);

        if (!checkResponseStatus(response, HttpStatus.SC_CREATED)) {
            sCacheHttpComms.postJSONObject(url, question);
            QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, false);
        }
        if (url.equals(HttpComms.URLQUERYPOST)) {
            if (checkResponseStatus(response, HttpStatus.SC_OK)) {
                JSONObject jObject = JSONParser.getParser(response);
                JSONArray questions = jObject.getJSONArray("questions");
                for (int i = 0; i < questions.length(); i++) {
                    String questionString = questions.getJSONObject(i).toString();
                    sCacheManager.pushFetchedQuestion(questionString);
                    sCacheManager.pushQueryQuestion(questionString);
                }
                if (jObject.has("next")) {
                    jObject.getString("next");
                }
            }

        }

        return response;
    }

    /**
     * @param b
     */
    public void setOnlineMode(boolean isOnline) {

        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, isOnline).apply();

    }

}
