/**
 * Team Name: Orange Chicken
 *  File Name: EmployeeChooseCus.java
 *  Description: Employee can choose any customer account and change to that page
 */
package rmomoko.cse110bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;

/**
 * Name:        EmployeeChooseCus
 * Purpose:     Change to specific customer account when teller search for that customer.
 * Description: Get the customer's email from database.
 *              Create tell's activity button to perform the activity.
 */
public class EmployeeChooseCus extends Activity {
    //store the searching customer email
    private EditText someoneEmail;
    //store user and user's two accounts
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    /**
     * Name:         OnCreate
     * Purpose:      Create the layout of Customer Account Information page
     * Description:  Create the UI of EmployeeChooseCus page.
     *               Get data from database to check the customer account
     */
    public void onCreate(Bundle savedInstanceState) {

        /* Create the layout of Tell's action page*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choice);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        someoneEmail = (EditText) findViewById(R.id.employee_choose_email);

        /* create credit button */
        Button empLoginCus = (Button) findViewById(R.id.employee_choose_cus);
        empLoginCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input searching email is valid
                if(!someoneEmail.getText().toString().isEmpty()) {
                    //load all users from database
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    //search the customer
                    query.whereEqualTo("email",someoneEmail.getText().toString());
                    query.include("CheckingAccount");
                    query.include("SavingAccount");
                    //if the customer exists, save its accounts,
                    //otherwise print out debug message
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if(e == null && !parseUsers.isEmpty())
                            {
                                someone = (User)parseUsers.get(0);
                                userCheckAccount = someone.getCheckingAccount();
                                userSaveAccount = someone.getSavingAccount();
                                //if the finding account still available,change page to there
                                //otherwise, print out the debug message
                                if(!userCheckAccount.isClosed() || !userSaveAccount.isClosed())
                                {
                                    pageChange();
                                }
                                else
                                {
                                    Toast.makeText(EmployeeChooseCus.this, "The accounts are all closed!", Toast.LENGTH_SHORT).show();
                                    someoneEmail.requestFocus();
                                }
                            }
                            else
                            {
                                someoneEmail.setError("You must input a valid email");
                                someoneEmail.requestFocus();
                            }
                        }
                    });
                }
                else
                {
                    someoneEmail.setError("You must input an email");
                    someoneEmail.requestFocus();
                }
            }
        });
    }


    /**
     * Name:         pageChange
     * Purpose:      Change to EmployeeModifiedCus page when empLoginCus button is clicked
     * Description:  Go to EmployeeModifiedCus page.
     */
    public void pageChange() {
        //create the new page and change to the targe page and close the current page
        Intent getScreen = new Intent(this, EmployeeModifiedCus.class);
        getScreen.putExtra("someoneEmail", someoneEmail.getText().toString());
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    /**
     * Name:         pageChangeToLogin
     * Purpose:      Change to LoginActivity page when return button is clicked
     * Description:  Go to EmployeeModifiedCus page.
     */
    public void pageChangeToLogin() {
        //create new page and change to it, then close the current page
        Intent getLoginActivity = new Intent(this, LoginActivity.class);
        final int result = 1;
        startActivityForResult(getLoginActivity, result);
        finish();
    }

    /**
     * Name:         onBackPressed
     * Purpose:      Change to LoginActivity page and logout the current account
     *               when back button is clicked
     * Description:  Go to EmployeeModifiedCus page by calling LoginActivity function
     */
    @Override
    public void onBackPressed()
    {
        ParseUser.logOut();
        pageChangeToLogin();
    }

    /**
     * Name:         onOptionsItemSelected
     * Purpose:      Change to LoginActivity page and logout the current account
     *               when left top button is clicked
     * Description:  Go to EmployeeModifiedCus page by calling LoginActivity function
     */
    public boolean onOptionsItemSelected(MenuItem item){
        ParseUser.logOut();
        pageChangeToLogin();
        return true;
    }
}
