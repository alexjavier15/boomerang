/**
 * 
 */
package epfl.sweng.servercomm;

import android.content.Context;

/**
 * @author Alex
 * 
 */
public final class CacheManager {
    /*
     * private final static String CACHE_FILE = "cache.tmp";
     * private final static String OFFLINE_TASKS = "offline.tmp";
     */

    private static CacheManager sSingletonObject = null;
    private Context mContext;

    /*
     * private FileOutputStream mFileOut;
     * private FileInputStream mFileIn;
     */

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
        setContext(context);

    }

    /**
     * Return the the context
     * 
     * @return the context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Set the context
     * 
     * @param context
     *            the context to set
     */
    public void setContext(Context context) {
        mContext = context;
    }

}
