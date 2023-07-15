package com.creapptors.funolympic.admin_functions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.SportsAdapter;
import com.creapptors.funolympic.main_more.Sports;
import com.creapptors.funolympic.model.Sport;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class AddSports extends AppCompatActivity {

    ImageView imageView;
    ImageButton imageButton;
    EditText editText;
    Button button;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sports);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseFirestore = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Adding and Sport");
        dialog.setMessage("please wait patiently...");
        dialog.setCanceledOnTouchOutside(false);

        imageView = findViewById(R.id.new_sport_back);
        imageButton = findViewById(R.id.add_sport_pic);
        editText = findViewById(R.id.sport_name);
        button = findViewById(R.id.new_sport_submit);

        imageView.setOnClickListener(view -> {
            finish();
        });

        button.setOnClickListener(view -> {
            String title = editText.getText().toString().trim();
            dialog.show();
            checkAthleteSportExists(title);
        });

    }

    private void checkAthleteSportExists(final String title) {
        firebaseFirestore.collection("Sport")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean emailExists = !Objects.requireNonNull(task.getResult()).isEmpty();

                        if (emailExists) {
                            dialog.dismiss();
                            Toast.makeText(this, "Sport already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            createSport(title);
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(this, "Failed to check email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createSport(String title) {
        Sport sport = new Sport(title, title);
        firebaseFirestore.collection("Sport")
                .document(title)
                .set(sport)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                    }
                });
    }
}