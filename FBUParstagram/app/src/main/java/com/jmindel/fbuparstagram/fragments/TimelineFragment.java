package com.jmindel.fbuparstagram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.scrolling.PostLayout;

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
}
