package com.creapptors.funolympic.admin_functions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.model.funOlympics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class scheduleFunOlympic extends AppCompatActivity {

    Button setDate, setSubmit;
    ImageButton setImg;
    ImageView setBack;
    EditText ed1, ed2;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog dialog;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_fun_olympic);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseFirestore = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Scheduling");
        dialog.setMessage("please wait patiently...");
        dialog.setCanceledOnTouchOutside(false);
        ed1 = findViewById(R.id.Fun_Title);
        ed2 = findViewById(R.id.Fun_Date);
        ed2.setVisibility(View.INVISIBLE);

        setDate =findViewById(R.id.buttonSetDate);
        setDate.setOnClickListener(view -> {
            newDate();
        });

        setSubmit = findViewById(R.id.new_Fun_submit);
        setSubmit.setOnClickListener(view -> {
            String title = ed1.getText().toString().trim();
            String date = ed2.getText().toString().trim();
            dialog.show();
            checkFunSchedule(title, date);
        });
        setBack = findViewById(R.id.new_fun_back);
        setBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void newDate() {
        //picks the date and coverts it to a string value named Date
        //Date is then set the ed2 EditText to be displayed
        // Get the current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog to pick a date
        DatePickerDialog datePickerDialog = new DatePickerDialog(scheduleFunOlympic.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Create a formatted date string
                        String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        ed2.setText(selectedDate);
                    }
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void createFunSchedule(String title, String date) {
        funOlympics fun = new funOlympics(title, date);
        firebaseFirestore.collection("FunOlympics")
                .document(title)
                .set(fun)
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
                        Toast.makeText(scheduleFunOlympic.this, "Error while creating record",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkFunSchedule(final String title, String date) {
        firebaseFirestore.collection("FunOlympics")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            boolean titleExists = !task.getResult().isEmpty();
                            if (titleExists) {
                                dialog.dismiss();
                                Toast.makeText(scheduleFunOlympic.this, "FunOlympic already scheduled", Toast.LENGTH_SHORT).show();
                            }else {
                                createFunSchedule(title, date);
                            }
                        }else {
                            dialog.dismiss();
                            Toast.makeText(scheduleFunOlympic.this, "Failed to check Title", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(scheduleFunOlympic.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}