package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class JobProfile extends AppCompatActivity {
    public static final String TAG = "JOB_PROFILE";
    public static final int IDX_ID = 0;
    public static final int IDX_TITLE = 1;
    public static final int IDX_DESCRIPTION = 2;
    public static final int IDX_SALARY = 3;
    public static final int IDX_ACTIVATED = 4;
    public static final int DATA_LENGTH = 5;
    ImageButton btnEdit, btnCancel;
    Button btnQuickApply;
    TextView txtTitle, txtDescription, txtSalary;
    EditText etTitle, etSalary, etDescription;
    Switch switchIsActive;
    SharedPreferences sharedPrefs;
    UseServer serverDAO;
    AtomicReference<String> saveResponse;
    int accountID;
    int employerID, jobID;
    boolean isAccountOwner;;
    String viewedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_profile);

        sharedPrefs = getSharedPreferences("user", MODE_PRIVATE);

        // Get saved account ID from login
        accountID = sharedPrefs.getInt("id", -1);
        Log.d(TAG, "Account ID: "+accountID);
        // Will get in populateProfile()
//        viewedID = -1;

        employerID = getIntent().getExtras().getInt("employerID", 0);
        jobID = getIntent().getExtras().getInt("jobID", 0);

        viewedUserName = getIntent().getStringExtra("username");

        isAccountOwner = (accountID == employerID);

        serverDAO = UseServer.getInstance(this);
        saveResponse = new AtomicReference<>();



        // Find active account and initialize blank profile if necessary
//        String encodedStoredProfile = accountLookup();
        accountLookup();
//        String[] profileData;
//        try {
//            profileData = encodedStoredProfile.split(Formatting.DELIMITER_1);
//        } catch (Exception e) {
//            profileData = new String[]{String.valueOf(accountID),"","","","false"};
//        }

        // --------------<<<   GET VIEWS   >>>-------------- \\

        btnEdit = findViewById(R.id.btnEditJob);
        btnCancel = findViewById(R.id.btnCancelEdit);
        btnQuickApply = findViewById(R.id.btnQuickApply);
        txtTitle = findViewById(R.id.txtJobTitle);
        txtDescription = findViewById(R.id.txtJobDescription);
        txtSalary = findViewById(R.id.txtJobSalary);
        etTitle = findViewById(R.id.etJobTitle);
        etSalary = findViewById(R.id.etJobSalary);
        etDescription = findViewById(R.id.etJobDescription);
        switchIsActive = findViewById(R.id.switchIsActive);

        // show switch ONLY if employer is viewing their own job
        switchIsActive.setVisibility(isAccountOwner ? View.VISIBLE : View.INVISIBLE);
        // show button ONLY if applicant is viewing job
        btnQuickApply.setVisibility(isAccountOwner ? View.INVISIBLE : View.VISIBLE);



        // --------------<<<   POPULATE VIEWS FROM BACKEND   >>>-------------- \\

        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  jobTitle: 1 |  jobDescription: 2 | \\
        // |  salary: 3  |  isActive: 4    |  workHistory:  5   | \\

