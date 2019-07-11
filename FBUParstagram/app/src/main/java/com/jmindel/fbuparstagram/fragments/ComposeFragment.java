package com.jmindel.fbuparstagram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jmindel.fbuparstagram.CameraManager;
import com.jmindel.fbuparstagram.R;
import com.jmindel.fbuparstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComposeFragment extends Fragment {
    private CameraManager cameraManager;
    String photoFileName = "photo.jpg";

    public static final String KEY_POST = "post";

    @BindView(R.id.ivImage)      ImageView ivImage;
    @BindView(R.id.etCaption)    EditText etCaption;
    @BindView(R.id.bFromLibrary) Button bFromLibrary;
    @BindView(R.id.bFromCamera)  Button bFromCamera;
    @BindView(R.id.bPost)        Button bPost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        cameraManager = new CameraManager(photoFileName, this);

        bFromLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                // openLibrary();
            }
        });
        bFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraManager.takePhoto();
            }
        });
        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String caption = etCaption.getText().toString();
                if (cameraManager.getPhotoFile() != null && !caption.equals("")) {
                    Post post = new Post();
                    post.setImage(new ParseFile(cameraManager.getPhotoFile()));
                    post.setCaption(caption);
                    post.setUser(ParseUser.getCurrentUser());
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e("ComposeFragment", "Failed to post");
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Failed to post", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("ComposeFragment", "Posted successfully!");
                                Toast.makeText(getContext(), "Posted successfully!", Toast.LENGTH_SHORT).show();
                                // TODO: Take user to their new post, and on close, go to TimelineFragment
                            }
                        }
                    });

                    // SHOULD: bundle.putSerializable(KEY_POST, post)
//                    getIntent().putExtra(KEY_POST, post);
//                    setResult(HomeActivity.MAKE_POST_REQUEST_CODE, getIntent());
//                    finish();
                } else {
                    Toast.makeText(getContext(), "Add an image and caption before posting.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CameraManager.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("MakePostActivity", "Successfully took photo");
                // by this point we have the camera photo on disk
                Bitmap scaledImage = cameraManager.fixOrientationAndSize();
                // Load the taken image into a preview
                ivImage.setImageBitmap(scaledImage);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("MakePostActivity", "Canceled open camera");
            }
        }
    }
}
