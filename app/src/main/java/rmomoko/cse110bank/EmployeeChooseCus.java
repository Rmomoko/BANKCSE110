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

/**
 * Created by Yuxiao on 11/16/2014.
 */
public class EmployeeChooseCus extends Activity {
    private EditText someoneEmail;
    private ParseUser someone;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_choice);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Parse.initialize(this, "dJqoRn28p66wHQsJkJKog1zaaRhP3iTDGoSDanYU", "FwLys6BrpNfLyoOWuQCD9vVhIgqYsfjv9RynGOEY");

        someoneEmail = (EditText) findViewById(R.id.employee_choose_email);

        Button empLoginCus = (Button) findViewById(R.id.employee_choose_cus);
        empLoginCus.setOnClickListener(new View.OnClickListener() {
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
                                    ParseObject selfAccount = someone.getParseObject("Account");
                                    selfAccount.fetchInBackground(new GetCallback<ParseObject>() {
                                        public void done(ParseObject object, ParseException e) {
                                            if (e == null) {
                                                if(!object.getBoolean("isClosed"))
                                                {
                                                    pageChange();
                                                }
                                                else
                                                {
                                                    Toast.makeText(EmployeeChooseCus.this, "Fail Login! That account is closed", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                            }
                                        }
                                    });

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
