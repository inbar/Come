package org.inbar.come;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.inbar.come.dialogs.FriendRequestDialog;
import org.inbar.come.model.FollowRequest;
import org.inbar.come.model.UserFollowInfo;
import org.inbar.come.util.Alerts;
import org.inbar.come.util.CloudFunctions;
import org.inbar.come.util.Logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements FriendRequestDialog.FriendRequestDialogListener {

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

        refreshFollowRequests();

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
                    // TODO populate lists
                }
            }
        });

        return null;
    }

    private void displayLogin() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void doCheckRequests(View view) {

        refreshFollowRequests();
    }

    private void refreshFollowRequests() {

        final ParseQuery<FollowRequest> query = ParseQuery.getQuery(FollowRequest.class);
        query.whereEqualTo(FollowRequest.Keys.FRIEND_EMAIL.getName(), currentUser.getUsername());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<FollowRequest>() {
            @Override
            public void done(List<FollowRequest> followRequests, ParseException e) {

                if (e == null) {
                    Logging.justLog(TAG, "got " + followRequests.size() + " requests", getApplicationContext());
                    List<String> tmpList = new ArrayList<String>();
                    for (FollowRequest followRequest : followRequests) {
                        String username = followRequest.getRequestingUser();
                        tmpList.add(username);
                    }
                    listAdapter.clear();
                    listAdapter.addAll(tmpList);
                } else {
                    Logging.handleError(TAG, e.getMessage(), e, getApplicationContext());
                }
            }

        });
    }

    public void openFriendRequestDialog(View view) {

        doOpenFriendRequestDialog();
    }

    private void doOpenFriendRequestDialog() {

        final FriendRequestDialog friendRequestDialog = FriendRequestDialog.newInstance();
        friendRequestDialog.show(getFragmentManager(), "FriendRequestDialog");
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

//    public void sendFollowRequest(final String email) {
//
//        final ParseQuery<FollowRequest> query = ParseQuery.getQuery(FollowRequest.class);
//        query.whereEqualTo(FollowRequest.Keys.FRIEND_EMAIL.getName(), email);
//        query.whereEqualTo(FollowRequest.Keys.REQUESTING_USER.getName(), currentUser.getUsername());
//
//        query.getFirstInBackground(new GetCallback<FollowRequest>() {
//            @Override
//            public void done(FollowRequest followRequest, ParseException e) {
//
//                if (followRequest == null) {
//                    doSendFriendRequest(email);
//                } else {
//                    Alerts.popup("Request already pending", getApplicationContext());
//                }
//            }
//        });
//    }

    private void doSendFriendRequest(String email) {

        final FollowRequest request = new FollowRequest();
        request.setRequestingUser(ParseUser.getCurrentUser().getUsername());
        request.setFriendEmail(email);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", email);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if (e == null) {
                    ParseACL acl = new ParseACL(ParseUser.getCurrentUser());
                    acl.setPublicWriteAccess(false);
                    acl.setPublicReadAccess(false);
                    acl.setReadAccess(parseUser, true);
                    request.setACL(acl);

                    request.saveInBackground();
                    Logging.justLog(TAG, "saving request", getApplicationContext());

                } else {
//                    Logging.handleError(TAG, e.getMessage(), e, getApplicationContext());
                    Alerts.popup("User does not exist", getApplicationContext());
                    doOpenFriendRequestDialog();
                }
            }
        });
    }


    public void doLogout(View view) {

        ParseUser.logOut();
        displayLogin();
    }

    // Friend request dialog callbacks

    @Override
    public void onFriendRequestDialogPositiveClick(String email) {

        Logging.alert("send friend request to " + email, this);
        sendFollowRequest(email);
    }

    @Override
    public void onFriendRequestDialogNegativeClick() {

        Logging.alert("cancle dialog", this);
    }


}
