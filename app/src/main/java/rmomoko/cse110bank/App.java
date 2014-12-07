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
 * Created by shengjie on 2014/11/30.
 */
public class App extends Application{

    @Override
    public void onCreate()
    {
        super.onCreate();
        ParseUser.registerSubclass(User.class);
        ParseObject.registerSubclass(Account.class);
        Account.registerSubclass(CheckingAccount.class);
        Account.registerSubclass(SavingAccount.class);
        User.registerSubclass(Customer.class);
        User.registerSubclass(Employee.class);
        Parse.initialize(this, "dJqoRn28p66wHQsJkJKog1zaaRhP3iTDGoSDanYU", "FwLys6BrpNfLyoOWuQCD9vVhIgqYsfjv9RynGOEY");
    }


}
