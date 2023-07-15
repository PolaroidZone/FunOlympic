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

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.EventsAdapter;
import com.creapptors.funolympic.model.Athlete;
import com.creapptors.funolympic.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllEvents extends Fragment {

    private RecyclerView eventRecycler;
    private EventsAdapter eventsAdapter;
    private List<Event> eventList;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public AllEvents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);
        eventRecycler = view.findViewById(R.id.AllEventsRecyclerView);
        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getContext(), eventList);
        eventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRecycler.setAdapter(eventsAdapter);
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
                        eventsAdapter.notifyDataSetChanged();
                    }
                });

        return view;
    }
}