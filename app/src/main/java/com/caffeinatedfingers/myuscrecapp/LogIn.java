package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    public void onButtonClicked(View view)
    { }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btn_login);
        login.setOnClickListener(v->{
            Intent intent = new Intent(LogIn.this,MapActivity.class);
            startActivity(intent);
        });

    }
}
