package epfl.sweng.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class CredentialManager {

    private static volatile CredentialManager instance = null;
    private SharedPreferences globalPreferences = null;

    private CredentialManager() {

        super();
    }

    public final static CredentialManager getInstance() {

        if (CredentialManager.instance == null) {

            instance = new CredentialManager();

        }
        return instance;

    }

    public void setContext(Context context) {

        globalPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void writePreference(String name, String value) {

        if (globalPreferences != null) {

            globalPreferences.edit().putString(name, value);

        }
    }

    public void removePreference(String name) {
        if (globalPreferences != null) {

            globalPreferences.edit().remove(name);

        }

    }

}
