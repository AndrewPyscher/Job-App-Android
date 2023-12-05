package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView botNavBar;

    public static final int LOCATION_REQUEST_CODE = 111;
    private static final int NOTIFICATION_REQUEST_CODE = 222 ;
    private static final String TAG = "PERMS STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botNavBar = findViewById(R.id.botNavBar);

        checkPermissions();

        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                //do nothing
                return true;
            } else if (id == R.id.search) {
                Intent i = new Intent(this, activity_jobs.class);
                startActivity(i);
                return true;
            } else if (id == R.id.profile) {
                Intent i = new Intent(this, UserProfile.class);
                return true;
            } else if (id == R.id.settings) {
                Intent i = new Intent(this, Settings.class);
                return true;
            } else if (id == R.id.navigation_notifications) {
                //Implement the navigation to a notification page.
                //This does not exist yet
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Set default selected item when in this activity in onStart
        //This will guarantee that we have the correct item selected when the activity starts
        botNavBar.setSelectedItemId(R.id.home);
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Notification permissions NOT granted, requesting...");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Notification permissions already granted");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Location permissions NOT granted, requesting...");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Location permissions already granted");
        }
    }
}