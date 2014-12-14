package cse110banktest;

/**
 * Created by Yuxiao on 11/22/2014.
 */

import android.content.Intent;
import android.test.InstrumentationTestCase;

import rmomoko.cse110bank.LoginActivity;
import rmomoko.cse110bank.Object.User;



public class UserTest extends InstrumentationTestCase {

    /**
     * Name:           testGetSomeUser
     *  Purpose:      To test the user function
     * Description:  Test the function
     *
     */
    public void testGetSomeUser() throws Exception {
        Intent getScreen = new Intent("LoginActivity");
        launchActivity("rmomoko.cse110bank", LoginActivity.class, null);

        User shuaige = new User();
        shuaige.getCheckingAccount();
        assertEquals("",shuaige.getClassName());
    }
}