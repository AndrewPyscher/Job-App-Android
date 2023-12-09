package com.example.project2;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class EmployerProfile extends AppCompatActivity {

    public static final String TAG = "EMPLOYER_PROFILE";
    public static final String EMPTY_FIELD = ".";

    UseServer serverDAO;

    TextView txtProfileCompanyName;
    Button btnNewJob;
    BottomNavigationView botNavBar;
    RecyclerView rvApplications;
    ApplicationAdapter applicationAdapter;
    ArrayList<JobApplication> applications;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);


        serverDAO = UseServer.getInstance(this);


        // Find active account and initialize if necessary
        accountLookup();


        // --------------<<<   GET VIEWS   >>>-------------- \\

        txtProfileCompanyName = findViewById(R.id.txtProfileCompanyName);
        btnNewJob = findViewById(R.id.btnNewJob);
        rvApplications = findViewById(R.id.rvApplications);
        botNavBar = findViewById(R.id.botNavBarEmployerProfile);

        rvApplications.setLayoutManager(new LinearLayoutManager(this));

        serverDAO.getCompanyName(response -> txtProfileCompanyName.setText(response), User.id);


        // Basically exists in case nothing is populated




        // --------------<<<   LISTENERS   >>>-------------- \\

        btnNewJob.setOnClickListener(v -> {
            Intent i = new Intent(this, JobCreation.class);
            startActivityForResult(i,1);
        });

        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                Intent i = new Intent(this, activity_jobs.class);
                startActivity(i);
                return true;
            } else if (id == R.id.search) {
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);
                return true;
            } else if (id == R.id.profile) {
                //You are here
                return true;
            } else if(id == R.id.settings){
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            } else if (id == R.id.Notifs) {
                Intent i = new Intent(this, ApplicationStatus.class);
                startActivity(i);
                return true;
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        botNavBar.setSelectedItemId(R.id.profile);
    }

    // --------------<<<   UTILITY METHODS   >>>-------------- \\

    /**
     * Find User's account and populate views accordingly
     */
    private void accountLookup() {
//        String result = "";
        Log.d(TAG, "UID:"+User.id);
        serverDAO.verifyLogin(loginResponse -> {
            Log.d(TAG, "VERIFY LOGIN : "+loginResponse);
            serverDAO.getEmployerApplications(response -> {
                Log.d(TAG, "Applications:{" +response+"}");
                loadApplicants(response);
            }, User.id);
        });
    }

    /**
     * Load list of pending applications for the employer
     * @param input Encoded applications string
     */
    private void loadApplicants(String input) {
        Context context = this;
        // Initialize JobApplication ArrayList (for adapter)
        applications = new ArrayList<>();
        // Store half-parsed applicant data
        String[] applicantData;
        try {
            applicantData = input.split(Formatting.DELIMITER_2);
        } catch (Exception e) {
            applicantData = new String[]{EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD};
        }

        Log.d(TAG, Arrays.toString(applicantData));

        for (String application : applicantData) {
            Log.d(TAG, "{" + application + "}");

            // If no data exists, exit
            if(application.equals(""))
                return;

            // Actually parsed applicant data
            String[] parsedApplication = application.split(Formatting.DELIMITER_1);
            // If application is complete, don't show it
            if(!parsedApplication[2].equals("pending"))
                continue;

            // Get job information (just for job title)
            serverDAO.oneJob(response -> {
                Log.d(TAG, "OneJob:"+response);
                // Add a new application to recyclerView arraylist
                applications.add(new JobApplication(
                        Integer.parseInt(parsedApplication[0]),
                        parsedApplication[3],
                        Integer.parseInt(parsedApplication[1]),
                        response.split(Formatting.DELIMITER_1)[1]));
                Log.d(TAG, ""+applications.get(0));
                applicationAdapter = new ApplicationAdapter(context, applications);
                rvApplications.setAdapter(applicationAdapter);
            }, Integer.parseInt(parsedApplication[1]));

        }
        Log.d(TAG,Arrays.toString(applications.toArray()));
    }
}