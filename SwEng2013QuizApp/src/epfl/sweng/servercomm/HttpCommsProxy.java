/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.authentication.SharedPreferenceManager;

import android.accounts.NetworkErrorException;

/**
 * @author Alex
 * 
 */
public final class HttpCommsProxy implements IHttpConnection {

    private static IHttpConnection sRealHttpComms = null;
    private static IHttpConnection sCacheHttpComms = null;
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
    private IHttpConnection getServerCommsInstance() {

        if (isOnlineMode()) {
            return sRealHttpComms;

        } else {
            return sCacheHttpComms;
        }
    }

    /*
     * (non-Javadoc)
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException,
        NetworkErrorException {

        return getServerCommsInstance().getHttpResponse(urlString);

    }

    /*
     * (non-Javadoc)
     * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return getServerCommsInstance().isConnected();
    }

    /*
     * (non-Javadoc)
     * @see epfl.sweng.servercomm.IHttpConnection#postJSONObject(java.lang.String, org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) throws ClientProtocolException, IOException,
        JSONException, NetworkErrorException {
        // TODO Auto-generated method stub
        return getServerCommsInstance().postJSONObject(url, question);
    }

    private boolean isOnlineMode() {

        return SharedPreferenceManager.getInstance().getBooleanPreference(PreferenceKeys.ONLINE_MODE);

    }

}
