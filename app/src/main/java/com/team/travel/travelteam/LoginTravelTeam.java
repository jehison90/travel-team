package com.team.travel.travelteam;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.travel.travelteam.data.api_interfaces.ApiClientMethods;
import com.team.travel.travelteam.data.entities.User;

import org.apache.commons.lang3.StringUtils;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via userName/password.
 */
public class LoginTravelTeam extends Activity {

    private static final String API_URL = "https://travel-team-api.herokuapp.com";

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_travel_team);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isNotBlank(mEmailView.getText()) && StringUtils.isNotBlank(mPasswordView.getText())){
                    attemptLogin();
                } else{
                    Toast.makeText(getApplicationContext(), "Please insert user and password", Toast.LENGTH_LONG).show();
                }
            }
        });

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.email_login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        showProgress(true);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API_URL).build();
        ApiClientMethods apiClientMethods = restAdapter.create(ApiClientMethods.class);
        apiClientMethods.findUser(mEmailView.getText().toString(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                String password = mPasswordView.getText().toString();
                if(user != null){
                    if(password.equals(user.getPassword())){
                        Toast.makeText(getApplicationContext(), "Login correcto", Toast.LENGTH_LONG).show();
                    } else{
                        Toast.makeText(getApplicationContext(), "User or Password are incorrect", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "User not found. Please register", Toast.LENGTH_LONG).show();
                }
                showProgress(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

