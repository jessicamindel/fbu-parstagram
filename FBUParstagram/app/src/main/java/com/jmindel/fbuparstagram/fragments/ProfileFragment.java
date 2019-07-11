package com.jmindel.fbuparstagram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmindel.fbuparstagram.CameraManager;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.model.Post;
import com.jmindel.fbuparstagram.scrolling.PostGridLayout;
import com.jmindel.fbuparstagram.scrolling.PostLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    @BindView(R.id.plPosts)         PostGridLayout pvPosts;
    @BindView(R.id.tvUsername)      TextView tvUsername;
    @BindView(R.id.cvProfileImage)  CardView cvProfileImage;
    @BindView(R.id.ivProfile)       ImageView ivProfile;

    CameraManager cameraManager;
    ParseUser user;
    boolean canSetPhoto;

    public ProfileFragment() {
        user = ParseUser.getCurrentUser();
        canSetPhoto = true;
    }

    public void setUser(ParseUser user) {
        this.user = user;
        canSetPhoto = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tvUsername.setText(user.getUsername());

        if (canSetPhoto) {
            cameraManager = new CameraManager("profile.jpg", this);
            cvProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeProfileImage();
                }
            });
        }

        pvPosts.setHandler(new PostLayout.Handler() {
            @Override
            public Post.Query makeQuery() {
                Post.Query query = super.makeQuery();
                query.whereEqualTo(Post.KEY_USER, user);
                return query;
            }
        });
        pvPosts.load();
    }

    private void changeProfileImage() {
        cameraManager.takePhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == CameraManager.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            user.put("profileImage", new ParseFile(cameraManager.getPhotoFile()));
            Bitmap scaledImage = cameraManager.fixOrientationAndSize();
            ivProfile.setImageBitmap(scaledImage);
        }
    }
}
