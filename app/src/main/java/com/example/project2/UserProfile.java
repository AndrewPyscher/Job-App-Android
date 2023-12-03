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

import java.util.ArrayList;
import java.util.Date;

public class UserProfile extends AppCompatActivity {

    public static final String TAG = "USER_PROFILE";
    ImageButton btnEdit, btnCancel;
    TextView txtName, txtDescription, txtPhone, txtEmail , txtExperience, txtEducation;
    EditText etName, etPhone, etEmail, etDescription;
//    ListView lstExperience, lstEducation;
    RecyclerView rvEmployment, rvEducation;
    JobAdapter jobAdapter;
    EducationAdapter educationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

        // --------------<<<   LIST VIEW SECTION   >>>-------------- \\

        // TODO: Get the user's real job history from backend
        ArrayList<Job> jobHistory = new ArrayList<>();
        jobHistory.add(new Job("Shift Manager", "McDonalds"));
        jobHistory.add(new Job("Frying Cook", "Krusty Krab", new Date(122,7,24), new Date(123,4,4)));


        Log.d(TAG, "Size of jobHistory: " + jobHistory.size());

        // ------ \\
//        txtExperience.setVisibility(View.INVISIBLE);
//        rvEmployment.setAdapter(jobAdapter);
        jobAdapter = new JobAdapter(this, jobHistory);
        rvEmployment.setLayoutManager(new LinearLayoutManager(this));
        rvEmployment.setAdapter(jobAdapter);


        // ------ \\

        ArrayList<School> educationHistory = new ArrayList<>();
        educationHistory.add(new School("Saginaw Valley State University", "Computer Science", new Date(124,4,4)));
        educationHistory.add(new School("Georgia Tech", "Computer Science", new Date(125,4,4)));

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

    /**
     * Hide keyboard if open
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btnEdit.getApplicationWindowToken(),0);
    }
}