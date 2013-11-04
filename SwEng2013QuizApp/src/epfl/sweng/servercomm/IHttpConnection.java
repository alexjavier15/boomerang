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
public interface IHttpConnection {
       
    HttpResponse getHttpResponse(String urlString)
        throws ClientProtocolException, IOException, NetworkErrorException;
    
    boolean isConnected();
        
    HttpResponse postJSONObject(String url, JSONObject question)
        throws ClientProtocolException, IOException,  JSONException, NetworkErrorException;
    

}
