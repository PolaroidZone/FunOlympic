package com.creapptors.funolympic.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creapptors.funolympic.R;
import com.creapptors.funolympic.adaptor.home_adapter;
import com.creapptors.funolympic.model.Video;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private RecyclerView videoRecyclerView;
    private home_adapter videoAdapter;
    private List<Video> videoList;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public Home() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        videoRecyclerView = view.findViewById(R.id.videoRecyclerView);
        videoList = new ArrayList<>();
        videoAdapter = new home_adapter(getContext(), videoList);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        videoRecyclerView.setAdapter(videoAdapter);
        fetchVideoData();
        return  view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchVideoData() {
        // Fetch video data from Firestore or any other data source
        // Add the fetched videos to the videoList
        firebaseFirestore.collection("Video")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Video video = documentSnapshot.toObject(Video.class);
                        videoList.add(video);
                    }
                    videoAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure scenario
                });
    }
}