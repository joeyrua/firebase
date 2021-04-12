package com.example.firebase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfile extends AppCompatActivity {
    Button log_out;
    ImageView user_iamge;
    TextView profile_name,profile_email,profile_phone;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String user_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        log_out = findViewById(R.id.logout);
        user_iamge = findViewById(R.id.user_image);
        profile_name = findViewById(R.id.user_name);
        profile_email = findViewById(R.id.user_email);
        profile_phone = findViewById(R.id.user_phone);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        user_ID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("users").document(user_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                profile_name.setText(value.getString("name"));
                profile_phone.setText(value.getString("phone"));
                profile_email.setText(value.getString("email"));
            }
        });
    }
    public boolean onKeyDown(int KeyCode , KeyEvent event) {
        if (KeyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(KeyCode, event);
    }

    public void log_out(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}