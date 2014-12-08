package rmomoko.cse110bank;
import android.content.Intent;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import parse library
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;

import java.util.List;

import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.SavingAccount;
/**
 * Created by Yuxiao on 12/7/2014.
 */
public class CusSummaryActivity extends Activity {
    private TextView checkHistory;
    private TextView saveHistory;
    private User user;
    private CheckingAccount userCheckAccount;
    private SavingAccount userSaveAccount;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_summary);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        checkHistory = (TextView) findViewById(R.id.check_history);
        saveHistory = (TextView) findViewById(R.id.save_history);

        user = (User)ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username",user.getUsername());
        query.include("CheckingAccount");
        query.include("SavingAccount");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null && !parseUsers.isEmpty())
                {
                    user = (User)parseUsers.get(0);
                    userCheckAccount = user.getCheckingAccount();
                    userSaveAccount = user.getSavingAccount();
                    checkHistory.setText("" + userCheckAccount.getHistory());
                    saveHistory.setText("" + userSaveAccount.getHistory());
                }
                else
                {
                    Toast.makeText(CusSummaryActivity.this, "Fail Catch!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button backButton = (Button) findViewById(R.id.cus_summary_back);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pageChangetoCusAcInfo();
            }
        });



    }






    public void pageChangetoCusAcInfo(){
        Intent getAccountInfoScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getAccountInfoScreen, result);
        finish();
    }
    public void onBackPressed()
    {
        pageChangetoCusAcInfo();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        pageChangetoCusAcInfo();
        return true;
    }
}
