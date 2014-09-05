package org.inbar.come;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.inbar.come.model.UserFollowInfo;
import org.inbar.come.util.Logging;


public class LoginActivity extends Activity {

    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public void doLogin(View view) {

        EditText emailView = (EditText) findViewById(R.id.email_field);
        EditText passwordView = (EditText) findViewById(R.id.password_field);

        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        if (email == null || email == "") {
            Logging.justLog(TAG, "password is empty", this);

        }

        if (password == null || password == "") {
            Logging.justLog(TAG, "password is empty", this);
        }

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if (parseUser != null) {
                    Logging.alert("LOGIN success", getApplicationContext());
                    returnToMainActivity();
                } else {
                    doSignup(email, password);
                }
            }
        });

    }

    private void doSignup(String email, String password) {

        final ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {

                if (e == null) {
                    Logging.alert("SIGNUP success", getApplicationContext());
                    returnToMainActivity();
                } else {
                    Log.e(TAG, "login failed", e);
                }
            }
        });
    }

    private void returnToMainActivity() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
