package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;

public interface HttpcommunicationsAdapter {
	
	public void requete();
	
	public void processHttpReponse(HttpResponse reponse);

}
