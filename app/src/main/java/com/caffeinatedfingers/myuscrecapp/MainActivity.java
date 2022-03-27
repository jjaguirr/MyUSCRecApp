package com.caffeinatedfingers.myuscrecapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        Button btn = findViewById(R.id.btn_inMap_gymtest);
        btn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, BookingPage.class);
            intent.putExtra("RecCenter", btn.getText());
            startActivity(intent);
        });
    }
}