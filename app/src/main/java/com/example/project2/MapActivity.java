package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

// TODO WAITING FOR
// RATING !!!!!!!!!!!!!!! ADDED TO LISTING BUT NEED TO TEST WITH DEMO DATA

// UPDATE JOB LISTINGS TO USE GET AND SET METHODS

// UPDATE INFOWINDOW TO BE HAVE DIFFERENCE FOR ACTIVE AND INACTIVE

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Formatting class instance
    Formatting formatting;

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
    Spinner sPrimary, sSecondary;
    BottomNavigationView navigationMenu;

    // Constant string message
    private final String ERROR_TITLE_MESSAGE = "Error find job listing",
            ERROR_ADDRESS_MESSAGE = "ERROR generating street address",
            ERROR_DESCRIPTION_MESSAGE = "Please refresh map data and retry";

    // Constant filtering strings
    private final String JOBS_ALL = "All Jobs", JOBS_ACTIVE = "Active Jobs",
            JOBS_INACTIVE = "Inactive Jobs", JOBS_CATEGORY = "Category",
            JOBS_EMPLOYER = "Employer ID", DEFAULT_NONE_VALUE = "-";

    // Constant database error value
    private final String ERROR_DATABASE = "Access Denied";

    // List for Spinner, either all, active, or inactive jobs
    private final String[] primarySpinnerList = new String[]{JOBS_ALL, JOBS_ACTIVE, JOBS_INACTIVE,
            JOBS_CATEGORY, JOBS_EMPLOYER};

    // List for Spinner, provides additional sorting
    private ArrayList<String> secondarySpinnerList = new ArrayList<>();

    // List for storing job listings, categories, and employer id
    private ArrayList<JobListing> allJobListings = new ArrayList<>();
    private ArrayList<JobListing> jobList = new ArrayList<>();
    private ArrayList<String> categoriesList = new ArrayList<>();
    private ArrayList<String> employerIdList = new ArrayList<>();

    // Flag for determining if map is ready to be updated
    private boolean mapReady = false;
    private int primaryFilter, secondaryFilter;

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

        sPrimary = findViewById(R.id.sMapFilterPrimary);
        sSecondary = findViewById(R.id.sMapFilterSecondary);

        navigationMenu = findViewById(R.id.navBar);

        // Initialize formatting class instance
        formatting = new Formatting();

        // Update default values to category, employer id, and secondary spinner list
        secondarySpinnerList.add(DEFAULT_NONE_VALUE);

        // Set up bottom navigation menu listener
        // TODO NEED TO SET UP LOGIC AND ICONS FOR OTHER ACTIVITIES
        navigationMenu.setOnItemSelectedListener(item -> {
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

        // Set up spinner adapter, onSelected listener, and default values
        ArrayAdapter<String> primaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,primarySpinnerList),
                secondaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,secondarySpinnerList);
        sPrimary.setAdapter(primaryAdapter);
        sPrimary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long l) {
                // Set primary filtering value
                primaryFilter = index;

                // Check if filter doesn't require another input from second filter
                String filterType = (String) sPrimary.getItemAtPosition(index);
                if (Objects.equals(filterType, JOBS_ALL) ||
                        Objects.equals(filterType, JOBS_ACTIVE) ||
                        Objects.equals(filterType, JOBS_INACTIVE)) {

                    // Make secondary filter spinner inaccessible
                    sSecondary.setClickable(false);

                    // Update info
                    updatedFilter();
                } else {
                    // Make secondary filter spinner accessible
                    sSecondary.setClickable(true);
                }

//                // Set secondary spinner to be default value
//                sSecondary.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        sSecondary.setAdapter(secondaryAdapter);
        sSecondary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long l) {
                // Set secondary filtering value
                secondaryFilter = index;

                // Update map filter if not default value
                if (secondaryFilter != 0) {
                    updatedFilter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Make secondary filter spinner inaccessible
        sSecondary.setClickable(false);

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
                    // TODO CHECK FOR PROFILE to set up phone or email to allow apply SHARED PREFERENCES
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
        // Set store google map object to returned google map object
        map = googleMap;

        // Load in initial job and rating information and populate all job listings list
        initialInfoLoading();

        // Set primary spinner to initial selection of default (all jobs)
        sPrimary.setSelection(0);

        // Set custom info window adapter
        map.setInfoWindowAdapter(new JobInfoWindow(this, allJobListings));

        // Update map ready flag
        mapReady = true;

        // TODO Set camera position based on ? AVG OF LOCATIONS? USE ALLJOBLISTINGS LATLONG OBJECT
        // Set map camera settings

        // Make secondary filter spinner accessible
        sSecondary.setClickable(true);

        // Make info progress bar invisible
        pbMap.setVisibility(View.INVISIBLE);

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

                // Pass marker to method to pull information and update display
                getJobInfo(marker);
            }
        });

    }

    // Takes in marker object, request a street address from google map's revers geocaching api,
    // place request into queue, when response or error returns then create string message either
    // containing address or error message and pass to update job information method.
    private void getJobInfo(Marker marker) {
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
                                // Pull address string from response
                                streetAddress = response.getJSONArray("results").
                                        getJSONObject(0).getString("formatted_address");

                            } catch (JSONException e) {
                                // Set address value to error value
                                streetAddress = ERROR_ADDRESS_MESSAGE;
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
            // TODO FIGURE OUT WHAT IS NEEDED
            throw new RuntimeException(nullPointerException);
        }
    }

    // Change visibility of progress bar and updates job information at bottom of activity
    private void updateJobInfo(JobListing jobListing, String streetAddress) {
        // TODO MAY NOT NEED TO RUN ON UI THREAD
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update job information
                tvMapTitle.setText(jobListing.getTitle());
                tvMapAddress.setText(streetAddress);
                tvMapDescription.setText(jobListing.getDescription());

                // Make info progress bar invisible
                pbInfo.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Takes in filter values, clears working job list and map, then based on those values calls
    // different import methods from database which in turn calls update map methods
    private void updatedFilter() {
        // Remove old map markers and job list
        jobList.clear();
        map.clear();

        // Check primary filtering and set array list of jobs
        switch (primarySpinnerList[primaryFilter]) {
            case JOBS_ALL: {
                // Filter for all jobs
                importMapDataAll("all");

                break;
            }
            case JOBS_ACTIVE: {
                // Filter for all active jobs
                importMapDataAll("");

                break;
            }
            case JOBS_INACTIVE: {
                // Filter for all inactive jobs
                importMapDataAll("false");

                break;
            }
            case JOBS_CATEGORY: {
                String filterValue = secondarySpinnerList.get(secondaryFilter);

                // Check if filter value is set to default value, skip if so
                if (!filterValue.equals(DEFAULT_NONE_VALUE)) {
                    // Filter for jobs with the same category
                    importMapDataByCategory(filterValue);
                }

                break;
            }
            case JOBS_EMPLOYER: {
                String filterValue = secondarySpinnerList.get(secondaryFilter);

                // Check if filter value is set to default value, skip if so
                if (!filterValue.equals(DEFAULT_NONE_VALUE)) {
                    // Filter for jobs with the same employer id
                    importMapDataByEmployerId(Integer.parseInt(filterValue));
                }

                break;
            }
        }
    }

    // Import job listings for map based on no filter values
    private void importMapDataAll(String filter) {
        // Create class object for importing data from database
        UseServer useServer = new UseServer(this);

        // Pull all jobs from response
        useServer.allJobs(new HandleResponse() {
            @Override
            public void response(String response) {
                // Check if response is null
                if (response != null) {
                    // Pass to formatting class to convert string to array list, then pass list to update map method
                    updateMapData(formatting.recieveJob(response));
                } else {
                    // Nothing returned in response
                    // TODO FIGURE OUT WHAT IS NEEDED
                }
            }
        }, filter);
    }

    // Import job listings for map based on category filter values
    private void importMapDataByCategory(String category) {
        // Create class object for importing data from database
        UseServer useServer = new UseServer(this);

        // Pull all jobs from response
        useServer.getJobsForCategory(new HandleResponse() {
            @Override
            public void response(String response) {
                // Check if response is null
                if (response != null) {
                    // Pass to formatting class to convert string to array list, then pass list to update map method
                    updateMapData(formatting.recieveJob(response));

                } else {
                    // Nothing returned in response
                    // TODO FIGURE OUT WHAT IS NEEDED
                }
            }
        }, category);
    }

    // Import job listings for map based on employer id filter values
    private void importMapDataByEmployerId(int employerId) {
        // Create class object for importing data from database
        UseServer useServer = new UseServer(this);

        // Pull all jobs from response
        useServer.jobByEmployer(new HandleResponse() {
            @Override
            public void response(String response) {
                // Check if response is null
                if (response != null) {
                    // Pass to formatting class to convert string to array list, then pass list to update map method
                    updateMapData(formatting.recieveJob(response));
                } else {
                    // Nothing returned in response
                    // TODO FIGURE OUT WHAT IS NEEDED
                }
            }
        }, employerId);
    }

    // Import all job information from database, pull filtering lists for category and employer
    // ids, then populate all job list from data.
    private void initialInfoLoading() {
        // Create class object for importing data from database
        UseServer useServer = new UseServer(this);

        // Pull all jobs from response
        useServer.allJobs(new HandleResponse() {
            @Override
            public void response(String response) {
                // Check if response is null
                if (response != null) {
                    // Split response and cycle through list of jobs pulled from response string
                    String[] listArray = response.split(formatting.delimiter2);
                    for (int i = 0; i < listArray.length; i++) {
                        // Job string into id, job title, description, salary, category, LatLng
                        String[] jobArray = listArray[i].split(formatting.delimiter);

                        // TODO MIGHT NEED TO ACCESS WHERE TO PUT CAMERA
//                        // Split location values by commas
//                        String[] cordArray = jobArray[5].split(",");

                        // Check if employer id is already added to category list
                        if (!employerIdList.contains(jobArray[1])) {
                            // If not added then add to employer list
                            employerIdList.add(jobArray[1]);
                        }

                        // Check if category is already added to category list
                        if (!categoriesList.contains(jobArray[5])) {
                            // If not added then add to list
                            categoriesList.add(jobArray[5]);
                        }
                    }

                    // Update all job listings from formatting
                    allJobListings = formatting.recieveJob(response);

                    // TODO COMMENETING OUT FOR TESTING
//                    // Load ratings
//                    loadRatings();

                } else {
                    // Nothing returned in response
                    // TODO FIGURE OUT WHAT IS NEEDED
                }

            }
        }, "all");
    }

    // TODO WILL STILL NEED TO TEST
    // Loop through all job listings list for each job listing, checking for matching employer id's,
    // if found then pulling rating info from that job listing else pull information from database.
    private void loadRatings() {
        for (int i = 0; i < allJobListings.size(); i++) {
            // Store job listing information
            JobListing jobListing = allJobListings.get(i);
            int rating = -1;

            // Search for prev job listing and pull rating from job listing
            for (int j = 0; j < allJobListings.size(); j++) {
                // Check if employer id matches prev job listing and it isn't the same job listing
                if (allJobListings.get(j).employer_id == jobListing.employer_id &&
                        !jobListing.equals(allJobListings.get(j))) {
                    // Set rating values to same as job listing
                    rating = allJobListings.get(j).getRating();

                    // Break for loop
                    break;
                }
            }

            // Check if rating was found from perv job listings
            if (rating != -1) {
                // Rating found
                jobListing.setRating(rating);
            } else {
                // Rating not found, pull rating from database
                getRating(jobListing);
            }
        }
    }

    // TODO NEED TO FIGURE OUT WHERE TO PUT
    private void getRating(JobListing jobListing) {
        // Create class object for importing data from database
        UseServer useServer = new UseServer(this);

        // Pull and compute ratings for employee id
        useServer.companyReviews((new HandleResponse() {
            @Override
            public void response(String response) {
                // Check if response is null
                if (response != null) {
                    // Pass to formatting class to convert employer id and then set job listing rating
                    // TODO TESTING
                    jobListing.setRating(formatting.receiveRating(response));

                } else {
                    // Nothing returned in response
                    jobListing.setRating(-1);

                }
            }
        }), jobListing.employer_id);
    }

    // Takes in a GoogleMap object, and ArrayList of JobListing objects, adds markers to map object
    private void updateMapData(ArrayList<JobListing> jobListings) {
        for (int i = 0; i < jobListings.toArray().length ; i++) {
            JobListing currentJobListing = jobListings.get(i);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(currentJobListing.getLocation()));
            marker.setTitle(currentJobListing.title);
            marker.setTag(currentJobListing);
        }
    }

}