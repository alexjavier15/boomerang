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
        return QuizApp.getInstance().getBaseContext();
    }

    public static SharedPreferences getPreferences() {
        return  QuizApp.getInstance().getSharedPreferences(PreferenceKeys.USER_PREFERENCE, MODE_PRIVATE);
    }

    public static Resources getResourcesStatic() {
        return  QuizApp.getInstance().getResources();
    }

    public static QuizApp getInstance() {
        if (sApp == null) {
            sApp = new QuizApp();
        }
        return sApp;
    }

    /**
     * 
     */
    public QuizApp() {
        super();

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
