/**
 * 
 */
package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

/**
 * @author Alex
 * 
 */
public interface IHttpConnectionHelper {

    HttpResponse getHttpResponse(String urlString);

    boolean isConnected();

    HttpResponse postJSONObject(String url, JSONObject question);

    /**
     * @param reponse
     */

}
