package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Button logIn = findViewById(R.id.btn_login);
        logIn.setOnClickListener(v->{
            Intent intent = new Intent(LogIn.this,MapActivity.class);
            startActivity(intent);
        });
    }
}