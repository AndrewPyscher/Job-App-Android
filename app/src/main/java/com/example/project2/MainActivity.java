package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView botNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botNavBar = findViewById(R.id.botNavBar);

        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                //do nothing
                return true;
            } else if (id == R.id.search) {
                //Use start activity with intents to start that particular activity
                return true;
            } else {
                return false;
            }
            //Etc etc etc, you can modify this however you want to change its behavior
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Set default selected item when in this activity in onStart
        //This will guarantee that we have the correct item selected when the activity starts
        botNavBar.setSelectedItemId(R.id.home);
    }
}