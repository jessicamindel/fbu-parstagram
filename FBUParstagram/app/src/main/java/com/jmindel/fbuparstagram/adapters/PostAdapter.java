package com.jmindel.fbuparstagram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.activities.DetailActivity;
import com.jmindel.fbuparstagram.model.Like;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;
    private Activity activity;

    public PostAdapter(Activity activity, List<Post> posts) {
        this.posts = posts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Post post = posts.get(i);
        viewHolder.tvUsername.setText(post.getUser().getUsername());
        viewHolder.tvCaption.setText(post.getCaption());
        Glide.with(activity).load(post.getImage().getUrl()).into(viewHolder.ivImage);
        // TODO: Profile pictures

        viewHolder.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_POST_ID, post.getObjectId());
                activity.startActivity(intent);
            }
        });

        viewHolder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for existing like to determine how to toggle
                Like.Query hasLikeQuery = new Like.Query();
                hasLikeQuery.withPost().withUser();
                hasLikeQuery.whereEqualTo(Like.KEY_POST, post);
                hasLikeQuery.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
                hasLikeQuery.findInBackground(new FindCallback<Like>() {
                    @Override
                    public void done(List<Like> objects, ParseException e) {
                        if (e != null) {
                            Log.e("PostAdapter", "Failed to (un)like post");
                            e.printStackTrace();
                            Toast.makeText(activity, "Failed to (un)like post", Toast.LENGTH_LONG).show();
                        } else {
                            if (objects.size() > 0) { // Add like
                                objects.get(0).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        viewHolder.ivLike.setSelected(false);
                                    }
                                });
                            } else { // Remove like
                                Like like = new Like();
                                like.setPost(post);
                                like.setUser(ParseUser.getCurrentUser());
                                like.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        // FIXME:
                                        viewHolder.ivLike.setSelected(true);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
        viewHolder.ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvCard)      CardView cvCard;
        @BindView(R.id.ivProfile)   ImageView ivProfile;
        @BindView(R.id.tvUsername)  TextView tvUsername;
        @BindView(R.id.ivImage)     ImageView ivImage;
        @BindView(R.id.tvCaption)   TextView tvCaption;
        @BindView(R.id.ivLike)      ImageView ivLike;
        @BindView(R.id.ivComment)   ImageView ivComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
