package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

public interface HttpcommunicationsAdapter {

	HttpResponse requete() throws ClientProtocolException, IOException,
			JSONException;

	void processHttpReponse(HttpResponse reponse);

}
