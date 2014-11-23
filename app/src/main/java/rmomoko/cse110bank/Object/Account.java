/**
 * Created by kobeguo on 2014/11/2.
 */
package rmomoko.cse110bank.Object;
import java.lang.String;
import java.sql.Time;


public class Account {
    private String owner;
    private boolean open;
    private double  balance;
   // private Time license100;


    public Account()
    {}

    public Account(String owner, boolean open, double balance){
        this.owner = owner;
        this.open = open;
        this.balance = balance;
//        this. license100.setTime(0);
    }

    public void close(){
        this.open = false;
    }

    public double depoiste(double amount){
        if(open) {
            balance += amount;
            return balance;
        }
        return -2;
    }
    public double withdraw(double amount){
        if(balance > amount && open) {
            balance -= amount;
            return balance;
        }
            return -1;

    }

    public  double checkBalance(){
        if(open)
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