//        txtTitle.setText(profileData[IDX_TITLE]);
//        txtDescription.setText(profileData[IDX_DESCRIPTION]);
//        txtSalary.setText(profileData[IDX_SALARY]);
//        switchIsActive.setChecked(Boolean.parseBoolean(profileData[IDX_ACTIVATED]));


        // --------------<<<   LISTENERS   >>>-------------- \\

        // 'Edit' button dual-functions as 'Save' button
        btnEdit.setOnClickListener(v -> {
            if(etDescription.getVisibility() == View.INVISIBLE)
                setProfileEditable();
            else {
                // Save Changes
                txtTitle.setText(etTitle.getText().toString().trim());
                txtDescription.setText(etDescription.getText().toString().trim());
                txtSalary.setText(etSalary.getText().toString().trim());

                // Get encoding of profile for backend
                String[] encodedProfile = generateEncodedProfileData();
                // Update profile
                // TODO: Actually make this data populate correctly
                serverDAO.updateJobPosting(response -> {
                            Log.d(TAG, "UPDATING PROFILE: "+response);
                        }, Integer.parseInt(encodedProfile[IDX_ID]),encodedProfile[IDX_TITLE],encodedProfile[IDX_DESCRIPTION]
                        ,encodedProfile[IDX_SALARY], Boolean.parseBoolean(encodedProfile[IDX_ACTIVATED]));

                Log.d(TAG, "-- Now showing detailed update info --");
                Log.d(TAG, encodedProfile[IDX_ID]);
                Log.d(TAG, encodedProfile[IDX_TITLE]);
                Log.d(TAG, encodedProfile[IDX_DESCRIPTION]);
                Log.d(TAG, encodedProfile[IDX_SALARY]);
                Log.d(TAG, encodedProfile[IDX_ACTIVATED]);

                setProfileStatic();
            }
        });

        btnCancel.setOnClickListener(v -> setProfileStatic());

        // TODO: Where is this message coming from
        btnQuickApply.setOnClickListener(v -> {
            serverDAO.insertApplication(response -> {
                Log.d(TAG, "Inserted Application. Response: "+response);
            }, jobID, accountID, "Test Application Message");
        });
    }

    // --------------<<<   UTILITY METHODS   >>>-------------- \\

    /**
     * Set profile into "editing mode" where users can change the information
     * displayed on their profile page.
     */
    private void setProfileEditable() {
        // Hide Static Fields
        txtTitle.setVisibility(View.INVISIBLE);
        txtSalary.setVisibility(View.INVISIBLE);
        txtDescription.setVisibility(View.INVISIBLE);


        // Display Editable Fields
        etTitle.setVisibility(View.VISIBLE);
        etSalary.setVisibility(View.VISIBLE);
        etDescription.setVisibility(View.VISIBLE);

        switchIsActive.setVisibility(View.VISIBLE);

        btnCancel.setVisibility(View.VISIBLE);
        btnEdit.setImageResource(R.drawable.save_button_temp);


        // Populate fields w/ current text
        etTitle.setText(txtTitle.getText());
        etSalary.setText(txtSalary.getText());
        etDescription.setText(txtDescription.getText());
    }

    /**
     * Set profile into "static mode" where elements are uneditable, and are
     * displayed just as an employer would see the profile.
     */
    private void setProfileStatic() {
        // Hide Static Fields
        etTitle.setVisibility(View.INVISIBLE);
        etDescription.setVisibility(View.INVISIBLE);
        etSalary.setVisibility(View.INVISIBLE);

        switchIsActive.setVisibility(View.INVISIBLE);

        btnCancel.setVisibility(View.INVISIBLE);
        btnEdit.setImageResource(R.drawable.edit_button_temp);

        // Display Editable Fields
        txtTitle.setVisibility(View.VISIBLE);
        txtDescription.setVisibility(View.VISIBLE);
        txtSalary.setVisibility(View.VISIBLE);

        // Hide keyboard - Signifies editing finished
        hideKeyboard();
    }

    private void accountLookup() {
//        String username = "";
//        serverDAO.verifyLogin(loginResponse -> {
//            Log.d(TAG, "VERIFY LOGIN : "+loginResponse);
//            serverDAO.myAccount(accountResponse -> {
//                Log.d(TAG, "serverDAO onCreate: " + accountResponse);
//                // Check if current user is account owner
//                viewedID = Integer.parseInt(accountResponse.split(Formatting.DELIMITER_1)[0]);
//                isAccountOwner = (accountID == viewedID);
////            serverDAO.job
//
//
//            }, viewedUserName);
//        });

        serverDAO.verifyLogin(loginResponse -> {
            Log.d(TAG, "VERIFY LOGIN : "+loginResponse);
            serverDAO.oneJob(jobResponse -> {
                Log.d(TAG, "Job Response:"+jobResponse);
                Log.d("STOP","");
                populateProfile(jobResponse);
            }, jobID);
        });


        // TODO: Find a way to check applicant/employer viewing page


//        result = saveResponse.get();

        // If no information exists
//        if(accountID != -1 && result = null)
//            serverDAO.updateProfile(response -> {
//                Log.d(TAG, "UPDATE:");
//            },accountID,"","About Me","New User","","","","");
    }

    private void populateProfile(String encodedString) {
        try {
            Log.d(TAG,encodedString);
            String[] profileData;
            try {
                profileData = encodedString.split(Formatting.DELIMITER_1);
            } catch (Exception e) {
                profileData = new String[]{String.valueOf(accountID),"","","",""};
            }

            Log.d(TAG, Arrays.toString(profileData));

            for (String item : profileData)
                Log.d(TAG, "{" + item + "}");

            // Check if current user is account owner
            isAccountOwner = (accountID == Integer.parseInt(profileData[IDX_ID]));

            etTitle.setText(profileData[IDX_TITLE]);
            etDescription.setText(profileData[IDX_DESCRIPTION]);
            etSalary.setText(profileData[IDX_SALARY]);

            switchIsActive.setChecked(Boolean.parseBoolean(profileData[IDX_ACTIVATED]));

            txtTitle.setText(profileData[IDX_TITLE]);
            txtDescription.setText(profileData[IDX_DESCRIPTION]);
            txtSalary.setText(profileData[IDX_SALARY]);

            // Also address the button
            if(!isAccountOwner)
                btnEdit.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.d(TAG, "Profile Encoding Broken. Resetting Page");
            txtTitle.setText("");
            txtDescription.setText("");
            txtSalary.setText("");
        }
    }

    private String[] generateEncodedProfileData() {
        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  jobName: 1   |  description: 2  |   \\
        // | salary: 3  |  isActive: 4  |                      \\

        String[] result = new String[DATA_LENGTH];

        result[IDX_ID] = String.valueOf(accountID); // Gotta actually get this somehow...
        result[IDX_TITLE] = txtTitle.getText().toString();
        result[IDX_DESCRIPTION] = txtDescription.getText().toString();

        result[IDX_SALARY] = txtSalary.getText().toString(); // Salary
        result[IDX_ACTIVATED] = String.valueOf(switchIsActive.isActivated()); // isActive(?)

        return result;
    }

    /**
     * Hide keyboard if open
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btnEdit.getApplicationWindowToken(),0);
    }
}