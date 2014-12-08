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
 * Created by Yuxiao on 11/2/2014.
 */
public class DebitActivity extends Activity{
    private EditText debitAmount;
    private double amount;
    private double current;
    private String someoneEmail;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;
    private DecimalFormat f = new DecimalFormat("##.00");
    private static final double LIMIT = 10000.0;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_debit);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        debitAmount = (EditText) findViewById(R.id.debit_amount);
        someoneEmail = getIntent().getStringExtra("someoneEmail");

        Button checkingCredit = (Button) findViewById(R.id.checking_debit_button);
        checkingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!debitAmount.getText().toString().isEmpty())
                {
                        withdrawalCheckingAccount();
                }
                else{
                    debitAmount.setError("You must input a number");
                    debitAmount.requestFocus();
                }

            }
        });

        Button savingCredit = (Button) findViewById(R.id.saving_debit_button);
        savingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!debitAmount.getText().toString().isEmpty())
                {
                        withdrawalSavingAccount();
                }
                else{
                    debitAmount.setError("You must input a number");
                    debitAmount.requestFocus();
                }
            }
        });
    }

    public void withdrawalCheckingAccount(){
        amount = Double.parseDouble(debitAmount.getText().toString());

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
                    current = userCheckAccount.getBalance();
                    if(amount > current)
                    {
                        debitAmount.setError("Not enough money");
                    }
                    else if(amount > LIMIT)
                    {
                        Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        userCheckAccount.put("isClosed", false);
                        userCheckAccount.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                String[] history = userCheckAccount.getHistory().split("\n");
                                Date today = userCheckAccount.getUpdatedAt();
                                double temp = 0;
                                String currentTime = "" + (today.getYear()+ 1900) + "/" + (today.getMonth()+1) + "/" + today.getDate();
                                for(int i = 0; i < history.length; i++) {
                                    String[] element = history[i].split(" ");
                                    if(!element[0].equals(currentTime)) break;
                                    if(element[2].equals("Debit")) temp += Double.parseDouble(element[3]);
                                }
                                if(temp  + amount> LIMIT)
                                {
                                    Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    userCheckAccount.put("balance", current - amount);
                                    Date currentTimes = userCheckAccount.getUpdatedAt();
                                    String temps = userCheckAccount.getHistory();
                                    temps = (currentTimes.getYear() + 1900) + "/" + (currentTimes.getMonth() + 1) + "/" + currentTimes.getDate() + " "
                                            + f.format(current - amount) + " Debit " + f.format(amount) + "\n" + temps;
                                    userCheckAccount.put("history", temps);
                                    userCheckAccount.saveInBackground(new SaveCallback() {
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
    public void withdrawalSavingAccount(){
        amount = Double.parseDouble(debitAmount.getText().toString());

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
                    current = userSaveAccount.getBalance();
                    if(amount > current)
                    {
                        debitAmount.setError("Not enough money");
                    }
                    else if(amount > LIMIT)
                    {
                        Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        userSaveAccount.put("isClosed", false);
                        userSaveAccount.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                String[] history = userSaveAccount.getHistory().split("\n");
                                Date today = userSaveAccount.getUpdatedAt();
                                double temp = 0;
                                String currentTime = "" + (today.getYear()+ 1900) + "/" + (today.getMonth()+1) + "/" + today.getDate();
                                for(int i = 0; i < history.length; i++) {
                                    String[] element = history[i].split(" ");
                                    if(!element[0].equals(currentTime)) break;
                                    if(element[2].equals("Debit")) temp += Double.parseDouble(element[3]);
                                }
                                if(temp  + amount> LIMIT)
                                {
                                    Toast.makeText(DebitActivity.this, "You have reach the limit!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    userSaveAccount.put("balance", current - amount);
                                    Date currentTimes = userSaveAccount.getUpdatedAt();
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
