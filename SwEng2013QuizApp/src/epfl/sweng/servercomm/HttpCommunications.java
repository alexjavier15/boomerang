package epfl.sweng.servercomm;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 * @author LorenzoLeon
 * 
 */
public class HttpCommunications {

	public final static String URL = "https://sweng-quiz.appspot.com/quizquestions/random";

	public static HttpResponse getHttpResponse(String urlString)
			throws ClientProtocolException, IOException {
		HttpClient client = SwengHttpClientFactory.getInstance();

		HttpGet request = new HttpGet(urlString);
		return client.execute(request);

	}
}
