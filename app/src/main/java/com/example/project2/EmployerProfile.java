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

        Log.d(TAG, "Account ID: "+User.id);

        // Find active account and initialize if necessary
        accountLookup();

        // --------------<<<   GET VIEWS   >>>-------------- \\

        txtProfileCompanyName = findViewById(R.id.txtProfileCompanyName);
        btnNewJob = findViewById(R.id.btnNewJob);
        rvApplications = findViewById(R.id.rvApplications);
        botNavBar = findViewById(R.id.botNavBarEmployerProfile);

        serverDAO.getCompanyName(response -> txtProfileCompanyName.setText(response),User.id);


        // Basically exists in case nothing is populated
        applications = new ArrayList<>();

        applicationAdapter = new ApplicationAdapter(this, applications);
        rvApplications.setLayoutManager(new LinearLayoutManager(this));
        rvApplications.setAdapter(applicationAdapter);



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


    private void accountLookup() {
//        String result = "";
        serverDAO.verifyLogin(loginResponse -> {
            Log.d(TAG, "VERIFY LOGIN : "+loginResponse);
            serverDAO.getEmployerApplications(response -> {
                Log.d(TAG, response);
            }, User.id);
        });
    }

    private void loadApplicants() {
//        Log.d(TAG,encodedString);
        String[] applicantData;
        try {
            applicantData = "encodedString".split(Formatting.DELIMITER_1);
        } catch (Exception e) {
            applicantData = new String[]{String.valueOf(User.id),EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD};
        }

        Log.d(TAG, Arrays.toString(applicantData));

        for (String item : applicantData) {
            Log.d(TAG, "{" + item + "}");
        }
    }

    private String[] generateEncodedProfileData() {
        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  address: 1 |  aboutMe: 2      |  name: 3        | \\
        // |  phone: 4  |  email: 5   |  workHistory:  6 |  education : 7  | \\

        return new String[1];
    }
}