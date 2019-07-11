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
import android.widget.Toast;

import com.jmindel.fbuparstagram.adapters.CommentAdapter;
import com.jmindel.fbuparstagram.model.Comment;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

public class CommentLayout extends EndlessScrollRefreshLayout<Comment, CommentAdapter.ViewHolder> {

    private Post post;

    public CommentLayout(@NonNull Context context) {
        super(context);
    }

    public CommentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public void load() {
        post.findCommentsInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e("CommentLayout", "Failed to load comments");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load comments", Toast.LENGTH_LONG).show();
                } else {
                    items.clear();
                    items.addAll(objects);
                    adapter.notifyDataSetChanged();
                    rvItems.scrollToPosition(0);
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public void loadMore() {
        Comment.Query query = new Comment.Query().withPost().withUser();
        query.whereEqualTo(Comment.KEY_POST, post);
        query.whereLessThan(Comment.KEY_CREATED_AT, items.get(items.size() - 1).getCreatedAt());
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e != null) {
                    Log.e("CommentLayout", "Failed to load comments");
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load comments", Toast.LENGTH_LONG).show();
                } else {
                    items.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public RecyclerView.Adapter<CommentAdapter.ViewHolder> makeAdapter() {
        // FIXME: Might not be an activity?
        return new CommentAdapter((Activity) getContext(), items);
    }

    @Override
    public LayoutManagerType getLayoutManagerType() {
        return LayoutManagerType.Linear;
    }

    @Override
    public LinearLayoutManager makeLinearLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public GridLayoutManager makeGridLayoutManager() {
        return null;
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
}
