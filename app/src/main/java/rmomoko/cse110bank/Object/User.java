/**
 * Team Name: Orange Chicken
 *  File Name: User.java
 *  Description: Create a User object. Get CheckingAccount and SavingAccount object in Parse.
 *                      Return the information in Parse to get the information of checking/saving account.
 *
 */

package rmomoko.cse110bank.Object;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Name:           User
 *  Purpose:      To get the user's information.
 * Description:  User class extends ParseUser that User can access the information in the database.
 *                      Get if the user is a customer or teller. Get the user's checking/ saving account information.
 *
 */
@ParseClassName("_User")
public class User extends ParseUser{

    private String userID;
    private String password;


    /**
     * Name:           User
     *  Purpose:       User constructor.
     * Description:  Constructor a User object.
     */
    public User()
    {}

    /**
     * Name:           isCustomer
     *  Purpose:      Check if the user is customer or banker.
     * Description:  get the boolean value in parse.
     * Return Value:  boolean isCustomer - return true if user is customer, otherwise return false.
     *
     */
    public boolean isCustomer()
    {
        return getBoolean("isCustomer");
    }

    /**
     * Name:           getCheckingAccount
     *  Purpose:      get the Checking Account object in Parse.
     * Description:  Get all the information in the CheckingAccount object in Parse.
     * Return Value:  CheckingAccount - the object of CheckingAccount in Parse.
     *
     */
    public CheckingAccount getCheckingAccount()
    {
        return (CheckingAccount) getParseObject("CheckingAccount");
    }
    /**
     * Name:           getSavingAccount
     *  Purpose:      get the Saving Account object in Parse.
     * Description:  Get all the information in the SavingAccount object in Parse.
     * Return Value:  CheckingAccount - the object of SavingAccount in Parse.
     *
     */
    public SavingAccount getSavingAccount()
    {
        return (SavingAccount) getParseObject("SavingAccount");
    }


    /**
     * Name:           close
     *  Purpose:      close the account
     * Description:  get the two account be null
     * Return Value:  void
     *
     */
    public void close()
    {
        put("SavingAccount", null);
        put("CheckingAccount", null);
    }

}
