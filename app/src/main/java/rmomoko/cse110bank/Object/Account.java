/**
 * Team Name: Orange Chicken
 *  File Name: Account.java
 *  Description:  The base class of checking and saving account. It defines the common functions of
 *                checking and saving account.Account provides the functionality of transferring
 *                money in and out.
 *
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


/**
 * Name:            Account
 * Purpose:        The account class allows to create a account object, check if account is closed,
 *                 close the account, find the amount average of recent 30 days(which is used to
 *                 determine the interest and penalty). It also provides the functionality of
 *                 getting account balance from database,setting this account's isClosed value
 *                 to false,depositing  and withdrawing money.
 * Description:    The base class of checking and saving account. It defines the common functions of
 *                checking and saving account.Account provides the functionality of transferring
 *                money in and out.
 *
 *
 */
@ParseClassName("Account")
public class Account extends ParseObject{

    /* Variable */
    private String owner;
    private boolean isClosed;           // boolean value to check account is closed or not
    private double  balance;
    // private Time license100;
    private static final long THIRTYDAY = 2592000000L;
    private static final long DAY = 86400000L;
    private static final double LIMIT = 10000.0;

    /**
     * Name:           Account
     * Purpose:       Create a Account object.
     */
    public Account()
    {
    }

    /**
     * Name:           isClosed
     * Purpose:       Check account closed or not.
     * Description: Get the boolean value in the data and return that boolean value.
     * Return Value: boolean isClosed - return true if the account is closed, otherwise return false.
     *
     */
    public boolean isClosed()
    {
        return getBoolean("isClosed");
    }

    /**
     * Name:           closeAccount
     * Purpose:       Process close account activity.
     * Description:  change the value true into database in isClosed object.
     *
     * Return Value: Void.
     *
     */
    public void closeAccount()
    {
        put("isClosed", true);
        saveInBackground();
    }

    /**
     * Name:           average
     * Purpose:        calculate the average balance of recent 30 days
     * Description:     get the history of current account, read the last transition
     *                  history of each recent 30 days and get the balance at the end of that day
     *                  and do the average
     * Return Value:   the amount average of recent 30 days
     *
     */
    public double average()
    {
        double totalAmount = 0;

        //get the date of today
        Date today = getUpdatedAt();

        //date of current history
        String currentTime = "" + (today.getYear()+ 1900) + "/" + (today.getMonth()+1) + "/" + today.getDate();

        //date of last history
        String lastTime;

        today = new Date((today.getYear()+ 1900),
                (today.getMonth()+1),
                today.getDate());


       //get history and split them by new line character
        String[] history = getString("history").split("\n");


        for(int i = 0; i < history.length; i++)
        {
            String[] element = history[i].split(" ");

            //check whether this history is the last his tory of that day, if yes, set that
            //day as the current time, and last time as the time worked on before
            if(!element[0].equals(currentTime))
            {
                lastTime = currentTime;
                currentTime = element[0];
            }
            else continue;

            //then convert the date of that day to Date time
            String[] time = currentTime.split("/");
            Date currentDate = new Date(Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]));
            time = lastTime.split("/");
            Date lastDate = new Date(Integer.parseInt(time[0]),
                    Integer.parseInt(time[1]),
                    Integer.parseInt(time[2]));

            //if this date is more than 30 days ago, then set the balance of the first date of
            //current 30 days period as the balance of current history, and times the time between
            //this day and last transaction day
            if(currentDate.getTime() + THIRTYDAY <= today.getTime())
            {
                totalAmount += (lastDate.getTime() - today.getTime() + THIRTYDAY)/DAY * Double.parseDouble(element[1]);
            }

            //if this date is within 30 days, let the balance of that day times the time between
            //this day and last transaction day
            else
            {
                totalAmount += (lastDate.getTime() - currentDate.getTime())/DAY * Double.parseDouble(element[1]);
            }
        }

        //do th average
        return totalAmount / 30;
    }

    /**
     * Name:           getBalance
     * Purpose:       get account balance from database.
     * Description:  get the double value of balance from parse.com then return the value.
     * Return Value:  double balance - balance in the account.
     *
     */
    public double getBalance()
    {
        return getNumber("balance").doubleValue();
    }

    /**
     * Name:           getHistory
     *  Purpose:       get account history from database.
     * Description:  get the string value of history from parse.com then return the value.
     * Return Value:  String history - history stored in parse.com
     *
     */
    public String getHistory()
    {
        return getString("history");
    }

    /**
     * Name:           close
     * Purpose:       make the account isClosed value to false.
     * Description:  Set this account's isClosed value to false.
     * Return Value:  Void
     *
     */
    public void close(){
        this.isClosed = false;
    }

    /**
     * Name:           deposite
     *  Purpose:       deposit money into account
     * Description:  Check if the account is closed or not. If closed, return account closed sign we set it to be -2.
     *                       If the account did not close, add the amount to the balance then return the new balance.
     * Return Value:  double balance - new balance after processing the deposit.
     *
     */
    public double depoiste(double amount){
        if(isClosed) {
            balance += amount;
            return balance;
        }
        return -2;
    }

    /**
     * Name:            withdraw
     *  Purpose:       withdraw money from account
     * Description:  Check if the account is closed or not. If closed, return account closed sign we set it to be -1.
     *                      If the account did not close and the withdraw amount is within the balance, then
     *                      process the transaction. Return the new balance after withdraw.
     * Return Value:  double balance - new balance after processing the deposit.
     *
     */
    public double withdraw(double amount){
        // check the money is enough in the account to withdraw and the account is not closed.
        if(balance > amount && isClosed) {
            balance -= amount;
            return balance;
        }

        return -1;
    }

    /**
     * Name:           checkBalance
     *  Purpose:       get balance for account information.
     * Description:  Check if the account is closed or not. If closed, return account closed sign we set it to be -2.
     *                      If the account did not close return the balance.
     * Return Value:  double balance - value of account balance.
     *
     */
    public  double checkBalance(){
        if(isClosed)
            return balance;
        else
            return -2;
    }

    //*****TODO******
    public void penalty(){
        if(balance < 100){

        }
    }
}





