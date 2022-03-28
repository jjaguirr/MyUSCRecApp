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

public class DAORecCenter {
    private final DatabaseReference databaseReference;
    public DAORecCenter(RecCenter recCenter, String date) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        this.databaseReference = db.getReference("reservations/"
                +recCenter.id+"/"+ date);
    }

    public void addUser(TimeSlot ts, User user, Context context){
        this.databaseReference.child(ts.id).child(user.id).setValue(user.userName)
                .addOnSuccessListener(suc->{
                    ts.notifyAddedUser();
                    Toast.makeText(context, "Successfully booked reservation.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(fail->{
                    Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    public void removeUser(TimeSlot ts, User user, Context context){
        this.databaseReference.child(ts.id).child(user.id).removeValue().addOnSuccessListener(suc->{
            ts.notifyRemovedUser();
            Toast.makeText(context, "Successfully cancelled reservation.", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail->{
            Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    //@TODO: Implement notifications. Create wait-list in FireBase Database per each Timeslot.
    public void remindUser(TimeSlot ts, User user, Context context){
        //Toast.makeText(context, "Successfully added to wait-list", Toast.LENGTH_SHORT).show();

    }
    //Returns a db ordered reference of the timeslots
    public Query get(String key){
        if (key==null) return databaseReference.orderByKey().limitToFirst(10);
        return databaseReference.orderByKey().startAfter(key).limitToFirst(10);
    }
}
