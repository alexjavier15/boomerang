package epfl.sweng.authentication;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpCommunications;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.servercomm.JSONParser;
import epfl.sweng.servercomm.QuestionReader;
import epfl.sweng.showquestions.HttpCommsBackgroundTask;
import epfl.sweng.testing.Debug;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AuthenticationActivity extends Activity implements HttpcommunicationsAdapter {

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

}
