/**
 * 
 */
package epfl.sweng.entry;

import epfl.sweng.authentication.PreferenceKeys;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

	public static SharedPreferences getPreferences() {
		return getContexStatic().getSharedPreferences(
				PreferenceKeys.USER_PREFERENCE, MODE_PRIVATE);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
	}
}
