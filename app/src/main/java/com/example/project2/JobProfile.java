package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicReference;

public class JobProfile extends AppCompatActivity {
    public static final String TAG = "JOB_PROFILE";
    ImageButton btnEdit, btnCancel;
    TextView txtJobName, txtDescription, txtPhone, txtEmail;
    EditText etPhone, etEmail, etDescription;
    UseServer serverDAO;
    AtomicReference<String> saveResponse;
    int accountID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_profile);

        // Account unable to be lcoated
        // Default value
        accountID = -1;

        serverDAO = UseServer.getInstance(this);
        saveResponse = new AtomicReference<>();

        // Get saved account ID from login
        accountID = getSharedPreferences("user", MODE_PRIVATE).getInt("id", -1);
        Log.d(TAG, "Account ID: "+accountID);

        // Find active account and initialize blank profile if necessary
        String encodedStoredProfile = accountLookup();
        String[] profileData;
        try {
            profileData = encodedStoredProfile.split(Formatting.DELIMITER_1);
        } catch (Exception e) {
            profileData = new String[]{String.valueOf(accountID),"","","","","","",""};
        }

        // --------------<<<   GET VIEWS   >>>-------------- \\

        btnEdit = findViewById(R.id.btnEditJob);
        btnCancel = findViewById(R.id.btnCancelEdit);
        txtJobName = findViewById(R.id.txtJobName);
        txtDescription = findViewById(R.id.txtJobDescription);
        txtPhone = findViewById(R.id.txtJobPhone);
        txtEmail = findViewById(R.id.txtJobEmail);
        etPhone = findViewById(R.id.etJobPhone);
        etEmail = findViewById(R.id.etJobEmail);
        etDescription = findViewById(R.id.etJobDescription);

        // --------------<<<   POPULATE VIEWS FROM BACKEND   >>>-------------- \\

        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  address: 1 |  aboutMe: 2      |  name: 3        | \\
        // |  phone: 4  |  email: 5   |  workHistory:  6 |  education : 7  | \\

        txtJobName.setText(profileData[3]);
        txtPhone.setText(profileData[4]);
        txtEmail.setText(profileData[5]);
        txtDescription.setText(profileData[2]);

        // --------------<<<   LISTENERS   >>>-------------- \\

        // 'Edit' button dual-functions as 'Save' button
        btnEdit.setOnClickListener(v -> {
            if(etDescription.getVisibility() == View.INVISIBLE)
                setProfileEditable();
            else {
                // Save Changes
                txtPhone.setText(etPhone.getText().toString().trim());
                txtEmail.setText(etEmail.getText().toString().trim());
                txtDescription.setText(etDescription.getText().toString().trim());

                // Get encoding of profile for backend
                String[] encodedProfile = generateEncodedProfileData();
                // Update profile
                // TODO: Actually make this data populate correctly
                serverDAO.updateJobPosting(response -> {
                            Log.d(TAG, "UPDATING PROFILE: "+response);
                        }, Integer.parseInt(encodedProfile[0]),encodedProfile[1],encodedProfile[2],encodedProfile[3],
                        Boolean.parseBoolean(encodedProfile[4]));

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
        });

        btnCancel.setOnClickListener(v -> setProfileStatic());
    }

    // --------------<<<   UTILITY METHODS   >>>-------------- \\

    /**
     * Set profile into "editing mode" where users can change the information
     * displayed on their profile page.
     */
    private void setProfileEditable() {
        // Hide Static Fields
        txtPhone.setVisibility(View.INVISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);
        txtDescription.setVisibility(View.INVISIBLE);


        // Display Editable Fields
        etPhone.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        etDescription.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnEdit.setImageResource(R.drawable.save_button_temp);


        // Populate fields w/ current text
        etPhone.setText(txtPhone.getText());
        etEmail.setText(txtEmail.getText());
        etDescription.setText(txtDescription.getText());
    }

    /**
     * Set profile into "static mode" where elements are uneditable, and are
     * displayed just as an employer would see the profile.
     */
    private void setProfileStatic() {
        // Hide Static Fields
        etPhone.setVisibility(View.INVISIBLE);
        etEmail.setVisibility(View.INVISIBLE);
        etDescription.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnEdit.setImageResource(R.drawable.edit_button_temp);

        // Display Editable Fields
        txtPhone.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.VISIBLE);
        txtDescription.setVisibility(View.VISIBLE);

        // Hide keyboard - Signifies editing finished
        hideKeyboard();
    }

    private String accountLookup() {
        String result = "";
        serverDAO.verifyLogin(response -> {
            Log.d(TAG, "VERIFY LOGIN : "+response);
        });

        // TODO: Find a way to check applicant/employer viewing page
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

    private String[] generateEncodedProfileData() {
        //  ------------------------- Index Table -------------------------  \\
        // |  id: 0     |  address: 1 |  aboutMe: 2      |  name: 3        | \\
        // |  phone: 4  |  email: 5   |  workHistory:  6 |  education : 7  | \\

        String[] result = new String[8];

        result[0] = String.valueOf(accountID); // Gotta actually get this somehow...
        result[1] = "myAddress";
        result[2] = txtDescription.getText().toString();
        result[3] = txtJobName.getText().toString();
        result[4] = txtPhone.getText().toString();
        result[5] = txtEmail.getText().toString();

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