package com.creapptors.funolympic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button logIn;
    EditText edit1, edit2;
    TextView tv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting app ready");
        progressDialog.setMessage("Validating...");
        progressDialog.setCanceledOnTouchOutside(false);

        edit1 = findViewById(R.id.editUser_log);
        edit2 = findViewById(R.id.editPass_log);

        logIn = findViewById(R.id.btSub_log);
        logIn.setOnClickListener(view -> {
            String email = edit1.getText().toString().trim();
            String password = edit2.getText().toString().trim();
            logIn(email, password);
        });
        tv = findViewById(R.id.tv_UserLog);
        tv.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
            finish();
        });

    }

    private void logIn(String email, String password) {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "this is the error" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}