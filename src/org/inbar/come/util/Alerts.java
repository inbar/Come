package org.inbar.come.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by inbar on 26/08/14.
 */
public class Alerts {


    // Private
    public static void popup(String msg, Context context) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
