package com.caffeinatedfingers.myuscrecapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {
    TextView fullName, uscID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    //database userID not uscID
    String userID;
    String UID;
    String stringUscID;
    String stringFullName;
    String stringUID;
    ImageView profileImage;
    Button uploadPhotoButton;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        uscID = findViewById(R.id.uscID_profile);
        fullName = findViewById(R.id.fullName_profile);
        profileImage = findViewById(R.id.profile_image);
        uploadPhotoButton = findViewById(R.id.btn_upload_photo);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileReference = storageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        profileReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(value -> {
            stringFullName = value.getString("fName");
            fullName.setText(stringFullName);
            stringUscID = value.getString("uscID");
            uscID.setText(stringUscID);
            stringUID=userID;
        });

        Button my_reservations = findViewById(R.id.btn_my_reservations);
        my_reservations.setOnClickListener(v->{
            Intent intent = new Intent(UserProfile.this, UpcomingReservations.class);
            intent.putExtra("UserId", stringUscID);
            intent.putExtra("UserName", stringFullName);
            intent.putExtra("uid",stringUID);
            startActivity(intent);
        });

        uploadPhotoButton.setOnClickListener(view -> {
            // open gallery on phone
            // this intent returns the image that the user has clicked on to select
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        Button logout_btn = findViewById(R.id.btn_logout);
            logout_btn.setOnClickListener(v->{
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this,WelcomePage.class));
                finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                // activity result is invoked by intent
                // perform operations
                // then populate firebase
                assert data != null;
                Uri imageURI = data.getData();
                //profileImage.setImageURI(imageURI);

                uploadImageToFirebase(imageURI);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageURI) {
        //use storage reference
        final StorageReference fileReference = storageReference.child("users/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/profile.jpg");
        fileReference.putFile(imageURI).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener
                (uri -> Picasso.get().load(uri).into(profileImage))).addOnFailureListener(e ->
                Toast.makeText(UserProfile.this, "Failed to Upload Photo", Toast.LENGTH_SHORT).show());
    }
}