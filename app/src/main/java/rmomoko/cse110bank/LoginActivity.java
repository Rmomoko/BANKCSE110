/**
 * Team Name: Orange Chicken
 *  File Name: LoginActivity.java
 *  Description: Normal login activity for the app. Create account is optional.
 */

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
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;

/**
 * Name:        LoginActivity
 * Purpose:     Let the user login into their account.
 * Description: Do the login check when the user try to login. If pass the check
 *              then will successful login. Otherwise will fail to login.
 */
public class LoginActivity extends Activity{

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private User curUser;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;


    @Override
    /**
     * Name:        onCreate
     * Purpose:     Create the layout for the login page.
     * Description: Create the UI for the login page.
     */
    protected void onCreate(Bundle savedInstanceState) {

        //create the layout for the login page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        curUser = new User();
        userCheckAccount = new CheckingAccount();
        userSaveAccount = new SavingAccount();

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        // Button activity for login
        Button mUsernameLogInButton = (Button) findViewById(R.id.login_button);
        mUsernameLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Botton activity for register
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


    /**
     * Name:        pageChangetoAcInfo
     * Purpose:     Change page when the employee login successful.
     * Description: Directly go the account infomation main page.
     */
    public void pageChangetoAcInfo(){
        Intent getAccountInfoScreen = new Intent(this, EmployeeChooseCus.class);
        final int result = 1;
        startActivityForResult(getAccountInfoScreen, result);
        finish();
    }

    /**
     * Name:        pageChangetoCusAcInfo
     * Purpose:     Change page when the customer login successful.
     * Description: Directly go the account information main page.
     */
    public void pageChangetoCusAcInfo(){
        Intent getAccountInfoScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getAccountInfoScreen, result);
        finish();
    }

    /**
     * Name:        pageChange
     * Purpose:     Change page when the user register successful.
     * Description: Register account successful.
     */
    public void pageChange(View view) {
        Intent getRegisterScreen = new Intent(this, RegisterActivity.class);
        final int result = 1;
        startActivityForResult(getRegisterScreen, result);
        finish();
    }


    /**
     * Name:        attemptLogin
     * Purpose:     Check if the login activity is valid.
     * Description: Check the user input a valid username and password or not.
     */
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
                    // If enter a vaild user name and password, do a
                    // login check
                    if(parseUser != null)
                    {
                        curUser = (User)User.getCurrentUser();
                        loginCheck();
                    }

                    // Otherwise output the error message for failed to login
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

    /**
     * Name:        loginCheck
     * Purpose:     Check if the user login successful or not.
     * Description: If the input match with the check process then login successful.
     *              Otherwise fail to login.
     */
    public void loginCheck()
    {
        // Valid username and password
        if(curUser.isCustomer())
        {

            userCheckAccount = curUser.getCheckingAccount();
            userSaveAccount = curUser.getSavingAccount();

            userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
                @Override

               // Check the checking account within the database
                public void done(CheckingAccount checkTemp, ParseException e) {

                    // Found match checking account
                    if(e == null)
                    {
                        userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
                            @Override

                            // Check the saving account within the database
                            public void done(SavingAccount saveTemp, ParseException e) {
                                // Found match saving account then pass the account check
                                if(e == null)
                                {
                                    accountCheck();
                                }

                                // No match data then fail the account check.
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

                    // No match checking account and fail to login
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

        // Pass login check and login successful
        else
        {
            Toast.makeText(LoginActivity.this, "Successful Login!", Toast.LENGTH_SHORT).show();
            pageChangetoAcInfo();
        }

    }

    /**
     * Name:            accountCheck
     * Purpose:         Check if the user account has been close or not.
     * Description:     Check if the user account has been close. If the account already
     *                  close then it cannot use to login anymore.
     */
    public void accountCheck()
    {
        // Check if one of the user account has been close
        // If one or more are still activity, user can successful login
        if(!userCheckAccount.isClosed() || !userSaveAccount.isClosed())
        {
            Toast.makeText(LoginActivity.this, "Successful Login!", Toast.LENGTH_SHORT).show();
            pageChangetoCusAcInfo();
        }

        // If the account has been close then cannot login anymore
        else
        {
            Toast.makeText(LoginActivity.this, "Fail Login! All Account Closed", Toast.LENGTH_SHORT).show();
            showProgress(false);
            View focus = mUsernameView;
            mUsernameView.setText("");
            mPasswordView.setText("");
            focus.requestFocus();
        }
    }


    // Password length check
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)

    /**
     * Name:        showProgress
     * Purpose:     Show the progress
     * Description: show the progress
     */
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