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
 * Created by yenhsialin on 10/25/2014.
 */
public class EmployeeModifiedCus extends Activity{
    private static final long THIRTYDAY = 2592000000L;
    private TextView checkingNumber;
    private TextView savingNumber;
    private String someoneEmail;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;
    private DecimalFormat f = new DecimalFormat("##.00");

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_change_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        checkingNumber = (TextView) findViewById(R.id.checkingNumber);
        savingNumber = (TextView) findViewById(R.id.savingNumber);
        someoneEmail = getIntent().getStringExtra("someoneEmail");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",someoneEmail);
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
                    if(!userCheckAccount.isClosed())
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

        Button creditButton = (Button) findViewById(R.id.credit_button);
        creditButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(someone != null) {
                    pageToCredit(view);
                }
            }
        });

        Button debitButton = (Button) findViewById(R.id.debit_button);
        debitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(someone != null) {
                    pageToDebit(view);
                }
            }
        });
        Button checkPenaltyButton = (Button) findViewById(R.id.check_penalty_button);
        checkPenaltyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userCheckAccount.isClosed()) {
                    userCheckAccount.put("isClosed", false);
                    userCheckAccount.saveInBackground();
                    if(userCheckAccount.getUpdatedAt().getTime() - userCheckAccount.getDate("lastTimePenalty").getTime() > THIRTYDAY)
                    {
                        double ave;
                        if(userCheckAccount.getHistory().isEmpty()) {
                            ave = userCheckAccount.getBalance();
                        }
                        else
                        {
                            ave = userCheckAccount.average();
                        }
                        if(ave < 100)
                        {
                            double current = userCheckAccount.getBalance();
                            userCheckAccount.put("balance", current - 25.0);
                            userCheckAccount.put("lastTimePenalty", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " penalty " + f.format(25) + "\n" +temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            checkingNumber.setText("$ " + f.format((current - 25)));
                            Toast.makeText(EmployeeModifiedCus.this, "Penalty Apply", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
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

        Button checkInterestButton = (Button) findViewById(R.id.check_interest_button);
        checkInterestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userCheckAccount.isClosed()) {
                    userCheckAccount.put("isClosed", false);
                    userCheckAccount.saveInBackground();
                    if(userCheckAccount.getUpdatedAt().getTime() - userCheckAccount.getDate("lastTimeInterest").getTime() > THIRTYDAY)
                    {
                        double ave;
                        if(userCheckAccount.getHistory().isEmpty()) {
                            ave = userCheckAccount.getBalance();
                        }
                        else
                        {
                            ave = userCheckAccount.average();
                        }
                        if(ave >= 1000 && ave < 2000)
                        {
                            double current = userCheckAccount.getBalance();
                            userCheckAccount.put("balance", current + ave * 0.01);
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " Interest " + f.format(ave * 0.01) + "\n"+temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            checkingNumber.setText("$ " + f.format((current + ave * 0.01)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        else if(ave >= 2000 && ave < 3000)
                        {
                            double current = userCheckAccount.getBalance();
                            userCheckAccount.put("balance", current + ave * 0.02);
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " Interest " + f.format(ave * 0.02) + "\n"+temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            checkingNumber.setText("$ " + f.format((current + ave * 0.02)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        else if(ave >= 3000)
                        {
                            double current = userCheckAccount.getBalance();
                            userCheckAccount.put("balance", current + ave * 0.03);
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
                            Date currentTime = userCheckAccount.getUpdatedAt();
                            String temp = userCheckAccount.getHistory();
                            temp = (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                    + f.format(userCheckAccount.getBalance()) + " Interest " + f.format(ave * 0.03) + "\n"+temp ;
                            userCheckAccount.put("history", temp);
                            userCheckAccount.saveInBackground();
                            checkingNumber.setText("$ " + f.format((current + ave * 0.03)));
                            Toast.makeText(EmployeeModifiedCus.this, "Interest Apply", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            userCheckAccount.put("lastTimeInterest", userCheckAccount.getUpdatedAt());
                            userCheckAccount.saveInBackground();
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








    public void pageToCredit(View view) {
        Intent getScreen = new Intent(this, CreditActivity.class);
        final int result = 1;
        getScreen.putExtra("someoneEmail",someoneEmail);
        startActivityForResult(getScreen, result);
        finish();
    }
    public void pageToDebit(View view) {
        Intent getScreen = new Intent(this, DebitActivity.class);
        final int result = 1;
        getScreen.putExtra("someoneEmail",someoneEmail);
        startActivityForResult(getScreen, result);
        finish();
    }

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
