package com.jmindel.fbuparstagram;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.jmindel.fbuparstagram.adapters.PostAdapter;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class PostView extends RecyclerView {
    // LEARN: Does this class's functionality break MVC best practices?

    private PostAdapter adapter;
    private List<Post> posts;
    private Handler handler;

    public PostView(@NonNull Context context) {
        super(context);
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        posts = new ArrayList<>();
        adapter = new PostAdapter((Activity) getContext(), posts);
        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
    }

    public PostView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        posts = new ArrayList<>();
        adapter = new PostAdapter((Activity) getContext(), posts);
        setAdapter(adapter);
        setLayoutManager(new LinearLayoutManager(context));
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Post getPost(int i) {
        return this.posts.get(i);
    }

    public void runQuery() {
        final Post.Query postsQuery = handler.makeQuery();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("PostView", "Post[" + i + "] = "
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

    public static class Handler {
        public Post.Query makeQuery() {
            Post.Query postsQuery = new Post.Query();
            postsQuery.getTop().withUser();
            postsQuery.orderByDescending(Post.KEY_CREATED_AT);
            return postsQuery;
        }
    }
}
