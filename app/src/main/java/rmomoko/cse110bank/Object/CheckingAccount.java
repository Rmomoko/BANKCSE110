/**
 * Team Name: Orange Chicken
 *  File Name: CheckingAccount.java
 *  Description:  Create a CheckingAccount type to identify checking and saving account
 *
 */
package rmomoko.cse110bank.Object;

import com.parse.ParseClassName;

import java.util.Date;


/**
 * Name:            CheckingAccount
 * Description:   CheckingAccount  object that extends Account to do the Account activity.
 *
 */
@ParseClassName("CheckingAccount")
public class CheckingAccount extends Account {
    private double interest;
    private static final long THIRTYDAY = 2592000000L;
    private static final long DAY = 86400000L;
    private static final double LIMIT = 10000.0;

    /**
     * Name:           calculateInterest
     *  Purpose:      To find the account's interest.
     * Description:  find the average first then return the interest.
     *
     */
    public double calculateInterest()
    {
        double bal = this.getBalance();
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

}
