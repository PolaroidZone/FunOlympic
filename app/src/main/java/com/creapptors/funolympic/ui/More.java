package com.creapptors.funolympic.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.creapptors.funolympic.Welcome;
import com.creapptors.funolympic.main_more.About;
import com.creapptors.funolympic.main_more.Athletes;
import com.creapptors.funolympic.main_more.FunOlympics;
import com.creapptors.funolympic.main_more.News;
import com.creapptors.funolympic.R;
import com.creapptors.funolympic.main_more.Sports;
import com.creapptors.funolympic.main_more.Users;
import com.google.firebase.auth.FirebaseAuth;

public class More extends Fragment {

    // Array of strings...
    String[] mobileArray = {"News","Users", "Athletes", "Sports", "FunOlympic", "About", "Logout"};

    FirebaseAuth auth;
    public More() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mobileArray);

        ListView listView = view.findViewById(R.id.option_list);
        listView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        intent = new Intent(getActivity(), News.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), Users.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), Athletes.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), Sports.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), FunOlympics.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), About.class);
                        startActivity(intent);
                        break;
                    case 6:
                        auth.signOut();
                        intent = new Intent(getActivity(), Welcome.class);
                        startActivity(intent);
                        requireActivity().finish();
                }
            }
        });
        return view;
    }
}