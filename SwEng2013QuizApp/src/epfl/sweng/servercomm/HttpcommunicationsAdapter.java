package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;

/**
 * This interface states the methods for the visitor pattern to be used in an AsyncTask implementation.
 * 
 * @author LorenzoLeon
 * 
 */
public interface HttpcommunicationsAdapter {

	void processHttpReponse(HttpResponse reponse);

	HttpResponse requete();

}
