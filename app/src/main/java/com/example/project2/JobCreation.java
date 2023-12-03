package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class JobCreation extends AppCompatActivity {

    ImageView ivNewJobImage;
    Button btnCreateNewJob, btnCancelJobPost;
    EditText etNewJobPosition, etNewJobPhone, etNewJobEmail, etNewJobDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_creation);

        ivNewJobImage = findViewById(R.id.ivNewJobImage);
        btnCreateNewJob = findViewById(R.id.btnCreateNewJob);
        btnCancelJobPost = findViewById(R.id.btnCancelJobPost);
        etNewJobPosition = findViewById(R.id.etNewJobPosition);
        etNewJobPhone = findViewById(R.id.etNewJobPhone);
        etNewJobEmail = findViewById(R.id.etNewJobEmail);
        etNewJobDesc = findViewById(R.id.etNewJobDesc);

        btnCreateNewJob.setOnClickListener(v -> {
            //Parse contents on each item to verify completeness
            if (checkJobContents()) {
                //post the job
                //We can use gson here to create the new job using the api endpoint from the backend
                //All we would do is create the job object with properties matching the json needed from the endpoint to put into the database
            }
        });
    }

    boolean checkJobContents() {
        return !etNewJobPosition.getText().equals("") && !etNewJobPhone.getText().equals("") &&
                !etNewJobEmail.getText().equals("") && !etNewJobDesc.getText().equals("");
    }
}