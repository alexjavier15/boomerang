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

/**
 * @author Alex
 * 
 */
public final class CacheHttpComms implements IHttpConnection {
    private static CacheHttpComms sCache = null;
    public static CacheHttpComms getInstance() {
        if (sCache == null) {
            sCache = new CacheHttpComms();
        }
        return sCache;
    }

    /**
     * @param context
     */
    private CacheHttpComms() {
       
    }

    public void updateStatus() {
    }

    /*
     * (non-Javadoc)
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException,
        NetworkErrorException {

        if (urlString.equals(HttpComms.URL)) {

            return loadCachedQuestion();

        } else {
            throw new UnsupportedOperationException("Unsupported operation in offline mode");
        }

    
    }

    /**
     * @return
     */
    private HttpResponse loadCachedQuestion() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see epfl.sweng.servercomm.IHttpConnection#postQuestion(java.lang.String, org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question)
        throws ClientProtocolException, IOException, JSONException, NetworkErrorException {
        if (url.equals(HttpComms.URLPUSH)) {

            return storeQuestionInCache();

        } else {
            throw new UnsupportedOperationException("Unsupported operation in offline mode");
        }
    }

    /**
     * @return
     */
    private HttpResponse storeQuestionInCache() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

}
