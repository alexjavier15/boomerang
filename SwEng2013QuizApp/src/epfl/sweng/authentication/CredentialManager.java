package epfl.sweng.authentication;

import epfl.sweng.servercomm.QuizApp;
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

	public boolean removeUserCredential() {
		return mUserPreferences.edit().clear().commit();
	}

	public boolean setUserCredential(String value) {
		return mUserPreferences.edit()
				.putString(PreferenceKeys.SESSION_ID, value).commit();
	}
}
