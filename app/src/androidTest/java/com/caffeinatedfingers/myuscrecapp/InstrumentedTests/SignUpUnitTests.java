package com.caffeinatedfingers.myuscrecapp.InstrumentedTests;

import static org.junit.Assert.*;


import com.caffeinatedfingers.myuscrecapp.SignUp;

import org.junit.Before;
import org.junit.Test;



public class SignUpUnitTests {

    @Test
    public void invalidEmail() {
        int result= SignUp.checkData("invalid","testtest","test test","1234567890"); //no @ sign
        assertEquals(result,1);
    }
    @Test
    public void invalidPassword(){
        int result=SignUp.checkData("david@usc.edu","test","test test","1234567890");//password not long enough
        assertEquals(result,2);
    }
    @Test
    public void invalidName(){
        int result=SignUp.checkData("david@usc.edu","testtest","nospace","1234567890");//no space in name
        assertEquals(result,3);
    }
    @Test
    public void invalidID(){
        int result=SignUp.checkData("david@usc.edu","testtest","test test","1");//USC ID not long enough
        assertEquals(result,4);
    }
    @Test
    public void validInfo(){
        int result=SignUp.checkData("david@usc.edu","testtest","test test","1234567890");
        assertEquals(result,0);
    }
}