package com.caffeinatedfingers.myuscrecapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DAOFireBase {
    private final DatabaseReference databaseReference, databaseReferenceReservations, databaseReferencePrevious;
    private final DatabaseReference databaseUsers;

    /**
     * Data Access Object used to make calls to the Firebase Realtime Database.
     */
    public DAOFireBase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.databaseReference = db.getReference("timeslots");
        this.databaseUsers = db.getReference("users");
        this.databaseReferenceReservations = db.getReference("reservations");
        this.databaseReferencePrevious = db.getReference("previous");
    }

    /**
     * @param ts Timeslot to add user to
     * @param user User to be added
     * @param context Context used to make toast texts.
     */
    public void addUser(@NonNull TimeSlot ts, @NonNull User user, Context context) {
        Reservation reservation = new Reservation(user, ts);
        this.databaseReference.child(ts.recCenter).child(ts.date)
                .child(ts.id).child("Registered").child(user.id).setValue(user.userName)
                .addOnSuccessListener(suc -> {
                    ts.notifyAddedUser();
                    ts.setThisUserReserved(true);
                    this.databaseReferenceReservations.child(user.id).child(reservation.id).setValue(reservation);
                    Toast.makeText(context, "Successfully booked reservation.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(fail -> {
            Toast.makeText(context, "" + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * @param ts Time slot to remove user from
     * @param user User to be removed
     * @param context Context for toast texts.
     */
    public void removeUser(@NonNull TimeSlot ts, @NonNull User user, Context context) {
        Reservation reservation = new Reservation(user, ts);
        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Registered").child(user.id)
                .removeValue().addOnSuccessListener(suc -> {
            ts.notifyRemovedUser();
            ts.setThisUserReserved(false);
            Toast.makeText(context, "Successfully cancelled reservation.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail -> {
            Toast.makeText(context, "" + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
        this.databaseReferenceReservations.child(user.id).child(reservation.id).removeValue().addOnSuccessListener(succ-> {
            Log.println(Log.ERROR,"DAO FIREBASE", "Successfully removed reservation from DB");
        });
    }

    /**
     * Adds user to wait list and will remind when it is its turn
     * @param ts Time slot to remind user
     * @param user User to be reminded
     * @param context Context for toast texts.
     */
    public void remindUser(@NonNull TimeSlot ts, @NonNull User user, Context context) {

        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Waitlist").child(user.id)
                .setValue(user.userName).addOnSuccessListener(suc -> {
            //@TODO set waiting for a notification
            Toast.makeText(context, "You're added to the waitlist!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail -> {
            Toast.makeText(context, "" + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    /**
     * Removes a user from a timeslot wait list
     * @param ts Time slot to remove from wait list
     * @param user User to be removed
     * @param context Context for toast texts.
     */
    public void unRemindUser(@NonNull TimeSlot ts, @NonNull User user, Context context) {

        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Waitlist").child(user.id)
                .removeValue().addOnSuccessListener(suc -> {
            //@TODO remove from waiting a notification
            Toast.makeText(context, "You're removed to the waitlist!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail -> {
            Toast.makeText(context, "" + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * @return A db ordered reference of the timeslots
     */
    public Query getTimeSlotsQuery(String recCenter, String date) {
        return databaseReference.child(recCenter).child(date).orderByKey();
    }
    /**
     * @return A db reference of a timeslot given a timeslot object
     */
    public Query getTimeSlotRegisteredQuery(@NonNull TimeSlot ts) {
        return databaseReference.child(ts.recCenter).child(ts.date).child(ts.id)
                .child("Registered");
    }
    /**
     * @return A db reference of a timeslot given a timeslot object
     */
    public Query getTimeSlotWaitListQuery(@NonNull TimeSlot ts) {
        return databaseReference.child(ts.recCenter).child(ts.date).child(ts.id)
                .child("Waitlist");
    }
    /**
     * @return A db reference of incoming reservations of a user
     */
    public Query getReservationsQuery(String userID) {
        return databaseReferenceReservations.child(userID);
    }
    /**
     * @return A db reference of previous reservations of a user
     */
    public Query getPreviousReservationQuery(String userID) {
        return databaseReferencePrevious.child(userID);
    }

}
