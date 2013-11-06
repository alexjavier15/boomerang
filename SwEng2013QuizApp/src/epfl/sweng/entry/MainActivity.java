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
import android.widget.CheckBox;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.authentication.AuthenticationActivity;
import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.authentication.SharedPreferenceManager;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.servercomm.CacheManager;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;

/**
 * @author AlbanMarguet, CanGuzelhan
 * 
 */
public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {

    private boolean authenticated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferenceManager.getInstance().addOnChangeListener(this);

        String newValue = CredentialManager.getInstance().getUserCredential();
        checkStatus(newValue);

    }

    private void checkStatus(String newValue) {
        if (newValue.equals("")) {
            Log.i("Session Id has been removed: logged out", newValue);
            setAthenticated(false);
            ((Button) findViewById(R.id.log_inout)).setText("Log in using Tequila");
        } else {
            Log.i("New session Id is: ", newValue);
            setAthenticated(true);
            ((Button) findViewById(R.id.log_inout)).setText("Log out");
            CacheManager.getInstance();
        }
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

    /**
     * 
     */
    @Override
    public void onResume() {
        super.onResume();
        TestCoordinator.check(TTChecks.MAIN_ACTIVITY_SHOWN);
    }

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

    public void logInOut(View view) {
        if (authenticated) {
            // this means you are logging out!
            CredentialManager.getInstance().removeUserCredential();
            TestCoordinator.check(TTChecks.LOGGED_OUT);
        } else {
            Intent loginActivityIntent = new Intent(this, AuthenticationActivity.class);
            startActivity(loginActivityIntent);
        }

    }

    /**
	 * 
	 */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
        Debug.out("listener notify");
        if (key.equals(PreferenceKeys.SESSION_ID)) {
            String newValue = pref.getString(key, "");
            Log.i("MainActivity Listener new key value session id : ", newValue);
            checkStatus(newValue);
        } else if (key.equals(PreferenceKeys.ONLINE_MODE)) {
            CheckBox offLineMode = (CheckBox) this.findViewById(R.id.offline_mode);
            boolean isOnlineMode = pref.getBoolean(key, false);
            offLineMode.setChecked(!isOnlineMode);
            if (isOnlineMode) {
                CacheManager.getInstance().syncPostCachedQuestions();
            }
        }

    }

    /**
     * 
     * @param newState
     */
    private void setAthenticated(boolean newState) {
        authenticated = newState;
        ((Button) findViewById(R.id.ShowQuestionButton)).setEnabled(newState);
        ((Button) findViewById(R.id.SubmitQuestionButton)).setEnabled(newState);
        ((CheckBox) findViewById(R.id.offline_mode)).setEnabled(newState);
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
        CheckBox offLineMode = (CheckBox) this.findViewById(R.id.offline_mode);
        offLineMode.setChecked(true);
        startActivity(submitActivityIntent);
    }

    /**
     * 
     * @param view
     */
    public void changeNetworkMode(View view) {

        CheckBox offLineMode = (CheckBox) view.findViewById(R.id.offline_mode);

        SharedPreferenceManager.getInstance().writeBooleaPreference(PreferenceKeys.ONLINE_MODE,
                !offLineMode.isChecked());
    }

}
