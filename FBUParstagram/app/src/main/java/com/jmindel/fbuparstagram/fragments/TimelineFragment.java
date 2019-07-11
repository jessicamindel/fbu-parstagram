package com.jmindel.fbuparstagram.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmindel.fbuparstagram.scrolling.PostLayout;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.activities.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineFragment extends Fragment {

    @BindView(R.id.plPosts)
    PostLayout pvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        pvPosts.load();
    }

    // File file = new File(path); --> ParseFile parseFile = new ParseFile(file);
//    private void createPost(String caption, ParseFile image, ParseUser user) {
//        final Post post = new Post();
//        post.setCaption(caption);
//        post.setImage(image);
//        post.setUser(user);
//
//        post.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.e("HomeActivity", "Posting failed");
//                    e.printStackTrace();
//                    Toast.makeText(getContext(), "Posting failed", Toast.LENGTH_LONG).show();
//                } else {
//                    Log.d("HomeActivity", "Create post success!");
//                    // TODO: Add to adapter
//                }
//            }
//        });
//    }

    public void logOut(View view) {
        ((Activity) getContext()).getIntent().putExtra(LoginActivity.KEY_LOGGED_OUT, true);
        ((Activity) getContext()).finish();
    }
}
