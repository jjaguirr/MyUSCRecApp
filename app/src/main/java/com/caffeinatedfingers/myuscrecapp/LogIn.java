package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class LogIn extends AppCompatActivity {

    EditText uEmail, uPassword;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uEmail = findViewById(R.id.email);
        uPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        Button logIn = findViewById(R.id.btn_login);

        logIn.setOnClickListener(view -> {
            String email = uEmail.getText().toString().trim();
            String password = uPassword.getText().toString().trim();
            int valid=checkFields(email,password);
            if(valid!=0){
               if(valid==1){
                   uEmail.setError("Please enter a valid email");
               }
               if(valid==2){
                   uPassword.setError("Please enter a valid password");
               }
            }


            //authenticate user
            boolean success=authenticate(email, password);

        });

    }
    protected boolean authenticate(String email, String password){
        AtomicBoolean success= new AtomicBoolean(false);
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                success.set(true);
                Toast.makeText(LogIn.this, "Log in was successful, Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogIn.this, MapActivity.class));

            }

            else
            {
                Toast.makeText(LogIn.this, "Error logging in!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return success.get();
    }
    protected static int checkFields(String email, String password){
        // validate the data in email and password - check for empty fields and such
        if (TextUtils.isEmpty(email)) {
            return 1;
        }

        if (TextUtils.isEmpty(password)) {
            return 2;
        }

        if (password.length() < 6) {
            return 2;
        }
        if(!email.contains("@")){

            return 1;
        }
        return 0;
    }
}