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
import java.util.Arrays;
import java.util.Date;

public class UserProfile extends AppCompatActivity {
    ImageButton btnEdit, btnCancel;
    TextView txtName, txtDescription;
    EditText etDescription;
    ListView lstExperience, lstEducation;
    EmploymentAdapter jobAdapter;
    EducationAdapter educationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_profile);

        // --------------<<<   INITIALIZE VIEWS   >>>-------------- \\

        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
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
                txtDescription.setText(etDescription.getText().toString().trim());
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
        txtDescription.setVisibility(View.INVISIBLE);

        // Display Editable Fields
        etDescription.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnEdit.setImageResource(R.drawable.save_button_temp);


        // Populate fields w/ current text
        etDescription.setText(txtDescription.getText());
    }

    /**
     * Set profile into "static mode" where elements are uneditable, and are
     * displayed just as an employer would see the profile.
     */
    private void setProfileStatic() {
        // Hide Static Fields
        etDescription.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
        btnEdit.setImageResource(R.drawable.edit_button_temp);

        // Display Editable Fields
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