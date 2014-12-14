/**
 * Team Name: Orange Chicken
 *  File Name: Customer.java
 *  Description:   Create a Customer type to identify customer or employee
 *
 */

package rmomoko.cse110bank.Object;

import com.parse.ParseClassName;

/**
 * Name:            Customer
 * Description:   Customer  object that extends User to obtain User information.
 *
 */
@ParseClassName("Customer")
public class Customer extends User{
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String email;
    private String phone;

    private SavingAccount savingAccount;
    private CheckingAccount checkingAccount;



    /**
     * Name:           editFirstName
     *  Purpose:      edit the first name
     * Description:   change the first name of the user
     * Return Value:  void
     *
     */
    public void editFirstName(String firstN)
    {
        put("FirstName", firstN);
    }


    /**
     * Name:           editLastName
     *  Purpose:      edit the last name
     * Description:   change the last name of the user
     * Return Value:  void
     *
     */

    public void editLastName(String lastN)
    {
        put("LastName", lastN);
    }


    /**
     * Name:           editGender
     *  Purpose:      edit the gender
     * Description:   change the gender of the user
     * Return Value:  void
     *
     */
    public void editGender(String g)
    {
        put("Gender", g);
    }

    /**
     * Name:           editAddress
     *  Purpose:      edit the address
     * Description:   change the address of the user
     * Return Value:  void
     *
     */
    public void editAddress(String a)
    {
        put("Address", a);
    }

    /**
     * Name:           transfer
     *  Purpose:      transfer money
     * Description:   transfer money from the first given account to the second
     * Return Value:  double  - see the error
     *
     */
    public double transfer(Account from, Account to, double amount)
    {
        double diff = from.getBalance() - to.getBalance() + amount;
        return 0;
    }


}
