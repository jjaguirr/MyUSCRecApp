package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUp extends AppCompatActivity
{
    EditText uFullName, uEmail, uPassword, uUserId;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        uFullName = findViewById(R.id.fullName);
        uUserId = findViewById(R.id.user_ID);
        uEmail = findViewById(R.id.email);
        uPassword = findViewById(R.id.password);

        Button signUp = findViewById(R.id.btn_sign_up);
        fAuth = FirebaseAuth.getInstance();

        //check if the user is logged in --> never logged out
        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignUp.this,MapActivity.class));
            finish();
        }

        signUp.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                // validate the data in email and password - check for empty fields and such
                if(TextUtils.isEmpty(email))
                {
                    uEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    uPassword.setError("Password is Required");
                    return;
                }

                if(password.length() < 6)
                {
                    uPassword.setError("Password must be at least 6 characters");
                }

                //register user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SignUp.this, "Account Created, Welcome!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, MapActivity.class));
                        }

                        else
                        {
                            Toast.makeText(SignUp.this, "Error creating account!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                {

                }
            }
        });


//        signUp.setOnClickListener(v->{
//            Intent intent = new Intent(SignUp.this,MapActivity.class);
//            startActivity(intent);
//        });

    }
}