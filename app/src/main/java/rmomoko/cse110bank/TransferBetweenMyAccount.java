package rmomoko.cse110bank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Yuxiao on 11/16/2014.
 */
public class TransferBetweenMyAccount extends Activity {
    private EditText transferAmount;
    private int amount;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_my_own);
        transferAmount = (EditText) findViewById(R.id.transfer_my_own_amount);



        Button fromSaToCkButton = (Button) findViewById(R.id.transfer_saving_to_checking);
        fromSaToCkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transferAmount.getText().toString().isEmpty())
                {
                   transferFromSaToCk();
                }
                else{
                    transferAmount.setError("You must input a number");
                    transferAmount.requestFocus();
                }

            }
        });

        Button fromCkToSaButton = (Button) findViewById(R.id.transfer_checking_to_saving);
        fromCkToSaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!transferAmount.getText().toString().isEmpty())
                {
                    transferFromCkToSa();
                }
                else{
                    transferAmount.setError("You must input a number");
                    transferAmount.requestFocus();
                }
            }
        });
    }

    public void transferFromSaToCk(){
        amount = Integer.parseInt(transferAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
        account.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int currentSaving = object.getNumber("savingAccount").intValue();
                    int currentChecking =  object.getNumber("checkingAccount").intValue();
                    if(amount > currentSaving)
                    {
                        transferAmount.setError("Not enough money");
                    }
                    else{
                        object.put("checkingAccount", currentChecking + amount);
                        object.put("savingAccount", currentSaving - amount);
                        object.saveInBackground();
                        Toast.makeText(TransferBetweenMyAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                } else {
                }
            }
        });
    }

    public void transferFromCkToSa(){
        amount = Integer.parseInt(transferAmount.getText().toString());
        ParseUser user = ParseUser.getCurrentUser();
        ParseObject account = user.getParseObject("Account");
        account.fetchInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int currentChecking = object.getNumber("checkingAccount").intValue();
                    int currentSaving = object.getNumber("savingAccount").intValue();
                    if(amount > currentChecking)
                    {
                        transferAmount.setError("Not enough money");
                    }
                    else{
                        object.put("checkingAccount", currentChecking - amount);
                        object.put("savingAccount", currentSaving + amount);
                        object.saveInBackground();
                        Toast.makeText(TransferBetweenMyAccount.this, "Successful transfer!", Toast.LENGTH_SHORT).show();
                        pageChange();
                    }
                } else {
                }
            }
        });
    }





    public void pageChange() {
        Intent getScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
}
