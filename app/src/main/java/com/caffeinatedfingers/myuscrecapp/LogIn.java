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

            // validate the data in email and password - check for empty fields and such
            if (TextUtils.isEmpty(email)) {
                uEmail.setError("Email is Required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                uPassword.setError("Password is Required");
                return;
            }

            if (password.length() < 6) {
                uPassword.setError("Password must be at least 6 characters");
                return;
            }

            //authenticate user
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    Toast.makeText(LogIn.this, "Log in was successful, Welcome!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogIn.this, MapActivity.class));
                }

                else
                {
                    Toast.makeText(LogIn.this, "Error logging in!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        });

    }
}