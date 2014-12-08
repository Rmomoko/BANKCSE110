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
 * Created by Yuxiao on 11/2/2014.
 */
public class CreditActivity extends Activity {
    private EditText creditAmount;
    private double amount;
    private String someoneEmail;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_credit);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        creditAmount = (EditText) findViewById(R.id.credit_amount);
        someoneEmail = getIntent().getStringExtra("someoneEmail");

        Button checkingCredit = (Button) findViewById(R.id.checking_credit_button);
        checkingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!creditAmount.getText().toString().isEmpty())
                {
                        depositeCheckingAccount();
                }
                else{
                    creditAmount.setError("You must input a number");
                    creditAmount.requestFocus();
                }
            }
        });

        Button savingCredit = (Button) findViewById(R.id.saving_credit_button);
        savingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!creditAmount.getText().toString().isEmpty())
                {
                        depositeSavingAccount();
                }
                else{
                    creditAmount.setError("You must input a number");
                    creditAmount.requestFocus();
                }
            }
        });
    }

    public void depositeCheckingAccount(){
        amount = Double.parseDouble(creditAmount.getText().toString());
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",someoneEmail);
        query.include("CheckingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null && !parseUsers.isEmpty())
                {
                    someone = (User)parseUsers.get(0);
                    userCheckAccount = someone.getCheckingAccount();

                    double current = userCheckAccount.getBalance();


                    DecimalFormat f = new DecimalFormat("##.00");
                    userCheckAccount.put("balance", current + amount);
                    userCheckAccount.saveInBackground();
                    Date currentTime = userCheckAccount.getUpdatedAt();
                    String temp = userCheckAccount.getHistory();
                    temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + f.format(userCheckAccount.getBalance()) + " Credit " + f.format(amount) + "\n"+ temp;
                    userCheckAccount.put("history", temp);
                    userCheckAccount.saveInBackground();
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
    public void depositeSavingAccount(){
        amount = Double.parseDouble(creditAmount.getText().toString());
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",someoneEmail);
        query.include("SavingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null && !parseUsers.isEmpty())
                {
                    someone = (User)parseUsers.get(0);
                    userSaveAccount = someone.getSavingAccount();

                    double current = userSaveAccount.getBalance();
                    DecimalFormat f = new DecimalFormat("##.00");
                    userSaveAccount.put("balance", current + amount);
                    userSaveAccount.saveInBackground();
                    Date currentTime = userSaveAccount.getUpdatedAt();
                    String temp = userSaveAccount.getHistory();
                    temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                            + f.format(userSaveAccount.getBalance()) + " Credit " + f.format(amount) + "\n" + temp;
                    userSaveAccount.put("history", temp);
                    userSaveAccount.saveInBackground();
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
    public void pageChange() {

        Intent getScreen = new Intent(this, EmployeeModifiedCus.class);
        final int result = 1;
        getScreen.putExtra("someoneEmail",someoneEmail);
        startActivityForResult(getScreen, result);
        finish();
    }

    public void onBackPressed()
    {
        pageChange();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        pageChange();
        return true;
    }
}


