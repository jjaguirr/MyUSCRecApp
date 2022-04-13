package com.caffeinatedfingers.myuscrecapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DAOFireBase {
    private final DatabaseReference databaseReference, databaseReferenceReservations, databaseReferencePrevious;
    private final DatabaseReference databaseUsers;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String fullName="";
    String uscID="";
    public DAOFireBase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    fullName=document.get("fName").toString();
                    uscID=document.get("uscID").toString();
                }
            }
        });
        this.databaseReference = db.getReference("timeslots");
        this.databaseUsers=db.getReference("users");
        this.databaseReferenceReservations = db.getReference("reservations");
        this.databaseReferencePrevious = db.getReference("previous");
    }

    public void addUser(TimeSlot ts, User user, Context context){
        Reservation reservation = new Reservation(uscID, ts.time, ts.recCenter, ts.capacity, ts.date);
        this.databaseReference.child(ts.recCenter).child(ts.date)
                .child(ts.id).child("Registered").child(uscID).setValue(fullName)
                .addOnSuccessListener(suc->{
                    ts.notifyAddedUser();
                    this.databaseReferenceReservations.child(uscID).child(reservation.id).setValue(reservation);
                    Toast.makeText(context, "Successfully booked reservation.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(fail->{
                    Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    public void removeUser(TimeSlot ts, User user, Context context){
        Reservation reservation = new Reservation(uscID, ts.time, ts.recCenter, ts.capacity, ts.date);
        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Registered").child(uscID)
                .removeValue().addOnSuccessListener(suc->{
            ts.notifyRemovedUser();
            ts.setThisUserReserved(false);
            this.databaseReferenceReservations.child(uscID).child(reservation.id).removeValue();
            Toast.makeText(context, "Successfully cancelled reservation.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail->{
            Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    //@TODO: Implement notifications. Create wait-list in FireBase Database per each Timeslot.
    public void remindUser(TimeSlot ts, User user, Context context){
        Toast.makeText(context, "You're added to the waitlist!", Toast.LENGTH_SHORT).show();
        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Waitlist").child(uscID).setValue(fullName);

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
