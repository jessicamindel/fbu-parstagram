package com.jmindel.fbuparstagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jmindel.fbuparstagram.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakePostActivity extends AppCompatActivity {
    @BindView(R.id.ivImage)      ImageView ivImage;
    @BindView(R.id.etCaption)    EditText etCaption;
    @BindView(R.id.bFromLibrary) Button bFromLibrary;
    @BindView(R.id.bFromCamera)  Button bFromCamera;
    @BindView(R.id.bPost)        Button bPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);
        ButterKnife.bind(this);
    }
}
