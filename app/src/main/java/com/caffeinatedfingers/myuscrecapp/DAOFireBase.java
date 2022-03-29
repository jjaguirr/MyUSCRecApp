package com.caffeinatedfingers.myuscrecapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DAOFireBase {
    private final DatabaseReference databaseReference, databaseReferenceReservations, databaseReferencePrevious;
    private final DatabaseReference databaseUsers;
    public DAOFireBase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.databaseReference = db.getReference("timeslots");
        this.databaseUsers=db.getReference("users");
        this.databaseReferenceReservations = db.getReference("reservations");
        this.databaseReferencePrevious = db.getReference("previous");
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
        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Registered").child(user.id)
                .removeValue().addOnSuccessListener(suc->{
            ts.notifyRemovedUser();
            ts.setThisUserReserved(false);
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

    public Query getTimeSlotQuery(TimeSlot ts){
        return databaseReference.child(ts.recCenter).child(ts.date).child(ts.id)
                .child("Registered");
    }

    public Query getReservations(String userID){
        return databaseReferenceReservations.child(userID);
    }

    public Query getPrevious(String userID){
        return databaseReferencePrevious.child(userID);
    }

}
