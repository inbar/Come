package org.inbar.come.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by inbar on 25/08/14.
 */

@ParseClassName("FollowRequest")
public class FollowRequest extends ParseObject {

    public enum Keys {
        REQUESTING_USER("requestingUser"),
        FRIEND_EMAIL("friendEmail");

        private final String name;

        private Keys(String name) {

            this.name = name;
        }

        public String getName() {

            return name;
        }
    }


    public void setRequestingUser(String requestingUser) {

        put(Keys.REQUESTING_USER.getName(), requestingUser);
    }

    public String getRequestingUser() {

        return getString(Keys.REQUESTING_USER.getName());
    }

    public void setFriendEmail(String email) {

        put(Keys.FRIEND_EMAIL.getName(), email);
    }

    public String getFriendEmail() {

        return getString(Keys.FRIEND_EMAIL.getName());
    }


}
