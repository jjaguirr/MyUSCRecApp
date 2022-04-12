package com.caffeinatedfingers.myuscrecapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    TextView fullName, uscID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //database userID not uscID
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        uscID = findViewById(R.id.uscID_profile);
        fullName = findViewById(R.id.fullName_profile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                assert value != null;
                fullName.setText(value.getString("fName"));
                uscID.setText(value.getString("uscID"));
            }
        });

        Button my_reservations = findViewById(R.id.btn_my_reservations);
        my_reservations.setOnClickListener(v->{
            Intent intent = new Intent(UserProfile.this, UpcomingReservations.class);
            intent.putExtra("UserId", "123456780");
            intent.putExtra("UserName", "Tommy Trojan");
            startActivity(intent);
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfile.this,WelcomePage.class));
        finish();
    }
}