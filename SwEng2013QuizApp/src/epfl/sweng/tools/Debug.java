package epfl.sweng.tools;

import android.util.Log;

/**
 * Creates an easy identifiable System out
 * 
 * @author LorenzoLeon
 * 
 */
public final class Debug {

    public static void out(Object msg) {
        Log.i("info", msg.toString());
    }

    private Debug() {
    }

}
