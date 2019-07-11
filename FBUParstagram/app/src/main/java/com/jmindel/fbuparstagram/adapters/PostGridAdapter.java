package com.jmindel.fbuparstagram.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.activities.DetailActivity;
import com.jmindel.fbuparstagram.model.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostGridAdapter extends RecyclerView.Adapter<PostGridAdapter.ViewHolder> {
    private List<Post> posts;
    private Activity activity;

    public PostGridAdapter(Activity activity, List<Post> posts) {
        this.posts = posts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_post_grid, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Post post = posts.get(i);
        Glide.with(activity).load(post.getImage().getUrl()).into(viewHolder.ivImage);

        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(DetailActivity.KEY_POST_ID, post.getObjectId());
                activity.startActivity(intent);
            }
        });

        // FIXME: This doesn't work well... how to make width = height?
//        ViewGroup.LayoutParams params = viewHolder.ivImage.getLayoutParams();
//        params.height = viewHolder.ivImage.getWidth();
//        viewHolder.ivImage.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage) ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
