package rmomoko.cse110bank;
import android.content.Intent;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import parse library
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;


public class RegisterActivity extends Activity{

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mRealNameView;
    private EditText mBirthdayView;
    private EditText mEmailView;
    private EditText mGenderView;
    private EditText mAddressView;
    private EditText mPhoneView;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // set application id and client key:
        Parse.initialize(this, "dJqoRn28p66wHQsJkJKog1zaaRhP3iTDGoSDanYU", "FwLys6BrpNfLyoOWuQCD9vVhIgqYsfjv9RynGOEY");

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.user_userName_edit);
        mPasswordView = (EditText) findViewById(R.id.user_password_edit);
        mRealNameView = (EditText) findViewById(R.id.user_realName_edit);
        mBirthdayView = (EditText) findViewById(R.id.user_birthday_edit);
        mEmailView = (EditText) findViewById(R.id.user_email_edit);
        mGenderView = (EditText) findViewById(R.id.user_gender_edit);
        mAddressView = (EditText) findViewById(R.id.user_address_edit);
        mPhoneView = (EditText) findViewById(R.id.user_phone_edit);

        Button mFinishButton = (Button) findViewById(R.id.finish_button);
        mFinishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView  = findViewById(R.id.register_progress);
    }

    public void attemptRegister() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mRealNameView.setError(null);
        mBirthdayView.setError(null);
        mEmailView.setError(null);
        mGenderView.setError(null);
        mAddressView.setError(null);
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String realName = mRealNameView.getText().toString();
        String birthday = mBirthdayView.getText().toString();
        String email = mEmailView.getText().toString();
        String gender = mGenderView.getText().toString();
        String address = mAddressView.getText().toString();
        String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone, if the user entered one.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_empty_required));
            focusView = mPhoneView;
            cancel = true;
        }

        // Check for a valid address, if the user entered one.
        if (TextUtils.isEmpty(address)) {
            mAddressView.setError(getString(R.string.error_empty_required));
            focusView = mAddressView;
            cancel = true;
        }

        // Check for a valid gender, if the user entered one.
        if (TextUtils.isEmpty(gender)) {
            mGenderView.setError(getString(R.string.error_empty_required));
            focusView = mGenderView;
            cancel = true;
        }


        // Check for a valid email, if the user entered one.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_empty_required));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid birthday, if the user entered one.
        if (TextUtils.isEmpty(birthday)) {
            mBirthdayView.setError(getString(R.string.error_empty_required));
            focusView = mBirthdayView;
            cancel = true;
        }

        // Check for a valid realName, if the user entered one.
        if (TextUtils.isEmpty(realName)) {
            mRealNameView.setError(getString(R.string.error_empty_required));
            focusView = mRealNameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_empty_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            //save data here
            // store information to parse.com  (edited by Joe)
            ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);

            // other fields can be set just like with ParseObject
            user.put("realname", realName);
            user.put("birthday",birthday);
            user.setEmail(email);
            user.put("gender",gender);
            user.put("address",address);
            user.put("phone",phone);
            user.put("isCustomer", true);

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        Toast.makeText(RegisterActivity.this, "Successful Signup!", Toast.LENGTH_SHORT).show();
                        ParseObject account = new ParseObject("Account");
                        account.put("savingAccount", 500);
                        account.put("checkingAccount", 0);

                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("Account", account);
                        user.saveInBackground();
                        // go back to log in page
                        pageChange();

                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    }
                }
            });

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void pageChange() {
        Intent getLoginActivity = new Intent(this, LoginActivity.class);
        final int result = 1;
        startActivityForResult(getLoginActivity, result);
        finish();
    }



















}
