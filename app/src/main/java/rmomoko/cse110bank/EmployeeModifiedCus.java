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

import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;

/**
 * Created by yenhsialin on 10/25/2014.
 */
public class EmployeeModifiedCus extends Activity{
    private TextView checkingNumber;
    private TextView savingNumber;
    private String someoneEmail;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

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
                        checkingNumber.setText("$ " + userCheckAccount.getBalance());
                    else
                        checkingNumber.setText("Account Closed");
                    if(!userSaveAccount.isClosed())
                        savingNumber.setText("$ " + userSaveAccount.getBalance());
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
