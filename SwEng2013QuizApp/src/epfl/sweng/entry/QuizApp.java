/**
 * 
 */
package epfl.sweng.entry;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

/**
 * @author Alex
 * 
 */
public class QuizApp extends Application {
    private static Context sContext;

    public static Resources getResourcesStatic() {
        return sContext.getResources();
    }

    public static Context getContexStatic() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }
}
