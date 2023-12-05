package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UserProfile extends AppCompatActivity {

    public static final String TAG = "USER_PROFILE";
    public static final String DELIMITER_CUSTOM_OUTER = "#@#";
    public static final String DELIMITER_CUSTOM_INNER = "@#@";
    ImageButton btnEdit, btnCancel;
    TextView txtName, txtDescription, txtPhone, txtEmail , txtExperience, txtEducation;
    EditText etName, etPhone, etEmail, etDescription;
//    ListView lstExperience, lstEducation;
    RecyclerView rvEmployment, rvEducation;
    JobAdapter jobAdapter;
    EducationAdapter educationAdapter;
    UseServer serverDAO;
    AtomicReference<String> saveResponse;

    ArrayList<Job> jobHistory;
    ArrayList<School> educationHistory;

    int accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        accountID = -1;

        serverDAO = UseServer.getInstance(this);
        saveResponse = new AtomicReference<>();

        accountID = getSharedPreferences("user", MODE_PRIVATE).getInt("id", -1);
        Log.d(TAG, "Account ID: "+accountID);

        // Find active account and initialize if necessary

        String encodedStoredProfile = accountLookup();
        String[] profileData;
        try {
            profileData = encodedStoredProfile.split(Formatting.DELIMITER_1);
        } catch (Exception e) {
            profileData = new String[]{String.valueOf(accountID),"","","","","","",""};
        }

        // --------------<<<   GET VIEWS   >>>-------------- \\

        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtExperience = findViewById(R.id.txtExperience);
        txtEducation = findViewById(R.id.txtEducation);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etDescription = findViewById(R.id.etDescription);
//        lstExperience = findViewById(R.id.lstExperience);
        rvEmployment = findViewById(R.id.rvEmployment);
//        lstEducation = findViewById(R.id.lstEducation);
        rvEducation = findViewById(R.id.rvEducation);

        // --------------<<<   POPULATE VIEWS FROM BACKEND   >>>-------------- \\

        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  address: 1 |  aboutMe: 2      |  name: 3        | \\
        // |  phone: 4  |  email: 5   |  workHistory:  6 |  education : 7  | \\

        txtName.setText(profileData[3]);
        txtPhone.setText(profileData[4]);
        txtEmail.setText(profileData[5]);
        txtDescription.setText(profileData[2]);


        // --------------<<<   LIST VIEW SECTION   >>>-------------- \\

        // TODO: Get the user's real job history from backend
        jobHistory = new ArrayList<>();
        jobHistory.add(new Job("Shift Manager", "McDonalds"));
        jobHistory.add(new Job("Frying Cook", "Krusty Krab", new Date(122,7,24), new Date(123,4,4)));
        // Use as backup somehow???

        jobHistory = getJobHistoryFromDAO(profileData[6]);

        Log.d(TAG, "Size of jobHistory: " + jobHistory.size());
        Log.d(TAG, "Contents of jobHistory: " + jobHistory);

        // ------ \\

//        txtExperience.setVisibility(View.INVISIBLE);
//        rvEmployment.setAdapter(jobAdapter);
        jobAdapter = new JobAdapter(this, jobHistory);
        rvEmployment.setLayoutManager(new LinearLayoutManager(this));
        rvEmployment.setAdapter(jobAdapter);


        // ------ \\

        educationHistory = new ArrayList<>();
        educationHistory.add(new School("Saginaw Valley State University", "Computer Science", new Date(124,4,4)));
        educationHistory.add(new School("Georgia Tech", "Computer Science", new Date(125,4,4)));

        // Get Education History for User
        educationHistory = getEducationHistoryFromDAO(profileData[7]);

        Log.d(TAG, "Size of educationHistory: " + educationHistory.size());
        Log.d(TAG, "Contents of educationHistory: " + educationHistory);

//        if(educationHistory.size() == 0)
//            txtEducation.setVisibility(View.INVISIBLE);
//        else {
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
                txtDescription.setText(etDescription.getText().toString().trim());

                jobAdapter.saveJobData();
                jobHistory.remove(jobHistory.size()-1);
                jobAdapter = new JobAdapter(this, jobHistory);
                rvEmployment.setAdapter(jobAdapter);

                educationAdapter.saveSchoolData();
                educationHistory.remove(educationHistory.size()-1);
                educationAdapter = new EducationAdapter(this, educationHistory);
                rvEducation.setAdapter(educationAdapter);

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
        txtDescription.setVisibility(View.INVISIBLE);


        // Display Editable Fields
        etName.setVisibility(View.VISIBLE);
        etPhone.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        etDescription.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnEdit.setImageResource(R.drawable.save_button_temp);


        // Populate fields w/ current text
        etName.setText(txtName.getText());
        etPhone.setText(txtPhone.getText());
        etEmail.setText(txtEmail.getText());
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
        txtDescription.setVisibility(View.VISIBLE);

        // Hide Static Fields
        etName.setVisibility(View.INVISIBLE);
        etPhone.setVisibility(View.INVISIBLE);
        etEmail.setVisibility(View.INVISIBLE);
        etDescription.setVisibility(View.INVISIBLE);

        btnCancel.setVisibility(View.INVISIBLE);
        btnEdit.setImageResource(R.drawable.edit_button_temp);

        // Hide keyboard - Signifies editing finished
        hideKeyboard();
    }

    private void callbackFnct() {
        callbackFnct(0);
    }

    private void callbackFnct(int n) {
        AtomicBoolean isFinished = new AtomicBoolean(false);
        serverDAO.myAccount(response -> {
            if (response == null) return;
            Log.d(TAG, "serverDAO onCreate: " + response);
            saveResponse.set(response);
            isFinished.set(true);
        }, "");
        if(n < 15 && !isFinished.get())
            callbackFnct(n+1);
    }

    private String accountLookup() {
        String result = "";
        serverDAO.verifyLogin(response -> {
            Log.d(TAG, "VERIFY LOGIN : "+response);
        });

        serverDAO.myAccount(response -> {
            Log.d(TAG, "serverDAO onCreate: " + response);
            saveResponse.set(response);
        }, "");

        // Get account ID if possible
        if(saveResponse.get() != null && !saveResponse.get().equals(""))
            accountID = Integer.parseInt(saveResponse.get().split(Formatting.DELIMITER_1)[0]);

        result = saveResponse.get();

        // If no information exists
//        if(accountID != -1 && result = null)
//            serverDAO.updateProfile(response -> {
//                Log.d(TAG, "UPDATE:");
//            },accountID,"","About Me","New User","","","","");

        return result;
    }

    private ArrayList<Job> getJobHistoryFromDAO(String input) {
        // Create job container to be returned
        ArrayList<Job> jobs = new ArrayList<>();

        // Get list of job encodings
        String[] encodedJobs = input.split(DELIMITER_CUSTOM_OUTER);

        // Iterate through encodings
        // Adding new jobs to ArrayList
        try {
            for (String curJobEncoding : encodedJobs) {
                String[] jobData = curJobEncoding.split(DELIMITER_CUSTOM_INNER);

                // Create new job - Title + Company
                Job newJob = new Job(jobData[0],jobData[1]);

                // Manually add startDate if applicable
                if(jobData[2].length() > 1)
                    try {
                        newJob.setDateStart(new SimpleDateFormat("MM/yyyy").parse(jobData[2]));
                    } catch (Exception ignored) {}

                // Manually add endDate if applicable
                if(jobData[3].length() > 1)
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
        String[] encodedSchools = input.split(DELIMITER_CUSTOM_OUTER);

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
        result[1] = "myAddress";
        result[2] = txtDescription.getText().toString();
        result[3] = txtName.getText().toString();
        result[4] = txtPhone.getText().toString();
        result[5] = txtEmail.getText().toString();

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