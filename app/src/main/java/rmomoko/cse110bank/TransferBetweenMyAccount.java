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

import java.util.Date;
import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
/**
 * Created by Yuxiao on 11/16/2014.
 */
public class TransferBetweenMyAccount extends Activity {
    private EditText transferAmount;
    private int amount;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_my_own);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        transferAmount = (EditText) findViewById(R.id.transfer_my_own_amount);



        Button fromSaToCkButton = (Button) findViewById(R.id.transfer_saving_to_checking);
        fromSaToCkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transferAmount.getText().toString().isEmpty())
                {
                   transferFromSaToCk();
                }
                else{
                    transferAmount.setError("You must input a number");
                    transferAmount.requestFocus();
                }

            }
        });

        Button fromCkToSaButton = (Button) findViewById(R.id.transfer_checking_to_saving);
        fromCkToSaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transferAmount.getText().toString().isEmpty())
                {
                    transferFromCkToSa();
                }
                else{
                    transferAmount.setError("You must input a number");
                    transferAmount.requestFocus();
                }
            }
        });
    }

    public void transferFromSaToCk(){
        amount = Integer.parseInt(transferAmount.getText().toString());
        someone = (User)ParseUser.getCurrentUser();
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
                    int currentSaving = userSaveAccount.getBalance();
                    int currentChecking = userCheckAccount.getBalance();
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
                        Date currentTime = userCheckAccount.getUpdatedAt();
                        String temp = userCheckAccount.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + userCheckAccount.getBalance() + " TransferIn " + amount + " from SelfAccount" + "\n";
                        userCheckAccount.put("history", temp);
                        userCheckAccount.saveInBackground();
                        temp = userSaveAccount.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + userSaveAccount.getBalance() + " TransferOut " + amount + " To SelfAccount" + "\n";
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

    public void transferFromCkToSa(){
        amount = Integer.parseInt(transferAmount.getText().toString());
        someone = (User)ParseUser.getCurrentUser();
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
                    int currentSaving = userSaveAccount.getBalance();
                    int currentChecking = userCheckAccount.getBalance();
                    if (amount > currentChecking) {
                        transferAmount.setError("Not enough money");
                    } else {
                        userCheckAccount.put("balance", currentChecking - amount);
                        userSaveAccount.put("balance", currentSaving + amount);
                        userCheckAccount.saveInBackground();
                        userSaveAccount.saveInBackground();
                        Date currentTime = userCheckAccount.getUpdatedAt();
                        String temp = userCheckAccount.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + userCheckAccount.getBalance() + " TransferOut " + amount + " To SelfAccount" + "\n";
                        userCheckAccount.put("history", temp);
                        userCheckAccount.saveInBackground();
                        temp = userSaveAccount.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + userSaveAccount.getBalance() + " TransferIn " + amount + " From SelfAccount" + "\n";
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


    public void pageChangeToChoice() {
        Intent getScreen = new Intent(this, TransferChoiceActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }


    public void pageChange() {
        Intent getScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void onBackPressed()
    {
        pageChangeToChoice();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        pageChangeToChoice();
        return true;
    }
}
