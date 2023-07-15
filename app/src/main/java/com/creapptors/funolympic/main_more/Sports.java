package com.creapptors.funolympic.main_more;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.SportsAdapter;
import com.creapptors.funolympic.admin_functions.AddSports;
import com.creapptors.funolympic.model.Sport;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Sports extends AppCompatActivity {

    ImageView back;
    FloatingActionButton add;
    SportsAdapter sportsAdapter;
    List<Sport> sportList;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        Objects.requireNonNull(getSupportActionBar()).hide();

        recyclerView = findViewById(R.id.sports_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sportList = new ArrayList<>();
        sportsAdapter = new SportsAdapter(sportList, this);
        recyclerView.setAdapter(sportsAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Sport")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            return;
                        }
                        sportList.clear();
                        for (DocumentSnapshot document : value.getDocuments()) {
                            Sport sport = document.toObject(Sport.class);
                            sportList.add(sport);
                        }
                        sportsAdapter.notifyDataSetChanged();
                    }
                });


        add = findViewById(R.id.sport_floatingBtn);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(Sports.this, AddSports.class);
            startActivity(intent);
        });

        back = findViewById(R.id.Sports_back);
        back.setOnClickListener(view -> {
            finish();
        });
    }
}