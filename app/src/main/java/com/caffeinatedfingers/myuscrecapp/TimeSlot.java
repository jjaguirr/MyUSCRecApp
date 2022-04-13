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

public class TimeSlot implements Serializable {
    String id;
    Long capacity;
    String recCenter;
    String date;
    String time; //or Timeobject
    int usersCount;

    boolean thisUserReserved;
    boolean thisUserInWaitlist;

    public TimeSlot(Long capacity, String recCenter, String time, String date) {
        this.capacity = capacity;
        this.id = time;
        this.recCenter = recCenter;
        this.time = time;
        this.date = date;
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

    public int getViewType(){
        //TOCANCEL
        if (isReserved()) return 1;
        //TOREMIND
        else if (!isAvailable() && !thisUserInWaitlist) return 2;
        //TO UNREMIND
        else if (thisUserInWaitlist) return 4 ;
        //TORESERVE
        else return (0);
    }
    public void notifyRemovedUser() {
        usersCount--;
        thisUserReserved = false;
    }

    public void notifyAddedUser() {
        usersCount++;
        thisUserReserved = true;
    }
}
