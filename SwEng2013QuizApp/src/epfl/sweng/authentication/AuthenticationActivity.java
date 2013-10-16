package epfl.sweng.authentication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import epfl.sweng.R;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.JSONParser;
import epfl.sweng.tools.SavedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Activity allows the user to login with his Gaspar account using Tequila
 * server to authenticate the request.
 * 
 * @author AlexRivas
 * 
 */
public class AuthenticationActivity extends Activity implements
		HttpcommunicationsAdapter {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
	}

	private void failedAuthenReset() {
		Toast.makeText(this,
				"Login was NOT successful. Please check your account info.",
				Toast.LENGTH_SHORT).show();
		((EditText) findViewById(R.id.GasparUsername_EditText)).setText("");
		((EditText) findViewById(R.id.GasparPassword_EditText)).setText("");
		TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authentication, menu);
		return true;
	}

	public void logIn(View view) {
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
	public HttpResponse requete() throws ClientProtocolException, IOException,
			JSONException {

		HttpResponse response = HttpComms.getHttpComs().getHttpResponse(HttpComms.URL_SWENG_SWERVER_LOGIN);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// JSONParser.

		}

		return null;
	}

	@Override
	public void processHttpReponse(HttpResponse response) {
		try {
			String sessionID = JSONParser.parseJsonGetKey(response, "session");
			SavedPreferences.getSavedPreferences(this).setSessionID(sessionID);
			this.finish();
		} catch (JSONException e) {
			failedAuthenReset();
			Log.e(getLocalClassName(), e.getMessage());
		} catch (IOException e) {
			failedAuthenReset();
			Log.e(getLocalClassName(), e.getMessage());
		}
		

	}

}
