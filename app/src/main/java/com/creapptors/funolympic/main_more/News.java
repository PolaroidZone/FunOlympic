package com.creapptors.funolympic.main_more;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.creapptors.funolympic.R;

import java.util.Objects;

public class News extends AppCompatActivity {

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Objects.requireNonNull(getSupportActionBar()).hide();

        back = findViewById(R.id.News_back);
        back.setOnClickListener(view -> {
            finish();
        });
    }
}