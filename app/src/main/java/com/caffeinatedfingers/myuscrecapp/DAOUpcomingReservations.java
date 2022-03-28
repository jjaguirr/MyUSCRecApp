package com.caffeinatedfingers.myuscrecapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DAOUpcomingReservations {
    private final DatabaseReference databaseReference;
    public DAOUpcomingReservations() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.databaseReference = db.getReference("reservations");
    }

    public void removeUser(TimeSlot ts, User user, Context context){
        this.databaseReference.child(ts.id).child(user.id).removeValue().addOnSuccessListener(suc->{
            ts.notifyRemovedUser();
            Toast.makeText(context, "Successfully cancelled reservation.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail->{
            Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    //Returns a db ordered reference of the timeslots
    public Query get(String key){
        if (key==null) return databaseReference.orderByKey().limitToFirst(10);
        return databaseReference.orderByKey().startAfter(key).limitToFirst(10);
    }
}
