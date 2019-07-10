package com.jmindel.fbuparstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_POST = "post";
    public static final String KEY_USER = "user";
    public static final String KEY_BODY = "body";
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

    public String getBody() {
        return getString(KEY_BODY);
    }

    public void setBody(String body) {
        put(KEY_BODY, body);
    }

    public Date getCreatedAt() {
        return getDate(KEY_CREATED_AT);
    }

    public static class Query extends ParseQuery<Comment> {
        public Query() {
            super(Comment.class);
        }
    }
}
