package com.jmindel.fbuparstagram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.Utils;
import com.jmindel.fbuparstagram.activities.ProfileActivity;
import com.jmindel.fbuparstagram.model.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Activity activity;
    private List<Comment> comments;

    public CommentAdapter(Activity activity, List<Comment> comments) {
        this.activity = activity;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Comment comment = comments.get(i);
        viewHolder.tvUsername.setText(comment.getUser().getUsername());
        viewHolder.tvBody.setText(comment.getBody());

        viewHolder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_USER_ID, comment.getUser().getObjectId());
                activity.startActivity(intent);
            }
        });

        Utils.loadProfileImage(activity, viewHolder.ivProfile, comment.getUser());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUsername)  TextView tvUsername;
        @BindView(R.id.tvBody)      TextView tvBody;
        @BindView(R.id.ivProfile)   ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
