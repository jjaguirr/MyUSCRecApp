package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity implements View.OnClickListener {

    public void onButtonClicked(View view)
    { }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
    }

    Button signUp = findViewById(R.id.btn_sign_up);
        signUp.setOnClickListener(v->{
        Intent intent = new Intent(WelcomePage.this,LogIn.class);
        intent.putExtra("RecCenter", signUp.getText());
        intent.putExtra("UserId", "01");
        intent.putExtra("UserName", "UserTest");
        startActivity(intent);
    });
    Button logIn = findViewById(R.id.btn_login);
        logIn.setOnClickListener(v->{
        Intent intent = new Intent(WelcomePage.this, SignUp.class);
        intent.putExtra("UserId", "01");
        intent.putExtra("UserName", "UserTest");
        startActivity(intent);
    });

}
