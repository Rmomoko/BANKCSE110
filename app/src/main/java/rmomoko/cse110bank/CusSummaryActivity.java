/**
 * Team Name: Orange Chicken
 *  File Name: CusSummaryActivity.java
 *  Description: print out the summary of different accounts for each customer
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

//import parse library
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;

/**
 * Name:         CusSummaryActivity
 * Purpose:      Display customer's account summary information when teller login and search for customer.
 * Description:  Get the customer's checking and saving balance from database.
 *                   Create tell's activity button to process transactions, and perform the activity.
 *
 */
public class CusSummaryActivity extends Activity {
    //view variable to show the history of checking and saving account
    private TextView checkHistory;
    private TextView saveHistory;
    //variable to access use, checking and saving account
    private User user;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    /**
     * Name:           OnCreate
     * Purpose:        Create the layout of Customer Account Information page
     * Description:    Create the UI of CustomerAccountActivity page.
     *                 Get data from database to display the amount of accounts.
     */
    public void onCreate(Bundle savedInstanceState) {

        /* Create the layout of Tell's action page*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_summary);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        checkHistory = (TextView) findViewById(R.id.check_history);
        saveHistory = (TextView) findViewById(R.id.save_history);

        //get the current user from database
        user = (User)ParseUser.getCurrentUser();
        //load all the users from database
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        // Get user information by finding username
        query.whereEqualTo("username",user.getUsername());
        query.include("CheckingAccount");
        query.include("SavingAccount");
        //print out the user account summary when find the customer
        //otherwise print out the debug message
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null && !parseUsers.isEmpty())
                {
                    user = (User)parseUsers.get(0);
                    userCheckAccount = user.getCheckingAccount();
                    userSaveAccount = user.getSavingAccount();
                    checkHistory.setText("" + userCheckAccount.getHistory());
                    saveHistory.setText("" + userSaveAccount.getHistory());
                }
                else
                {
                    Toast.makeText(CusSummaryActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Create customer summary return button
        Button backButton = (Button) findViewById(R.id.cus_summary_back);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //change to customer account information page
                pageChangetoCusAcInfo();
            }
        });

    }




    /**
     * Name:         pageChangetoCusAcInfo
     * Purpose:      Change page when print back button clicked.
     * Description:  Go to customer account page.
     */
    public void pageChangetoCusAcInfo(){
        //create the target page
        Intent getAccountInfoScreen = new Intent(this, CustomerAccountActivity.class);
        //change to next page and close the current page
        final int result = 1;
        startActivityForResult(getAccountInfoScreen, result);
        finish();
    }

    /**
     * Name:         onBackPressed
     * Purpose:      Change page to Customer account information when the back button is clicked
     * Description:  Call the change page function to customer account information
     */
    public void onBackPressed()
    {
        pageChangetoCusAcInfo();
    }

    /**
     * Name:         onOptionsItemSelected
     * Purpose:      Change page to Customer account information when left top button is clicked
     * Description:  Call the change page function to customer account information
     */
    public boolean onOptionsItemSelected(MenuItem item){
        pageChangetoCusAcInfo();
        return true;
    }
}
