package com.creapptors.funolympic;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.creapptors.funolympic.model.Admin;
import com.creapptors.funolympic.passwordmanager.passwordChecker;
import com.creapptors.funolympic.passwordmanager.symbolChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Signup extends AppCompatActivity {

    EditText userName, emaIl, passWord;
    FirebaseAuth auth;
    ProgressDialog dialog;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Objects.requireNonNull(getSupportActionBar()).hide();

        View textClick = findViewById(R.id.HaveActv_sig);
        View submit = findViewById(R.id.btSub_sig);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating you account");
        dialog.setMessage("please wait patiently...");
        dialog.setCanceledOnTouchOutside(false);

        //edit text
        userName = findViewById(R.id.editUser_sig);
        emaIl = findViewById(R.id.editEmail_sig);
        passWord = findViewById(R.id.editPass_sig);

        textClick.setOnClickListener(view -> {
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
            finish();
        });

        submit.setOnClickListener(view -> {
            String user = userName.getText().toString().trim();
            String email = emaIl.getText().toString().trim();
            String password = passWord.getText().toString().trim();
            checkFields(user, email, password);
        });

    }

    private void checkFields(String user, String email, String password){

        if (user.equals("")){
            Toast.makeText(Signup.this, "Enter a username", Toast.LENGTH_SHORT).show();
        }else if (email.equals("")){
            Toast.makeText(Signup.this, "Enter your email", Toast.LENGTH_SHORT).show();
        }else if(password.equals("")){
            Toast.makeText(Signup.this, "Enter a password", Toast.LENGTH_SHORT).show();
        }else {
            checkPass(user, email, password);
        }
    }

    private void checkPass(String username, String email, String password){
        passwordChecker passCheck = new passwordChecker();
        String passResults = passCheck.lengthChecker(password);
        regexCheck(username, email, password);
    }

    private void regexCheck(String username, String email, String password){
        symbolChecker symCheck = new symbolChecker();
        String regResults = symCheck.regexCheck(password);
        String uid = auth.getUid();
        checkUsername(username, new OnUsernameCheckedListener() {
            @Override
            public void onUsernameChecked(boolean isAvailable) {
                createAC(uid, username, email, password);
            }
        });
    }

    private void createAC(String uid, String username,String email, String password){
        dialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if(task.isSuccessful()) {
                    String uid = auth.getUid();
                    createUserRecord(uid, username, email, password);
                }else {
                    Toast.makeText(Signup.this, "Your Email is already in use by another account", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    private void checkUsername(String username, OnUsernameCheckedListener listener) {
        // Get the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query the "admin" collection to see if a document with the specified username exists
        db.collection("admin")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Query succeeded, check if any documents were found
                        boolean isAvailable = task.getResult().isEmpty();
                        listener.onUsernameChecked(isAvailable);
                    } else {
                        // Query failed, log the error message
                        Log.w(TAG, "Error checking username availability: ", task.getException());
                        listener.onUsernameChecked(false);
                        Toast.makeText(Signup.this, "Username already in use.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error checking username availability: "+ e);
                    }
                });
    }

    // Define the OnUsernameCheckedListener interface
    interface OnUsernameCheckedListener {
        void onUsernameChecked(boolean isAvailable);
    }

    private void createUserRecord(String uid, String username, String email, String password){

        //create a user with uid, email, username and password.
       // Map<String, Object> admin = new HashMap<>();
        Admin admin = new Admin(uid, username, email, password);
//        admin.put("username", username);
//        admin.put("email", email);
//        admin.put("password", password);

        firebaseFirestore.collection("admin")
                .document(uid)
                .set(admin)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Log.w(TAG, "User record created for: "+ uid);
                        Intent intent = new Intent(Signup.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Log.w(TAG, "Failed to create a record for: "+ uid);
                    }
                });
    }
}