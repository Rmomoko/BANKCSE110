package cse110banktest;

/**
 * Created by Yuxiao on 11/22/2014.
 */

import android.test.InstrumentationTestCase;
import rmomoko.cse110bank.Object.Account;


public class withdrawTest extends InstrumentationTestCase {

    /**
     * Name:           testGetBalance
     *  Purpose:      To test the credit function
     * Description:  Test the function
     *
     */
    public void testGetBalance() throws Exception {


       Account test = new Account();
       test.withdraw(500);
       assertEquals(500.0, test.checkBalance());
       test.withdraw(400);
       assertEquals(100.0, test.checkBalance());
    }
}