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
 * Created by yenhsialin on 10/25/2014.
 */
public class AccountInfo extends Activity{
    private String _ac_num, _balance, _activity;
    private TextView checkingNumber;
    private TextView savingNumber;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        checkingNumber = (TextView) findViewById(R.id.checkingNumber);
        savingNumber = (TextView) findViewById(R.id.savingNumber);
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
        account.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    checkingNumber.setText("$ " + object.getNumber("checkingAccount").toString());
                    savingNumber.setText("$ " + object.getNumber("savingAccount").toString());
                } else {
                }
            }
        });


        Button creditButton = (Button) findViewById(R.id.credit_button);
        creditButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToCredit(view);
            }
        });

        Button debitButton = (Button) findViewById(R.id.debit_button);
        debitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToDebit(view);
            }
        });




    }

    public void pageToCredit(View view) {
        Intent getScreen = new Intent(this, CreditActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    public void pageToDebit(View view) {
        Intent getScreen = new Intent(this, DebitActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
}
