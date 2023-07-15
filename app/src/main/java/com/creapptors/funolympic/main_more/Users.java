package com.creapptors.funolympic.main_more;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.UsersAdapter;
import com.creapptors.funolympic.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Users extends AppCompatActivity {

    ImageView back;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    List<User> userList;
    UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        Objects.requireNonNull(getSupportActionBar()).hide();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        usersAdapter = new UsersAdapter(userList, this);
        recyclerView.setAdapter(usersAdapter);

        firebaseFirestore.collection("user")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            return;
                        }
                        userList.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()){
                            User user = documentSnapshot.toObject(User.class);
                            userList.add(user);
                        }
                        usersAdapter.notifyDataSetChanged();
                    }
                });

        back = findViewById(R.id.users_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}