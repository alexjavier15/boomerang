package epfl.sweng.entry;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.IBinder;
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
import epfl.sweng.servercomm.BackgroundServiceTask;
import epfl.sweng.servercomm.CacheManagerService;
import epfl.sweng.servercomm.CacheManagerService.CacheServiceBinder;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.servercomm.QuizApp;
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
    private boolean isBound;
    private CacheManagerService mCacheService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferenceManager.getInstance();
        HttpCommsProxy.getInstance();
        CredentialManager.getInstance().addOnchangeListener(this);
        String newValue = CredentialManager.getInstance().getUserCredential();
        Intent intent = new Intent(QuizApp.getContexStatic().getApplicationContext(), CacheManagerService.class);
        this.getApplicationContext().bindService(intent, mCacheConnection, Context.BIND_AUTO_CREATE);
        Debug.out("service bound: " + isBound);
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
            SharedPreferenceManager.getInstance().writeBooleaPreference(PreferenceKeys.ONLINE_MODE,
                    HttpComms.getInstance().isConnected());
            Debug.out("is connected :"
                    + SharedPreferenceManager.getInstance().getBooleanPreference(PreferenceKeys.ONLINE_MODE));
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

    private ServiceConnection mCacheConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CacheServiceBinder binder = (CacheServiceBinder) service;
            mCacheService = binder.getService();
            isBound = true;
            if (HttpComms.getInstance().isConnected()) {
                SharedPreferenceManager.getInstance().writeBooleaPreference(PreferenceKeys.ONLINE_MODE, true);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Debug.out("service disconnected");

        }

    };

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
        if (key.equals(PreferenceKeys.SESSION_ID)) {
            String newValue = pref.getString(key, "");
            Log.i("MainActivity Listener new key value session id : ", newValue);
            checkStatus(newValue);
        } else if (key.equals(PreferenceKeys.ONLINE_MODE)) {
            if (pref.getBoolean(key, false)) {

                (new BackgroundServiceTask(mCacheService)).execute(null, null);
            } else {

                CheckBox offLineMode = (CheckBox) this.findViewById(R.id.offline_mode);
                offLineMode.setChecked(true);
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
