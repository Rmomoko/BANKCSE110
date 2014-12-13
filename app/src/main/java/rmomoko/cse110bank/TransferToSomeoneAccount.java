/**
 * Team Name: Orange Chicken
 *  File Name: TransferToSomeoneAccount.java
 *  Description: Transfer the money to some other user's account
 */
package rmomoko.cse110bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
/**
 * Name:          TransferToSomeoneAccount
 * Purpose:       Transfer the money to some other user's account
 * Description:   This class extends activity and creates many functions in order to be able to
 *                transfer money to some other user's account.
 */
public class TransferToSomeoneAccount extends Activity {

    // initialize variables for transferAmount,someoneEmail of EditText type
    private EditText transferAmount;
    private EditText someoneEmail;

    // initialize amount
    private double amount;
    private double currentTo;

    // initialize User type current user, "someone" the money transferred to.
    // Initialize the amount of money in current user's checking,
    // and the amount of money in someone's checking
    private User someone;
    private CheckingAccount someoneCheckAccount;
    private User user;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    // make sure the amount of money is in two decimal space
    private DecimalFormat f = new DecimalFormat("##.00");

    /**
     * Name:           OnCreate
     * Purpose:        Create the layout for transfer between users' accounts page
     * Description:    Create the UI for transfer between users' accounts page,and link each
     *                 button with its corresponding storage of data.
     */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_someone);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        transferAmount = (EditText) findViewById(R.id.transfer_to_someone_amount);
        someoneEmail = (EditText) findViewById(R.id.transfer_to_someone_email);

        // get current user from database
        user = (User)ParseUser.getCurrentUser();

        // initialize from checking Button
        Button fromCkButton = (Button) findViewById(R.id.transfer_from_checking);
        fromCkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    // fetch information from the database
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", someoneEmail.getText().toString());
                    query.include("CheckingAccount");
                    query.include("SavingAccount");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            // check for different cases
                            if (e == null && !parseUsers.isEmpty()) {
                                // check for valid user and account
                                someone = (User) parseUsers.get(0);
                                if (!transferAmount.getText().toString().isEmpty()) {
                                    someoneCheckAccount = someone.getCheckingAccount();
                                    if (!someoneCheckAccount.isClosed()) {
                                        if (!someone.getEmail().equals(user.getEmail())) {
                                            currentTo = someoneCheckAccount.getBalance();
                                            transferFromCk();
                                        } else {
                                            Toast.makeText(TransferToSomeoneAccount.this, "Fail transfer! Cannot transfer to yourself", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(TransferToSomeoneAccount.this, "Fail transfer! That account is closed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                // check for valid input
                                else {
                                    transferAmount.setError("You must input a number");
                                    transferAmount.requestFocus();
                                }

                            }
                            // check for valid input
                            else
                            {
                                transferAmount.setError("You must input an email");
                                transferAmount.requestFocus();
                            }
                        }
                    });
                }
                // check for valid input
                else
                {
                    someoneEmail.setError("You must input an email");
                    someoneEmail.requestFocus();
                }
            }
        });

        // initialize from saving Button
        Button fromSaButton = (Button) findViewById(R.id.transfer_from_saving);
        fromSaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    // fetch information from the database
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", someoneEmail.getText().toString());
                    query.include("CheckingAccount");
                    query.include("SavingAccount");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            // check for different cases
                            if (e == null && !parseUsers.isEmpty()) {
                                // check for valid user and account
                                someone = (User) parseUsers.get(0);
                                if (!transferAmount.getText().toString().isEmpty()) {
                                    someoneCheckAccount = someone.getCheckingAccount();
                                    if (!someoneCheckAccount.isClosed()) {
                                        if (!someone.getEmail().equals(user.getEmail())) {
                                            currentTo = someoneCheckAccount.getBalance();
                                            transferFromSa();
                                        } else {
                                            Toast.makeText(TransferToSomeoneAccount.this, "Fail transfer! Cannot transfer to yourself", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(TransferToSomeoneAccount.this, "Fail transfer! That account is closed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                // check for valid input
                                else {
                                    transferAmount.setError("You must input a number");
                                    transferAmount.requestFocus();
                                }

                            }
                            // check for valid input
                            else
                            {
                                transferAmount.setError("You must input an email");
                                transferAmount.requestFocus();
                            }
                        }
                    });
                }
                else
                // check for valid input
                {
                    someoneEmail.setError("You must input an email");
                    someoneEmail.requestFocus();
                }
            }
        });
    }

    /**
     * Name:          transferFromCk
     * Purpose:       Transfer money from checking account to some other user's account
     * Description:   Fetch current data from database, and transfer money from checking account
     *                to some other user's account based on the amount specified by the user.
     */
    public void transferFromCk(){

        // convert transfer amount from String to double type
        amount = Double.parseDouble(transferAmount.getText().toString());

        // get Checking account of current user
        userCheckAccount = user.getCheckingAccount();
        userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
            @Override
            public void done(CheckingAccount Object, ParseException e) {
                if (e == null) {
                    double current = Object.getBalance();
                    // check if there is enough money
                    if (amount > current) {
                        transferAmount.setError("Not enough money");

                    // if there is enough money
                    } else {

                        // deduct certain amount of money from current user's checking account
                        // in database based on the transfer amount
                        Object.put("balance", current - amount);
                        Object.saveInBackground();
                        // increase certain amount of money in someone's checking account
                        // in database based on the transfer amount
                        someoneCheckAccount.put("balance", currentTo + amount);
                        someoneCheckAccount.saveInBackground();

                        // create transaction history based on time information and transfer detail
                        Date currentTime = someoneCheckAccount.getUpdatedAt();
                        String temp = someoneCheckAccount.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(someoneCheckAccount.getBalance()) + " TransferIn " + f.format(amount) + " from " + user.getEmail() + "\n"+ temp;
                        someoneCheckAccount.put("history", temp);
                        someoneCheckAccount.saveInBackground();
                        temp = Object.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(Object.getBalance()) + " TransferOut " + f.format(amount) + " To " + someone.getEmail() + "\n"+ temp;
                        Object.put("history", temp);
                        Object.saveInBackground();

                        Toast.makeText(TransferToSomeoneAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                } else {
                }
            }
        });
    }


    /**
     * Name:          transferFromSa
     * Purpose:       Transfer money from saving account to some other user's account
     * Description:   Fetch current data from database, and transfer money from saving account
     *                to some other user's account based on the amount specified by the user.
     */
    public void transferFromSa(){
        amount = Double.parseDouble(transferAmount.getText().toString());

        userSaveAccount = user.getSavingAccount();
        userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
            @Override
            public void done(SavingAccount Object, ParseException e) {
                if (e == null) {
                    double current = Object.getBalance();
                    // check for different cases
                    if (amount > current) {
                        transferAmount.setError("Not enough money");
                    } else {
                        // deduct certain amount of money from current user's saving account
                        // in database based on the transfer amount
                        Object.put("balance", current - amount);

                        // increase certain amount of money in someone's saving account
                        // in database based on the transfer amount
                        someoneCheckAccount.put("balance", currentTo + amount);
                        Object.saveInBackground();
                        someoneCheckAccount.saveInBackground();

                        // create transaction history based on time information and transfer detail
                        Date currentTime = someoneCheckAccount.getUpdatedAt();
                        String temp = someoneCheckAccount.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(someoneCheckAccount.getBalance()) + " TransferIn " + f.format(amount) + " from " + user.getEmail() + "\n"+ temp;
                        someoneCheckAccount.put("history", temp);
                        someoneCheckAccount.saveInBackground();
                        temp = Object.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(Object.getBalance()) + " TransferOut " + f.format(amount) + " To " + someone.getEmail() + "\n"+ temp;
                        Object.put("history", temp);
                        Object.saveInBackground();
                        
                        Toast.makeText(TransferToSomeoneAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                } else {
                }
            }
        });
    }
    /**
     * Name:           pageToChoose
     * Purpose:        Change page to search for customer when customer's account closed.
     * Description:  Go to search for customer page.
     */
    public void pageChangeToChoice() {
        Intent getScreen = new Intent(this, TransferChoiceActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
     * Name:          pageChange
     * Purpose:      Go to a certain page in this case the customer account activity page
     * Description:  Go to the customer account activity page
     */
    public void pageChange() {
        Intent getScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
     * Name:          onBackPressed
     * Purpose:       Go back to the previous page
     * Description:   Go back to the recent previous page.
     */
    public void onBackPressed()
    {
        pageChangeToChoice();
    }
    /**
     * Name:          onOptionsItemSelected
     * Purpose:     Enforce the customer to logout and go to login page.
     * Description:  Enforce the customer to logout and go to login page.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        pageChangeToChoice();
        return true;
    }
}
