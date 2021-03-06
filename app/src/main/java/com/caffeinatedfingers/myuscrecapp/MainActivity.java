package com.caffeinatedfingers.myuscrecapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void onButtonClicked(View view){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button test = findViewById(R.id.btn_inMap_gymtest);
        test.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, BookingPage.class);
            intent.putExtra("RecCenter", test.getText());
            intent.putExtra("UserId", "01");
            intent.putExtra("UserName", "UserTest");
            intent.putExtra("UID","uid");
            startActivity(intent);
        });

        Button upcomingReservations = findViewById(R.id.btn_upcoming_reservations);
        upcomingReservations.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, UpcomingReservations.class);
            intent.putExtra("UserId", "01");
            intent.putExtra("UserName", "UserTest");
            intent.putExtra("UID","uid");
            startActivity(intent);
        });


    }
}