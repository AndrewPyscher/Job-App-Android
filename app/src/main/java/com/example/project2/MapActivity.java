package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// TODO WAITING FOR
// RATING

// TODO LIST
// Add in refresh button to refresh map information
// Filtering
// Inactive/active jobs included in maps, include button/switch/spinner for job filtering
// CHECK ON COLORING MARKERS BASED ON CATAGORY

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Create a google map object
    GoogleMap map;

    // Create request queue for volley
    RequestQueue queue;

    // Create a current JobListing object
    JobListing currentJobListing;

    // Required views
    TextView tvMapTitle, tvMapAddress, tvMapDescription;
    Button btnMapApply;
    ProgressBar pbMap, pbInfo;

    // Create constant string message
    private final String ERROR_TITLE_MESSAGE = "Error find job listing",
            ERROR_ADDRESS_MESSAGE = "ERROR generating street address",
            ERROR_DESCRIPTION_MESSAGE = "Please refresh map data and retry";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Set up required views
        tvMapTitle = findViewById(R.id.tvMapTitle);
        tvMapAddress = findViewById(R.id.tvMapAddress);
        tvMapDescription = findViewById(R.id.tvMapDescription);
        tvMapDescription.setMovementMethod(new ScrollingMovementMethod());
        btnMapApply = findViewById(R.id.btnMapApply);
        pbInfo = findViewById(R.id.pbMapInfo);
        pbMap = findViewById(R.id.pbMapFragment);
        // Make info progress bar visible
        pbMap.setVisibility(View.VISIBLE);

        // Set current job listing to null
        currentJobListing = null;

        // Set up button listener for apply button
        btnMapApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check there is a current job listing
                if (currentJobListing != null) {
                    // TODO When figuring out where to push apply event
                }
            }
        });

        // Set up map fragment and street address request queue for view
        queue = Volley.newRequestQueue(this);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Make info progress bar invisible
        pbMap.setVisibility(View.INVISIBLE);

        // Set store google map object to returned google map object
        map = googleMap;

        // TODO ADD ABILITY TO CHOOSE ACTIVE/INACTIVE
        // Import data for map from postgreSQL to ArrayList of JobListing objects
        ArrayList<JobListing> jobListings = importMapData();

        // Update map with job listings
        updateMapData(map, jobListings);

        // Set custom info window adapter
        map.setInfoWindowAdapter(new JobInfoWindow(this, jobListings));

        // Set map camera settings
//        CameraPosition cameraPosition = CameraPosition.builder()
//                .zoom(12)
//                .tilt(30)
//                .build();
//        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // TODO REMOVE AFTER TESTING SETS CAMERA LOCATION
        map.moveCamera(CameraUpdateFactory.newLatLng(jobListings.get(0).getLocation()));

        // Set up click listener to update camera position to clicked coordinates
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        // Set up click listener for info window to update to bottom display
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                // Make info progress bar visible
                pbInfo.setVisibility(View.VISIBLE);

                // Set current job listing
                currentJobListing = (JobListing) marker.getTag();

                // Pass marker to method to pull information from it
                getAddress(marker);
            }
        });

    }

    // Takes in marker object, request a street address from google map's revers geocaching api
    private void getAddress(Marker marker) {
        try {
            // Find JobListing object from marker
            JobListing jobListing = (JobListing) marker.getTag();

            // Create LatLng from marker object's location
            LatLng markerLocation = jobListing.getLocation();

            // Create api url
            String url = "https://maps.googleapis.com/maps/api/geocode/json?" +
                    "latlng=" + markerLocation.latitude + "," + markerLocation.longitude +
                    "&result_type=street_address&key=" +
                    "AIzaSyD524YfI0YzrCR1kbUwG4kLOe93O55kTng";

            // Create api request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Format response value to get string of street address
                            String streetAddress;
                            try {
                                // TODO TESTING
                                Log.d("test", String.valueOf(response.getJSONArray("results").getJSONObject(0).getString("formatted_address")));

                                // Pull address string from response
                                streetAddress = response.getJSONArray("results").
                                        getJSONObject(0).getString("formatted_address");
                            } catch (JSONException e) {
                                // Set address value to error value
                                streetAddress = ERROR_ADDRESS_MESSAGE;
//                                throw new RuntimeException(e);
                            }

                            // Pass JobListing and street address to update job information
                            updateJobInfo(jobListing,streetAddress);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Pass error response to update job information
                            updateJobInfo(jobListing, ERROR_ADDRESS_MESSAGE);
                        }
                    }
            );

            // Attach request to queue to be processed
            queue.add(request);

        } catch (NullPointerException nullPointerException) {
            // Pass error object and error response to update job information
            updateJobInfo(new JobListing(ERROR_TITLE_MESSAGE,ERROR_DESCRIPTION_MESSAGE),
                    ERROR_ADDRESS_MESSAGE);

            // TODO ADD MORE
            // TODO MAYBE
            throw new RuntimeException(nullPointerException);
        }
    }

    // Change visibility of progress bar and updates job information at bottom of activity
    private void updateJobInfo(JobListing jobListing, String streetAddress) {
        // TODO
        Log.d("test", jobListing.title + " " + streetAddress);

        // TODO MAY NOT NEED TO RUN ON UI THREAD
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Make info progress bar invisible
                pbInfo.setVisibility(View.INVISIBLE);

                // Update job information
                tvMapTitle.setText(jobListing.getTitle());
                tvMapAddress.setText(streetAddress);
                tvMapDescription.setText(jobListing.getDescription());
            }
        });
    }

    // Import data from postgreSQL database to populate ArrayList of JobListing objects.
    private ArrayList<JobListing> importMapData() {
        // Initialize ArrayList
        ArrayList<JobListing> jobListings = new ArrayList<>();

        // TODO REMOVE TESTING SECTION
        // Pull data from database
        String testResult = "0!@#Mowers NYC INC.!@#" +
                "Lawn care in NYC" +
                "!@#2500.0!@#Labor!@#40.7443679675679,-73.98867886292477";

        // Cycle through list of jobs
        String[] listArray = testResult.split("\\$%\\^");
        for (int i = 0; i < listArray.length; i++) {
            // Job string into id, job title, description, salary, category, LatLng
            String[] jobArray = listArray[i].split("!@#");

            // Split location values by commas
            String[] cordArray = jobArray[5].split(",");

            // Add new JobListing to job listz

            // I commented this out because i did this in the formatting class
//            jobListings.add(
//                    new JobListing(
//                            Integer.parseInt(jobArray[0])
//                            ,jobArray[1]
//                            ,jobArray[2]
//                            ,jobArray[4]
//                            ,Double.parseDouble(jobArray[3])
//                            ,new LatLng(Double.parseDouble(cordArray[0]),
//                                Double.parseDouble(cordArray[1]))
//                    )
//            );
        }

        // Return ArrayList
        return jobListings;
    }

    // Takes in a GoogleMap object, and ArrayList of JobListing objects, adds markers to map object
    private void updateMapData(GoogleMap map, ArrayList<JobListing> jobListings) {
        for (int i = 0; i < jobListings.toArray().length ; i++) {
            JobListing currentJobListing = jobListings.get(i);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(currentJobListing.getLocation()));
            marker.setTitle(currentJobListing.title);
            marker.setTag(currentJobListing);
        }
    }


}