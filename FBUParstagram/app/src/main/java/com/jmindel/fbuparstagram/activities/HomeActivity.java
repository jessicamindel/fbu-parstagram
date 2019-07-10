package com.jmindel.fbuparstagram.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.fragments.ComposeFragment;
import com.jmindel.fbuparstagram.fragments.ProfileFragment;
import com.jmindel.fbuparstagram.fragments.TimelineFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    // ids start with 11
    public static final int MAKE_POST_REQUEST_CODE = 11;

    @BindView(R.id.bottom_navigation)   BottomNavigationView bottomNav;

    FragmentManager fragmentManager;
    TimelineFragment timelineFragment;
    ComposeFragment composeFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // TODO: Migrate all timeline data fetching logic to a wrapped object here so that data persists
        // TODO: Migrate logout actions to a wrapper object as well
        fragmentManager = getSupportFragmentManager();
        timelineFragment = new TimelineFragment();
        composeFragment = new ComposeFragment();
        profileFragment = new ProfileFragment();

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.miCompose:
                        fragment = composeFragment;
                        break;
                    case R.id.miProfile:
                        fragment = profileFragment;
                        break;
                    case R.id.miHome:
                    default:
                        fragment = timelineFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flFragmentContainer, fragment).commit();
                return true;
            }
        });

        // Set logged out to false
        getIntent().putExtra(LoginActivity.KEY_LOGGED_OUT, false);
    }

//    public void onMakePost(View view) {
//        Intent i = new Intent(this, MakePostActivity.class);
//        startActivityForResult(i, MAKE_POST_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK && requestCode == MAKE_POST_REQUEST_CODE) {
////            final Post post = data.getParcelableExtra(MakePostActivity.KEY_POST);
////            post.saveInBackground(new SaveCallback() {
////                @Override
////                public void done(ParseException e) {
////                    if (e != null) {
////                        Log.e("HomeActivity", "Posting failed");
////                        e.printStackTrace();
////                        Toast.makeText(HomeActivity.this, "Posting failed", Toast.LENGTH_LONG).show();
////                    } else {
////                        Log.d("HomeActivity", "Posting succeeded!");
////                        posts.add(0, post);
////                        adapter.notifyItemInserted(0);
////                        rvPosts.scrollToPosition(0);
////                        // TODO: Navigate to new post and/or otherwise show it
////                    }
////                }
////            });
//        }
//    }
}
