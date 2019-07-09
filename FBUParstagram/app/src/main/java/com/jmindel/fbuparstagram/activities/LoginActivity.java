package com.jmindel.fbuparstagram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jmindel.fbuparstagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etUsername)  EditText etUsername;
    @BindView(R.id.etPassword)  EditText etPassword;
    @BindView(R.id.etEmail)     EditText etEmail;
    @BindView(R.id.bLogin)      Button bLogin;
    @BindView(R.id.bSignUp)     Button bSignUp;

    // ids start with 1
    public static final int HOME_REQUEST_CODE = 1;
    public static final String KEY_LOGGED_OUT = "loggedOut";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Set up button clicks
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                if (!username.equals("") && !password.equals("")) {
                    login(username, password);
                }
            }
        });
        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String email = etEmail.getText().toString();

                if (!username.equals("") && !password.equals("") && !email.equals("")) {
                    signUp(username, password, email);
                }
            }
        });

        // Automatically log in cached user
        ParseUser currUser = ParseUser.getCurrentUser();
        if (currUser != null) {
            Log.d("LoginActivity", "Logged-in user persisted successfully.");
            launchHome();
        }
    }

    protected void login(final @NonNull String username, final @NonNull String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful!");
                    launchHome();
                } else {
                    Log.e("LoginActivity", "Login failed.");
                    e.printStackTrace();
                }
            }
        });
    }

    protected void signUp(final @NonNull String username, final @NonNull String password, final @NonNull String email) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Sign up succeeded!");
                    login(username, password);
                } else {
                    Log.d("LoginActivity", "Sign up failed.");
                }
            }
        });
    }

    protected void launchHome() {
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        startActivityForResult(i, HOME_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == HOME_REQUEST_CODE) {
            boolean loggedOut = data.getBooleanExtra(KEY_LOGGED_OUT, false);
            if (loggedOut) finish();
        }
    }
}
