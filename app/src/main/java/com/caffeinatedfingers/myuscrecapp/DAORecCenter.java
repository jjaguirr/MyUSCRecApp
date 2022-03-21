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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAORecCenter {
    private final DatabaseReference databaseReference;
    public DAORecCenter(RecCenter recCenter) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.databaseReference = db.getReference("reservations/"
                +recCenter.id);
    }

    public void addUser(TimeSlot ts, User user){
        this.databaseReference.child(ts.id).child(user.id).setValue(user.userName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    //Returns a db ordered reference of the timeslots
    public Query get(String key){
        if (key==null) return databaseReference.orderByKey().limitToFirst(10);
        return databaseReference.orderByKey().startAfter(key).limitToFirst(10);
    }
}
