package com.jmindel.fbuparstagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.adapters.CommentAdapter;
import com.jmindel.fbuparstagram.adapters.PostAdapter;
import com.jmindel.fbuparstagram.model.Comment;
import com.jmindel.fbuparstagram.model.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_POST_ID = "postId";

    List<Post> posts;
    List<Comment> comments;

    PostAdapter postAdapter;
    CommentAdapter commentAdapter;

    @BindView(R.id.rvPost)      RecyclerView rvPost;
    @BindView(R.id.rvComments)  RecyclerView rvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(this, posts);
        LinearLayoutManager postLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvPost.setLayoutManager(postLayoutManager);
        rvPost.setAdapter(postAdapter);

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, comments);
        // TODO: Set up comment view

        // TODO: Create a way to post comments

        // TODO: Probably wrap the entire thing in a ScrollView
    }
}
