package epfl.sweng.tools;

import android.util.Log;

/**
 * Creates an easy identifiable System out
 * 
 * @author LorenzoLeon
 * 
 */
public final class Debug {

    public static void out(Class<?> clas, Object msg) {
        Log.i(clas.getName(), msg.toString());
    }

    private Debug() {
    }

}
