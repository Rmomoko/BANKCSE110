/**
 * Team Name: Orange Chicken
 *  File Name: Employee.java
 *  Description: Create a Employee r type to identify customer or employee
 *
 */
package rmomoko.cse110bank.Object;

import com.parse.ParseClassName;

/**
 * Name:            Employee
 * Description:  Employee  object that extends User to obtain User information.
 *
 */
@ParseClassName("Employee")
public class Employee extends User{

    /**
     * Name:           getCus
     *  Purpose:      get the current Customer
     * Description:   return the current Customer to check
     * Return Value:  double  - see the error
     *
     */
   public Customer getCus(String email)
   {
       return (Customer)getCurrentUser();
   }

    /**
     * Name:           Debit
     *  Purpose:      debit money
     * Description:   debit money from the first given account
     * Return Value:  double  - see the error
     *
     */
    public double Debit(Account account, double amount)
    {
        double diff = account.checkBalance() + amount;
        return 0;
    }

    /**
     * Name:           Credit
     *  Purpose:      credit money
     * Description:   credit money from the first given account
     * Return Value:  double  - see the error
     *
     */
    public double Credit(Account account, double amount)
    {
        double diff = account.checkBalance() - amount;
        return 0;
    }

    /**
     * Name:           transfer
     *  Purpose:      transfer money
     * Description:   transfer money from the first given account to the second
     * Return Value:  double  - see the error
     *
     */
    public double transfer(String email, double amount)
    {
        Account temp;
        double diff = amount;
        return 0;
    }

}
