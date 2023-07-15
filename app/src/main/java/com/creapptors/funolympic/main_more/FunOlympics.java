package com.creapptors.funolympic.main_more;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.FunOlympicAdapter;
import com.creapptors.funolympic.admin_functions.scheduleFunOlympic;
import com.creapptors.funolympic.model.funOlympics;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FunOlympics extends AppCompatActivity {

    ImageView back;
    FloatingActionButton actionButton;
    FunOlympicAdapter funOlympicAdapter;
    List<funOlympics> funOlympicsList;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_olympics);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.fun_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        funOlympicsList = new ArrayList<>();
        funOlympicAdapter = new FunOlympicAdapter(funOlympicsList, this);
        recyclerView.setAdapter(funOlympicAdapter);

        firebaseFirestore.collection("FunOlympics")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        funOlympicsList.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            funOlympics fun = documentSnapshot.toObject(funOlympics.class);
                            funOlympicsList.add(fun);
                        }
                        funOlympicAdapter.notifyDataSetChanged();
                    }
                });

        actionButton = findViewById(R.id.Fun_floatingBtn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FunOlympics.this, scheduleFunOlympic.class);
                startActivity(intent);
            }
        });

        back = findViewById(R.id.Fun_back);
        back.setOnClickListener(view -> {
            finish();
        });

    }
}