package com.caffeinatedfingers.myuscrecapp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.Time;

public class TimeSlotTests {
    long capacity = 5;
    @Test
    public void getRemaining() {
        TimeSlot testTS = new TimeSlot(capacity, "GYM TEST", "TIME TEST", "DATE TEST");
        assertEquals(capacity, (long) testTS.getRemaining());

        for(int i = 0; i<capacity ; i++){
            testTS.notifyAddedUser();
        }
        assertEquals(0, (long) testTS.getRemaining());

        testTS.notifyRemovedUser();

        assertEquals(1, (long) testTS.getRemaining());
    }

    @Test
    public void isAvailable() {
        TimeSlot testTS = new TimeSlot(capacity, "GYM TEST", "TIME TEST", "DATE TEST");
        assertTrue(testTS.isAvailable());

        for(int i = 0; i<capacity ; i++){
            testTS.notifyAddedUser();
        }
        assertFalse(testTS.isAvailable());

        testTS.notifyRemovedUser();

        assertTrue(testTS.isAvailable());
    }

    @Test
    public void getViewType() {
        TimeSlot testTS = new TimeSlot(capacity, "GYM TEST", "TIME TEST", "DATE TEST");
        testTS.setThisUserReserved(false);
        testTS.setThisUserInWaitlist(false);

        //available 0
        assertEquals(0,testTS.getViewType());

        //unavailable 2
        for(int i = 0; i<capacity ; i++){
            testTS.notifyAddedUser();
        }
        assertFalse(testTS.isAvailable());
        assertEquals(2, testTS.getViewType());

        //un remind mode 4
        testTS.setThisUserInWaitlist(true);
        assertEquals(3, testTS.getViewType());

        testTS.setThisUserInWaitlist(false);

        //cancel mode(after booking) 1
        testTS.notifyRemovedUser();
        testTS.setThisUserReserved(true);
        assertEquals(1, testTS.getViewType());
    }

    @Test
    public void getViewTypeEdge() {
        TimeSlot testTS = new TimeSlot(capacity, "GYM TEST", "TIME TEST", "DATE TEST");

        //unavailable 2
        for(int i = 0; i<capacity ; i++){
            testTS.notifyAddedUser();
        }
        assertEquals(2, testTS.getViewType());

        //un remind mode 4 and reserved, should return cancel mode 1
        testTS.setThisUserInWaitlist(true);
        testTS.setThisUserReserved(true);
        assertEquals(1, testTS.getViewType());

        //should return to unremind
        testTS.thisUserReserved = false;
        assertEquals(3, testTS.getViewType());
    }
}