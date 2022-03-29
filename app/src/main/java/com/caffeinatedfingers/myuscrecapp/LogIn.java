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
        Button signUp = findViewById(R.id.btn_login);
        signUp.setOnClickListener(v->{
            Intent intent = new Intent(LogIn.this,MapActivity.class);
            //intent.putExtra("RecCenter", signUp.getText());
            //intent.putExtra("UserId", "01");
            //intent.putExtra("UserName", "UserTest");
            startActivity(intent);
        });

    }
}
