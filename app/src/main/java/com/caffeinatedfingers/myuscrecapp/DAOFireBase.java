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
import androidx.annotation.Nullable;

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
    User u=null;
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
                    u=new User(uscID,fullName);
                }
            }
        });
        this.databaseReference = db.getReference("timeslots");
        this.databaseUsers = db.getReference("users");
        this.databaseReferenceReservations = db.getReference("reservations");
        this.databaseReferencePrevious = db.getReference("previous");
    }

    public void addUser(TimeSlot ts, User user, Context context){

        Reservation reservation = new Reservation(u, ts);
        this.databaseReference.child(ts.recCenter).child(ts.date)
                .child(ts.id).child("Registered").child(uscID).setValue(fullName)
                .addOnSuccessListener(suc->{
                    ts.notifyAddedUser();
                    this.databaseReferenceReservations.child(uscID).child(reservation.id).setValue(reservation);
                    Toast.makeText(context, "Successfully booked reservation.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(fail -> {
            Toast.makeText(context, "" + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    public void removeUser(TimeSlot ts, User user, Context context){
        Reservation reservation = new Reservation(u, ts);
        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Registered").child(uscID)
                .removeValue().addOnSuccessListener(suc->{
            ts.notifyRemovedUser();
            ts.setThisUserReserved(false);
            this.databaseReferenceReservations.child(uscID).child(reservation.id).removeValue();
            Toast.makeText(context, "Successfully cancelled reservation.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail -> {
            Toast.makeText(context, "" + fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Adds user to wait list and will remind when it is its turn
     * @param ts Time slot to remind user
     * @param user User to be reminded
     * @param context Context for toast texts.
     */

    //@TODO: Implement notifications. Create wait-list in FireBase Database per each Timeslot.
    public void remindUser(TimeSlot ts, User user, Context context){
        Toast.makeText(context, "You're added to the waitlist!", Toast.LENGTH_SHORT).show();
        this.databaseReference.child(ts.recCenter).child(ts.date).child(ts.id).child("Waitlist").child(uscID).setValue(fullName);
    }

        //notifications
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
    public Query getTimeSlotQuery(@NonNull TimeSlot ts) {
        return databaseReference.child(ts.recCenter).child(ts.date).child(ts.id)
                .child("Registered");
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
    private void notification(Context context){
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("waitlistOpen","waitlistOpen", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context, "waitlistOpen").setContentTitle("USCRecApp").setAutoCancel(true).setContentText("Good news! A spot opened for your slot.");
        NotificationManagerCompat managerCompat= NotificationManagerCompat.from(context);
        managerCompat.notify(999,builder.build());
    }


}
