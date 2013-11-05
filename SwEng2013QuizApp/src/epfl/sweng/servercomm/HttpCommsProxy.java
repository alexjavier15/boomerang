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
import epfl.sweng.authentication.SharedPreferenceManager;

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
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException,
            NetworkErrorException {

        HttpResponse reponse = getServerCommsInstance().getHttpResponse(urlString);
        if (reponse != null) {
            String entity = EntityUtils.toString(reponse.getEntity());
            String entity2 = new String(entity);

            reponse.setEntity(new StringEntity(entity));

            HttpResponse reponseFiltered = filerReponseforCaching(reponse);
            if (reponse != null && isConnected()) {

                sCacheHttpComms.pushQuestion(reponseFiltered);

                reponse.setEntity(new StringEntity(entity2));

            }

        }

        return reponse;
    }

    /**
     * @param reponse
     * @return
     */
    private HttpResponse filerReponseforCaching(HttpResponse reponse) {

        switch (reponse.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
                return reponse;

            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                SharedPreferenceManager.getInstance().writeBooleaPreference(PreferenceKeys.ONLINE_MODE, false);
                return null;

            default:
                return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return getServerCommsInstance().isConnected();
    }

    /*
     * (non-Javadoc)
     * 
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
