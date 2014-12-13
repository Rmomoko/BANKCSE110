/**
 * Team Name: Orange Chicken
 *  File Name: TransferBetweenMyAccount.java
 *  Description: Transfer the money between the checking account and saving account
 */

package rmomoko.cse110bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;

/**
 * Name:          TransferBetweenMyAccount
 * Purpose:       Transfer the money between the checking account and saving account
 * Description:   This class extends activity and creates many functions in order to be able to
 *                transfer money between the checking account and the saving account.
 */
public class TransferBetweenMyAccount extends Activity {
    private EditText transferAmount;
    private double amount;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;
    // create format for money
    private DecimalFormat f = new DecimalFormat("##.00");

    /**
     * Name:           OnCreate
     * Purpose:        Create the layout transfer between inner accounts page
     * Description:    Create the UI for transfer between inner accounts page,and link each
     *                 button with its corresponding storage of data.
     */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_my_own);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        transferAmount = (EditText) findViewById(R.id.transfer_my_own_amount);


        // create button for transfer money from saving to checking
        Button fromSaToCkButton = (Button) findViewById(R.id.transfer_saving_to_checking);
        fromSaToCkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transferAmount.getText().toString().isEmpty())
                {
                   transferFromSaToCk();
                }
                // check for valid input
                else{
                    transferAmount.setError("You must input a number");
                    transferAmount.requestFocus();
                }

            }
        });

        // create button for transfer money from checking to saving
        Button fromCkToSaButton = (Button) findViewById(R.id.transfer_checking_to_saving);
        fromCkToSaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transferAmount.getText().toString().isEmpty())
                {
                    transferFromCkToSa();
                }
                // check for valid input
                else{
                    transferAmount.setError("You must input a number");
                    transferAmount.requestFocus();
                }
            }
        });
    }

    /**
     * Name:          transferFromSaToCk
     * Purpose:       Transfer money from saving account to checking account
     * Description:   Fetch current data from database, and transfer money from saving account
     *                to checking account based on the amount specified by the user.
     */
    public void transferFromSaToCk(){
        amount = Double.parseDouble(transferAmount.getText().toString());
        someone = (User)ParseUser.getCurrentUser();
        // fetch information from the database
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",someone.getUsername());
        query.include("CheckingAccount");
        query.include("SavingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null && !parseUsers.isEmpty())
                {
                    someone = (User)parseUsers.get(0);
                    userCheckAccount = someone.getCheckingAccount();
                    userSaveAccount = someone.getSavingAccount();
                    double currentSaving = userSaveAccount.getBalance();
                    double currentChecking = userCheckAccount.getBalance();
                    if(amount > currentSaving)
                    {
                        transferAmount.setError("Not enough money");
                    }
                    else
                    {
                        userCheckAccount.put("balance", currentChecking + amount);
                        userSaveAccount.put("balance", currentSaving - amount);
                        userCheckAccount.saveInBackground();
                        userSaveAccount.saveInBackground();
                        // create transaction history based on time information and transfer detail
                        Date currentTime = userCheckAccount.getUpdatedAt();
                        String temp = userCheckAccount.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(userCheckAccount.getBalance()) + " TransferIn " + f.format(amount) + " from SelfAccount" + "\n"+temp ;
                        userCheckAccount.put("history", temp);
                        userCheckAccount.saveInBackground();
                        temp = userSaveAccount.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(userSaveAccount.getBalance()) + " TransferOut " + f.format(amount) + " To SelfAccount" + "\n"+ temp;
                        userSaveAccount.put("history", temp);
                        userSaveAccount.saveInBackground();
                        Toast.makeText(TransferBetweenMyAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                }
                else
                {
                    Toast.makeText(TransferBetweenMyAccount.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * Name:          transferFromCkToSa
     * Purpose:       Transfer money from checking account to saving account
     * Description:   Fetch current data from database, and transfer money from checking account
     *                to saving account based on the amount specified by the user.
     */
    public void transferFromCkToSa(){
        amount = Double.parseDouble(transferAmount.getText().toString());
        someone = (User)ParseUser.getCurrentUser();
        // fetch information from the database
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",someone.getUsername());
        query.include("CheckingAccount");
        query.include("SavingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null && !parseUsers.isEmpty()) {
                    someone = (User) parseUsers.get(0);
                    userCheckAccount = someone.getCheckingAccount();
                    userSaveAccount = someone.getSavingAccount();
                    double currentSaving = userSaveAccount.getBalance();
                    double currentChecking = userCheckAccount.getBalance();
                    // check for different cases
                    if (amount > currentChecking) {
                        transferAmount.setError("Not enough money");
                    } else {
                        userCheckAccount.put("balance", currentChecking - amount);
                        userSaveAccount.put("balance", currentSaving + amount);
                        userCheckAccount.saveInBackground();
                        userSaveAccount.saveInBackground();
                        // create transaction history based on time information and transfer detail
                        Date currentTime = userCheckAccount.getUpdatedAt();
                        String temp = userCheckAccount.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(userCheckAccount.getBalance()) + " TransferOut " + f.format(amount) + " To SelfAccount" + "\n" +temp;
                        userCheckAccount.put("history", temp);
                        userCheckAccount.saveInBackground();
                        temp = userSaveAccount.getHistory();
                        temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                +f.format(userSaveAccount.getBalance()) + " TransferIn " + f.format(amount) + " From SelfAccount" + "\n"+ temp;
                        userSaveAccount.put("history", temp);
                        userSaveAccount.saveInBackground();
                        Toast.makeText(TransferBetweenMyAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                } else {
                    Toast.makeText(TransferBetweenMyAccount.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
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
