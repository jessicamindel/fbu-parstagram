package com.jmindel.fbuparstagram.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.activities.LoginActivity;
import com.jmindel.fbuparstagram.adapters.PostAdapter;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineFragment extends Fragment {

    PostAdapter adapter;
    List<Post> posts;

    @BindView(R.id.rvPosts) RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        posts = new ArrayList<>();
        adapter = new PostAdapter((Activity) getContext(), posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.orderByDescending(Post.KEY_CREATED_AT);
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("Home -> Timeline", "Post[" + i + "] = "
                                + objects.get(i).getCaption()
                                + " : username = " + objects.get(i).getUser().getUsername()
                        );
                    }
                    // TODO: Update, not just override
                    posts.clear();
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // File file = new File(path); --> ParseFile parseFile = new ParseFile(file);
    private void createPost(String caption, ParseFile image, ParseUser user) {
        final Post post = new Post();
        post.setCaption(caption);
        post.setImage(image);
        post.setUser(user);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("HomeActivity", "Posting failed");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Posting failed", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("HomeActivity", "Create post success!");
                    // TODO: Add to adapter
                }
            }
        });
    }

    public void logOut(View view) {
        ((Activity) getContext()).getIntent().putExtra(LoginActivity.KEY_LOGGED_OUT, true);
        ((Activity) getContext()).finish();
    }
}
