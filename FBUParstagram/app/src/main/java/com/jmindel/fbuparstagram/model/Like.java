package com.jmindel.fbuparstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_POST = "post";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    public Post getPost() {
        return (Post) getParseObject(KEY_POST);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public Date getCreatedAt() {
        return getDate(KEY_CREATED_AT);
    }

    public static class Query extends ParseQuery<Like> {
        public Query() {
            super(Like.class);
        }

        public Query withUser() {
            include("user");
            return this;
        }

        public Query withPost() {
            include("post");
            return this;
        }

        public Query fromCurrUser() {
            whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
            return this;
        }

        public Query forPost(Post post) {
            whereEqualTo(Like.KEY_POST, post);
            return this;
        }
    }
}
