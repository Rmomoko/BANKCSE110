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

@ParseClassName("Account")
public class Account extends ParseObject{
    private String owner;
    private boolean isClosed;
    private double  balance;
   // private Time license100;


    public Account()
    {
    }



    public boolean isClosed()
    {
        return getBoolean("isClosed");
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





