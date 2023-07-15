package com.creapptors.funolympic.admin_functions;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.Athlete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class addAthlete extends AppCompatActivity {

    ImageButton addImg;
    Button submit;
    ImageView back;
    EditText name, email, password;
    RadioGroup radioGroup;
    RadioButton r1, r2;

    FirebaseFirestore firestore;
    FirebaseStorage storage;

    ProgressDialog dialog;
    Dialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_athlete);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Adding an athlete");
        dialog.setMessage("please wait patiently...");
        dialog.setCanceledOnTouchOutside(false);

        alert = new Dialog(this);
        alert.setTitle("Record error");

        addImg = findViewById(R.id.add_athl_pic);
        submit = findViewById(R.id.new_athl_submit);
        back = findViewById(R.id.new_AThl_back);
        name = findViewById(R.id.athlete_Username);
        email =findViewById(R.id.athlete_email);

        r1 = findViewById(R.id.radioButtonMale);
        r2 = findViewById(R.id.radioButtonFemale);
        r1.setChecked(true);

        radioGroup = findViewById(R.id.radioGroupAthlete);

        addImg.setOnClickListener(view -> {
            addImage();
        });
        submit.setOnClickListener(view -> {
            String username = name.getText().toString().trim();
            String email = this.email.getText().toString().trim();
            int selectedR = radioGroup.getCheckedRadioButtonId();
            RadioButton selected = findViewById(selectedR);
            String gender = selected.getText().toString().toLowerCase(Locale.ROOT);
            if (username.equals("") || email.equals("")){
                Toast.makeText(this, "Cannot submit empty fields", Toast.LENGTH_SHORT).show();
            } else if(selectedR == -1) {
                Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            }else {
                dialog.show();
                //createAthlete(username, email, gender);
                checkAthleteEmailExists(username, email, gender);
            }
        });
        back.setOnClickListener(view ->{
            finish();
        });
    }

    private void checkAthleteEmailExists(String username, final String email, String gender) {
        firestore.collection("Athlete")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean emailExists = !Objects.requireNonNull(task.getResult()).isEmpty();

                        if (emailExists) {
                            dialog.dismiss();
                            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            createAthlete(username, email, gender);
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(this, "Failed to check email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createAthlete(String username, String email, String gender) {
        String uid = email;
        dialog.dismiss();
        Athlete athlete = new Athlete(uid, username, email, gender);
        firestore.collection("Athlete")
                .document(uid)
                .set(athlete)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Log.w( TAG,"record created for: "+ uid);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });
    }

    private void addImage() {
    }
}