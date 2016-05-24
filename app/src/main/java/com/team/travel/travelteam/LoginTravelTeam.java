package com.team.travel.travelteam;

import android.app.Activity;
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
import com.team.travel.travelteam.dialogs.ProgressDialogUtility;
import com.team.travel.travelteam.dialogs.RegisterDialog;

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

        ProgressDialogUtility.setContext(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(), 0);
        ProgressDialogUtility.showProgressDialog();
        RestAdapterHelper.getApiClientMethods().findUser(mEmailView.getText().toString(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                String password = mPasswordView.getText().toString();
                if (user != null) {
                    if (password.equals(user.getPassword())) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra(MapsActivity.LOGGED_USER_KEY, user);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "User or Password are incorrect", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User not found. Please register", Toast.LENGTH_LONG).show();
                }
                ProgressDialogUtility.dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                ProgressDialogUtility.dismissProgressDialog();
            }
        });
    }

    private void register(){
        final RegisterDialog registerDialog = new RegisterDialog(this);
        registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        registerDialog.show();
        Button registerButton = (Button) registerDialog.findViewById(R.id.button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialogUtility.showProgressDialog();
                final String userName = ((EditText) registerDialog.findViewById(R.id.etRegisterUser)).getText().toString();
                final String password = ((EditText) registerDialog.findViewById(R.id.etRegisterPassword)).getText().toString();
                final String email = ((EditText) registerDialog.findViewById(R.id.etRegisterEmail)).getText().toString();
                registerUser(new User(userName, password, email), registerDialog);
            }
        });
    }

    private void registerUser(final User userToRegister, final RegisterDialog dialog){
        RestAdapterHelper.getApiClientMethods().findUser(userToRegister.getUser(), new CallBackRegisterUser(userToRegister, dialog));
    }

    private class CallBackRegisterUser implements Callback<User>{

        User userToRegister;
        RegisterDialog parentView;

        public CallBackRegisterUser(User userToRegister, RegisterDialog parentView) {
            this.userToRegister = userToRegister;
            this.parentView = parentView;
        }

        @Override
        public void success(User user, Response response) {
            if (user == null) {
                RestAdapterHelper.getApiClientMethods().addUser(userToRegister, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        ProgressDialogUtility.dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                        parentView.dismiss();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogUtility.dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), "Error registering user " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "User already registered. Sing in or use another user name", Toast.LENGTH_LONG).show();
            }
            ProgressDialogUtility.dismissProgressDialog();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(getApplicationContext(), "Error registering user " + error.getMessage(), Toast.LENGTH_LONG).show();
            ProgressDialogUtility.dismissProgressDialog();
        }
    }

}

