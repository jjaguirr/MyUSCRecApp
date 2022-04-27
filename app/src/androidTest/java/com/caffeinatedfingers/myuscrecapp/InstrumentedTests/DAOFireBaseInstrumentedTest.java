package com.caffeinatedfingers.myuscrecapp.InstrumentedTests;

import androidx.test.platform.app.InstrumentationRegistry;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.*;

import com.caffeinatedfingers.myuscrecapp.DAOFireBase;
import com.caffeinatedfingers.myuscrecapp.Reservation;
import com.caffeinatedfingers.myuscrecapp.TimeSlot;
import com.caffeinatedfingers.myuscrecapp.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class DAOFireBaseInstrumentedTest {
    private String testUserID;
    private String testUserName;
    private String testRecCenterID;
    private String testDate;
    private String testTime;
    private String testUID;
    private Long testCapacity;
    private User testUser;
    private TimeSlot testTimeSlot;
    private DAOFireBase daoFireBase;
    private Reservation testReservation;
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Before
    public void setUp(){
        this.testUserID = "00000000";
        this.testUserName = "Test User";
        this.testRecCenterID = "Test Rec Center";
        this.testUID="uid";
        this.testDate = "TODAY";
        this.testTime = "11-12 PM";
        this.testCapacity = 50L;
        this.testUser = new User(testUserID, testUserName,testUID);
        this.testTimeSlot = new TimeSlot(50L, testRecCenterID, testTime, testDate);
        this.daoFireBase = new DAOFireBase();
        this.testReservation = new Reservation(testUser, testTimeSlot);
    }

    @Test
    public void testAddUser() {
        try {
            daoFireBase.addUser(testTimeSlot, testUser, appContext);
            Thread.sleep(500);
            Task<DataSnapshot> taskReservation = daoFireBase.getReservationsQuery(testUserID).get();
            Task<DataSnapshot> taskTimeSlot =daoFireBase.getTimeSlotRegisteredQuery(testTimeSlot).get();
            Thread.sleep(1000);
            DataSnapshot DSReservations= taskReservation.getResult();
            DataSnapshot DSTimeSlots= taskTimeSlot.getResult();
            assertTrue(DSReservations.child(testReservation.id).exists());
            assertTrue(DSTimeSlots.child(testUserID).exists());
        } catch (InterruptedException e) {
            fail();
        }
        daoFireBase.removeUser(testTimeSlot, testUser, appContext);
    }
    @Test
    public void testRemoveUser() {
        try {
            daoFireBase.addUser(testTimeSlot, testUser, appContext);
            Thread.sleep(500);
            daoFireBase.removeUser(testTimeSlot, testUser, appContext);
            Thread.sleep(500);
            Task<DataSnapshot> taskReservation = daoFireBase.getReservationsQuery(testUserID).get();
            Task<DataSnapshot> taskTimeSlot =daoFireBase.getTimeSlotRegisteredQuery(testTimeSlot).get();
            Thread.sleep(500);
            DataSnapshot DSReservations= taskReservation.getResult();
            DataSnapshot DSTimeSlots= taskTimeSlot.getResult();
            assertFalse(DSReservations.child(testReservation.id).exists());
            assertFalse(DSTimeSlots.child(testUserID).exists());
        } catch (InterruptedException e) {
            fail();
        }
    }
    @Test
    public void testRemindUser() {
        try {
            daoFireBase.remindUser(testTimeSlot, testUser, appContext);
            Thread.sleep(500);
            Task<DataSnapshot> taskTimeSlot =daoFireBase.getTimeSlotWaitListQuery(testTimeSlot).get();
            Thread.sleep(500);
            DataSnapshot DSTimeSlots= taskTimeSlot.getResult();
            assertTrue(DSTimeSlots.child(testUserID).exists());
        } catch (InterruptedException e) {
            fail();
        }
        daoFireBase.unRemindUser(testTimeSlot, testUser, appContext);
    }
    @Test
    public void testUnRemindUser() {
        try {
            daoFireBase.remindUser(testTimeSlot, testUser, appContext);
            Thread.sleep(500);
            daoFireBase.unRemindUser(testTimeSlot, testUser, appContext);
            Thread.sleep(500);
            Task<DataSnapshot> taskTimeSlot =daoFireBase.getTimeSlotWaitListQuery(testTimeSlot).get();
            Thread.sleep(500);
            DataSnapshot DSTimeSlots= taskTimeSlot.getResult();
            assertFalse(DSTimeSlots.child(testUserID).exists());
        } catch (InterruptedException e) {
            fail();
        }
    }

}