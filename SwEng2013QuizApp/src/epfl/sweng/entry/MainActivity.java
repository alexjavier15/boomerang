package epfl.sweng.entry;

import android.app.Activity;
import android.content.Intent;
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
import epfl.sweng.cache.CacheManager;
import epfl.sweng.editquestions.EditQuestionActivity;
import epfl.sweng.patterns.CheckProxyHelper;
import epfl.sweng.searchquestions.SearchActivity;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * @author AlbanMarguet, CanGuzelhan
 * 
 */

public class MainActivity extends Activity {

    private boolean authenticated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpPreferences();
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
    protected void onStart() {

        super.onStart();
        String newValue = CredentialManager.getInstance().getUserCredential();
        Log.v(this.getClass().getName(), "CREDENTIAL MANAGER IS RETURNING : " + newValue);
        checkStatus(newValue);
        CheckBox check = (CheckBox) findViewById(R.id.offline_mode);
        check.setChecked(!QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true));
        TestCoordinator.check(TTChecks.MAIN_ACTIVITY_SHOWN);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(this.getClass().getName(), (new CheckProxyHelper()).getServerCommunicationClass().getName());
        HttpCommsProxy.getInstance();
        if (authenticated && QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true)) {
            CacheManager.getInstance().init();

        }

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

    /**
     * Launches a new Search activity where a query can be composed to the server.
     * 
     * @param view
     */
    public void searchQuestion(View view) {
        Toast.makeText(this, "You are on the page to search a question!", Toast.LENGTH_SHORT).show();
        Intent searchActivityIntent = new Intent(this, SearchActivity.class);
        startActivity(searchActivityIntent);
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

    public void logInOut(View view) {
        if (authenticated) {
            // this means you are logging out!
            CredentialManager.getInstance().removeUserCredential();
            TestCoordinator.check(TTChecks.LOGGED_OUT);
            setAthenticated(false);
        } else {
            Intent loginActivityIntent = new Intent(this, AuthenticationActivity.class);
            startActivity(loginActivityIntent);
            
        }
    }

    /**
     * 
     * @param view
     */
    public void changeNetworkMode(View view) {

        CheckBox offlineCheckBox = (CheckBox) view;
        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, !offlineCheckBox.isChecked()).apply();
        update();
    }

    private void setUpPreferences() {

        QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, true).apply();

    }

    private void update() {
        if (QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true)) {
            TestCoordinator.check(TTChecks.OFFLINE_CHECKBOX_DISABLED);
            ((CheckBox) findViewById(R.id.offline_mode)).setChecked(false);
            CacheManager.getInstance().init();
        } else {
            ((CheckBox) findViewById(R.id.offline_mode)).setChecked(true);
            TestCoordinator.check(TTChecks.OFFLINE_CHECKBOX_ENABLED);
        }

    }

    private void checkStatus(String newValue) {
        if ("".equals(newValue)) {
            Log.i("Session Id has been removed: logged out", newValue);
            setAthenticated(false);
            ((Button) findViewById(R.id.log_inout)).setText("Log in using Tequila");
        } else {
            Log.i("New session Id is: ", newValue);
            setAthenticated(true);
            ((Button) findViewById(R.id.log_inout)).setText("Log out");

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
        ((Button) findViewById(R.id.SearchQuestionButton)).setEnabled(newState);
        ((CheckBox) findViewById(R.id.offline_mode)).setEnabled(newState);
    }
}
