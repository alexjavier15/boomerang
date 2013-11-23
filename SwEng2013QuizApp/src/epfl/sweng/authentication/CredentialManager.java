package epfl.sweng.authentication;

import epfl.sweng.entry.QuizApp;
import android.content.SharedPreferences;

/**
 * SavedPreferences is a singleton class which creates an object where all
 * activities can go and search for a shared preferences key. this case being a
 * session id
 * 
 * @author LorenzoLeon
 * 
 */
public final class CredentialManager {

	private static CredentialManager sUserCredentials = null;
	private static SharedPreferences mUserPreferences = null;

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
	
	public boolean getOnlineStatus() {
	    return mUserPreferences.getBoolean(PreferenceKeys.ONLINE_MODE, true);
	}

	public boolean removeUserCredential() {
		return mUserPreferences.edit().remove(PreferenceKeys.SESSION_ID).commit();
	}

	public boolean setUserCredential(String value) {
		return mUserPreferences.edit()
				.putString(PreferenceKeys.SESSION_ID, value).commit();
	}
}
