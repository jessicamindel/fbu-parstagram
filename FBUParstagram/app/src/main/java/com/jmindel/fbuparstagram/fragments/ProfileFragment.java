package com.jmindel.fbuparstagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmindel.fbuparstagram.PostView;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    @BindView(R.id.pvPosts) PostView pvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        pvPosts.setHandler(new PostView.Handler() {
            @Override
            public Post.Query makeQuery() {
                Post.Query query = super.makeQuery();
                query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
                return query;
            }
        });
        pvPosts.runQuery();
    }
}
