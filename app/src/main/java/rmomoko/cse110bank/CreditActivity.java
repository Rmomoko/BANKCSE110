package rmomoko.cse110bank;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.Integer;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

/**
 * Created by Yuxiao on 11/2/2014.
 */
public class CreditActivity extends Activity {
    private EditText creditAmount;
    private int amount;
    private String someoneEmail;
    private ParseUser someone;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_credit);
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
         amount = Integer.parseInt(creditAmount.getText().toString());
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
                                    object.put("checkingAccount", current + amount);
                                    object.saveInBackground();
                                    Toast.makeText(CreditActivity.this, "Successful deposite!", Toast.LENGTH_SHORT).show();
                                    pageChange();
                                } else {
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(CreditActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(CreditActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void depositeSavingAccount(){
        amount = Integer.parseInt(creditAmount.getText().toString());
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
                                    object.put("savingAccount", current + amount);
                                    object.saveInBackground();
                                    Toast.makeText(CreditActivity.this, "Successful deposite!", Toast.LENGTH_SHORT).show();
                                    pageChange();
                                } else {
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(CreditActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                    }
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
}


