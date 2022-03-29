package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    public void onButtonClicked(View view)
    { }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Button my_reservations = findViewById(R.id.btn_my_reservations);
        my_reservations.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,UpcomingReservations.class);
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });

        Button my_profile = findViewById(R.id.btn_my_profile);
        my_profile.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,UserProfile.class);
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });

        Button lyon_center = findViewById(R.id.btn_lyon_center);
        lyon_center.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,BookingPage.class);
            intent.putExtra("RecCenter", "Lyon Center");
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });

        Button village_center = findViewById(R.id.btn_village_center);
        village_center.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,BookingPage.class);
            intent.putExtra("RecCenter", "USC Village Center");
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });

        Button aquatics_center = findViewById(R.id.btn_aquatics);
        aquatics_center.setOnClickListener(v->{
            Intent intent = new Intent(MapActivity.this,BookingPage.class);
            intent.putExtra("RecCenter", "Uytengsu Aquatics Center");
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });

    }
}