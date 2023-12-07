package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_jobs extends AppCompatActivity {

    BottomNavigationView botNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        botNavBar = findViewById(R.id.botNavBarJobSearch);

        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                //You are here
                return true;
            } else if (id == R.id.search) {
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);
                return true;
            } else if (id == R.id.profile) {
                Intent i = new Intent(this, UserProfile.class);
                startActivity(i);
                return true;
                // TODO need to add the rest of the navbar buttons to their respective activities
            } else {
                return false;
            }
        });
    }
}