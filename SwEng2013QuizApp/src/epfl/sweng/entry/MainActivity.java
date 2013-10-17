package epfl.sweng.entry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * @author AlbanMarguet, CanGuzelhan
 * 
 */
public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {

    private boolean authenticated = true;

    /**
     * Launches the new Activity ShowQuestionsActivity to display a random question
     * 
     * @param view
     *            The view that was clicked.
     */
    public void askQuestion(View view) {
        Toast.makeText(this, "You are on the page to show a random question!", Toast.LENGTH_SHORT).show();
        Intent showQuestionActivityIntent = new Intent(this, ShowQuestionsActivity.class);
        this.startActivity(showQuestionActivityIntent);
    }

    private void checkStatus(String newValue) {
        if (newValue.equals("")) {
            Log.i("Session Id has been removed: logged out", newValue);
            HttpComms.getInstance(this).setSessionID(null);
            setAthenticated(false);
            ((Button) findViewById(R.id.log_inout)).setText("Log in using Tequila");
        } else {
            Log.i("New session Id is: ", newValue);
            // set new header
            HttpComms.getInstance(this).setSessionID("Tequila " + newValue);
            setAthenticated(true);
            ((Button) findViewById(R.id.log_inout)).setText("Log out");
        }
    }

    public void logInOut(View view) {
        if (authenticated) {
            // this means you are logging out!
            CredentialManager.getInstance(this).removePreference(PreferenceKeys.SESSION_ID);
            TestCoordinator.check(TTChecks.LOGGED_OUT);
        } else {
            Intent loginActivityIntent = new Intent(this, AuthenticationActivity.class);
            startActivity(loginActivityIntent);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CredentialManager.getInstance(this).setListener(this);
        String newValue = CredentialManager.getInstance(this).getPreferences().getString(PreferenceKeys.SESSION_ID, "");
        checkStatus(newValue);
        TestCoordinator.check(TTChecks.MAIN_ACTIVITY_SHOWN);
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * 
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        TestCoordinator.check(TTChecks.MAIN_ACTIVITY_SHOWN);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
        if (key.equals("SESSION_ID")) {
            String newValue = pref.getString(key, "");
            Log.i("MainActivity Listener new key value session id : ", newValue);
            checkStatus(newValue);
        }
    }

    private void setAthenticated(boolean newState) {
        authenticated = newState;
        ((Button) findViewById(R.id.ShowQuestionButton)).setEnabled(newState);
        ((Button) findViewById(R.id.SubmitQuestionButton)).setEnabled(newState);
    }

    /**
     * Launches the new Activity EditQuestionActivity to permit the user to submit a new question to the server
     * 
     * @param view
     *            The view that was clicked.
     */
    public void submitQuestion(View view) {
        Toast.makeText(this, "You are on the page to submit a question!", Toast.LENGTH_SHORT).show();
        Intent submitActivityIntent = new Intent(this, EditQuestionActivity.class);
        startActivity(submitActivityIntent);
    }
}
