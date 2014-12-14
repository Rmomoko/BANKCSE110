package cse110banktest;

/**
 * Created by Yuxiao on 11/22/2014.
 */

import android.test.InstrumentationTestCase;

import rmomoko.cse110bank.Object.Account;


public class checkbanlanceTest extends InstrumentationTestCase {

    /**
     * Name:           testGetBalance
     *  Purpose:      To test the getBalance function
     * Description:  Test the function
     *
     */
    public void testGetBalance() throws Exception {


    Account test = new Account();
        test.put("Balance", 0);
    assertEquals(0.0, test.checkBalance());
    Account test1 = new Account();
        test1.put("Balance", 100);
    assertEquals(100.0, test1.checkBalance());
    Account test2 = new Account();
        test2.put("Balance", 900);
    assertEquals(900.0, test2.checkBalance());
    }
}