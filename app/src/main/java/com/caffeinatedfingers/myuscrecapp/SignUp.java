package com.caffeinatedfingers.myuscrecapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUp = findViewById(R.id.btn_sign_up);
        signUp.setOnClickListener(v->{
            Intent intent = new Intent(SignUp.this, MapActivity.class);
            // username, email, password
            //intent.putExtra("RecCenter", signUp.getText());
            //intent.putExtra("UserId", "01");
            //intent.putExtra("UserName", "UserTest");
            startActivity(intent);
        });
    }
}