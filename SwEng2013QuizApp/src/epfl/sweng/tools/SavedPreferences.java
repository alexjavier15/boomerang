package epfl.sweng.tools;

import epfl.sweng.entry.MainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * SavedPreferences is a singleton class which creates an object where all
 * activities can go and search for a shared preferences key. this case being a
 * session id
 * 
 * @author LorenzoLeon
 * 
 */
@SuppressLint("CommitPrefEdits")
public final class SavedPreferences {
	private static SavedPreferences singletonObject = null;
	private Context context;
	private String user = "user_session";
	private String id = "SESSION_ID";

	private SavedPreferences(Context cont) {
		context = cont;
		context.getSharedPreferences(user, Context.MODE_PRIVATE).edit().clear().commit();
	}

	public static SavedPreferences getInstance(Context cont) {
		if (singletonObject == null) {
			singletonObject = new SavedPreferences(cont);
		}
		return singletonObject;
	}

	private SharedPreferences getPreferences() {
		return context.getSharedPreferences(user, Context.MODE_PRIVATE);
	}

	public String getSessionID() {
		return getPreferences().getString(id, "");
	}

	public boolean setSessionID(String string) {
		return getPreferences().edit().putString(id, string).commit();
	}

	public boolean removeSessionID() {
		return getPreferences().edit().remove(id).commit();
	}

	public void setListener(MainActivity listener) {
		Log.i("SavedPreferences", "register listener");
		getPreferences().registerOnSharedPreferenceChangeListener(listener);

	}
}
