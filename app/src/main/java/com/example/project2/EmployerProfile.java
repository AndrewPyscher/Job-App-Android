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

import java.util.ArrayList;
import java.util.Arrays;

public class EmployerProfile extends AppCompatActivity {

    public static final String TAG = "EMPLOYER_PROFILE";
    public static final String EMPTY_FIELD = ".";

    UseServer serverDAO;

    Button btnNewJob;
    RecyclerView rvApplications;
    ApplicationAdapter applicationAdapter;
    ArrayList<JobApplication> applications;


    int accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profile);

        accountID = getSharedPreferences("user", MODE_PRIVATE).getInt("id", -1);

        serverDAO = UseServer.getInstance(this);

        Log.d(TAG, "Account ID: "+accountID);

        // Find active account and initialize if necessary
        accountLookup();


        // --------------<<<   GET VIEWS   >>>-------------- \\

        btnNewJob = findViewById(R.id.btnNewJob);
        rvApplications = findViewById(R.id.rvApplications);

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

    }

    // --------------<<<   UTILITY METHODS   >>>-------------- \\

    /**
     * Set profile into "editing mode" where users can change the information
     * displayed on their profile page.
     */
    private void setProfileEditable() {
        // Hide Static Fields


        // Display Editable Fields


        // Populate fields w/ current text
    }

    /**
     * Set profile into "static mode" where elements are uneditable, and are
     * displayed just as an employer would see the profile.
     */
    private void setProfileStatic() {
        // Display Editable Fields

        // Hide Static Fields

        // Hide keyboard - Signifies editing finished
        hideKeyboard();
    }

    private void accountLookup() {
//        String result = "";
        serverDAO.verifyLogin(loginResponse -> {
            Log.d(TAG, "VERIFY LOGIN : "+loginResponse);

        });

        serverDAO.jobByEmployer(response -> {
            Log.d(TAG, response);
        }, accountID);
//
//        serverDAO.myAccount(accountResponse -> {
//            Log.d(TAG, "serverDAO onCreate: " + accountResponse);
//            // Check if current user is account owner
//            loadApplicants();
//        }, "");




//            accountID = Integer.parseInt(saveResponse.get().split(Formatting.DELIMITER_1)[0]);


        // If no information exists
//        if(accountID != -1 && result = null)
//            serverDAO.updateProfile(response -> {
//                Log.d(TAG, "UPDATE:");
//            },accountID,"","About Me","New User","","","","");

//        return saveResponse.get();
    }

    private void loadApplicants() {
//        Log.d(TAG,encodedString);
        String[] applicantData;
        try {
            applicantData = "encodedString".split(Formatting.DELIMITER_1);
        } catch (Exception e) {
            applicantData = new String[]{String.valueOf(accountID),EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD};
        }

        Log.d(TAG, Arrays.toString(applicantData));

        for (String item : applicantData) {
            Log.d(TAG, "{" + item + "}");
        }


        // Also address the button
//        if(!isAccountOwner)
//            btnEdit.setVisibility(View.INVISIBLE);
    }


    private String[] generateEncodedProfileData() {
        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  address: 1 |  aboutMe: 2      |  name: 3        | \\
        // |  phone: 4  |  email: 5   |  workHistory:  6 |  education : 7  | \\

        return new String[1];
    }

    /**
     * Hide keyboard if open
     */
    private void hideKeyboard() {
//        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(btnEdit.getApplicationWindowToken(),0);
    }
}