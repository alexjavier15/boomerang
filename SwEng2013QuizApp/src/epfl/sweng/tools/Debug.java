package epfl.sweng.tools;

import android.util.Log;

public final class Debug {

    public static void out(Object msg) {
        Log.i("info", msg.toString());
    }

    private Debug() {
    }

}
