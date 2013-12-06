/**
 * 
 */
package epfl.sweng.servercomm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import epfl.sweng.authentication.PreferenceKeys;

/**
 * @author Alex
 * 
 */
public class QuizApp extends Application {
    private static QuizApp sApp;

    public static Context getContexStatic() {
        return sApp.getBaseContext();
    }

    public static SharedPreferences getPreferences() {
        return sApp.getBaseContext().getSharedPreferences(PreferenceKeys.USER_PREFERENCE, MODE_PRIVATE);
    }

    public static Resources getResourcesStatic() {
        return sApp.getBaseContext().getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;

    }
}
