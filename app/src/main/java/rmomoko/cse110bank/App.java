/**
 * Team Name: Orange Chicken
 *  File Name: App.java
 *  Description: build up the whole picture with user and object
 */

package rmomoko.cse110bank;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import rmomoko.cse110bank.Object.SavingAccount;
import rmomoko.cse110bank.Object.User;
import rmomoko.cse110bank.Object.Account;
import rmomoko.cse110bank.Object.CheckingAccount;
import rmomoko.cse110bank.Object.Customer;
import rmomoko.cse110bank.Object.Employee;
/**
 * Name:         App
 * Purpose:      Extend the regular Application
 * Description:  Extend the Application with override the function
 */
public class App extends Application{

    /**
     * Name:         OnCreate
     * Purpose:      Set up all object relationships on the onCreate function
     * Description:  Call registerSubclass to set the whole tree design
     *               Initialize the database
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        /* Build the relationship in User and account
         * Database user contains local user
         * Database object contains local account
         * Account contains checking and saving account
         * User contains customer and employee
         */
        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Account.class);
        Account.registerSubclass(CheckingAccount.class);
        Account.registerSubclass(SavingAccount.class);
        User.registerSubclass(Customer.class);
        User.registerSubclass(Employee.class);
        //connect database
        Parse.initialize(this, "dJqoRn28p66wHQsJkJKog1zaaRhP3iTDGoSDanYU", "FwLys6BrpNfLyoOWuQCD9vVhIgqYsfjv9RynGOEY");
    }


}
