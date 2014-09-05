package org.inbar.come.model;

import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inbar on 25/08/14.
 */

@ParseClassName("UserFollowInfo")
public class UserFollowInfo extends ParseObject {

    public enum Keys {
        INCOMING_REQUESTS("incomingRequests"),
        OUTGOING_REQUESTS("outgoingRequests"),
        FOLLOWING("following"),
        FOLLOWD_BY("followdBy");

        private final String name;

        private Keys(String name) {

            this.name = name;
        }

        public String getName() {

            return name;
        }
    }

    public static UserFollowInfo newEmptyInstance(ParseUser user) {

        final UserFollowInfo userFollowInfo = new UserFollowInfo();

        final ParseACL acl = new ParseACL(user);
        acl.setPublicReadAccess(true);
        userFollowInfo.setACL(acl);
        userFollowInfo.setUsername(user.getUsername());

        return userFollowInfo;
    }

    private void setUsername(String username) {

        put("username", username);
    }

    public String getUsername() {

        return getString("username");
    }

    public List<String> getFollowingList() {

        return getList(Keys.FOLLOWING.getName());
    }

    public List<String> getFollowersList() {

        return getList(Keys.FOLLOWD_BY.getName());
    }

    public List<String> getIncomingRequestsList() {

        return getList(Keys.INCOMING_REQUESTS.getName());
    }

    public List<String> getOutgoingRequestsList() {

        return getList(Keys.OUTGOING_REQUESTS.getName());
    }

}
