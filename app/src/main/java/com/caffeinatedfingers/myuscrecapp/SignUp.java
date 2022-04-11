package com.caffeinatedfingers.myuscrecapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity
{
    EditText uFullName, uEmail, uPassword, uUscId;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //firebase user id not usc id
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        uFullName = findViewById(R.id.fullName);
        uUscId = findViewById(R.id.user_ID);
        uEmail = findViewById(R.id.email);
        uPassword = findViewById(R.id.password);

        Button signUp = findViewById(R.id.btn_sign_up);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //check if the user is logged in --> never logged out
        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignUp.this,MapActivity.class));
            finish();
        }

        signUp.setOnClickListener(view -> {
            String email = uEmail.getText().toString().trim();
            String password = uPassword.getText().toString().trim();
            String fullName = uFullName.getText().toString().trim();
            String uscID = uUscId.getText().toString();

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
            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignUp.this, "Account Created, Welcome!", Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    //store the data in the document --> hash map
                    Map<String, Object> user = new HashMap<String, Object>();
                    // insert data into map using put
                    user.put("fName", fullName);
                    user.put("email", email);
                    user.put("uscID", uscID);

                    // now insert into cloud database
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "onSuccess: user Profile is created for" + userID);
                        }
                    });
                    startActivity(new Intent(SignUp.this, MapActivity.class));
                }

                else
                {
                    Toast.makeText(SignUp.this, "Error creating account!" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        });


//        signUp.setOnClickListener(v->{
//            Intent intent = new Intent(SignUp.this,MapActivity.class);
//            startActivity(intent);
//        });

    }
}