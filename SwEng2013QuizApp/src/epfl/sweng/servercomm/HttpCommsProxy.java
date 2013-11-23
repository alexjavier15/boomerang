/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */

public final class HttpCommsProxy implements IHttpConnectionHelper {

    private static IHttpConnectionHelper sRealHttpComms = null;
    private static CacheHttpComms sCacheHttpComms = null;
    private static CacheQueryManager sQueryCacheComms = null;
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
        if (sQueryCacheComms == null) {
            sQueryCacheComms = CacheQueryManager.getInstance();
        }
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, sRealHttpComms.isConnected());
        // TODO check if necessary
        CacheManager.getInstance();
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
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String )
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws NetworkErrorException, NullPointerException,
            ParseException, IOException {

        HttpResponse response = getServerCommsInstance().getHttpResponse(urlString);
        if (response != null) {

            if (checkResponseStatus(response, HttpStatus.SC_OK)) {
                // Httpresponse can't be read twice
                String entity = EntityUtils.toString(response.getEntity());

                response.setEntity(new StringEntity(entity));
                saveQuery(response, 0);
                response.setEntity(new StringEntity(entity));

            }

        }

        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#postJSONObject(java.lang.String , org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject jObject) throws ClientProtocolException, IOException,
            NetworkErrorException {

        HttpResponse response = getServerCommsInstance().postJSONObject(url, jObject);

        if (url.equals(HttpComms.URL_SWENG_QUERY_POST) && !checkResponseStatus(response, HttpStatus.SC_OK)) {

            // sCacheHttpComms.postJSONObject(url, jObject);
            QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, false);

        } else if (url.equals(HttpComms.URL_SWENG_PUSH) && !checkResponseStatus(response, HttpStatus.SC_CREATED)) {

            sCacheHttpComms.postJSONObject(url, jObject);
            QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, false);
        }

        return response;
    }

    /**
     * Check the status the {@link HttpResponse} against an expected status. If the status is not as expected The proxy
     * switch to the offline state.
     * 
     * @param reponse
     * @param expectedStatus
     */
    private boolean checkResponseStatus(HttpResponse reponse, int expectedStatus) {
        return reponse.getStatusLine().getStatusCode() == expectedStatus;
    }

    private boolean isOnlineMode() {
        Debug.out(this.getClass(), QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true));
        return QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true);
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

    public void saveQuery(HttpResponse response, int indexHash) {
        if (isOnlineMode()) {
            try {
                String quizQuestion = JSONParser.getParser(response).toString();
                sCacheHttpComms.pushQuestion(quizQuestion, indexHash);
            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public HttpResponse getQueryQuestion(int indexHash) throws JSONException, IOException {
        return CacheManager.getInstance().getHashQuestion(indexHash);
    }

    /**
     * @param b
     */
    public void setOnlineMode(boolean isOnline) {

        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, isOnline).apply();

    }

    public HttpResponse pollQuestion(String url, JSONObject query) throws ClientProtocolException,
            NetworkErrorException, IOException {
        return sQueryCacheComms.postJSONObject(url, query);
    }

}
