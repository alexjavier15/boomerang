package epfl.sweng.authentication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.servercomm.QuestionReader;
import epfl.sweng.showquestions.HttpCommsBackgroundTask;
import epfl.sweng.testing.Debug;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class AuthenticationActivity extends Activity implements HttpcommunicationsAdapter {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authentication, menu);
		return true;
	}

	public void requete(View view) {
		HttpResponse reponse = null;

		try {
			reponse = new HttpCommsBackgroundTask(this).execute().get();
			
			Debug.out(reponse.getStatusLine());
			for (Header h : reponse.getAllHeaders()) {
				Debug.out(h);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (reponse != null) {

		}

	}

	@Override
	public void readQuestion(QuizQuestion question) {
		// TODO Auto-generated method stub

	}

	@Override
	public HttpResponse requete() throws ClientProtocolException, IOException,
			JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processHttpReponse(HttpResponse reponse) {
		// TODO Auto-generated method stub
		
	}

}
