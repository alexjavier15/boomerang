/**
 * 
 */
package epfl.sweng.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import epfl.sweng.servercomm.QuizApp;

/**
 * @author Alex
 * 
 */
public final class SharedPreferenceManager {

    private static SharedPreferenceManager sUserPreferences = null;
    private static SharedPreferences sSharedPreferences = null;
    private static Context sContext = null;

    public static SharedPreferenceManager getInstance() {
        if (sUserPreferences == null) {
            sUserPreferences = new SharedPreferenceManager();
        }
        return sUserPreferences;
    }

    // Fix with correct pre-condition implemetation

    private SharedPreferenceManager() {

        sContext = QuizApp.getContexStatic();
        sSharedPreferences = sContext.getSharedPreferences(PreferenceKeys.USER_PREFERENCE, Context.MODE_MULTI_PROCESS);
        // reset de conenction at the dafault status "on line"
        writeBooleaPreference(PreferenceKeys.ONLINE_MODE, true);

    }

    public Object getSystemService(String name) {

        return sContext.getSystemService(name);

    }

    public boolean getBooleanPreference(String key) {

        return sSharedPreferences.getBoolean(key, false);

    }

    public String getStringPreference(String key) {

        return sSharedPreferences.getString(key, "");

    }

    public int getIntPreference(String key) {

        return sSharedPreferences.getInt(key, -1);

    }

    public boolean writeIntPreference(String key, int value) {
        return sSharedPreferences.edit().putInt(key, value).commit();

    }

    public synchronized boolean writeBooleaPreference(String key, boolean value) {
        boolean res = sSharedPreferences.edit().putBoolean(key, value).commit();
        sSharedPreferences.edit().apply();

        return res;

    }

    public boolean writeStringPreference(String key, String value) {
        return sSharedPreferences.edit().putString(key, value).commit();

    }

    public boolean removePreference(String key) {
        return sSharedPreferences.edit().remove(key).commit();

    }

    /**
     * @param listener
     */
    public void addOnChangeListener(OnSharedPreferenceChangeListener listener) {
        sSharedPreferences.registerOnSharedPreferenceChangeListener(listener);

    }

}
