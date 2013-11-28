package epfl.sweng.authentication;

import android.content.SharedPreferences;
import epfl.sweng.servercomm.QuizApp;

/**
 * SavedPreferences is a singleton class which creates an object where all activities can go and search for a shared
 * preferences key. this case being a session id
 * 
 * @author LorenzoLeon
 * 
 */
public final class CredentialManager {

    private static SharedPreferences mUserPreferences = null;
    private static CredentialManager sUserCredentials = null;

    public static CredentialManager getInstance() {
        if (sUserCredentials == null) {
            sUserCredentials = new CredentialManager();
        }
        return sUserCredentials;
    }

    private CredentialManager() {
        mUserPreferences = QuizApp.getPreferences();
    }

    public String getUserCredential() {

        return mUserPreferences.getString(PreferenceKeys.SESSION_ID, "");
    }

    public boolean removeUserCredential() {
        return setUserCredential("");
    }

    public boolean setUserCredential(String value) {
        return mUserPreferences.edit().putString(PreferenceKeys.SESSION_ID, value).commit();
    }
}
