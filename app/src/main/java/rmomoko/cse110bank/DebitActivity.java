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

import java.util.List;

/**
 * Created by Yuxiao on 11/2/2014.
 */
public class DebitActivity extends Activity{
    private EditText debitAmount;
    private int amount;
    private String someoneEmail;
    private ParseUser someone;

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
        amount = Integer.parseInt(debitAmount.getText().toString());

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",someoneEmail);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null)
                {
                    if(!parseUsers.isEmpty()) {
                        someone = parseUsers.get(0);
                        ParseObject account = someone.getParseObject("Account");
                        account.fetchInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    int current = object.getNumber("checkingAccount").intValue();
                                    if(amount > current)
                                    {
                                        debitAmount.setError("Not enough money");
                                    }
                                    else{
                                        object.put("checkingAccount", current - amount);
                                        object.saveInBackground();
                                        Toast.makeText(DebitActivity.this, "Successful withdrawal!", Toast.LENGTH_SHORT).show();
                                        pageChange();
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(DebitActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
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
        amount = Integer.parseInt(debitAmount.getText().toString());
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",someoneEmail);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null)
                {
                    if(!parseUsers.isEmpty()) {
                        someone = parseUsers.get(0);
                        ParseObject account = someone.getParseObject("Account");
                        account.fetchInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    int current = object.getNumber("savingAccount").intValue();
                                    if(amount > current)
                                    {
                                        debitAmount.setError("Not enough money");
                                    }
                                    else{
                                        object.put("savingAccount", current - amount);
                                        object.saveInBackground();
                                        Toast.makeText(DebitActivity.this, "Successful withdrawal!", Toast.LENGTH_SHORT).show();
                                        pageChange();
                                    }
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(DebitActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
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
