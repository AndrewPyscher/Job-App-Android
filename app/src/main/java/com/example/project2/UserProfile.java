package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class UserProfile extends AppCompatActivity {
    ImageButton btnEdit, btnCancel;
    TextView txtName, txtDescription, txtPhone, txtEmail;
    EditText etPhone, etEmail, etDescription;
    ListView lstExperience, lstEducation;
    EmploymentAdapter jobAdapter;
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
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etDescription = findViewById(R.id.etDescription);
        lstExperience = findViewById(R.id.lstExperience);
        lstEducation = findViewById(R.id.lstEducation);

        // --------------<<<   LIST VIEW SECTION   >>>-------------- \\

        ArrayList<Job> jobHistory = new ArrayList<>();
        jobHistory.add(new Job("Shift Manager", "McDonalds"));

        jobAdapter = new EmploymentAdapter(this, jobHistory);

        lstExperience.setAdapter(jobAdapter);

        // ------ \\

        ArrayList<School> educationHistory = new ArrayList<>();
        educationHistory.add(new School("Saginaw Valley State University", "Computer Science", new Date(124,4,4)));

        educationAdapter = new EducationAdapter(this, educationHistory);

        lstEducation.setAdapter(educationAdapter);


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
                setProfileStatic();
            }
            EmploymentAdapter.isEditable = !EmploymentAdapter.isEditable;
            EducationAdapter.isEditable = !EducationAdapter.isEditable;
            jobAdapter.notifyDataSetChanged();
            educationAdapter.notifyDataSetChanged();
        });

        btnCancel.setOnClickListener(v -> {
            setProfileStatic();
            EmploymentAdapter.isEditable = !EmploymentAdapter.isEditable;
            EducationAdapter.isEditable = !EducationAdapter.isEditable;
            jobAdapter.notifyDataSetChanged();
            educationAdapter.notifyDataSetChanged();
        });
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

    /**
     * Hide keyboard if open
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btnEdit.getApplicationWindowToken(),0);
    }
}