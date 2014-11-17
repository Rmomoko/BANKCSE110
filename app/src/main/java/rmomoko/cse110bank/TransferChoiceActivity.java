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
 * Created by Yuxiao on 11/16/2014.
 */
public class TransferChoiceActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_choice);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Button transferMyOwnButton = (Button) findViewById(R.id.transfer_my_own_button);
        transferMyOwnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToMyOwn();
            }
        });

        Button transferOtherButton = (Button) findViewById(R.id.transfer_to_someone_button);
        transferOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToSomeone();
            }
        });

    }
    public void pageToMyOwn() {
        Intent getScreen = new Intent(this, TransferBetweenMyAccount.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void pageToSomeone() {
        Intent getScreen = new Intent(this, TransferToSomeoneAccount.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void pageToAcInfo() {
        Intent getScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }

    public void onBackPressed()
    {
        pageToAcInfo();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        pageToAcInfo();
        return true;
    }
}
