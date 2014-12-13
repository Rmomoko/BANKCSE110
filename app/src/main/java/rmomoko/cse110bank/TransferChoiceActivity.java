/**
 * Team Name: Orange Chicken
 *  File Name: TransferChoiceActivity.java
 *  Description: This class defines different transitions between pages evoked by the user.
 */

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
 * Name:          TransferChoiceActivity
 * Purpose:       Be able to switch between pages
 * Description:   This class extends activity and creates many functions in order to be able to
 *                switch between pages corresponding to the user interaction.
 */
public class TransferChoiceActivity extends Activity {

    /**
     * Name:           OnCreate
     * Purpose:        Create the layout transfer choices page
     * Description:    Create the UI of transfer choices page, and link each button with its
     *                 corresponding storage of data.
     */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_choice);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // create button for transfer money
        Button transferMyOwnButton = (Button) findViewById(R.id.transfer_my_own_button);
        transferMyOwnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToMyOwn();
            }
        });

        // create button for transfer money
        Button transferOtherButton = (Button) findViewById(R.id.transfer_to_someone_button);
        transferOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageToSomeone();
            }
        });

    }
    /**
     * Name:          pageToMyOwn
     * Purpose:     Switch to another page.
     * Description:  In this case we switch to transfer between user's account page.
     */
    public void pageToMyOwn() {
        Intent getScreen = new Intent(this, TransferBetweenMyAccount.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
     * Name:          pageToSomeone
     * Purpose:     Switch to another page.
     * Description:  In this case we switch to transfer another user's account page.
     */
    public void pageToSomeone() {
        Intent getScreen = new Intent(this, TransferToSomeoneAccount.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
     * Name:          pageToAcInfo
     * Purpose:     Switch to another page.
     * Description:  In this case we switch to customer's account activity page.
     */
    public void pageToAcInfo() {
        Intent getScreen = new Intent(this, CustomerAccountActivity.class);
        final int result = 1;
        startActivityForResult(getScreen, result);
        finish();
    }
    /**
     * Name:          onBackPressed
     * Purpose:       Go back to the previous page
     * Description:   Go back to the recent previous page.
     */
    public void onBackPressed()
    {
        pageToAcInfo();
    }
    /**
     * Name:          onOptionsItemSelected
     * Purpose:     Enforce the customer to logout and go to login page.
     * Description:  Enforce the customer to logout and go to login page.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        pageToAcInfo();
        return true;
    }
}
