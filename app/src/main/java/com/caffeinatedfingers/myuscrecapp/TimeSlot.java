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

public class TimeSlot {
    String id;
    int capacity;
    RecCenter recCenter;
    String time; //or Timeobject
    int usersCount;

    boolean thisUserReserved;

    public TimeSlot(int capacity, String recCenter, String time) {
        this.capacity = capacity;
        this.id = time;
        this.time = time;
    }
    public int getRemaining(){
        return this.capacity-this.usersCount;
    }

    public Boolean isAvailable(){
        return this.usersCount < this.capacity;
    }

    public boolean isReserved(){
        return thisUserReserved;
    }
}
