package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MapActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userName="";
    String userID="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Button my_reservations = findViewById(R.id.btn_my_reservations);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    userName=document.get("fName").toString();
                    userID=document.get("uscID").toString();
                }
            }
        });

        my_reservations.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,UpcomingReservations.class);
            intent.putExtra("UserId", userID);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });

        Button my_profile = findViewById(R.id.btn_my_profile);
        my_profile.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,UserProfile.class);
            intent.putExtra("UserId", userID);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });

        Button lyon_center = findViewById(R.id.btn_lyon_center);
        lyon_center.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,BookingPage.class);
            intent.putExtra("RecCenter", "Lyon Center");
            intent.putExtra("UserId", userID);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });

        Button village_center = findViewById(R.id.btn_village_center);
        village_center.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,BookingPage.class);
            intent.putExtra("RecCenter", "USC Village Center");
            intent.putExtra("UserId", userID);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });

        Button aquatics_center = findViewById(R.id.btn_aquatics);
        aquatics_center.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,BookingPage.class);
            intent.putExtra("RecCenter", "Uytengsu Aquatics Center");
            intent.putExtra("UserId", userID);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        });

    }
}