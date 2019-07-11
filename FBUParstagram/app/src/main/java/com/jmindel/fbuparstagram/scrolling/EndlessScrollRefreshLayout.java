package com.jmindel.fbuparstagram.scrolling;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jmindel.fbuparstagram.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class EndlessScrollRefreshLayout<Item, VH extends RecyclerView.ViewHolder> extends FrameLayout {

    protected RecyclerView.Adapter<VH> adapter;
    protected List<Item> items;
    protected EndlessRecyclerViewScrollListener scrollListener;

    @BindView(R.id.rvItems)         protected RecyclerView rvItems;
    @BindView(R.id.swipeContainer)  protected SwipeRefreshLayout swipeContainer;

    public EndlessScrollRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public EndlessScrollRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessScrollRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_endless_scroll_refresh, this);
        ButterKnife.bind(this, this);

        // Configure RecyclerView
        items = new ArrayList<>();
        adapter = makeAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.addItemDecoration(new EdgeDecorator(getEdgePadding(), shouldPadTopEdge(), shouldPadBottomEdge()));

        // Configure infinite scrolling
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                loadMore();
            }
        };
        // Add the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);

        // Add swipe to refresh actions
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                adapter.notifyDataSetChanged();
                load();
            }
        });
        swipeContainer.setColorSchemeResources(getColorScheme());
    }

    public abstract void load();
    public abstract void loadMore();

    public abstract RecyclerView.Adapter<VH> makeAdapter();

    public abstract int[] getColorScheme();

    public abstract int getEdgePadding();
    public abstract boolean shouldPadTopEdge();
    public abstract boolean shouldPadBottomEdge();
}
