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
 * Created by Yuxiao on 11/22/2014.
 */
@ParseClassName("_User")
public class User extends ParseUser{


    public User()
    {}

    public boolean isCustomer()
    {
        return getBoolean("isCustomer");
    }

    public Account getAccount()
    {
        return (Account) getParseObject("Account");
    }

    public void getSomeUser(String email)
    {
       /* ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("email",email);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                System.out.println("2");
                if(e == null)
                {
                    System.out.println("3");
                    if(!parseUsers.isEmpty()) {
                        username = parseUsers.get(0).getUsername();
                        System.out.println("4");
                        System.out.print(username);
                    }
                }
            }
        });*/
    }




}
