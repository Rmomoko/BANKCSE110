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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.parse.ParseQuery;
/**
 * Created by Yuxiao on 11/2/2014.
 */
public class DebitActivity extends Activity{
    private EditText debitAmount;
    private int amount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_debit);
        debitAmount = (EditText) findViewById(R.id.debit_amount);

        Button checkingCredit = (Button) findViewById(R.id.checking_debit_button);
        checkingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                withdrawalCheckingAccount();
            }
        });

        Button savingCredit = (Button) findViewById(R.id.saving_debit_button);
        savingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                withdrawalSavingAccount();
            }
        });
    }

    public void withdrawalCheckingAccount(){
        amount = Integer.parseInt(debitAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
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
                } else {
                }
            }
        });
    }
    public void withdrawalSavingAccount(){
        amount = Integer.parseInt(debitAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
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
                } else {
                }
            }
        });
    }
    public void pageChange() {

        Intent getScreen = new Intent(this, AccountInfo.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
}
