package rmomoko.cse110bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
/**
 * Created by Yuxiao on 11/16/2014.
 */
public class EmployeeChooseCus extends Activity {
    private EditText someoneEmail;
    private User someone;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choice);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        someoneEmail = (EditText) findViewById(R.id.employee_choose_email);

        Button empLoginCus = (Button) findViewById(R.id.employee_choose_cus);
        empLoginCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!someoneEmail.getText().toString().isEmpty()) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email",someoneEmail.getText().toString());
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
                                if(!userCheckAccount.isClosed() || !userSaveAccount.isClosed())
                                {
                                    pageChange();
                                }
                                else
                                {
                                    Toast.makeText(EmployeeChooseCus.this, "The accounts are all closed!", Toast.LENGTH_SHORT).show();
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

    public void pageChange() {
        Intent getScreen = new Intent(this, EmployeeModifiedCus.class);
        getScreen.putExtra("someoneEmail", someoneEmail.getText().toString());
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void pageChangeToLogin() {
        Intent getLoginActivity = new Intent(this, LoginActivity.class);
        final int result = 1;
        startActivityForResult(getLoginActivity, result);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        ParseUser.logOut();
        pageChangeToLogin();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        ParseUser.logOut();
        pageChangeToLogin();
        return true;
    }
}
