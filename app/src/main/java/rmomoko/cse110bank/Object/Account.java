/**
 * Created by kobeguo on 2014/11/2.
 */
package rmomoko.cse110bank.Object;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.Parse;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.lang.String;
import java.sql.Time;
import java.util.Date;

@ParseClassName("Account")
public class Account extends ParseObject{
    private String owner;
    private boolean isClosed;
    private double  balance;
   // private Time license100;
    private static final long THIRTYDAY = 2592000000L;
    private static final long DAY = 86400000L;
    private static final double LIMIT = 10000.0;

    public Account()
    {
    }



    public boolean isClosed()
    {
        return getBoolean("isClosed");
    }



    public void closeAccount()
    {
        put("isClosed", true);
        saveInBackground();
    }


    public double average()
    {
        double totalAmount = 0;
        Date today = getUpdatedAt();
        String currentTime = "" + (today.getYear()+ 1900) + "/" + (today.getMonth()+1) + "/" + today.getDate();
        String lastTime;
        today = new Date((today.getYear()+ 1900),
                (today.getMonth()+1),
                today.getDate());
        String[] history = getString("history").split("\n");

        for(int i = 0; i < history.length; i++)
        {
            String[] element = history[i].split(" ");
            if(!element[0].equals(currentTime))
            {
                lastTime = currentTime;
                currentTime = element[0];
            }
            else continue;
            String[] time = currentTime.split("/");
            Date currentDate = new Date(Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]));
            time = lastTime.split("/");
            Date lastDate = new Date(Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]));
            if(currentDate.getTime() + THIRTYDAY <= today.getTime())
            {
                totalAmount += (lastDate.getTime() - today.getTime() + THIRTYDAY)/DAY * Double.parseDouble(element[1]);
            }
            else
            {
                totalAmount += (lastDate.getTime() - currentDate.getTime())/DAY * Double.parseDouble(element[1]);
            }
        }
        return totalAmount / 30;
    }

    public double getBalance()
    {
        return getNumber("balance").doubleValue();
    }

    public String getHistory()
    {
        return getString("history");
    }

    public void close(){
        this.isClosed = false;
    }

    public double depoiste(double amount){
        if(isClosed) {
            balance += amount;
            return balance;
        }
        return -2;
    }
    public double withdraw(double amount){
        if(balance > amount && isClosed) {
            balance -= amount;
            return balance;
        }
            return -1;

    }

    public  double checkBalance(){
        if(isClosed)
            return balance;
        else
            return -2;
    }

    //big pa pa pa说这个先不写。。。。。。
    public void penalty(){
        if(balance < 100){

        }
    }


}





