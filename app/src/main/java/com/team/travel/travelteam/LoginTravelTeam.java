package com.team.travel.travelteam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.team.travel.travelteam.data.adapter.RestAdapterHelper;
import com.team.travel.travelteam.data.entities.User;

import org.apache.commons.lang3.StringUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via userName/password.
 */
public class LoginTravelTeam extends Activity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    private ProgressDialog spinner;

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

        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      register();
                                                  }
                                              });

                mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.email_login_form);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(), 0);
        showProgress(true);
        RestAdapterHelper.getApiClientMethods().findUser(mEmailView.getText().toString(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                String password = mPasswordView.getText().toString();
                if (user != null) {
                    if (password.equals(user.getPassword())) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "User or Password are incorrect", Toast.LENGTH_LONG).show();
                    }
                } else {
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

    private void register(){
        final RegisterDialog d = new RegisterDialog(this);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
        Button registerButton = (Button) d.findViewById(R.id.button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                final String userName = ((EditText) d.findViewById(R.id.etRegisterUser)).getText().toString();
                final String password = ((EditText) d.findViewById(R.id.etRegisterPassword)).getText().toString();
                final String email = ((EditText) d.findViewById(R.id.etRegisterEmail)).getText().toString();
                registerUser(new User(userName, password, email));
            }
        });
    }

    private void registerUser(final User userToRegister){
        RestAdapterHelper.getApiClientMethods().findUser(userToRegister.getUser(), new CallBackRegisterUser(userToRegister));
    }

    private void showProgress(final boolean show) {

        if(spinner == null){
            spinner = new ProgressDialog(this, R.style.spinnerDialog);
            spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            spinner.setMessage("Loading ...");
            spinner.setCancelable(false);
        }

        if(show){
            spinner.show();
        } else{
            spinner.dismiss();
        }
    }

    private class CallBackRegisterUser implements Callback<User>{

        User userToRegister;

        public CallBackRegisterUser(User userToRegister) {
            this.userToRegister = userToRegister;
        }

        @Override
        public void success(User user, Response response) {
            if (user == null) {
                RestAdapterHelper.getApiClientMethods().addUser(userToRegister, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        showProgress(false);
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showProgress(false);
                        Toast.makeText(getApplicationContext(), "Error registering user " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "User already registered. Sing in or use another user name", Toast.LENGTH_LONG).show();
            }
            showProgress(false);
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getApplicationContext(), "Error registering user " + error.getMessage(), Toast.LENGTH_LONG).show();
            showProgress(false);
        }
    }

}

