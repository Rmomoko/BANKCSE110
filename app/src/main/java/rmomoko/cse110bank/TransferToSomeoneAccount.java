package rmomoko.cse110bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Yuxiao on 11/16/2014.
 */
public class TransferToSomeoneAccount extends Activity {

    private EditText transferAmount;
    private EditText someoneEmail;
    private int amount;
    private ParseUser someone;
    private int currentTo;
    private ParseObject someoneAccount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_someone);
        Parse.initialize(this, "dJqoRn28p66wHQsJkJKog1zaaRhP3iTDGoSDanYU", "FwLys6BrpNfLyoOWuQCD9vVhIgqYsfjv9RynGOEY");
        transferAmount = (EditText) findViewById(R.id.transfer_to_someone_amount);
        someoneEmail = (EditText) findViewById(R.id.transfer_to_someone_email);

        Button fromCkButton = (Button) findViewById(R.id.transfer_from_checking);
        fromCkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email",someoneEmail.getText().toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if(e == null)
                            {
                                if(!parseUsers.isEmpty()) {
                                    someone = parseUsers.get(0);
                                    if (!transferAmount.getText().toString().isEmpty()) {
                                        if(!someone.getBoolean("isClosed"))
                                        {
                                            transferFromCk();
                                        }
                                        else
                                        {
                                            Toast.makeText(TransferToSomeoneAccount.this, "Fail transfer! That account is closed", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        transferAmount.setError("You must input a number");
                                        transferAmount.requestFocus();
                                    }
                                }
                                else
                                {
                                    someoneEmail.setError("You must input a valid email");
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

        Button fromSaButton = (Button) findViewById(R.id.transfer_from_saving);
        fromSaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email",someoneEmail.getText().toString());
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            if(e == null)
                            {
                                if(!parseUsers.isEmpty()) {
                                    someone = parseUsers.get(0);
                                    if (!transferAmount.getText().toString().isEmpty()) {
                                        if(!someone.getBoolean("isClosed"))
                                        {
                                            transferFromSa();
                                        }
                                        else
                                        {
                                            Toast.makeText(TransferToSomeoneAccount.this, "Fail transfer! That account is closed", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        transferAmount.setError("You must input a number");
                                        transferAmount.requestFocus();
                                    }
                                }
                                else
                                {
                                    someoneEmail.setError("You must input a valid email");
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



    public void transferFromCk(){
        amount = Integer.parseInt(transferAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();

        ParseObject selfAccount = user.getParseObject("Account");
        someoneAccount = someone.getParseObject("Account");

        someoneAccount.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                currentTo = parseObject.getNumber("checkingAccount").intValue();
            }
        });

        selfAccount.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    int current = parseObject.getNumber("checkingAccount").intValue();
                    if (amount > current) {
                        transferAmount.setError("Not enough money");
                    } else {
                        parseObject.put("checkingAccount", current - amount);
                        someoneAccount.put("checkingAccount", currentTo + amount);
                        parseObject.saveInBackground();
                        someoneAccount.saveInBackground();
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
        ParseUser user = ParseUser.getCurrentUser();

        ParseObject selfAccount = user.getParseObject("Account");
        someoneAccount = someone.getParseObject("Account");

        someoneAccount.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                currentTo = parseObject.getNumber("checkingAccount").intValue();
            }
        });

        selfAccount.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    int current = parseObject.getNumber("savingAccount").intValue();
                    if (amount > current) {
                        transferAmount.setError("Not enough money");
                    } else {
                        parseObject.put("savingAccount", current - amount);
                        someoneAccount.put("checkingAccount", currentTo + amount);
                        parseObject.saveInBackground();
                        someoneAccount.saveInBackground();
                        Toast.makeText(TransferToSomeoneAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                } else {
                }
            }
        });
    }



    public void pageChange() {
        Intent getScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
}
