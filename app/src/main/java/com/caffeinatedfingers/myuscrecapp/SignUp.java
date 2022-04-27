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
import com.google.firebase.messaging.FirebaseMessaging;

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

            int valid=checkData(email,password,fullName,uscID);
            if(valid!=0){
                if(valid==1){
                    uEmail.setError("Please enter a valid email");
                    return;
                }
                if(valid==2){
                    uPassword.setError("Please enter a valid password");
                    return;
                }
                if(valid==3){
                    uFullName.setError("Please enter your first and last name");

                    return;
                }
                if(valid==4){
                    uUscId.setError("Please enter a valid USC ID");
                    return;
                }

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
                    user.put("notificationToken", FirebaseMessaging.getInstance().getToken().getResult());

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
    public static int checkData(String email, String password, String fullName, String uscID){
        // validate the data in email and password - check for empty fields and such
        if(TextUtils.isEmpty(email))
        {

            return 1;
        }

        if(TextUtils.isEmpty(password))
        {

            return 2;
        }

        if(password.length() < 6)
        {
            return 2;
        }
        if(!fullName.contains(" ")){
            return 3;
        }
        if(uscID.length()!=10){
            return 4;
        }
        if(!email.contains("@")){
            return 1;
        }
        return 0;
    }
}