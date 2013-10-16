package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface HttpcommunicationsAdapter {
	
	public HttpResponse requete() throws ClientProtocolException, IOException;
	
	public void processHttpReponse(HttpResponse reponse);

}
