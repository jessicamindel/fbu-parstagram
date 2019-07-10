package com.jmindel.fbuparstagram;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.jmindel.fbuparstagram.adapters.PostAdapter;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostLayout extends FrameLayout {
    // LEARN: Does this class's functionality break MVC best practices?

    private Handler handler;
    private PostAdapter adapter;
    private List<Post> posts;
    private EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.rvPosts)         RecyclerView rvPosts;
    @BindView(R.id.swipeContainer)  SwipeRefreshLayout swipeContainer;

    public PostLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public PostLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PostLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_post, this);
        ButterKnife.bind(this, this);

        // Configure RecyclerView
        posts = new ArrayList<>();
        adapter = new PostAdapter((Activity) getContext(), posts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);

        // Configure infinite scrolling
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Post.Query query = handler.makeQuery();
                query.whereLessThan(Post.KEY_CREATED_AT, posts.get(posts.size() - 1).getCreatedAt());
                runQuery(query, false);
            }
        };
        // Add the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        // Add swipe to refresh actions
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                adapter.notifyDataSetChanged();
                runQuery();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Post getPost(int i) {
        return this.posts.get(i);
    }

    public void runQuery() {
        runQuery(true);
    }

    public void runQuery(boolean resetPosts) {
        runQuery(handler.makeQuery(), resetPosts);
    }

    private void runQuery(Post.Query query, final boolean resetPosts) {
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("PostLayout", "Post[" + i + "] = "
                                + objects.get(i).getCaption()
                                + " : username = " + objects.get(i).getUser().getUsername()
                        );
                    }
                    if (resetPosts) posts.clear();
                    posts.addAll(objects);
                    adapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public static class Handler {
        public Post.Query makeQuery() {
            Post.Query postsQuery = new Post.Query();
            postsQuery.getTop(10).withUser();
            postsQuery.orderByDescending(Post.KEY_CREATED_AT);
            return postsQuery;
        }
    }
}
