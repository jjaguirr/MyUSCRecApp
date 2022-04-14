package com.caffeinatedfingers.myuscrecapp.InstrumentedTests;

import static org.junit.Assert.*;


import android.content.Context;
import android.os.Looper;

import com.caffeinatedfingers.myuscrecapp.LogIn;

import org.junit.Before;
import org.junit.Test;


public class LoginUnitTests {



    @Test
    public void invalidEmail(){
        int result= LogIn.checkFields("test","password");
        assertEquals(result,1);
    }
    @Test
    public void invalidPassword(){
        int result=LogIn.checkFields("test@test.com","test");
        assertEquals(result,2);
    }

    @Test
    public void validInfo() {
        int result=LogIn.checkFields("email@email.com","password");
        assertEquals(result,0);
    }
}