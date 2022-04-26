package com.caffeinatedfingers.myuscrecapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot implements Serializable {
    public String getId() {
        return id;
    }

    public String id;
    public Long capacity;
    public String recCenter;
    public String date;
    public String time; //or Timeobject
    public int usersCount;
    public transient boolean thisUserReserved;
    public transient boolean thisUserInWaitlist;

    public TimeSlot(){

    }
    public TimeSlot(Long capacity, String recCenter, String time, String date) {
        this.capacity = capacity;
        this.id = time;
        this.recCenter = recCenter;
        this.time = time;
        this.date = date;
        this.thisUserReserved = false;
        this.thisUserInWaitlist = false;
    }

    public Long getRemaining(){
        return this.capacity-this.usersCount;
    }
    public Boolean isAvailable(){
        return this.usersCount < this.capacity;
    }

    public void setThisUserReserved(boolean thisUserReserved) {
        this.thisUserReserved = thisUserReserved;
    }
    public void setThisUserInWaitlist(boolean thisUserInWaitlist){
        this.thisUserInWaitlist = thisUserInWaitlist;
    }

    public boolean isReserved(){
        return thisUserReserved;
    }

    /**
     * @return 0: RESERVE VIEW (BOOK BUTTON)
     *         1: UNAVAILABLE (REMIND ME BUTTON)
     *         2: RESERVED (CANCEL BUTTON)
     *         3:
     */
    public int getViewType(){
        //TOCANCEL
        if (isReserved()) return 1;
        //TOREMIND
        else if (!isAvailable() && !thisUserInWaitlist) return 2;
        //TO UNREMIND
        else if (thisUserInWaitlist) return 3 ;
        //TORESERVE
        else return (0);
    }
    public void notifyRemovedUser() {
        usersCount--;
    }

    public void notifyAddedUser() {
        usersCount++;
    }
}
