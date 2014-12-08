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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.parse.ParseQuery;


import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;

/**
 * Created by Yuxiao on 11/9/2014.
 */
public class CustomerAccountActivity extends Activity{

    private TextView checkingNumber;
    private TextView savingNumber;
    private User user;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        checkingNumber = (TextView) findViewById(R.id.cus_check_number);
        savingNumber = (TextView) findViewById(R.id.cus_save_number);

        user = (User)ParseUser.getCurrentUser();

        userCheckAccount = user.getCheckingAccount();
        userSaveAccount = user.getSavingAccount();

        userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
            public void done(CheckingAccount object, ParseException e) {
                if (e == null) {
                    userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
                        public void done(SavingAccount object, ParseException e) {
                            if (e == null) {
                                if(!userCheckAccount.isClosed())
                                    checkingNumber.setText("$ " + userCheckAccount.getBalance());
                                else
                                    checkingNumber.setText("Account Closed");
                                if(!userSaveAccount.isClosed())
                                    savingNumber.setText("$ " + userSaveAccount.getBalance());
                                else
                                    savingNumber.setText("Account Closed");
                            }
                            else
                            {}
                        }
                    });
                } else
                {}
            }
        });

        Button printSumButton = (Button) findViewById(R.id.cus_print_summary_button);
        printSumButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToSummary(view);
            }
        });

        Button transferButton = (Button) findViewById(R.id.cus_transfer_button);
        transferButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToTransfer(view);
            }
        });

        Button closeCheckButton = (Button) findViewById(R.id.cus_close_check_button);
        closeCheckButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                user = (User)ParseUser.getCurrentUser();
                userCheckAccount = user.getCheckingAccount();
                userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
                    public void done(CheckingAccount object, ParseException e) {
                        if (e == null) {
                            if(object.isClosed() == false) {
                                object.closeAccount();
                                checkingNumber.setText("Account Closed");
                                Toast.makeText(CustomerAccountActivity.this, "Your account is closed now!", Toast.LENGTH_SHORT).show();
                                check();
                            }
                            else
                            {
                                Toast.makeText(CustomerAccountActivity.this, "Your account is already closed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });

            }
        });

        Button closeSaveButton = (Button) findViewById(R.id.cus_close_saving_button);
        closeSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                user = (User)ParseUser.getCurrentUser();
                userSaveAccount = user.getSavingAccount();
                userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
                    public void done(SavingAccount object, ParseException e) {
                        if (e == null) {
                            if(object.isClosed() == false) {
                                object.closeAccount();
                                savingNumber.setText("Account Closed");
                                Toast.makeText(CustomerAccountActivity.this, "Your account is closed now!", Toast.LENGTH_SHORT).show();
                                check();
                            }
                            else
                            {
                                Toast.makeText(CustomerAccountActivity.this, "Your account is already closed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });

            }
        });
    }

    public void check()
    {
        if(userCheckAccount.isClosed() && userSaveAccount.isClosed())
        {
            user.logOut();
            pageToLogin();
        }
    }

    public void pageToSummary(View view) {
        Intent getScreen = new Intent(this, CusSummaryActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void pageToTransfer(View view) {
        Intent getScreen = new Intent(this, TransferChoiceActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void pageToLogin() {
        Intent getScreen = new Intent(this, LoginActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    public void onBackPressed()
    {
        ParseUser.logOut();
        pageToLogin();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        ParseUser.logOut();
        pageToLogin();
        return true;
    }
}
