package rmomoko.cse110bank;
import android.content.Intent;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Application;

//import parse library
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;

import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;

public class LoginActivity extends Activity{

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private User curUser;
    private Account userAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        curUser = new User();
        userAccount = new Account();

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mUsernameLogInButton = (Button) findViewById(R.id.login_button);
        mUsernameLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.create_account_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pageChange(view);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView  = findViewById(R.id.login_progress);
    }



    public void pageChangetoAcInfo(){
        Intent getAccountInfoScreen = new Intent(this, EmployeeChooseCus.class);
        final int result = 1;
        startActivityForResult(getAccountInfoScreen, result);
        finish();
    }

    public void pageChangetoCusAcInfo(){
        Intent getAccountInfoScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getAccountInfoScreen, result);
        finish();
    }


    public void pageChange(View view) {
        Intent getRegisterScreen = new Intent(this, RegisterActivity.class);
        final int result = 1;
        startActivityForResult(getRegisterScreen, result);
        finish();
    }



    public void attemptLogin() {
        User shuaige = new User();
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        }
        else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            User.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if(parseUser != null)
                    {
                        curUser = (User)User.getCurrentUser();
                        loginCheck();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Fail Login!", Toast.LENGTH_LONG).show();
                        showProgress(false);
                        View focus = mUsernameView;
                        mUsernameView.setError("The username or password is incorrect");
                        mUsernameView.setText("");
                        mPasswordView.setText("");
                        focus.requestFocus();
                    }
                }
            });
        }
    }

    public void loginCheck()
    {
        if(curUser.isCustomer())
        {
            userAccount = curUser.getAccount();
            userAccount.fetchInBackground(new GetCallback<Account>() {
                @Override
                public void done(Account temp, ParseException e) {
                    if(e == null)
                    {
                        accountCheck();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Fail Login! No Account", Toast.LENGTH_LONG).show();
                        showProgress(false);
                        View focus = mUsernameView;
                        mUsernameView.setText("");
                        mPasswordView.setText("");
                        focus.requestFocus();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Successful Login!", Toast.LENGTH_SHORT).show();
            pageChangetoAcInfo();
        }

    }

    public void accountCheck()
    {
        if(!userAccount.isClosed())
        {
            Toast.makeText(LoginActivity.this, "Successful Login!", Toast.LENGTH_SHORT).show();
            pageChangetoCusAcInfo();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Fail Login! Account Closed", Toast.LENGTH_SHORT).show();
            showProgress(false);
            View focus = mUsernameView;
            mUsernameView.setText("");
            mPasswordView.setText("");
            focus.requestFocus();
        }
    }



    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
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