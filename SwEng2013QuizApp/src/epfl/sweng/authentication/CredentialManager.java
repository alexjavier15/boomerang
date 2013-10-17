package epfl.sweng.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

/**
 * SavedPreferences is a singleton class which creates an object where all activities can go and search for a shared
 * preferences key. this case being a session id
 * 
 * @author LorenzoLeon
 * 
 */
@SuppressLint("CommitPrefEdits")
public final class CredentialManager {

    private static CredentialManager singletonObject = null;
    public final static String USER_PREFERENCE = "user_session";

    public static CredentialManager getInstance(Context cont) {
        if (singletonObject == null) {
            singletonObject = new CredentialManager(cont);
        }
        return singletonObject;
    }

    private Context mContext;

    private CredentialManager(Context cont) {
        mContext = cont;

    }

    public SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
    }

    public boolean removePreference(String name) {
        return getPreferences().edit().remove(name).commit();
    }

    public void setListener(OnSharedPreferenceChangeListener listener) {
        Log.i("SavedPreferences", "register listener");
        getPreferences().registerOnSharedPreferenceChangeListener(listener);

    }

    public boolean writePreference(String name, String value) {
        return getPreferences().edit().putString(name, value).commit();

    }

}
