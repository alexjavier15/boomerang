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
public interface IHttpConnectionHelper {

    HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException, NetworkErrorException,
            NullPointerException, JSONException;

    boolean isConnected();

    HttpResponse postJSONObject(String url, JSONObject question) throws ClientProtocolException, IOException,
            JSONException, NetworkErrorException;

    /**
     * @param reponse
     */

}
