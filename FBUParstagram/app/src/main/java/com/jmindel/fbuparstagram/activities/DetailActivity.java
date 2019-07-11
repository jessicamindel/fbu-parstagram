package com.jmindel.fbuparstagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.adapters.PostAdapter;
import com.jmindel.fbuparstagram.model.Comment;
import com.jmindel.fbuparstagram.model.Post;
import com.jmindel.fbuparstagram.scrolling.CommentLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_POST_ID = "postId";

    List<Post> posts;

    PostAdapter postAdapter;

    @BindView(R.id.rvPost)      RecyclerView rvPost;
    @BindView(R.id.clComments)  CommentLayout clComments;
    @BindView(R.id.bComment)    Button bComment;
    @BindView(R.id.etComment)   EditText etComment;

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

        bComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etComment.getText().toString();
                if (!body.equals("")) {
                    Comment comment = new Comment();
                    comment.setBody(body);
                    comment.setPost(posts.get(0));
                    comment.setUser(ParseUser.getCurrentUser());
                    comment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            etComment.setText("");
                            clComments.load();
                            // TODO: Clean this up
                        }
                    });
                } else { /* TODO */ }
            }
        });

        String currPostId = getIntent().getStringExtra(KEY_POST_ID);
        Post.Query currPostQuery = new Post.Query().withUser();
        currPostQuery.whereEqualTo(Post.KEY_ID, currPostId);
        currPostQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                posts.add(objects.get(0));
                postAdapter.notifyItemInserted(0);
                clComments.setPost(posts.get(0));
                clComments.load();
            }
        });
    }
}
