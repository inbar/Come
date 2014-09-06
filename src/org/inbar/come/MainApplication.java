package org.inbar.come;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.inbar.come.model.UserFollowInfo;

public class MainApplication extends Application {

    public static final String TAG = "MainApplication";

    @Override
    public void onCreate() {

        super.onCreate();

        String parse_app_id = "";
        String parse_client_key = "";

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            parse_app_id = bundle.getString("PARSE_APP_ID");
            parse_client_key = bundle.getString("PARSE_CLIENT_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        // Add your initialization code here
        ParseObject.registerSubclass(UserFollowInfo.class);

        Parse.initialize(this, parse_app_id, parse_client_key);


//		ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

    }

}
