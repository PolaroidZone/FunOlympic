package com.creapptors.funolympic.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.EventsAdapter;
import com.creapptors.funolympic.adaptor.schedule_adapter;
import com.creapptors.funolympic.model.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends Fragment {
    private RecyclerView recyclerView;
    private schedule_adapter scheduleAdapter;
    private List<Event> eventList;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public Schedule() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        recyclerView = view.findViewById(R.id.scheduleRecyclerView);
        eventList = new ArrayList<>();
        scheduleAdapter = new schedule_adapter(view.getContext(), eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(scheduleAdapter);
        firebaseFirestore.collection("Event")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            return;
                        }
                        eventList.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            Event event = documentSnapshot.toObject(Event.class);
                            eventList.add(event);
                        }
                        scheduleAdapter.notifyDataSetChanged();
                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}