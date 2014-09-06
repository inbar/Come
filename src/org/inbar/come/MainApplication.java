package org.inbar.come;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

import android.app.Application;
import android.util.Log;

import org.inbar.come.model.UserFollowInfo;

public class MainApplication extends Application {

    public static final String APPLICATION_ID = "8X6rs9B1bXUYD6VcBcQ8Lu0HXBF9ljQ3ghkiYznd";
    public static final String CLIENT_KEY = "g94k4VCVwTEFFSQjmCp2ifJmnXcRCnHjD6RYX0cq";
    public static final String TAG = "MainApplication";

    @Override
	public void onCreate() {
		super.onCreate();

        Log.i(TAG, "main");

        // Add your initialization code here
//        ParseObject.registerSubclass(FollowRequest.class);
        ParseObject.registerSubclass(UserFollowInfo.class);

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);


//		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);

    }

}
