package rmomoko.cse110bank.Object;

import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Yuxiao on 11/22/2014.
 */
public class User{

    String username;

    public User()
    {
        username = "";
    }

    public void getCurUser()
    {
        ParseUser current= ParseUser.getCurrentUser();
        if(current != null)
        {
            username = current.getUsername();
        }
    }

    public void getSomeUser(String email)
    {
        System.out.println("1");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
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
        });
    }

    public String getUserName()
    {
        return username;
    }



}
