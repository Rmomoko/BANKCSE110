package cse110banktest;

/**
 * Created by Yuxiao on 11/22/2014.
 */

import android.test.InstrumentationTestCase;

import rmomoko.cse110bank.Object.Account;


public class depoisteTest extends InstrumentationTestCase {


    public void testGetBalance() throws Exception {


        Account test = new Account("tester", true ,0);
        test.depoiste(500);
        assertEquals(500.0, test.checkBalance());
        test.depoiste(400);
        assertEquals(900.0, test.checkBalance());
    }
}