package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class JobProfile extends AppCompatActivity {
    ImageButton btnEdit, btnCancel;
    TextView txtJobName, txtDescription, txtPhone, txtEmail;
    EditText etPhone, etEmail, etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_profile);

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

    /**
     * Hide keyboard if open
     */
    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(btnEdit.getApplicationWindowToken(),0);
    }
}