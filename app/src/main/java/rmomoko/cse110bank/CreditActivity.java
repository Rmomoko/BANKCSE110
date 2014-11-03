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

import java.lang.Integer;

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
public class CreditActivity extends Activity {
    private EditText creditAmount;
    private int amount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_credit);
        creditAmount = (EditText) findViewById(R.id.credit_amount);

        Button checkingCredit = (Button) findViewById(R.id.checking_credit_button);
        checkingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                depositeCheckingAccount();
            }
        });

        Button savingCredit = (Button) findViewById(R.id.saving_credit_button);
        savingCredit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                depositeSavingAccount();
            }
        });
    }

    public void depositeCheckingAccount(){
        amount = Integer.parseInt(creditAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
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
    public void depositeSavingAccount(){
        amount = Integer.parseInt(creditAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
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
    public void pageChange() {

        Intent getScreen = new Intent(this, AccountInfo.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
}


