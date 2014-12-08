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
 * Created by Yuxiao on 11/16/2014.
 */
public class TransferToSomeoneAccount extends Activity {

    private EditText transferAmount;
    private EditText someoneEmail;
    private int amount;
    private int currentTo;
    private User someone;
    private CheckingAccount someoneCheckAccount;
    private User user;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_someone);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        transferAmount = (EditText) findViewById(R.id.transfer_to_someone_amount);
        someoneEmail = (EditText) findViewById(R.id.transfer_to_someone_email);

        user = (User)ParseUser.getCurrentUser();

        Button fromCkButton = (Button) findViewById(R.id.transfer_from_checking);
        fromCkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", someoneEmail.getText().toString());
                    query.include("CheckingAccount");
                    query.include("SavingAccount");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if (e == null && !parseUsers.isEmpty()) {
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
                                } else {
                                    transferAmount.setError("You must input a number");
                                    transferAmount.requestFocus();
                                }

                            }
                            else
                            {
                                transferAmount.setError("You must input an email");
                                transferAmount.requestFocus();
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

        Button fromSaButton = (Button) findViewById(R.id.transfer_from_saving);
        fromSaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", someoneEmail.getText().toString());
                    query.include("CheckingAccount");
                    query.include("SavingAccount");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if (e == null && !parseUsers.isEmpty()) {
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
                                } else {
                                    transferAmount.setError("You must input a number");
                                    transferAmount.requestFocus();
                                }

                            }
                            else
                            {
                                transferAmount.setError("You must input an email");
                                transferAmount.requestFocus();
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



    public void transferFromCk(){
        amount = Integer.parseInt(transferAmount.getText().toString());

        userCheckAccount = user.getCheckingAccount();
        userCheckAccount.fetchInBackground(new GetCallback<CheckingAccount>() {
            @Override
            public void done(CheckingAccount Object, ParseException e) {
                if (e == null) {
                    int current = Object.getBalance();
                    if (amount > current) {
                        transferAmount.setError("Not enough money");
                    } else {
                        Object.put("balance", current - amount);
                        Object.saveInBackground();
                        someoneCheckAccount.put("balance", currentTo + amount);
                        someoneCheckAccount.saveInBackground();

                        Date currentTime = someoneCheckAccount.getUpdatedAt();
                        String temp = someoneCheckAccount.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + someoneCheckAccount.getBalance() + " TransferIn " + amount + " from " + user.getEmail() + "\n";
                        someoneCheckAccount.put("history", temp);
                        someoneCheckAccount.saveInBackground();
                        temp = Object.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + Object.getBalance() + " TransferOut " + amount + " To " + someone.getEmail() + "\n";
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



    public void transferFromSa(){
        amount = Integer.parseInt(transferAmount.getText().toString());

        userSaveAccount = user.getSavingAccount();
        userSaveAccount.fetchInBackground(new GetCallback<SavingAccount>() {
            @Override
            public void done(SavingAccount Object, ParseException e) {
                if (e == null) {
                    int current = Object.getBalance();
                    if (amount > current) {
                        transferAmount.setError("Not enough money");
                    } else {

                        Object.put("balance", current - amount);
                        someoneCheckAccount.put("balance", currentTo + amount);
                        Object.saveInBackground();
                        someoneCheckAccount.saveInBackground();

                        Date currentTime = someoneCheckAccount.getUpdatedAt();
                        String temp = someoneCheckAccount.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + someoneCheckAccount.getBalance() + " TransferIn " + amount + " from " + user.getEmail() + "\n";
                        someoneCheckAccount.put("history", temp);
                        someoneCheckAccount.saveInBackground();
                        temp = Object.getHistory();
                        temp = temp + (currentTime.getYear()+ 1900) + "/" + (currentTime.getMonth()+1) + "/" + currentTime.getDate() + " "
                                + Object.getBalance() + " TransferOut " + amount + " To " + someone.getEmail() + "\n";
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
