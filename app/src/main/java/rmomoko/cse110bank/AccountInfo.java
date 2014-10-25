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
/**
 * Created by yenhsialin on 10/25/2014.
 */
public class AccountInfo extends Activity{
    private String _ac_num, _balance, _activity;
    //private View mAccountInfoView;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        // TODO: Button funtion not add yet
        Button openCheckingButton = (Button) findViewById(R.id.open_checking_button);
        Button openSavingButton = (Button) findViewById(R.id.open_saving_button);
        Button openCreditButton = (Button) findViewById(R.id.open_credit_button);
        //openCheckingButton.setOnClickListener();

        //mAccountInfoView = findViewById(R.id.ac_info);

    }

}
