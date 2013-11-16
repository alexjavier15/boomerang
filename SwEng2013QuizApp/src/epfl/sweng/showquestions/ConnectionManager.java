package epfl.sweng.showquestions;

import org.apache.http.HttpResponse;

import epfl.sweng.servercomm.HttpcommunicationsAdapter;

/**
 * @author LorenzoLeon
 * 
 */
public abstract class ConnectionManager implements HttpcommunicationsAdapter {

	@Override
	public abstract void processHttpReponse(HttpResponse reponse);

	@Override
	public abstract HttpResponse requete();

	public abstract String getErrorMessage();

}
