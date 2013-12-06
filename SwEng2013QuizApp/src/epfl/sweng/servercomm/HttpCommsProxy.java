/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import epfl.sweng.authentication.PreferenceKeys;

/**
 * @author Alex
 * 
 */

public final class HttpCommsProxy implements IHttpConnectionHelper {
    private static HttpCommsProxy proxy = null;
    private static CacheHttpComms sCacheHttpComms = null;
    private static IHttpConnectionHelper sRealHttpComms = null;

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

    public Class<?> getServerCommsClass() {

        return getServerCommsInstance().getClass();
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String )
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) {

        HttpResponse response = getServerCommsInstance().getHttpResponse(urlString);
        if (response != null) {

            if (urlString.equals(HttpComms.URL_SWENG_RANDOM_GET) && isOnlineMode()
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Log.v(this.getClass().getName(), " pushing question");
                String entity;
                try {
                    entity = EntityUtils.toString(response.getEntity());
                    response.setEntity(new StringEntity(entity));
                    sCacheHttpComms.pushQuestion(response);
                    response.setEntity(new StringEntity(entity));
                } catch (ParseException e) {
                    Log.e(this.getClass().getName(), e.getMessage(), e);
                } catch (IOException e) {
                    Log.e(this.getClass().getName(), e.getMessage(), e);
                } catch (NullPointerException e) {
                    Log.e(this.getClass().getName(), e.getMessage(), e);
                }

            } else if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                setOnlineMode(false);

            }

        } else {
            DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
            response = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_BAD_REQUEST, null), null);
            setOnlineMode(false);

        }

        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#postJSONObject(java.lang.String , org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) {
        HttpResponse response = null;
        if (question != null) {
            response = getServerCommsInstance().postJSONObject(url, question);

            if (url.equals(HttpComms.URL_SWENG_PUSH)) {
                sCacheHttpComms.pushQuestion(question);
            }

        }

        if (response != null) {
            if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                sCacheHttpComms.postJSONObject(url, question);
                setOnlineMode(false);
            }
        } else {
            DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
            response = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_BAD_REQUEST, null), null);
            setOnlineMode(false);
        }

        return response;
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

    private boolean isOnlineMode() {
        Log.v(this.getClass().getName(),
                "client status : " + QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true));
        return QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true);
    }

    /**
     * @param b
     */
    public void setOnlineMode(boolean isOnline) {

        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, isOnline).apply();

    }
}
