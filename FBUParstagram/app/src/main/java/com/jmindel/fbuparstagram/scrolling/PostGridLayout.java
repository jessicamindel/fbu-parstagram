package com.jmindel.fbuparstagram.scrolling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import com.jmindel.fbuparstagram.adapters.PostGridAdapter;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class PostGridLayout extends EndlessScrollRefreshLayout<Post, PostGridAdapter.ViewHolder> {

    // TODO: Refactor to extract handler + query functionality from PostGridLayout and PostLayout into separate class

    protected PostLayout.Handler handler;

    public void setHandler(PostLayout.Handler handler) {
        this.handler = handler;
    }

    public PostGridLayout(@NonNull Context context) {
        super(context);
        setHandler(new PostLayout.Handler());
    }

    public PostGridLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setHandler(new PostLayout.Handler());
    }

    public PostGridLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHandler(new PostLayout.Handler());
    }

    @Override
    public void load() {
        runQuery(handler.makeQuery(), true);
    }

    @Override
    public void loadMore() {
        Post.Query query = handler.makeQuery();
        query.whereLessThan(Post.KEY_CREATED_AT, items.get(items.size() - 1).getCreatedAt());
        runQuery(query, false);
    }

    @Override
    public RecyclerView.Adapter<PostGridAdapter.ViewHolder> makeAdapter() {
        return new PostGridAdapter((Activity) getContext(), items);
    }

    @Override
    public LayoutManagerType getLayoutManagerType() {
        return LayoutManagerType.Grid;
    }

    @Override
    public LinearLayoutManager makeLinearLayoutManager() {
        return null;
    }

    @Override
    public GridLayoutManager makeGridLayoutManager() {
        rvItems.setHasFixedSize(true);
        return new GridLayoutManager(getContext(), 4);
    }

    @Override
    public StaggeredGridLayoutManager makeStaggeredGridLayoutManager() {
        return null;
    }

    @Override
    public int[] getColorScheme() {
        return new int[] {
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        };
    }

    @Override
    public int getEdgePadding() {
        return 0;
    }

    @Override
    public boolean shouldPadTopEdge() {
        return false;
    }

    @Override
    public boolean shouldPadBottomEdge() {
        return false;
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
                    if (resetPosts) items.clear();
                    items.addAll(objects);
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
