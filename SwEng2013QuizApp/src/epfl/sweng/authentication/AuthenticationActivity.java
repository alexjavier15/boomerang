package epfl.sweng.authentication;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
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
	private static final int UNAUTHENTICATED = 0;
	private static final int TOKEN = 1;
	private static final int TEQUILA = 2;
	private static final int CONFIRMATION = 3;
	private static final int AUTHENTICATED = 4;
	private static final int ERROR_OVERLOAD = -1;
	private static final int MAX_NUMBER_OF_FAILS = 3;
	private int state;
	private int failedCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		// by default initial state is
		state = UNAUTHENTICATED;
		TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
	}

	private void failedAuthenReset() {
		Toast.makeText(this,
				"Login was NOT successful. Please check your account info.",
				Toast.LENGTH_SHORT).show();
		((EditText) findViewById(R.id.GasparUsername_EditText)).setText("");
		((EditText) findViewById(R.id.GasparPassword_EditText)).setText("");
		state = UNAUTHENTICATED;
		failedCount = 0;
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
		/*
		 * check state before start of machine... check number of fails if
		 * applicable or reset to zero. unless reset? Check savedPreferences too
		 * just in case or else all communications even with Tequila will have
		 * faulty header (session ID)
		 */
		return stateMachine(null);
	}

	public HttpResponse stateMachine(HttpResponse response) throws ClientProtocolException, 
			IOException, JSONException {
		if (failedCount > MAX_NUMBER_OF_FAILS) {
			// too many fails! reset fields!
			state = ERROR_OVERLOAD;
			return null;
		}
		switch (state) {
			case UNAUTHENTICATED:
				Log.i("Authentication state: ", "UNAUTHENTICATED, requesting token");
				return stateMachine(requestAuthToken(response));
			case TOKEN:
				Log.i("Authentication state: ", "TOKEN, posting token");
				return stateMachine(postTequilaToken(response));
			case TEQUILA:
				Log.i("Authentication state: ", "TEQUILA, checking tequila response");
				return stateMachine(checkTequila(response));
			case CONFIRMATION:
				Log.i("Authentication state: ", "CONFIRMATION, confirming with server");
				return stateMachine(confirm(response));
			case AUTHENTICATED:
				Log.i("Authentication state: ", "AUTHENTICATED, returning session id");
				return response;
			default:
				// nullPointerException => failedAuthenReset()
				return null;
		}
	}

	private HttpResponse requestAuthToken(HttpResponse starter)
		throws ClientProtocolException, IOException {
		HttpResponse response = HttpComms.getInstance().getHttpResponse(
				HttpComms.URL_SWENG_SWERVER_LOGIN);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			state = TOKEN;
			return response;
		} else {
			failedCount++;
			Log.w("Authentication state: AUTHENTICATION", ", failedcount: " + failedCount);
			return starter;
		}
	}

	private HttpResponse postTequilaToken(HttpResponse tokenResponse)
        throws JSONException, IOException {
		String token = JSONParser.parseJsonGetKey(tokenResponse, "token");
		String username = ((EditText) findViewById(R.id.GasparUsername_EditText))
				.getText().toString();
		String password = ((EditText) findViewById(R.id.GasparPassword_EditText))
				.getText().toString();
		NameValuePair[] namList = {
            new BasicNameValuePair("requestkey", token),
			new BasicNameValuePair("username", username),
			new BasicNameValuePair("password", password) };
		UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(
				Arrays.asList(namList));
		state = TEQUILA;
		return HttpComms.getInstance().postEntity(HttpComms.URL_TEQUILA,
				urlEntity);

	}

	private HttpResponse checkTequila(HttpResponse response) {
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
			state = CONFIRMATION;
			return response;
		} else {
			state = UNAUTHENTICATED;
			failedCount++;
			Log.w("Authentication state: TEQUILA", ", failedcount: " + failedCount);
			return null;
		}

	}

	private HttpResponse confirm(HttpResponse response)
		throws ClientProtocolException, IOException, JSONException {
		response = HttpComms.getInstance().postQuestion(
				HttpComms.URL_SWENG_SWERVER_LOGIN,
				JSONParser.parseTokentoJSON(JSONParser.parseJsonGetKey(
						response, "token")));
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			state = AUTHENTICATED;
			return response;
		} else {
			state = UNAUTHENTICATED;
			failedCount++;
			Log.w("Authentication state: CONFIRMATION", ", failedcount: " + failedCount);
			return null;
		}
	}

	@Override
	public void processHttpReponse(HttpResponse response) {
		try {
			String sessionID = JSONParser.parseJsonGetKey(response, "session");
			SavedPreferences.getInstance(this).setSessionID(sessionID);
			this.finish();
		} catch (NullPointerException e) {
			failedAuthenReset();
		} catch (JSONException e) {
			failedAuthenReset();
			Log.e(getLocalClassName(), e.getMessage());
		} catch (IOException e) {
			failedAuthenReset();
			Log.e(getLocalClassName(), e.getMessage());
		}

	}

}
