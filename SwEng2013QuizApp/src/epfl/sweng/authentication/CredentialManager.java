package epfl.sweng.authentication;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

/**
 * SavedPreferences is a singleton class which creates an object where all activities can go and search for a shared
 * preferences key. this case being a session id
 * 
 * @author LorenzoLeon
 * 
 */
public final class CredentialManager {

    private static CredentialManager sUserCredentials = null;
    private static SharedPreferenceManager mUserPreferences = null;

    public static CredentialManager getInstance() {
        if (sUserCredentials == null) {
            sUserCredentials = new CredentialManager();
        }
        return sUserCredentials;
    }

    private CredentialManager() {
        mUserPreferences = SharedPreferenceManager.getInstance();

    }

    public String getUserCredential() {
        return mUserPreferences.getStringPreference(PreferenceKeys.SESSION_ID);
    }

    public boolean removeUserCredential() {
        return mUserPreferences.removePreference(PreferenceKeys.SESSION_ID);
    }

    public void addOnchangeListener(OnSharedPreferenceChangeListener listener) {
        Log.i("SavedPreferences", "register listener");
        mUserPreferences.addOnChangeListener(listener);
    }

    public boolean setUserCredential(String value) {
        return mUserPreferences.writeStringPreference(PreferenceKeys.SESSION_ID, value);

    }

}
