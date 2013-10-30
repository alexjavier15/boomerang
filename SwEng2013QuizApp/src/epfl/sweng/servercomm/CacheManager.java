/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.authentication.PreferenceKeys;
import android.content.Context;

/**
 * @author Alex
 *
 */
public final class CacheManager {
    private final static String CACHE_FILE = "cache.tmp";
    private final static String OFFLINE_TASKS = "offline.tmp";
    
    private static CacheManager sSingletonObject = null;
    private Context mContext;
    private boolean mOnlineMode = false;
    private FileOutputStream mFileOut;
    private FileInputStream mFileIn;
    
   
  
    
    public static CacheManager getInstance(Context cont) {
        if (sSingletonObject == null) {
            sSingletonObject = new CacheManager(cont);
        }
        return sSingletonObject;
    }


    /**
     * @param context
     */
    public CacheManager(Context context) {
             mContext = context;
             mOnlineMode =CredentialManager.getInstance(context).getPreferences()
                 .getBoolean(PreferenceKeys.SESSION_ON, false);
    }
    public void updateStatus(){}
    
    
    
    

    
    
    

}
