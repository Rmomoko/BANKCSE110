package cse110banktest;

/**
 * Created by Yuxiao on 11/22/2014.
 */

import android.test.InstrumentationTestCase;

import rmomoko.cse110bank.Object.Account;


public class depoisteTest extends InstrumentationTestCase {

    /**
     * Name:           testGetBalance
     *  Purpose:      To test the deposite function
     * Description:  Test the function
     *
     */
    public void testGetBalance() throws Exception {


        Account test = new Account();
        test.depoiste(500);
        assertEquals(500.0, test.checkBalance());
        test.depoiste(400);
        assertEquals(900.0, test.checkBalance());
    }
}