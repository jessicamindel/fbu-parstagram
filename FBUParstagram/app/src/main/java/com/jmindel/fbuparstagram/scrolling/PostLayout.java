package com.jmindel.fbuparstagram.scrolling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.jmindel.fbuparstagram.adapters.PostAdapter;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class PostLayout extends EndlessScrollRefreshLayout<Post, PostAdapter.ViewHolder> {

    protected Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public PostLayout(@NonNull Context context) {
        super(context);
        setHandler(new Handler());
    }

    public PostLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setHandler(new Handler());
    }

    public PostLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setHandler(new Handler());
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
    public RecyclerView.Adapter<PostAdapter.ViewHolder> makeAdapter() {
        // FIXME: What if it's in a fragment?
        return new PostAdapter((Activity) getContext(), items);
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
        return 32;
    }

    @Override
    public boolean shouldPadTopEdge() {
        return true;
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
