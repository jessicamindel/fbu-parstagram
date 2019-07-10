package com.jmindel.fbuparstagram;

import android.app.Application;

import com.jmindel.fbuparstagram.model.Comment;
import com.jmindel.fbuparstagram.model.Like;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Like.class);
        ParseObject.registerSubclass(Comment.class);

        final Parse.Configuration config = new Parse.Configuration.Builder(this)
                .applicationId("jmindel-fbu-parstagram")
                .clientKey("FqLsHk\"(:SaRh65?C5PLC>k\"egc=bnQbS@'6&;Fi")
                .server("https://jmindel-fbu-parstagram.herokuapp.com/parse")
                .build();

        Parse.initialize(config);
    }
}
