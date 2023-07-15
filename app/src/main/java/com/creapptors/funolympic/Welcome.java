package com.creapptors.funolympic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Welcome extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Objects.requireNonNull(getSupportActionBar()).hide();

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(Welcome.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        View getStarted = findViewById(R.id.get_started_wel);
        getStarted.setOnClickListener(view -> {
            Intent intent = new Intent(Welcome.this, Signup.class);
            startActivity(intent);
//            overridePendingTransition(R.anim.slide_up, R.anim.fade_in);
            finish();
        });
    }
}