package com.jmindel.fbuparstagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // ids start with 11
    public static final int MAKE_POST_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set logged out to false
        getIntent().putExtra(LoginActivity.KEY_LOGGED_OUT, false);

        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();

        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getCaption()
                                + " : username = " + objects.get(i).getUser().getUsername()
                        );
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    // File file = new File(path); --> ParseFile parseFile = new ParseFile(file);
    private void createPost(String caption, ParseFile image, ParseUser user) {
        final Post post = new Post();
        post.setCaption(caption);
        post.setImage(image);
        post.setUser(user);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "Create post success!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void logOut(View view) {
        getIntent().putExtra(LoginActivity.KEY_LOGGED_OUT, true);
        finish();
    }

    public void onMakePost(View view) {
        Intent i = new Intent(this, MakePostActivity.class);
        startActivityForResult(i, MAKE_POST_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == MAKE_POST_REQUEST_CODE) {
            // TODO: Get result back from make post
        }
    }
}