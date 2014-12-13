/**
 * Team Name: Orange Chicken
 *  File Name: CreditActivity.java
 *  Description: Create the UI of credit activity page.
 *                      Employee can enter the amount the customer wants to deposit.
 *                      Get the User information in Parse to update the balance and account history.
 */


package rmomoko.cse110bank;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.Integer;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;

/**
 * Name:         Credit Activity
 * Purpose:     Processing credit activity.
 * Description:  Create all the text field and buttons for UI.
 *                      Get the string of amount and convert it to double type.
 *                      Processing the credit activity into saving or checking account.
 */
public class CreditActivity extends Activity {
    /* edit text field to enter the amount the customer wants to credit */
    private EditText creditAmount;
    /* the number of the amount */
    private double amount;
    /* the email the customer provided */
    private String someoneEmail;
    /* User information in Parse */
    private User someone;

    /* Account information in Parse */
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    /**
        * Name:           OnCreate
        * Purpose:        Create the layout of Credit Activity page
        * Description:  Create the UI of Credit Activity page.
        *                      Ask the employee to enter an amount then do the transaction.
        *                      Button created to let customer choose deposit into checking or saving account.
        */
    public void onCreate(Bundle savedInstanceState) {
        /* Create the layout of CreditActivity page*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_credit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /* assign the ID in the xml file to get the amount */
        creditAmount = (EditText) findViewById(R.id.credit_amount);
        /* get the email the customer provided */
        someoneEmail = getIntent().getStringExtra("someoneEmail");

        /* create credit into checking button */
        Button checkingCredit = (Button) findViewById(R.id.checking_credit_button);
        checkingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if the employee enter an amount */
                if(!creditAmount.getText().toString().isEmpty())
                {
                        /* deposit */
                        depositeCheckingAccount();
                }
                /* the amount is empty */
                else{
                    /* show error message */
                    creditAmount.setError("You must input a number");
                    creditAmount.requestFocus();
                }
            }
        });

        /* create credit into saving account button */
        Button savingCredit = (Button) findViewById(R.id.saving_credit_button);
        savingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                /* If the employee enter an amount */
                if(!creditAmount.getText().toString().isEmpty())
                {
                        /* deposit into Saving */
                        depositeSavingAccount();
                }
                /* if the amount is empty */
                else{
                    /* show error message */
                    creditAmount.setError("You must input a number");
                    creditAmount.requestFocus();
                }
            }
        });
    }

    /**
        * Name:           depositeCheckingAccount
        * Purpose:        Do the transaction deposit into checking account.
        * Description:   Check if the email the customer provided exists.
        *                      Processing the transaction if the customer exists.
        *                      Get the user information to update the amount in Parse.
        *                      Update account history.
        */
    public void depositeCheckingAccount(){
        /* convert the string the employee enter into double value of amount */
        amount = Double.parseDouble(creditAmount.getText().toString());
        /* get the customer information in Parse */
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        /* find the customer using the email provided */
        query.whereEqualTo("email",someoneEmail);
        /* get the checking account information */
        query.include("CheckingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                /* customer exist */
                if(e == null && !parseUsers.isEmpty())
                {
                    /* get the customer's information */
                    someone = (User)parseUsers.get(0);
                    userCheckAccount = someone.getCheckingAccount();
                    /* customer's current balance */
                    double current = userCheckAccount.getBalance();


                    DecimalFormat f = new DecimalFormat("##.00");
                    /* debit amount into current balance */
                    userCheckAccount.put("balance", current + amount);
                    userCheckAccount.saveInBackground();

                    /* create time of deposit into account history */
                    Date currentTime = userCheckAccount.getUpdatedAt();

                    /* string using to show the account history */
                    String temp = userCheckAccount.getHistory();
                    temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(userCheckAccount.getBalance()) + " Credit " + f.format(amount) + "\n"+ temp;
                    userCheckAccount.put("history", temp);
                    userCheckAccount.saveInBackground();
                    /* complete deposit, show successful deposit message */
                    Toast.makeText(CreditActivity.this, "Successful deposite!", Toast.LENGTH_SHORT).show();
                    pageChange();
                }
                else
                {
                    Toast.makeText(CreditActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
        * Name:           depositeSavingingAccount
        * Purpose:        Do the transaction deposit into saving account.
        * Description:   Check if the email the customer provided exists.
        *                      Processing the transaction if the customer exists.
        *                      Get the user information to update the amount in Parse.
        *                      Update account history.
        */
    public void depositeSavingAccount(){
        /* convert the string the employee enter into double value of amount */
        amount = Double.parseDouble(creditAmount.getText().toString());
        /* get the customer information in Parse */
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        /* find the customer using the email provided */
        query.whereEqualTo("email",someoneEmail);
        /* get the checking account information */
        query.include("SavingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                /* customer exist */
                if(e == null && !parseUsers.isEmpty())
                {
                    /* get the customer's information */
                    someone = (User)parseUsers.get(0);
                    userSaveAccount = someone.getSavingAccount();
                    /* debit amount into current balance */
                    double current = userSaveAccount.getBalance();
                    DecimalFormat f = new DecimalFormat("##.00");
                    userSaveAccount.put("balance", current + amount);
                    userSaveAccount.saveInBackground();
                    /* create time of deposit into account history */
                    Date currentTime = userSaveAccount.getUpdatedAt();

                    /* string using to show the account history */
                    String temp = userSaveAccount.getHistory();
                    temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                            + f.format(userSaveAccount.getBalance()) + " Credit " + f.format(amount) + "\n" + temp;
                    userSaveAccount.put("history", temp);
                    userSaveAccount.saveInBackground();

                    /* complete deposit, show successful deposit message */
                    Toast.makeText(CreditActivity.this, "Successful deposite!", Toast.LENGTH_SHORT).show();
                    pageChange();
                }
                /* customer not found */
                else
                {
                    Toast.makeText(CreditActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
        * Name:          pageChange
        *   Purpose:       Go back to search for customer page
        *   Description:  Go back to search for customer page
        */
    public void pageChange() {

        Intent getScreen = new Intent(this, EmployeeModifiedCus.class);
        final int result = 1;
        getScreen.putExtra("someoneEmail",someoneEmail);
        startActivityForResult(getScreen, result);
        finish();
    }

    /**
        * Name:          onBackPresse
         * Purpose:      Page change to the search for customer page.
        * Description:  Page change to the search for customer page.
        */
    public void onBackPressed()
    {
        pageChange();
    }
    /**
        * Name:          onOptionsItemSelected
        *   Purpose:  Page change to the search for customer page.
        * Description:  Page change to the search for customer page.
         */
    public boolean onOptionsItemSelected(MenuItem item){
        pageChange();
        return true;
    }
}


