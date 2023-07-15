package com.creapptors.funolympic.main_more;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.AthleteAdapter;
import com.creapptors.funolympic.admin_functions.addAthlete;
import com.creapptors.funolympic.model.Athlete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Athletes extends AppCompatActivity {
    ImageView backArrow;
    FloatingActionButton btn;
    RecyclerView recyclerView;
    AthleteAdapter athleteAdapter;
    List<Athlete> athletesList;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletes);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.athlete_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        athletesList = new ArrayList<>();
        athleteAdapter = new AthleteAdapter(athletesList, this);
        recyclerView.setAdapter(athleteAdapter);

        firebaseFirestore.collection("Athlete")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null){
                                    return;
                                }
                                athletesList.clear();
                                assert value != null;
                                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                    Athlete athlete = documentSnapshot.toObject(Athlete.class);
                                    athletesList.add(athlete);
                                }
                                athleteAdapter.notifyDataSetChanged();
                            }
                        });

        btn = findViewById(R.id.ATh_floatingBtn);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(Athletes.this, addAthlete.class);
            startActivity(intent);
        });
        backArrow = findViewById(R.id.AThl_back);
        backArrow.setOnClickListener(view -> {
            finish();
        });
    }
}