package com.caffeinatedfingers.myuscrecapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAOFireBase {
    private final DatabaseReference databaseReference, databaseReferenceReservations;
    public DAOFireBase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.databaseReference = db.getReference("timeslots");
        this.databaseReferenceReservations = db.getReference("reservations");
    }

    public void addUser(TimeSlot ts, User user, Context context){
        Reservation reservation = new Reservation(user.id, ts.time, ts.recCenter, ts.capacity, ts.date);
        this.databaseReference.child(ts.recCenter).child(ts.date)
                .child(ts.id).child("Registered").child(user.id).setValue(user.userName)
                .addOnSuccessListener(suc->{
                    ts.notifyAddedUser();
                    this.databaseReferenceReservations.child(user.id).child(reservation.id).setValue(reservation);
                    Toast.makeText(context, "Successfully booked reservation.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(fail->{
                    Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    public void removeUser(TimeSlot ts, User user, Context context){
        Reservation reservation = new Reservation(user.id, ts.time, ts.recCenter, ts.capacity, ts.date);
        this.databaseReference.child(ts.id).child("Registered").child(user.id)
                .removeValue().addOnSuccessListener(suc->{
            ts.notifyRemovedUser();
            this.databaseReferenceReservations.child(user.id).child(reservation.id).removeValue();
            Toast.makeText(context, "Successfully cancelled reservation.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail->{
            Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    //@TODO: Implement notifications. Create wait-list in FireBase Database per each Timeslot.
    public void remindUser(TimeSlot ts, User user, Context context){
        //Toast.makeText(context, "Successfully added to wait-list", Toast.LENGTH_SHORT).show();
        //notifications
    }

    //Returns a db ordered reference of the timeslots
    public Query getTimeSlots(String recCenter, String date){
        return databaseReference.child(recCenter).child(date).orderByKey();
    }

    public int getTimeSlotCount(TimeSlot timeSlot){
        final int[] count = new int[1];
        databaseReference.child(timeSlot.recCenter).child(timeSlot.date).child(timeSlot.id)
                .child("Registered").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                count[0] = (int) dataSnapshot.getChildrenCount();
            }
        });
        return count[0];
    }

    public Query getReservations(String userID){
        return databaseReferenceReservations.child(userID).orderByKey();
    }
}
