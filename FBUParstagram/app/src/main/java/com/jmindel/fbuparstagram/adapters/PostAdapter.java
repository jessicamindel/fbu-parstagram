package com.jmindel.fbuparstagram.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.Utils;
import com.jmindel.fbuparstagram.activities.DetailActivity;
import com.jmindel.fbuparstagram.activities.ProfileActivity;
import com.jmindel.fbuparstagram.model.Comment;
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

    private boolean enablePostDetailClick;

    public PostAdapter(Activity activity, List<Post> posts) {
        this.posts = posts;
        this.activity = activity;
        this.enablePostDetailClick = true;
    }

    public void setEnablePostDetailClick(boolean enablePostDetailClick) {
        this.enablePostDetailClick = enablePostDetailClick;
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
        viewHolder.tvDate.setText(Utils.getRelativeTimeAgo(post.getCreatedAt()));
        Glide.with(activity).load(post.getImage().getUrl()).into(viewHolder.ivImage);
        Utils.loadProfileImage(activity, viewHolder.ivProfile, post.getUser());

        // Route card click to post details
        viewHolder.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enablePostDetailClick) {
                    Intent intent = new Intent(activity, DetailActivity.class);
                    intent.putExtra(DetailActivity.KEY_POST_ID, post.getObjectId());
                    activity.startActivity(intent);
                }
            }
        });

        // Route liking and commenting clicks
        viewHolder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for existing like to determine how to toggle
                Like.Query hasLikeQuery = new Like.Query();
                hasLikeQuery.withPost().withUser().fromCurrUser().forPost(post);
                hasLikeQuery.findInBackground(new FindCallback<Like>() {
                    @Override
                    public void done(final List<Like> objects, ParseException e) {
                        if (e != null) {
                            Log.e("PostAdapter", "Failed to (un)like post");
                            e.printStackTrace();
                            Toast.makeText(activity, "Failed to (un)like post", Toast.LENGTH_LONG).show();
                        } else {
                            if (objects.size() > 0) { // Remove like
                                objects.get(0).deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        updateLike(false, objects.size(), viewHolder, e);
                                    }
                                });
                            } else { // Add like
                                Like like = new Like();
                                like.setPost(post);
                                like.setUser(ParseUser.getCurrentUser());
                                like.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        updateLike(true, objects.size(), viewHolder, e);
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
                final View vComment = activity.getLayoutInflater().inflate(R.layout.dialog_comment, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setView(vComment)
                        .setTitle("Comment")
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                EditText etComment = vComment.findViewById(R.id.etComment);
                                String body = etComment.getText().toString();

                                if (!body.equals("")) {
                                    Comment comment = new Comment();
                                    comment.setPost(post);
                                    comment.setUser(ParseUser.getCurrentUser());
                                    comment.setBody(body);
                                    comment.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Toast.makeText(activity, "Posted comment!", Toast.LENGTH_SHORT).show();
                                            int prevNumComments = Integer.parseInt(viewHolder.tvComments.getText().toString());
                                            viewHolder.tvComments.setText(Integer.toString(prevNumComments + 1));
                                            dialog.dismiss();
                                        }
                                    });
                                } else {
                                    Toast.makeText(activity, "Write a message before sending!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        // Route profile image click to user profile
        viewHolder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_USER_ID, post.getUser().getObjectId());
                activity.startActivity(intent);
            }
        });

        // Set like and comment values and like active state
        post.findLikesInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> objects, ParseException e) {
                viewHolder.tvLikes.setText(Integer.toString(objects.size()));
            }
        });
        Like.Query likeQuery = new Like.Query().withPost().withUser().fromCurrUser().forPost(post);
        likeQuery.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> objects, ParseException e) {
                if (objects.size() > 0) {
                    toggleLikeIcon(true, viewHolder);
                } else {
                    toggleLikeIcon(false, viewHolder);
                }
            }
        });

        post.findCommentsInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                viewHolder.tvComments.setText(Integer.toString(objects.size()));
            }
        });
    }

    private void updateLike(boolean liked, int prevNumLikes, ViewHolder viewHolder, Throwable e) {
        if (e != null) {
            Log.e("PostAdapter", "Failed to " + ((liked) ? "" : "un") + "like post");
            e.printStackTrace();
            Toast.makeText(activity, "Failed to unlike post", Toast.LENGTH_LONG).show();
        } else {
            toggleLikeIcon(liked, viewHolder);
            viewHolder.tvLikes.setText(Integer.toString(prevNumLikes + ((liked) ? 1 : -1)));
        }
    }

    private void toggleLikeIcon(boolean active, ViewHolder viewHolder) {
        if (active) {
            viewHolder.ivLike.setImageResource(R.drawable.ufi_heart_active);
        } else {
            viewHolder.ivLike.setImageResource(R.drawable.ufi_heart);
        }
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
        @BindView(R.id.tvLikes)     TextView tvLikes;
        @BindView(R.id.tvComments)  TextView tvComments;
        @BindView(R.id.tvDate)      TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
