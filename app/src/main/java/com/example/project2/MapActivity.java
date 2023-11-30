package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Create a google map object
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Set up map fragment for view
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Set store google map object to returned google map object
        map = googleMap;

        // Import data for map from postgreSQL to ArrayList of JobListing objects
        ArrayList<JobListing> jobListings = importMapData();

        // Update map with job listings
        map = updateMapData(map, jobListings);

        // Set custom info window adapter
        map.setInfoWindowAdapter(new JobInfoWindow(this, jobListings));

        // Set up click listener to shift camera to clicked location

        // Set up click listener for info window to update bottom of activity job information


    }

    // TODO
    // Import data from postgreSQL database to populate ArrayList of JobListing objects.
    private ArrayList<JobListing> importMapData() {
        // Initialize ArrayList
        ArrayList<JobListing> jobListings = new ArrayList<>();

        // Pull data from database
        // TODO

        // Populate JobListing objects store in ArrayList
        // TODO REMOVE TESTING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        jobListings.add(new JobListing("Lawn mowing", "Mowers NYC INC.",
                new LatLng(40.7443679675679, -73.98867886292477), "Labor",
                2500.0, 4.8));

        // Return ArrayList
        return jobListings;
    }

    // Takes in a GoogleMap object, and ArrayList of JobListing objects, adds markers to map then
    // returns updated map object
    private GoogleMap updateMapData(GoogleMap map, ArrayList<JobListing> jobListings) {
        // TODO
        return map;
    }
}