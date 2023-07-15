package com.creapptors.funolympic.main_more;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.creapptors.funolympic.R;

import java.util.Objects;

public class About extends AppCompatActivity {

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Objects.requireNonNull(getSupportActionBar()).hide();

        back = findViewById(R.id.about_back);
        back.setOnClickListener(view -> {
            finish();
        });
    }
}