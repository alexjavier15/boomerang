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
    private static Context sContext;

    public static Context getContexStatic() {
        return sContext;
    }

    public static SharedPreferences getPreferences() {
        return getContexStatic().getSharedPreferences(PreferenceKeys.USER_PREFERENCE, MODE_PRIVATE);
    }
    
    private static void setContext(Context context) {
        QuizApp.sContext = context;
        
    }

    public static Resources getResourcesStatic() {
        return sContext.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        QuizApp.setContext(this.getBaseContext());
    }
}