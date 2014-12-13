/**
 * Team Name: Orange Chicken
 *  File Name: DebitActivity.java
 *  Description:  Create the UI of debit activity page.
 *                      Employee can enter the amount the customer wants to withdraw.
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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;


/**
 * Name:         Credit Account
 * Purpose:     Processing debit activity.
 * Description: Create all the text field and buttons for UI.
 *                      Get the string of amount and convert it to double type.
 *                      Processing the debit activity from saving or checking account.
 */
public class DebitActivity extends Activity{

    /* edit text field to enter the amount the customer wants to withdraw*/
    private EditText debitAmount;

    /* the number of the amount */
    private double amount;
    private double current;
    /* the email the customer provided */
    private String someoneEmail;

    /* User information in Parse */
    private User someone;
    /* Account information in Parse */
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    private DecimalFormat f = new DecimalFormat("##.00");
    private static final double LIMIT = 10000.0;

    /**
        * Name:           OnCreate
        * Purpose:        Create the layout of Debit Activity page
        * Description:  Create the UI of Debit Activity page.
        *                      Ask the employee to enter an amount then do the transaction.
        *                      Button created to let customer choose withdraw from checking or saving account.
        */
    public void onCreate(Bundle savedInstanceState) {
        /* Create the layout of DebitActivity page*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_debit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /* assign the ID in the xml file to get the amount */
        debitAmount = (EditText) findViewById(R.id.debit_amount);
        /* get the email the customer provided */
        someoneEmail = getIntent().getStringExtra("someoneEmail");

        /* create withdraw from checking button */
        Button checkingCredit = (Button) findViewById(R.id.checking_debit_button);
        checkingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* if the employee enter an amount */
                if(!debitAmount.getText().toString().isEmpty())
                {
                    /* withdraw */
                    withdrawalCheckingAccount();
                }
                /* the amount is empty */
                else{
                    /* show error message */
                    debitAmount.setError("You must input a number");
                    debitAmount.requestFocus();
                }

            }
        });

        /* create debit from saving account button */
        Button savingCredit = (Button) findViewById(R.id.saving_debit_button);
        savingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* If the employee enter an amount */
                if(!debitAmount.getText().toString().isEmpty())
                {
                    /* withdraw from Saving */
                    withdrawalSavingAccount();
                }
                /* if the amount is empty */
                else{
                    /* show error message */
                    debitAmount.setError("You must input a number");
                    debitAmount.requestFocus();
                }
            }
        });
    }

    /**
        * Name:           withdrawalCheckingAccount
        * Purpose:        Do the transaction withdraw from  checking account.
        * Description:   Check if the email the customer provided exists.
        *                      Processing the transaction if the customer exists.
        *                      Get the user information to update the amount in Parse.
        *                      Update account history.
        */
    public void withdrawalCheckingAccount(){
         /* convert the string the employee enter into double value of amount */
        amount = Double.parseDouble(debitAmount.getText().toString());
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
                    current = userCheckAccount.getBalance();

                    /* if the withdraw is more than the balance */
                    if(amount > current)
                    {
                        /* if the withdraw is more than the balance */
                        debitAmount.setError("Not enough money");
                    }
                    /* if the withdraw amount is more than limit */
                    else if(amount > LIMIT)
                    {
                        /* show warning */
                        Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                    }
                    /* process the withdraw */
                    else {
                        userCheckAccount.put("isClosed", false);
                        userCheckAccount.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                /* get the history from parse */
                                String[] history = userCheckAccount.getHistory().split("\n");
                                Date today = userCheckAccount.getUpdatedAt();
                                double temp = 0;
                                /* get current time */
                                String currentTime = "" + (today.getYear()+ 1900) + "/" + (today.getMonth()+1) + "/" + today.getDate();
                                /* check amount withdraw from account history */
                                for(int i = 0; i < history.length; i++) {
                                    String[] element = history[i].split(" ");
                                    if(!element[0].equals(currentTime)) break;
                                    if(element[2].equals("Debit")) temp += Double.parseDouble(element[3]);
                                }
                                /* if the amount withdraw + previous withdraw is over the limit */
                                if(temp  + amount> LIMIT)
                                {
                                    /* show error message */
                                    Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    /* processing withdraw */
                                    userCheckAccount.put("balance", current - amount);
                                    /* get the withdraw activity time */
                                    Date currentTimes = userCheckAccount.getUpdatedAt();
                                    /* update the account history */
                                    String temps = userCheckAccount.getHistory();
                                    temps = (currentTimes.getYear() + 1900) + "/" + (currentTimes.getMonth() + 1) + "/" + currentTimes.getDate() + " "
                                            + f.format(current - amount) + " Debit " + f.format(amount) + "\n" + temps;
                                    userCheckAccount.put("history", temps);
                                    userCheckAccount.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            /* show successfully withdraw message */
                                            Toast.makeText(DebitActivity.this, "Successful withdrawal!", Toast.LENGTH_SHORT).show();
                                            pageChange();
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(DebitActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
        * Name:           withdrawalSavingAccount
        * Purpose:        Do the transaction withdraw from  saving account.
        * Description:   Check if the email the customer provided exists.
        *                      Processing the transaction if the customer exists.
        *                      Get the user information to update the amount in Parse.
        *                      Update account history.
        */
    public void withdrawalSavingAccount(){
        /* convert the string the employee enter into double value of amount */
        amount = Double.parseDouble(debitAmount.getText().toString());

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",someoneEmail);
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
                    current = userSaveAccount.getBalance();
                    /* if the withdraw is more than the balance */
                    if(amount > current)
                    {
                        /* if the withdraw is more than the balance */
                        debitAmount.setError("Not enough money");
                    }
                    /* if the withdraw amount is more than limit */
                    else if(amount > LIMIT)
                    {
                        Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                    }
                    /* process the withdraw */
                    else {
                        userSaveAccount.put("isClosed", false);
                        userSaveAccount.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                /* get the history from parse */
                                String[] history = userSaveAccount.getHistory().split("\n");
                                Date today = userSaveAccount.getUpdatedAt();
                                double temp = 0;
                                /* get current time */
                                String currentTime = "" + (today.getYear()+ 1900) + "/" + (today.getMonth()+1) + "/" + today.getDate();
                                /* check amount withdraw from account history */
                                for(int i = 0; i < history.length; i++) {
                                    String[] element = history[i].split(" ");
                                    if(!element[0].equals(currentTime)) break;
                                    if(element[2].equals("Debit")) temp += Double.parseDouble(element[3]);
                                }
                                /* if the amount withdraw + previous withdraw is over the limit */
                                if(temp  + amount> LIMIT)
                                {
                                    Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    /* processing withdraw */
                                    userSaveAccount.put("balance", current - amount);
                                    /* get the withdraw activity time */
                                    Date currentTimes = userSaveAccount.getUpdatedAt();

                                    /* update the account history */
                                    String temps = userSaveAccount.getHistory();
                                    temps = (currentTimes.getYear() + 1900) + "/" + (currentTimes.getMonth() + 1) + "/" + currentTimes.getDate() + " "
                                            + f.format(current - amount) + " Debit " + f.format(amount) + "\n" + temps;
                                    userSaveAccount.put("history", temps);
                                    userSaveAccount.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Toast.makeText(DebitActivity.this, "Successful withdrawal!", Toast.LENGTH_SHORT).show();
                                            pageChange();
                                        }
                                    });
                                }
                            }
                        });



                    }
                }
                else
                {
                    Toast.makeText(DebitActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
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
     * Name:          onBackPressed
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
