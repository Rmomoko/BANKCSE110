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
 * Created by Yuxiao on 11/9/2014.
 */
public class CustomerAccountActivity extends Activity{

    private TextView checkingNumber;
    private TextView savingNumber;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);
        checkingNumber = (TextView) findViewById(R.id.cus_check_number);
        savingNumber = (TextView) findViewById(R.id.cus_save_number);

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



    }

}
