/**
 * Team Name: Orange Chicken
 *  File Name: CustomerAccountActivity.java
 *  Description: Display Customer's account information, and the customer can click on each button to continue the activity.
 *
 */

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


import java.text.DecimalFormat;

import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;

/**
 * Name:            CustomerAccountActivity
 * Purpose:        Display customer's account information when customers login.
 * Description:  Get the customer's checking and saving balance from database.
 *                      Create customer's account activity button, and perform the activity.
 *                      Check the account
 * Created by Yuxiao on 11/9/2014.
 */
public class CustomerAccountActivity extends Activity{

    /* TextView to show the balance of account amount */
    private TextView checkingNumber;
    private TextView savingNumber;

    /* Data type from Parse */
    private User user;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    /* Decimal to display amount */
    private DecimalFormat f = new DecimalFormat("##.00");

    /**
     * Name:           OnCreate
     * Purpose:        Create the layout of Customer Account Information page
     * Description:  Create the UI of CustomerAccountActivity page.
     *                      Get data from database to display the amount of accounts.
     *                       Create all the buttons to do all the customer account activities.
     */
    public void onCreate(Bundle savedInstanceState) {
        /* Create the layout of CustomerAccountActivity page*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        checkingNumber = (TextView) findViewById(R.id.cus_check_number);
        savingNumber = (TextView) findViewById(R.id.cus_save_number);

        /* get the User information of the customer */
        user = (User)ParseUser.getCurrentUser();

        /* get the checking account / saving account  information of the customer */
        userCheckAccount = user.getCheckingAccount();
        userSaveAccount = user.getSavingAccount();

        userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
            public void done(CheckingAccount object, ParseException e) {
                if (e == null) {
                    userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
                        public void done(SavingAccount object, ParseException e) {
                            if (e == null) {
                                /* Check if account is not closed */
                                if(!userCheckAccount.isClosed())
                                    /* display the balance */
                                    checkingNumber.setText("$ " + f.format(userCheckAccount.getBalance()));
                                else
                                    /* account closed */
                                    checkingNumber.setText("Account Closed");
                                if(!userSaveAccount.isClosed())
                                    savingNumber.setText("$ " + f.format(userSaveAccount.getBalance()));
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

        /* create print summary button */
        Button printSumButton = (Button) findViewById(R.id.cus_print_summary_button);
        printSumButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Change page to summary */
                pageToSummary(view);
            }
        });

        /* create transfer money button */
        Button transferButton = (Button) findViewById(R.id.cus_transfer_button);
        transferButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Change page to transfer money */
                pageToTransfer(view);
            }
        });

        /* create close checking account button */
        Button closeCheckButton = (Button) findViewById(R.id.cus_close_check_button);
        closeCheckButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* get user information */
                user = (User)ParseUser.getCurrentUser();
                /* get account information */
                userCheckAccount = user.getCheckingAccount();
                userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
                    public void done(CheckingAccount object, ParseException e) {
                        if (e == null) {
                            /* if the account has not been closed */
                            if(object.isClosed() == false) {
                                /* perform close account */
                                object.closeAccount();
                                /* show confirm close message */
                                checkingNumber.setText("Account Closed");
                                /* temporarily message */
                                Toast.makeText(CustomerAccountActivity.this, "Your account is closed now!", Toast.LENGTH_SHORT).show();
                                check();
                            }
                            else
                            {
                                /* confirm closed already, no action needed */
                                Toast.makeText(CustomerAccountActivity.this, "Your account is already closed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });

            }
        });

        /* almost the same as close checking button  above*/
        Button closeSaveButton = (Button) findViewById(R.id.cus_close_saving_button);
        closeSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                 /* get user information */
                user = (User)ParseUser.getCurrentUser();
                /* get account information */
                userSaveAccount = user.getSavingAccount();
                userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
                    public void done(SavingAccount object, ParseException e) {
                        if (e == null) {
                             /* if the account has not been closed */
                            if(object.isClosed() == false) {
                                object.closeAccount();
                                savingNumber.setText("Account Closed");
                                Toast.makeText(CustomerAccountActivity.this, "Your account is closed now!", Toast.LENGTH_SHORT).show();
                                check();
                            }
                            else
                            {
                                /* confirm closed already, no action needed */
                                Toast.makeText(CustomerAccountActivity.this, "Your account is already closed!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                        }
                    }
                });

            }
        });
    }

    /**
     * Name:           check
     * Purpose:        Check if the user close both checking and saving account goto login page.
     * Description:  Check both account is closed or not.
     *                       Force the customer logout and goto login page.
     */
    public void check()
    {
        if(userCheckAccount.isClosed() && userSaveAccount.isClosed())
        {
            user.logOut();
            pageToLogin();
        }
    }
    /**
     * Name:           pageToSummary
     * Purpose:        Change page when print summary button clicked.
     * Description:  Go to summary page.
     */
    public void pageToSummary(View view) {
        Intent getScreen = new Intent(this, CusSummaryActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    /**
     * Name:           pageToTransfer
     * Purpose:        Change page when make transfer button clicked.
     * Description:  Go to Make transfer page.
     */
    public void pageToTransfer(View view) {
        Intent getScreen = new Intent(this, TransferChoiceActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
     * Name:          pageToLogin
     * Purpose:       Go back to login page.
     * Description:  Go to login page.
     */
    public void pageToLogin() {
        Intent getScreen = new Intent(this, LoginActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    /**
     * Name:          onBackPresse
     * Purpose:       Enforce the customer to logout and go to login page.
     * Description:  Enforce the customer to logout and go to login page.
     */
    public void onBackPressed()
    {
        ParseUser.logOut();
        pageToLogin();
    }
    /**
     * Name:          onOptionsItemSelected
     *   Purpose:     Enforce the customer to logout and go to login page.
     * Description:  Enforce the customer to logout and go to login page.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        ParseUser.logOut();
        pageToLogin();
        return true;
    }
}
