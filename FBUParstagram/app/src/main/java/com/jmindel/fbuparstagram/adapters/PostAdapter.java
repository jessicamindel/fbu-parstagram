package com.jmindel.fbuparstagram.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.model.Post;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Post post = posts.get(i);
        viewHolder.tvUsername.setText(post.getUser().getUsername());
        viewHolder.tvCaption.setText(post.getCaption());
        Glide.with(activity).load(post.getImage().getUrl()).into(viewHolder.ivImage);
        // TODO: Profile pictures
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfile)   ImageView ivProfile;
        @BindView(R.id.tvUsername)  TextView tvUsername;
        @BindView(R.id.ivImage)     ImageView ivImage;
        @BindView(R.id.tvCaption)   TextView tvCaption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
