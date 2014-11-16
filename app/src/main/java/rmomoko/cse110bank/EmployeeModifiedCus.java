package rmomoko.cse110bank;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseException;

/**
 * Created by yenhsialin on 10/25/2014.
 */
public class EmployeeModifiedCus extends Activity{
    private TextView checkingNumber;
    private TextView savingNumber;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_change_account);

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
