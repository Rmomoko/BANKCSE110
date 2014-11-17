package rmomoko.cse110bank;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
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

import java.util.List;

/**
 * Created by yenhsialin on 10/25/2014.
 */
public class EmployeeModifiedCus extends Activity{
    private TextView checkingNumber;
    private TextView savingNumber;
    private String someoneEmail;
    private ParseUser someone;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_change_account);

        checkingNumber = (TextView) findViewById(R.id.checkingNumber);
        savingNumber = (TextView) findViewById(R.id.savingNumber);
        someoneEmail = getIntent().getStringExtra("someoneEmail");

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
                                    checkingNumber.setText("$ " + object.getNumber("checkingAccount").toString());
                                    savingNumber.setText("$ " + object.getNumber("savingAccount").toString());
                                } else {
                                    checkingNumber.setText("No User");
                                    savingNumber.setText("No User");
                                }
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(EmployeeModifiedCus.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                    }
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

        Button empCloseButton = (Button) findViewById(R.id.emp_close_button);
        empCloseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(""+someone.getObjectId());
               // someone.becomeInBackground("");
                /*
                someone.put("birthday", "123123123");
                someone.saveInBackground();
                Toast.makeText(EmployeeModifiedCus.this, "This account is closed!", Toast.LENGTH_SHORT).show();
                pageToChoose();*/
                //ParseUser user;
                ParseObject selfAccount = someone.getParseObject("Account");
                selfAccount.fetchInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.put("isClosed", true);
                            object.saveInBackground();
                            Toast.makeText(EmployeeModifiedCus.this, "This account is closed!", Toast.LENGTH_SHORT).show();
                            pageToChoose();
                        } else {
                        }
                    }
                });

                /*
                someone.fetchInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        user.put("isClosed", true);
                        user.saveInBackground();

                        Toast.makeText(EmployeeModifiedCus.this, "This account is closed!", Toast.LENGTH_SHORT).show();
                        pageToChoose();
                    }
                });*/
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
}
