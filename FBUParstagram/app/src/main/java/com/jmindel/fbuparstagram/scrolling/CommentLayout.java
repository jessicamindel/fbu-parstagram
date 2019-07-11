package com.jmindel.fbuparstagram.scrolling;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jmindel.fbuparstagram.adapters.CommentAdapter;
import com.jmindel.fbuparstagram.model.Comment;

public class CommentLayout extends EndlessScrollRefreshLayout<Comment, CommentAdapter.ViewHolder> {

    public CommentLayout(@NonNull Context context) {
        super(context);
    }

    public CommentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void load() {
        // TODO
    }

    @Override
    public void loadMore() {
        // TODO
    }

    @Override
    public RecyclerView.Adapter<CommentAdapter.ViewHolder> makeAdapter() {
        // FIXME: Might not be an activity?
        return new CommentAdapter((Activity) getContext(), items);
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
