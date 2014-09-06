package org.inbar.come.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by inbar on 25/08/14.
 */
public class Logging {

    public static final boolean DEBUG = true;

    // API

    public static void handleError(String tag, String msg, Throwable t, Context context) {

        if (DEBUG) {
            displayOnScreen(msg, context);
        }

        logError(tag, msg, t);
    }

    public static void justLog(String tag, String msg) {
        log(tag, msg);
    }

    public static void alert(String msg, Context context) {

        if (DEBUG) {
            displayOnScreen(msg, context);
        }
    }

    // Private
    private static void displayOnScreen(String msg, Context context) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private static void logError(String tag, String msg, Throwable t) {

        Log.e(tag, msg, t);
    }

    private static void log(String tag, String msg) {

        Log.i(tag, msg);
    }

}
