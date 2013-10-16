package epfl.sweng.authentication;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import epfl.sweng.R;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.JSONParser;
import epfl.sweng.tools.SavedPreferences;
import android.os.Bundle;
import android.app.Activity;
<<<<<<< HEAD
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
=======
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
>>>>>>> 93a6ac63a6ea9dff740ce92ab3447eda045c22fd

/**
 * This Activity allows the user to login with his Gaspar account using Tequila
 * server to authenticate the request.
 * 
 * @author AlexRivas
 * 
 */
public class AuthenticationActivity extends Activity implements
		HttpcommunicationsAdapter {

<<<<<<< HEAD
    public static final String PREFS_NAME = "user_session";

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

    public void requete(View view) throws IllegalStateException, IOException {
        HttpResponse response = null;

        try {
            response = new HttpCommsBackgroundTask(this).execute().get();

            Debug.out(response.getStatusLine());
            for (Header h : response.getAllHeaders()) {
                Debug.out(h);
            }
            Debug.out(response.getEntity().getContent().toString());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (response != null) {

        }

    }

    @Override
    public HttpResponse requete() throws ClientProtocolException, IOException, JSONException {
        String token = null;

        HashMap<String, String> postParametters = new HashMap<String, String>(3);

        HttpResponse response = HttpCommunications.getHttpResponse(HttpCommunications.URL_TEQUILA);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            token = JSONParser.parseJsonTequila(response, JSONParser.Tequila.token).get("token");

            postParametters.putAll(getUserLogin());
            postParametters.put("requestkey", token);

            JSONObject ob = new JSONObject();
            ob.put("password", postParametters.get("password"));
            ob.put("username", postParametters.get("username"));
            ob.put("requestkey", postParametters.get("requestkey"));

            Debug.out(ob.toString());
            response = HttpCommunications.postQuestion(HttpCommunications.URL_TEQUILA_LOGIN, ob);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {

                HashMap<String, String> tokenMap = new HashMap<String, String>(1);
                tokenMap.put("token", token);
                response = HttpCommunications.post(HttpCommunications.URL_TEQUILA, tokenMap);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HashMap<String, String> authentification = JSONParser.parseJsonTequila(response,
                            JSONParser.Tequila.session);

                    if (authentification.containsKey(JSONParser.SessionKeys.session.name())) {
                        String session_id = authentification.get(JSONParser.SessionKeys.session.name());

                        CredentialManager credentials = CredentialManager.getInstance();
                        credentials.setContext(getApplicationContext());
                        credentials.writePreference(PREFS_NAME, session_id);

                    }

                }
            }
        }

        return response;
    }

    private Map<String, String> getUserLogin() {

        Map<String, String> loginMap = new HashMap<String, String>(2);
        String username = ((TextView) findViewById(R.id.GASPAR_username)).getText().toString();

        String password = ((TextView) findViewById(R.id.GASPAR_Password)).getText().toString();
        if (username != null || username != "" || password != null || password != "") {
            loginMap = new HashMap<String, String>(2);

            loginMap.put("password", password);
            loginMap.put("username", username);

        }

        return loginMap;
    }

    @Override
    public void processHttpReponse(HttpResponse reponse) {
        // TODO Auto-generated method stub

    }
=======
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
		@SuppressWarnings("unused")
		HttpResponse reponse = null;

		/*try {
			reponse = */new HttpCommsBackgroundTask(this).execute();/*.get();

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

		}*/

	}

	@Override
	public HttpResponse requete() throws ClientProtocolException, IOException,
			JSONException {
/*
		HttpResponse response = HttpCommunications
				.getHttpResponse(HttpCommunications.URL_SWENG_SWERVER_LOGIN);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// JSONParser.

		}*/

		return null;
	}

	@Override
	public void processHttpReponse(HttpResponse response) {
		/*try {
			String sessionID = JSONParser.parseJsonGetKey(response, "session");
			SavedPreferences.getSavedPreferences(this).setSessionID(sessionID);
			*/SavedPreferences.getSavedPreferences(this).setSessionID("piadjpidajpiajd");
			this.finish();
		/*} catch (JSONException e) {
			failedAuthenReset();
			Log.e(getLocalClassName(), e.getMessage());
		} catch (IOException e) {
			failedAuthenReset();
			Log.e(getLocalClassName(), e.getMessage());
		}*/
		

	}
>>>>>>> 93a6ac63a6ea9dff740ce92ab3447eda045c22fd

}
