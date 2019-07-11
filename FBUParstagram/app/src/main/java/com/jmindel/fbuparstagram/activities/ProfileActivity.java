package com.jmindel.fbuparstagram.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.fragments.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import butterknife.BindView;

public class ProfileActivity extends AppCompatActivity {

    public static final String KEY_USER_ID = "objectId";

    FragmentManager fragmentManager;
    private ProfileFragment fragment;

    @BindView(R.id.flFragmentContainer) FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fragmentManager = getSupportFragmentManager();

        // Retrieve user passed in from id
        String uid = getIntent().getStringExtra(KEY_USER_ID);
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo(KEY_USER_ID, uid);
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null || objects.size() == 0) {
                    Log.e("ProfileActivity", "Failed fetching user");
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Failed fetching user", Toast.LENGTH_LONG).show();
                } else {
                    // Launch fragment specific to fetched user
                    ParseUser user = objects.get(0);
                    fragment = new ProfileFragment();
                    fragment.setUser(user);
                    fragmentManager.beginTransaction().replace(R.id.flFragmentContainer, fragment).commit();
                }
            }
        });
    }
}
