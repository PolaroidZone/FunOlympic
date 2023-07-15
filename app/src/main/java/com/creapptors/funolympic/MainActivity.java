package com.creapptors.funolympic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.creapptors.funolympic.admin_functions.AddEvent;
import com.creapptors.funolympic.ui.AllEvents;
import com.creapptors.funolympic.ui.Home;
import com.creapptors.funolympic.ui.More;
import com.creapptors.funolympic.ui.Schedule;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FloatingActionButton floatingActionButton;

    Schedule schedule = new Schedule();
    AllEvents allEvents = new AllEvents();
    More more = new More();
    Home home = new Home();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        navigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEvent.class);
                startActivity(intent);
                }
            });

        navigationView.setSelectedItemId(R.id.home_frag);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, home) // Replace default fragment with home fragment
                .commit();

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_frag){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, home)
                            .commit();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    return true;
                }else if (item.getItemId() == R.id.schedule_frag) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, schedule)
                            .commit();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    return true;
                }else if (item.getItemId() == R.id.events_frag) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, allEvents)
                            .commit();
                    floatingActionButton.setVisibility(View.VISIBLE);
                    return true;
                }else if (item.getItemId() == R.id.more_frag) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flFragment, more)
                            .commit();
                    floatingActionButton.setVisibility(View.INVISIBLE);
                    return true;
                }else {
                    floatingActionButton.setVisibility(View.VISIBLE);
                    return false;
                    }
            }
        });
    }

}