package com.chessboard.tc_chessboard;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    DB_handler db_handler;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.tc_chessboard", appContext.getPackageName());
    }

    @Test
    public void correctLogin(){
        db_handler = new DB_handler();

        // admin credentials
        String username = "admin";
        String password = "admin";

        // Retrieve successful connection
        boolean connected = db_handler.verifyUserCredentialsFromDB(username, password);
        assertTrue("The connection was successful.", connected);
    }

    @Test
    public void incorrectLogin(){
        db_handler = new DB_handler();

        // admin credentials
        String username = "BadGuy";
        String password = "LoveChess";

        // Retrieve successful connection
        boolean connected = db_handler.verifyUserCredentialsFromDB(username, password);
        assertFalse("Wrong username or password.", connected);
    }

    @Test
    public void successfulUserRegistration(){
        db_handler = new DB_handler();

        // admin credentials
        String username = "testRegistration";
        String email    = "test@gmail.com";
        String password = "test";

        // Retrieve successful registration
        boolean connected = db_handler.registerNewUser(username, email, password);
        assertTrue("The connection was successful.", connected);
    }
}