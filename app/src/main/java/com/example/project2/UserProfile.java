package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UserProfile extends AppCompatActivity {

    public static final String TAG = "USER_PROFILE";
    public static final String DELIMITER_CUSTOM_OUTER = "#@#";
    public static final String DELIMITER_CUSTOM_INNER = "@#@";
    public static final String EMPTY_FIELD = ".";
    ImageButton btnEdit, btnCancel;
    TextView txtName, txtDescription, txtPhone, txtEmail, txtAddress, txtExperience, txtEducation;
    EditText etName, etPhone, etAddress, etEmail, etDescription;
//    ListView lstExperience, lstEducation;
    RecyclerView rvEmployment, rvEducation;
    BottomNavigationView botNavBar;
    JobAdapter jobAdapter;
    EducationAdapter educationAdapter;
    UseServer serverDAO;
    AtomicReference<String> saveResponse;
    String viewedUserName;

    ArrayList<Job> jobHistory;
    ArrayList<School> educationHistory;

    int accountID, ownerID;
    boolean isAccountOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

//        accountID = getSharedPreferences("user", MODE_PRIVATE).getInt("id", -1);;
        accountID = User.id;
        ownerID = -1;
        isAccountOwner = false;

        viewedUserName = getIntent().getStringExtra("username");
        if(viewedUserName==null)
            viewedUserName="";


        Log.d(TAG, "Viewed USERNAME:"+viewedUserName);

        serverDAO = new UseServer(this, User.session);
        saveResponse = new AtomicReference<>();

        Log.d(TAG, "Account ID: "+accountID);

        // Find active account and initialize if necessary

//        String encodedStoredProfile = accountLookup();
        accountLookup();
//        Log.d(TAG, "accountLookupResult:" + encodedStoredProfile);
//        String[] profileData;
//        try {
//            profileData = encodedStoredProfile.split(Formatting.DELIMITER_1);
//        } catch (Exception e) {
//            profileData = new String[]{String.valueOf(accountID),"","","","","","",""};
//        }

        // --------------<<<   GET VIEWS   >>>-------------- \\

        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtExperience = findViewById(R.id.txtExperience);
        txtEducation = findViewById(R.id.txtEducation);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etDescription = findViewById(R.id.etDescription);
//        lstExperience = findViewById(R.id.lstExperience);
        rvEmployment = findViewById(R.id.rvEmployment);
//        lstEducation = findViewById(R.id.lstEducation);
        rvEducation = findViewById(R.id.rvEducation);
        botNavBar = findViewById(R.id.botNavBarProfile);


        // --------------<<<   LIST VIEW SECTION   >>>-------------- \\

        jobHistory = new ArrayList<>();

        jobAdapter = new JobAdapter(this, jobHistory);
        rvEmployment.setLayoutManager(new LinearLayoutManager(this));
        rvEmployment.setAdapter(jobAdapter);


        // ------ \\

        educationHistory = new ArrayList<>();

        educationAdapter = new EducationAdapter(this, educationHistory);
        rvEducation.setLayoutManager(new LinearLayoutManager(this));
        rvEducation.setAdapter(educationAdapter);
//        }



        // --------------<<<   LISTENERS   >>>-------------- \\

        // 'Edit' button dual-functions as 'Save' button
        btnEdit.setOnClickListener(v -> {
            if(etDescription.getVisibility() == View.INVISIBLE) {
                setProfileEditable();
                jobHistory.add(new Job(false));
                jobAdapter = new JobAdapter(this, jobHistory);
                rvEmployment.setAdapter(jobAdapter);

                educationHistory.add(new School(false));
                educationAdapter = new EducationAdapter(this, educationHistory);
                rvEducation.setAdapter(educationAdapter);
            }

            else {
                // Save Changes
                // TODO: Move saving to separate method call
                txtName.setText(etName.getText().toString().trim());
                txtPhone.setText(etPhone.getText().toString().trim());
                txtEmail.setText(etEmail.getText().toString().trim());
                txtAddress.setText(etAddress.getText().toString().trim());
                txtDescription.setText(etDescription.getText().toString().trim());

                jobAdapter.saveJobData();
                jobHistory.remove(jobHistory.size()-1);
                jobAdapter = new JobAdapter(this, jobHistory);
                rvEmployment.setAdapter(jobAdapter);
                if(jobHistory.size() == 0)
                    txtExperience.setText("Edit Profile to Add Work Experience");

                educationAdapter.saveSchoolData();
                educationHistory.remove(educationHistory.size()-1);
                educationAdapter = new EducationAdapter(this, educationHistory);
                rvEducation.setAdapter(educationAdapter);
                if(educationHistory.size() == 0)
                    txtEducation.setText("Edit Profile to Add Education History");

                // Get encoding of profile for backend
                String[] encodedProfile = generateEncodedProfileData();
                // Update profile
                serverDAO.updateProfile(response -> {
                    Log.d(TAG, "UPDATING PROFILE: "+response);
                }, Integer.parseInt(encodedProfile[0]),encodedProfile[1],encodedProfile[2],encodedProfile[3],
                        encodedProfile[4],encodedProfile[5],encodedProfile[6],encodedProfile[7]);

                Log.d(TAG, "-- Now showing detailed update info --");
                Log.d(TAG, encodedProfile[0]);
                Log.d(TAG, encodedProfile[1]);
                Log.d(TAG, encodedProfile[2]);
                Log.d(TAG, encodedProfile[3]);
                Log.d(TAG, encodedProfile[4]);
                Log.d(TAG, encodedProfile[5]);
                Log.d(TAG, encodedProfile[6]);
                Log.d(TAG, encodedProfile[7]);
                setProfileStatic();
            }
            JobAdapter.isEditable = !JobAdapter.isEditable;
            EducationAdapter.isEditable = !EducationAdapter.isEditable;
            jobAdapter.notifyDataSetChanged();
            educationAdapter.notifyDataSetChanged();
        });

        btnCancel.setOnClickListener(v -> {
            jobAdapter.cancelChanges();
            educationAdapter.cancelChanges();
            setProfileStatic();
            JobAdapter.isEditable = !JobAdapter.isEditable;
            EducationAdapter.isEditable = !EducationAdapter.isEditable;
            jobAdapter.notifyDataSetChanged();
            rvEmployment.setAdapter(jobAdapter);
            educationAdapter.notifyDataSetChanged();
            rvEducation.setAdapter(educationAdapter);
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
            } else if (id == R.id.settings) {
                Log.d(TAG, "SETTINGS INTENT");
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        botNavBar.setSelectedItemId(R.id.profile);
    }

    // --------------<<<   UTILITY METHODS   >>>-------------- \\

    /**
     * Set profile into "editing mode" where users can change the information
     * displayed on their profile page.
     */
    private void setProfileEditable() {
        // Hide Static Fields
        txtName.setVisibility(View.INVISIBLE);
        txtPhone.setVisibility(View.INVISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);
        txtAddress.setVisibility(View.INVISIBLE);
        txtDescription.setVisibility(View.INVISIBLE);


        // Display Editable Fields
        etName.setVisibility(View.VISIBLE);
        etPhone.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        etAddress.setVisibility(View.VISIBLE);
        etDescription.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnEdit.setImageResource(R.drawable.save_button_temp);


        // Populate fields w/ current text
        etName.setText(txtName.getText());
        etPhone.setText(txtPhone.getText());
        etEmail.setText(txtEmail.getText());
        etAddress.setText(txtAddress.getText());
        etDescription.setText(txtDescription.getText());
    }

    /**
     * Set profile into "static mode" where elements are uneditable, and are
     * displayed just as an employer would see the profile.
     */
    private void setProfileStatic() {
        // Display Editable Fields
        txtName.setVisibility(View.VISIBLE);
        txtPhone.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.VISIBLE);
        txtAddress.setVisibility(View.VISIBLE);
        txtDescription.setVisibility(View.VISIBLE);

        // Hide Static Fields
        etName.setVisibility(View.INVISIBLE);
        etPhone.setVisibility(View.INVISIBLE);
        etEmail.setVisibility(View.INVISIBLE);
        etAddress.setVisibility(View.INVISIBLE);
        etDescription.setVisibility(View.INVISIBLE);

        btnCancel.setVisibility(View.INVISIBLE);
        btnEdit.setImageResource(R.drawable.edit_button_temp);

        // Hide keyboard - Signifies editing finished
        hideKeyboard();
    }

    private void accountLookup() {
//        String result = "";
        serverDAO.verifyLogin(loginResponse -> {
            Log.d(TAG, "VERIFY LOGIN : "+loginResponse);

        });
        serverDAO.myAccount(accountResponse -> {
            Log.d(TAG, accountResponse);
            // Reset account if no formatting exists (including empty string)
            if(!accountResponse.contains(Formatting.DELIMITER_1)) {
                serverDAO.updateProfile(response -> {},accountID,".",".",".",".",".", ".", ".");
                return;
            }

            Log.d(TAG, "serverDAO onCreate: " + accountResponse);
            // Check if current user is account owner
            ownerID = Integer.parseInt(accountResponse.split(Formatting.DELIMITER_1)[0]);
            isAccountOwner = (accountID == ownerID);
            populateProfile(accountResponse);
        }, viewedUserName);
    }

    private void populateProfile(String encodedString) {
        Log.d(TAG,encodedString);
        String[] profileData;
        try {
            profileData = encodedString.split(Formatting.DELIMITER_1);
        } catch (Exception e) {
            profileData = new String[]{String.valueOf(ownerID),EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD,EMPTY_FIELD};
        }

        Log.d(TAG, Arrays.toString(profileData));

        for (String item : profileData) {
            Log.d(TAG, "{" + item + "}");
        }

        etName.setText(profileData[1].equals(EMPTY_FIELD) ? "" : profileData[1]);
        etPhone.setText(profileData[3].equals(EMPTY_FIELD) ? "" : profileData[3]);
        etEmail.setText(profileData[4].equals(EMPTY_FIELD) ? "" : profileData[4]);
        etAddress.setText(profileData[2].equals(EMPTY_FIELD) ? "" : profileData[2]);
        etDescription.setText(profileData[5].equals(EMPTY_FIELD) ? "" : profileData[5]);

        txtName.setText(profileData[1].equals(EMPTY_FIELD) ? "" : profileData[1]);
        txtPhone.setText(profileData[3].equals(EMPTY_FIELD) ? "" : profileData[3]);
        txtEmail.setText(profileData[4].equals(EMPTY_FIELD) ? "" : profileData[4]);
        txtAddress.setText(profileData[2].equals(EMPTY_FIELD) ? "" : profileData[2]);
        txtDescription.setText(profileData[5].equals(EMPTY_FIELD) ? "" : profileData[5]);

        if(profileData.length >= 7)
            if(profileData[6] != null && profileData[6].length()>1) {
                jobHistory = getJobHistoryFromDAO(profileData[6]);
                jobAdapter = new JobAdapter(this, jobHistory);
                rvEmployment.setAdapter(jobAdapter);

            }
        if(jobHistory.size() == 0 && isAccountOwner)
            txtExperience.setText("Edit Profile to Add Work Experience");
        else if(jobHistory.size() == 0 && !isAccountOwner)
            txtExperience.setVisibility(View.INVISIBLE);

        if(profileData.length >= 8)
            if(profileData[7] != null && profileData[7].length()>1) {
                educationHistory = getEducationHistoryFromDAO(profileData[7]);
                educationAdapter = new EducationAdapter(this, educationHistory);
                rvEducation.setAdapter(educationAdapter);
            }

        if(educationHistory.size() == 0 && isAccountOwner)
            txtEducation.setText("Edit Profile to Add Education History");
        else if(educationHistory.size() == 0 && !isAccountOwner)
            txtEducation.setVisibility(View.INVISIBLE);

        // Also address the button
        if(!isAccountOwner)
            btnEdit.setVisibility(View.INVISIBLE);
    }

    private ArrayList<Job> getJobHistoryFromDAO(String input) {
        // Create job container to be returned
        ArrayList<Job> jobs = new ArrayList<>();

        // Get list of job encodings
        String[] encodedJobs = new String[1];

        if(input.contains(DELIMITER_CUSTOM_OUTER))
            encodedJobs = input.split(DELIMITER_CUSTOM_OUTER);
        else
            encodedJobs[0] = input;

        // Iterate through encodings
        // Adding new jobs to ArrayList
        try {
            for (String curJobEncoding : encodedJobs) {
                String[] jobData = curJobEncoding.split(DELIMITER_CUSTOM_INNER);
                Log.d(TAG, Arrays.toString(jobData));
                // Create new job - Title + Company
                Job newJob = new Job(jobData[0],jobData[1]);

                // Manually add startDate if applicable
                if(jobData.length > 2 && jobData[2].length() > 1)
                    try {
                        newJob.setDateStart(new SimpleDateFormat("MM/yyyy").parse(jobData[2]));
                    } catch (Exception ignored) {}

                // Manually add endDate if applicable
                if(jobData.length > 3 && jobData[3].length() > 1)
                    try {
                        newJob.setDateStart(new SimpleDateFormat("MM/yyyy").parse(jobData[3]));
                    } catch (Exception ignored) {}

                // Add new job to ArrayList
                jobs.add(newJob);
            }
        } catch (Exception e) {
            Log.d(TAG, "Corrupt job encoding. Skipping...");
        }

        // Return list of decoded jobs :)
        return jobs;
    }

    private ArrayList<School> getEducationHistoryFromDAO(String input) {
        // Create job container to be returned
        ArrayList<School> schools = new ArrayList<>();

        // Get list of school encodings
        String[] encodedSchools = new String[1];

        if(input.contains(DELIMITER_CUSTOM_OUTER))
            encodedSchools = input.split(DELIMITER_CUSTOM_OUTER);
        else
            encodedSchools[0] = input;

        // Iterate through encodings
        // Adding new schools to ArrayList
        try {
            for (String curSchoolEncoding : encodedSchools) {
                String[] schoolData = curSchoolEncoding.split(DELIMITER_CUSTOM_INNER);

                // Create new job - Title + Company
                School newSchool = new School(schoolData[0],schoolData[1]);

                // Manually add gradDate if applicable
                if(schoolData[2].length() > 1)
                    try {
                        newSchool.setGraduationDate(new SimpleDateFormat("MM/yyyy").parse(schoolData[2]));
                    } catch (Exception ignored) {}

                // Add new job to ArrayList
                schools.add(newSchool);
            }
        } catch (Exception e) {
            Log.d(TAG, "Corrupt job encoding. Skipping...");
        }


        // Return list of decoded schools :)
        return schools;
    }

    private String[] generateEncodedProfileData() {
        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  address: 1 |  aboutMe: 2      |  name: 3        | \\
        // |  phone: 4  |  email: 5   |  workHistory:  6 |  education : 7  | \\

        String[] result = new String[8];

        result[0] = String.valueOf(accountID); // Gotta actually get this somehow...
        result[1] = txtAddress.getText().toString();
        result[2] = txtDescription.getText().toString();
        result[3] = txtName.getText().toString();
        result[4] = txtPhone.getText().toString();
        result[5] = txtEmail.getText().toString();

        // Insert placeholders if necessary
        if(result[1].equals("")) result[1] = EMPTY_FIELD;
        if(result[2].equals("")) result[2] = EMPTY_FIELD;
        if(result[3].equals("")) result[3] = EMPTY_FIELD;
        if(result[4].equals("")) result[4] = EMPTY_FIELD;
        if(result[5].equals("")) result[5] = EMPTY_FIELD;

        // Add jobHistory
        String encodedHistory = "";
        for (Job job : jobHistory) {
            encodedHistory += job;
            if(jobHistory.indexOf(job) != jobHistory.size()-1)
                encodedHistory += DELIMITER_CUSTOM_OUTER;
        }
        result[6] = encodedHistory;
        Log.d("Look", encodedHistory);

        // Add educationHistory
        String encodedEducation = "";
        for (School school : educationHistory) {
            encodedEducation += school;
            if(educationHistory.indexOf(school) != educationHistory.size()-1)
                encodedEducation += DELIMITER_CUSTOM_OUTER;
        }
        result[7] = encodedEducation;

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