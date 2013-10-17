package epfl.sweng.authentication;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * This Activity allows the user to login with his Gaspar account using Tequila server to authenticate the request.
 * 
 * @author AlexRivas
 * 
 */
public class AuthenticationActivity extends Activity implements HttpcommunicationsAdapter {
    private static final int AUTHENTICATED = 4;
    private static final int CONFIRMATION = 3;
    private static final int ERROR = -1;
    private static final String INTERNAL_ERROR_MSG = "An internal error has occurred during "
        + "authentication. Please try again.";
    private static final String SUCCEFUL_MSG = "You have succesfully logged in";
    private static final String SWENG_ERROR_MSG = "Authentication with SwEgQuizServed has failed.";
    private static final int TEQUILA = 2;
    private static final String TEQUILA_ERROR_MSG = "Login with Tequila was  NOT successful. "
        + "Please check your account infos.";
    private static final int TOKEN = 1;
    private static final int UNAUTHENTICATED = 0;
    private static final String UNEXPECTED_ERROR_MSG = "An unexpected error has occured. "
        + "Your credentials couldn't be saved. Please try again";
    private String mStatusMsg = "";

    // private static final int MAX_NUMBER_OF_FAILS = 3;
    private int state;
    // private int failedCount;
    private String token;

    private HttpResponse checkTequila(HttpResponse response) throws AuthenticationException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
            state = CONFIRMATION;
            return response;
        } else {
            state = ERROR;
            // failedCount++;

            Log.w("Authentication state: TEQUILA", ", failedcount: ");
            throw new AuthenticationException(TEQUILA_ERROR_MSG);
        }

    }

    private HttpResponse confirm(HttpResponse response) throws ClientProtocolException, IOException, JSONException,
        NetworkErrorException, AuthenticationException {
        response = HttpComms.getInstance(this).postQuestion(HttpComms.URL_SWENG_SWERVER_LOGIN,
            JSONParser.parseTokentoJSON(token));
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            state = AUTHENTICATED;
            mStatusMsg = SUCCEFUL_MSG;
            return response;
        } else {
            throw new AuthenticationException(SWENG_ERROR_MSG);
        }
    }

    private void failedAuthenReset() {
        Toast.makeText(this, mStatusMsg, Toast.LENGTH_SHORT).show();
        ((EditText) findViewById(R.id.GasparPassword_EditText)).setText("");
        ((EditText) findViewById(R.id.GasparUsername_EditText)).setText("");
        state = UNAUTHENTICATED;
        TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
    }

    public void logIn(View view) {
        if (HttpComms.getInstance(this).isConnected()) {

            new HttpCommsBackgroundTask(this).execute();
        } else {

            Toast.makeText(this, "Not internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        // by default initial state is
        state = UNAUTHENTICATED;
        TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authentication, menu);
        return true;
    }

    private HttpResponse postTequilaToken(HttpResponse tokenResponse) throws JSONException, IOException,
        NetworkErrorException {
        token = JSONParser.parseJsonGetKey(tokenResponse, "token");
        String username = ((EditText) findViewById(R.id.GasparUsername_EditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.GasparPassword_EditText)).getText().toString();
        NameValuePair[] namList = {new BasicNameValuePair("requestkey", token),
            new BasicNameValuePair("username", username), new BasicNameValuePair("password", password)};
        UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(Arrays.asList(namList));
        state = TEQUILA;
        return HttpComms.getInstance(this).postEntity(HttpComms.URL_TEQUILA, urlEntity);

    }

    @Override
    public void processHttpReponse(HttpResponse response) {
        if (response != null) {

            try {
                String sessionID = JSONParser.parseJsonGetKey(response, "session");
                CredentialManager.getInstance(this).writePreference(PreferenceKeys.SESSION_ID, sessionID);
                Toast.makeText(this, mStatusMsg, Toast.LENGTH_SHORT).show();
                Debug.out(sessionID);

                this.finish();
            } catch (NullPointerException e) {
                mStatusMsg = UNEXPECTED_ERROR_MSG;
                failedAuthenReset();
                Log.e(getLocalClassName(), e.getMessage());
            } catch (JSONException e) {
                mStatusMsg = INTERNAL_ERROR_MSG;
                failedAuthenReset();
                Log.e(getLocalClassName(), e.getMessage());
            } catch (IOException e) {
                mStatusMsg = INTERNAL_ERROR_MSG;
                failedAuthenReset();
                Log.e(getLocalClassName(), e.getMessage());

            }
        } else {
            failedAuthenReset();

        }
    }

    private HttpResponse requestAuthToken(HttpResponse starter) throws ClientProtocolException, IOException,
        NetworkErrorException {
        HttpResponse response = HttpComms.getInstance(this).getHttpResponse(HttpComms.URL_SWENG_SWERVER_LOGIN);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            state = TOKEN;
            return response;
        } else {

            Log.w("Authentication state: AUTHENTICATION", ", failedcount: ");
            return null;
        }
    }

    @Override
    public HttpResponse requete() {

        HttpResponse response = null;

        try {
            response = stateMachine(null);
        } catch (AuthenticationException e) {
            mStatusMsg = e.getMessage();
            Log.e(getLocalClassName(), e.getMessage());
        } catch (NetworkErrorException e) {
            mStatusMsg = e.getMessage();
            Log.e(getLocalClassName(), e.getMessage());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mStatusMsg = INTERNAL_ERROR_MSG;
            Log.e(getLocalClassName(), e.getMessage());
        } catch (IOException e) {
            mStatusMsg = INTERNAL_ERROR_MSG;
            Log.e(getLocalClassName(), e.getMessage());
        } catch (JSONException e) {
            mStatusMsg = INTERNAL_ERROR_MSG;
            Log.e(getLocalClassName(), e.getMessage());

        }

        return response;
    }

    private HttpResponse stateMachine(HttpResponse response) throws ClientProtocolException, IOException,
        JSONException, NetworkErrorException, AuthenticationException {
        /*
         * if (failedCount > MAX_NUMBER_OF_FAILS) { // too many fails! reset fields! state = ERROR_OVERLOAD;
         * return null; }
         */
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
}
