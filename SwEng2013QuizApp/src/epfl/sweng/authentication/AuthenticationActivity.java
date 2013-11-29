package epfl.sweng.authentication;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.JSONParser;

/**
 * This Activity allows the user to login with his Gaspar account using Tequila server to authenticate the request.
 * 
 * @author AlexRivas
 * 
 */

public class AuthenticationActivity extends Activity implements HttpcommunicationsAdapter {

    public static final String INTERNAL_ERROR_MSG = "An internal error has occurred during "
            + "authentication. Please try again.";
    private static final String SUCCEFUL_MSG = "You have succesfully logged in";
    public static final String SWENG_ERROR_MSG = "Authentication with SwEgQuizServed has failed.";
    public static final String TEQUILA_ERROR_MSG = "Login with Tequila was  NOT successful. "
            + "Please check your account infos.";
    public static final String UNEXPECTED_ERROR_MSG = "An unexpected error has occured. "
            + "Your credentials couldn't be saved. Please try again";
    private HttpResponse mResult = null;
    private String mStatusMsg = "";

    private String token;
    private Toast mMyToast = Toast.makeText(QuizApp.getContexStatic(), null, Toast.LENGTH_LONG);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        // by default initial state is
        TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.authentication, menu);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onDestroy() {

        super.onStop();
        mMyToast.cancel();
    }

    public void logIn(View view) {
        if (HttpComms.getInstance().isConnected()) {
            new HttpCommsBackgroundTask(this).execute();
        } else {

            mMyToast.setText("Not internet connection");
            mMyToast.show();
        }
    }

    @Override
    public HttpResponse requete() {

        try {
            requestAuthToken();
        } catch (AuthenticationException e) {
            mResult = null;
            mStatusMsg = e.getMessage();
            mMyToast.setText(mStatusMsg);
            Log.e(getLocalClassName(), e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            mResult = null;
            mStatusMsg = INTERNAL_ERROR_MSG;
            mMyToast.setText(INTERNAL_ERROR_MSG);
            Log.e(getLocalClassName(), e.getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            mResult = null;
            mStatusMsg = INTERNAL_ERROR_MSG;
            mMyToast.setText(INTERNAL_ERROR_MSG);
            Log.e(getLocalClassName(), e.getMessage());
        }

        return mResult;
    }

    @Override
    public void processHttpReponse(HttpResponse response) {
        if (response != null) {
            try {
                String sessionID = JSONParser.getParser(response).getString("session");
                CredentialManager.getInstance().setUserCredential(sessionID);
                mMyToast.setText(mStatusMsg);
                mMyToast.show();
                this.finish();

            } catch (JSONException e) {
                mStatusMsg = UNEXPECTED_ERROR_MSG;
                mMyToast.setText(UNEXPECTED_ERROR_MSG);
                e.printStackTrace();
                failedAuthenReset();
            }

        } else {
            failedAuthenReset();
        }
    }

    private void requestAuthToken() throws AuthenticationException, JSONException, IOException {

        mResult = HttpComms.getInstance().getHttpResponse(HttpComms.URL_SWENG_SWERVER_LOGIN);

        if (mResult != null && mResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            postTequilaToken();
        } else {
            Log.w("Authentication state: UNAUTHENTICATED", INTERNAL_ERROR_MSG);
            throw new AuthenticationException(INTERNAL_ERROR_MSG);
        }
    }

    private void postTequilaToken() throws AuthenticationException, JSONException, IOException {

        token = JSONParser.getParser(mResult).getString("token");
        String username = ((EditText) findViewById(R.id.GasparUsername_EditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.GasparPassword_EditText)).getText().toString();
        NameValuePair[] namList = {new BasicNameValuePair("requestkey", token),
            new BasicNameValuePair("username", username), new BasicNameValuePair("password", password)};
        UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(Arrays.asList(namList));
        mResult = HttpComms.getInstance().postEntity(HttpComms.URL_TEQUILA_LOGIN, urlEntity);
        checkTequila();

    }

    private void checkTequila() throws AuthenticationException, JSONException, IOException {
        if (mResult != null && mResult.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
            confirm();

        } else {
            Log.w("Authentication state: TEQUILA", TEQUILA_ERROR_MSG);
            throw new AuthenticationException(TEQUILA_ERROR_MSG);
        }

    }

    private void confirm() throws AuthenticationException, JSONException, IOException {
        mResult = HttpComms.getInstance().postJSONObject(HttpComms.URL_SWENG_SWERVER_LOGIN,
                (new JSONObject()).put("token", token));

        if (mResult != null && mResult.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            mStatusMsg = SUCCEFUL_MSG;

        } else {
            Log.w("Authentication state: CONFIRMATION", SWENG_ERROR_MSG);
            throw new AuthenticationException(SWENG_ERROR_MSG);
        }
    }

    private void failedAuthenReset() {
        Log.v("message", mStatusMsg);
        mMyToast.show();
        TestCoordinator.check(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);

        ((EditText) findViewById(R.id.GasparPassword_EditText)).setText("");
        ((EditText) findViewById(R.id.GasparUsername_EditText)).setText("");

    }

}
