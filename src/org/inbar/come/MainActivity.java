package org.inbar.come;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.inbar.come.dialogs.FollowRequestDialog;
import org.inbar.come.model.UserFollowInfo;
import org.inbar.come.util.Alerts;
import org.inbar.come.util.CloudFunctions;
import org.inbar.come.util.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements FollowRequestDialog.FriendRequestDialogListener {

    public static final String TAG = "MainActivity";
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> friendRequestList = new ArrayList();
    private ParseUser currentUser;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ParseAnalytics.trackAppOpened(getIntent());

        final ListView friendRequestListView = (ListView) findViewById(R.id.friend_request_list);
        listAdapter = new ArrayAdapter<String>(this, R.layout.row, friendRequestList);
        friendRequestListView.setAdapter(listAdapter);

    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i(TAG, "onResume");

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            displayLogin();
            return;
        } else {
            final TextView usernameLabel = (TextView) findViewById(R.id.username_label);
            usernameLabel.setText(currentUser.getUsername());
            getUserFolloInfo();
        }

    }

    private UserFollowInfo getUserFolloInfo() {

        final ParseQuery<UserFollowInfo> query = ParseQuery.getQuery(UserFollowInfo.class);
        query.whereEqualTo("username", currentUser.getUsername());
        query.getFirstInBackground(new GetCallback<UserFollowInfo>() {
            @Override
            public void done(UserFollowInfo userFollowInfo, ParseException e) {

                if (userFollowInfo == null) {
                    userFollowInfo = UserFollowInfo.newEmptyInstance(MainActivity.this.currentUser);
                    userFollowInfo.saveInBackground();
                } else {
                    Logging.justLog(TAG, userFollowInfo.getUsername());
                    Logging.justLog(TAG, "Followers list -> " + userFollowInfo.getFollowersList());
                    Logging.justLog(TAG, "Following list -> " + userFollowInfo.getFollowingList());
                    Logging.justLog(TAG, "Incoming list -> " + userFollowInfo.getIncomingRequestsList());
                    Logging.justLog(TAG, "Outgoing list -> " + userFollowInfo.getOutgoingRequestsList());
                }
            }
        });

        return null;
    }

    private void displayLogin() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /*
    * Controller methods (View API)
    * */

    public void doLogout(View view) {

        ParseUser.logOut();
        displayLogin();
    }


    public void doCheckRequests(View view) {

        refreshFollowRequests();
    }

    private void refreshFollowRequests() {

        getUserFolloInfo();
    }


    public void doOpenFollowRequestDialog(View view) {

        openFollowRequestDialog();
    }

    private void openFollowRequestDialog() {

        final FollowRequestDialog friendRequestDialog = FollowRequestDialog.newInstance();
        friendRequestDialog.show(getFragmentManager(), "FollowRequestDialog");
    }



    /*
    * Follow request dialog
    * */

    // Callbacks
    @Override
    public void onFollowRequestDialogPositiveClick(String email) {

        Logging.alert("send friend request to " + email, this);
        sendFollowRequest(email);
    }

    @Override
    public void onFollowRequestDialogNegativeClick() {

        Logging.alert("cancle dialog", this);
    }

    public void sendFollowRequest(final String email) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("requestingUser", this.currentUser.getUsername());
        params.put("targetUser", email);

        ParseCloud.callFunctionInBackground(CloudFunctions.SEND_FOLLOW_REQUEST.getFunctionName(), params, new FunctionCallback<String>() {
            @Override
            public void done(String response, ParseException e) {

                if (e == null) {
                    Alerts.popup("request sent: " + MainActivity.this.currentUser.getUsername() + "->" + email + " response: " + response, getApplicationContext());
                } else {
                    Logging.handleError(TAG, e.getMessage(), e, getApplicationContext());
                }
            }
        });

    }


}
