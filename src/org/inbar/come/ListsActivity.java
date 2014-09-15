package org.inbar.come;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.inbar.come.model.UserFollowInfo;
import org.inbar.come.util.Logging;

public class ListsActivity extends Activity {

    private static final String TAG = "ListsActivity";

    FoollowingListsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    private ParseUser currentUser;

    List<String> incoming = new ArrayList<String>();
    List<String> outgoing = new ArrayList<String>();
    List<String> following = new ArrayList<String>();
    List<String> followers = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        currentUser = ParseUser.getCurrentUser();

        updateUserFollowInfo();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new FoollowingListsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int position) {

            }
        });
        viewPager.setAdapter(sectionsPagerAdapter);

    }


    public class FoollowingListsPagerAdapter extends FragmentPagerAdapter {

        public FoollowingListsPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            final ListFragment listFragment = new ListFragment();
            List list = null;
            switch (position) {
                case 0:
                    list = following;
                    break;
                case 1:
                    list = followers;
                    break;
                case 2:
                    list = incoming;
                    break;
                case 3:
                    list = outgoing;
                    break;
            }

            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(ListsActivity.this, R.layout.row, list);
            listFragment.setListAdapter(listAdapter);

            return  listFragment;
        }

        @Override
        public int getCount() {

            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Following";
                case 1:
                    return "Followers";
                case 2:
                    return "Incoming requests";
                case 3:
                    return "Outgoing requests";
            }
            return null;
        }
    }


    private UserFollowInfo updateUserFollowInfo() {

        final ParseQuery<UserFollowInfo> query = ParseQuery.getQuery(UserFollowInfo.class);
        query.whereEqualTo("username", currentUser.getUsername());
        query.getFirstInBackground(new GetCallback<UserFollowInfo>() {
            @Override
            public void done(UserFollowInfo userFollowInfo, ParseException e) {

                if (userFollowInfo == null) {
                    currentUser = ParseUser.getCurrentUser();
                    userFollowInfo = UserFollowInfo.newEmptyInstance(currentUser);
                    userFollowInfo.saveInBackground();
                } else {
                    final List<String> followingList = userFollowInfo.getFollowingList();
                    final List<String> followersList = userFollowInfo.getFollowersList();
                    final List<String> incomingRequestsList = userFollowInfo.getIncomingRequestsList();
                    final List<String> outgoingRequestsList = userFollowInfo.getOutgoingRequestsList();

                    if (followingList != null) {
                        following.clear();
                        following.addAll(followingList);
                    }
                    if (followersList != null) {
                        followers.clear();
                        followers.addAll(followersList);
                    }
                    if (incomingRequestsList != null) {
                        incoming.clear();
                        incoming.addAll(incomingRequestsList);
                    }
                    if (outgoingRequestsList != null) {
                        outgoing.clear();
                        outgoing.addAll(outgoingRequestsList);
                    }

                    Logging.justLog(TAG, userFollowInfo.getUsername());
                    Logging.justLog(TAG, "Followers list -> " + followersList);
                    Logging.justLog(TAG, "Following list -> " + followingList);
                    Logging.justLog(TAG, "Incoming list -> " + incomingRequestsList);
                    Logging.justLog(TAG, "Outgoing list -> " + outgoingRequestsList);

                }
            }
        });

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
