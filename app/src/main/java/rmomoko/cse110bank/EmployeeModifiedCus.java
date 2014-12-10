/**
 * Team Name: Orange Chicken
 *  File Name: EmployeeModifiedCus.java
 *  Description: Display Customer's account information when teller login.
 *
 */


package rmomoko.cse110bank;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
 * Name:            EmployeeModifiedCus
 * Purpose:        Display customer's account information when teller login and search for customer.
 * Description:  Get the customer's checking and saving balance from database.
 *                      Create tell's activity button to process transactions, and perform the activity.
 *
 */
public class EmployeeModifiedCus extends Activity{
    /* 30 days period to count interest and penalty*/
    private static final long THIRTYDAY = 2592000000L;

    /* TextView to display customer's account balance */
    private TextView checkingNumber;
    private TextView savingNumber;

    /* String to stores the customer's email */
    private String someoneEmail;

    /* Data type from Parse */
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    /* Decimal to display amount */
    private DecimalFormat f = new DecimalFormat("##.00");

    /**
        * Name:           OnCreate
        * Purpose:        Create the layout of Customer Account Information page
        * Description:  Create the UI of CustomerAccountActivity page.
        *                      Get data from database to display the amount of accounts.
        *                      Create all the buttons to do all the customer account activities from teller.
        */
    public void onCreate(Bundle savedInstanceState){
        /* Create the layout of Tell's action page*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_change_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        checkingNumber = (TextView) findViewById(R.id.checkingNumber);
        savingNumber = (TextView) findViewById(R.id.savingNumber);
        someoneEmail = getIntent().getStringExtra("someoneEmail");

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        /* Get user information by finding the email */
        query.whereEqualTo("email",someoneEmail);
        /* have the user's account information */
        query.include("CheckingAccount");
        query.include("SavingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                /* If the customer exist */
                if(e == null && !parseUsers.isEmpty())
                {
                    /* get user information */
                    someone = (User)parseUsers.get(0);
                    /* get account information */
                    userCheckAccount = someone.getCheckingAccount();
                    userSaveAccount = someone.getSavingAccount();
                    /* check if account closed */
                    if(!userCheckAccount.isClosed())
                        /* show checking account balance */
                        checkingNumber.setText("$ " + f.format(userCheckAccount.getBalance()));
                    else
                        checkingNumber.setText("Account Closed");
                    if(!userSaveAccount.isClosed())
                        savingNumber.setText("$ " + f.format(userSaveAccount.getBalance()));
                    else
                        savingNumber.setText("Account Closed");
                }
                else
                {
                    Toast.makeText(EmployeeModifiedCus.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* create credit button */
        Button creditButton = (Button) findViewById(R.id.credit_button);
        creditButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(someone != null) {
                    /* Change page to credit */
                    pageToCredit(view);
                }
            }
        });

        /* create debit button */
        Button debitButton = (Button) findViewById(R.id.debit_button);
        debitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(someone != null) {
                    /* Change page to dedit */
                    pageToDebit(view);
                }
            }
        });

        /* create checking account  penalty button */
        Button checkPenaltyButton = (Button) findViewById(R.id.check_penalty_button);
        checkPenaltyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* user's accoun not closed */
                if(!userCheckAccount.isClosed()) {
                    userCheckAccount.put("isClosed", false);
                    userCheckAccount.saveInBackground();

                    /* check the penalty every 30 days */
                    if(userCheckAccount.getUpdatedAt().getTime() - userCheckAccount.getDate("lastTimePenalty").getTime() > THIRTYDAY)
                    {
                        double ave;
                        /* if the user does not have account activity */
                        if(userCheckAccount.getHistory().isEmpty()) {
                            /* get the balance */
                            ave = userCheckAccount.getBalance();
                        }
                        else
                        {
                            /* get the last time balance */
                            ave = userCheckAccount.average();
                        }
                        /* check account balance less than 100 */
                        if(ave < 100)
                        {
                            /* apply penalty on current balance */
                            double current = userCheckAccount.getBalance();
                            userCheckAccount.put("balance", current - 25.0);

                            /* update the time of penalty */
                            userCheckAccount.put("lastTimePenalty", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();

                            /* update penalty time */
                            Date currentTime = userCheckAccount.getUpdatedAt();

                            /* string to show penalty history on customer's account summary */
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " penalty " + f.format(25) + "\n" +temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();

                            /* reset the checking account amount */
                            checkingNumber.setText("$ " + f.format((current - 25)));
                            Toast.makeText(EmployeeModifiedCus.this, "Penalty Apply", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            /* no penalty applied, update the penalty check time */
                            userCheckAccount.put("lastTimePenalty", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Toast.makeText(EmployeeModifiedCus.this, "No need to penalty within 30 days", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(EmployeeModifiedCus.this, "Already did the penalty within 30 days", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EmployeeModifiedCus.this, "Account is closed!", Toast.LENGTH_SHORT).show();
            }
        });

        /* create saving account  penalty button (( same as checking account penalty )) */
        Button savePenaltyButton = (Button) findViewById(R.id.save_penalty_button);
        savePenaltyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userSaveAccount.isClosed()) {
                    userSaveAccount.put("isClosed", false);
                    userSaveAccount.saveInBackground();
                    if(userSaveAccount.getUpdatedAt().getTime() - userSaveAccount.getDate("lastTimePenalty").getTime() > THIRTYDAY)
                    {
                        double ave;
                        if(userSaveAccount.getHistory().isEmpty()) {
                            ave = userSaveAccount.getBalance();
                        }
                        else
                        {
                            ave = userSaveAccount.average();
                        }
                        if(ave < 100)
                        {
                            double current = userSaveAccount.getBalance();
                            userSaveAccount.put("balance", current - 25.0);
                            userSaveAccount.put("lastTimePenalty", userSaveAccount.getUpdatedAt());
                            userSaveAccount.saveInBackground();
                            Date currentTime = userSaveAccount.getUpdatedAt();
                            String temp = userSaveAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userSaveAccount.getBalance()) + " penalty " + f.format(25) + "\n"+temp ;
                            userSaveAccount.put("history", temp);
                            userSaveAccount.saveInBackground();
                            savingNumber.setText("$ " + f.format((current - 25)));
                            Toast.makeText(EmployeeModifiedCus.this, "Penalty Apply", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            userSaveAccount.put("lastTimePenalty", userSaveAccount.getUpdatedAt());
                            userSaveAccount.saveInBackground();
                            Toast.makeText(EmployeeModifiedCus.this, "No need to penalty within 30 days", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(EmployeeModifiedCus.this, "Already did the penalty within 30 days", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EmployeeModifiedCus.this, "Account is closed!", Toast.LENGTH_SHORT).show();
            }
        });

         /* create checking account  interest button */
        Button checkInterestButton = (Button) findViewById(R.id.check_interest_button);
        checkInterestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /* user's account not closed */
                if(!userCheckAccount.isClosed()) {
                    userCheckAccount.put("isClosed", false);
                    userCheckAccount.saveInBackground();

                    /* check the interest every 30 days */
                    if(userCheckAccount.getUpdatedAt().getTime() - userCheckAccount.getDate("lastTimeInterest").getTime() > THIRTYDAY)
                    {
                        double ave;

                        /* user did not have activities in the 30 days */
                        if(userCheckAccount.getHistory().isEmpty()) {
                            /* get the balance directly */
                            ave = userCheckAccount.getBalance();
                        }
                        else
                        {
                            /* get average amount in 30 days */
                            ave = userCheckAccount.average();
                        }
                        /* check the average amount range */
                        if(ave >= 1000 && ave < 2000)
                        {
                            double current = userCheckAccount.getBalance();
                            /* apply 1% interest to checking accoun balance */
                            userCheckAccount.put("balance", current + ave * 0.01);
                            /* update the interest apply date */
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();

                            /* String to update in customer's account summary */
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " Interest " + f.format(ave * 0.01) + "\n"+temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            /* update the checking account amount */
                            checkingNumber.setText("$ " + f.format((current + ave * 0.01)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        /* check the average amount range */
                        else if(ave >= 2000 && ave < 3000)
                        {
                            double current = userCheckAccount.getBalance();
                            /* apply 2% interest to checking account balance */
                            userCheckAccount.put("balance", current + ave * 0.02);
                            /* update the interest apply date */
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();

                            /* String to update in customer's account summary */
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " Interest " + f.format(ave * 0.02) + "\n"+temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            /* update the checking account amount */
                            checkingNumber.setText("$ " + f.format((current + ave * 0.02)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        /* check the average amount range */
                        else if(ave >= 3000)
                        {
                            double current = userCheckAccount.getBalance();
                            /* apply 3% interest to checking account balance */
                            userCheckAccount.put("balance", current + ave * 0.03);
                            /* update the interest apply date */
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();

                            /* String to update in customer's account summary */
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " Interest " + f.format(ave * 0.03) + "\n"+temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            /* update the checking account amount */
                            checkingNumber.setText("$ " + f.format((current + ave * 0.03)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        /* low balance, so no interest apply */
                        else
                        {
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Toast.makeText(EmployeeModifiedCus.this, "No need to Interest within 30 days", Toast.LENGTH_SHORT).show();
                        }
                    }
                    /* apply interest every 30 days */
                    else
                        Toast.makeText(EmployeeModifiedCus.this, "Already did the Interest within 30 days", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EmployeeModifiedCus.this, "Account is closed!", Toast.LENGTH_SHORT).show();
            }
        });

        /* create saving account  interest button (( same as checking account interest )) */
        Button saveInterestButton = (Button) findViewById(R.id.save_interest_button);
        saveInterestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userSaveAccount.isClosed()) {
                    userSaveAccount.put("isClosed", false);
                    userSaveAccount.saveInBackground();
                    if(userSaveAccount.getUpdatedAt().getTime() - userSaveAccount.getDate("lastTimeInterest").getTime() > THIRTYDAY)
                    {
                        double ave;
                        if(userSaveAccount.getHistory().isEmpty()) {
                            ave = userSaveAccount.getBalance();
                        }
                        else
                        {
                            ave = userSaveAccount.average();
                        }
                        if(ave >= 1000 && ave < 2000)
                        {
                            double current = userSaveAccount.getBalance();
                            userSaveAccount.put("balance", current + ave * 0.02);
                            userSaveAccount.put("lastTimeInterest", userSaveAccount.getUpdatedAt());
                            userSaveAccount.saveInBackground();
                            Date currentTime = userSaveAccount.getUpdatedAt();
                            String temp = userSaveAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userSaveAccount.getBalance()) + " Interest " + f.format(ave * 0.02) + "\n"+temp ;
                            userSaveAccount.put("history", temp);
                            userSaveAccount.saveInBackground();
                            savingNumber.setText("$ " + f.format((current + ave * 0.02)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        else if(ave >= 2000 && ave < 3000)
                        {
                            double current = userSaveAccount.getBalance();
                            userSaveAccount.put("balance", current + ave * 0.03);
                            userSaveAccount.put("lastTimeInterest", userSaveAccount.getUpdatedAt());
                            userSaveAccount.saveInBackground();
                            Date currentTime = userSaveAccount.getUpdatedAt();
                            String temp = userSaveAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    +f.format( userSaveAccount.getBalance()) + " Interest " + f.format(ave * 0.03) + "\n"+temp ;
                            userSaveAccount.put("history", temp);
                            userSaveAccount.saveInBackground();
                            savingNumber.setText("$ " +f.format( (current + ave * 0.03)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        else if(ave >= 3000)
                        {
                            double current = userSaveAccount.getBalance();
                            userSaveAccount.put("balance", current + ave * 0.04);
                            userSaveAccount.put("lastTimeInterest", userSaveAccount.getUpdatedAt());
                            userSaveAccount.saveInBackground();
                            Date currentTime = userSaveAccount.getUpdatedAt();
                            String temp = userSaveAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userSaveAccount.getBalance()) + " Interest " + f.format(ave * 0.04) + "\n"+temp ;
                            userSaveAccount.put("history", temp);
                            userSaveAccount.saveInBackground();
                            savingNumber.setText("$ " + f.format((current + ave * 0.04)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            userSaveAccount.put("lastTimeInterest", userSaveAccount.getUpdatedAt());
                            userSaveAccount.saveInBackground();
                            Toast.makeText(EmployeeModifiedCus.this, "No need to Interest within 30 days", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(EmployeeModifiedCus.this, "Already did the Interest within 30 days", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(EmployeeModifiedCus.this, "Account is closed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
        * Name:             pageToCredit
        * Purpose:        Change page when print credit button clicked.
        * Description:  Go to credit account page.
        */
    public void pageToCredit(View view) {
        Intent getScreen = new Intent(this, CreditActivity.class);
        final int result = 1;
        getScreen.putExtra("someoneEmail",someoneEmail);
        startActivityForResult(getScreen, result);
        finish();
    }

    /**
        * Name:             pageToDredit
        * Purpose:        Change page when print dredit button clicked.
        * Description:  Go to dredit account page.
        */
    public void pageToDebit(View view) {
        Intent getScreen = new Intent(this, DebitActivity.class);
        final int result = 1;
        getScreen.putExtra("someoneEmail",someoneEmail);
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
         * Name:           pageToChoose
         * Purpose:        Change page to search for customer when customer's account closed.
         * Description:  Go to search for customer page.
        */
    public void pageToChoose() {

        Intent getScreen = new Intent(this, EmployeeChooseCus.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        pageToChoose();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        pageToChoose();
        return true;
    }
}
