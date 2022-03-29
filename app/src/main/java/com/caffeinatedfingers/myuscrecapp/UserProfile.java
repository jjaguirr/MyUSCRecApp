package com.caffeinatedfingers.myuscrecapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button my_reservations = findViewById(R.id.btn_my_reservations);
        my_reservations.setOnClickListener(v->{
            Intent intent = new Intent(UserProfile.this, UpcomingReservations.class);
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });
    }
}