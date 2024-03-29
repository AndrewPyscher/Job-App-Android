package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.atomic.AtomicReference;

public class JobCreation extends AppCompatActivity {

    ImageView ivNewJobImage;
    Button btnCreateNewJob, btnCancelJobPost;
    EditText etNewJobTitle, etNewJobPhone, etNewJobEmail, etNewJobDesc, etNewJobSalary, etNewJobType;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_creation);

        queue = Volley.newRequestQueue(this);

        ivNewJobImage = findViewById(R.id.ivNewJobImage);
        btnCreateNewJob = findViewById(R.id.btnCreateNewJob);
        btnCancelJobPost = findViewById(R.id.btnCancelJobPost);
        etNewJobTitle = findViewById(R.id.etNewJobTitle);
        etNewJobPhone = findViewById(R.id.etNewJobPhone);
        etNewJobEmail = findViewById(R.id.etNewJobEmail);
        etNewJobSalary = findViewById(R.id.etNewJobSalary);
        etNewJobDesc = findViewById(R.id.etNewJobDesc);
        etNewJobType = findViewById(R.id.etNewJobType);

        btnCreateNewJob.setOnClickListener(v -> {
            //Parse contents on each item to verify completeness
            if (checkJobContents()) {
                UseServer useServer = new UseServer(this, User.session);
                AtomicReference<String> saveResponse = new AtomicReference<>("");
                useServer.createPosting(response -> {
                    Log.d("test","Creating a new job posting: " + response);
                    saveResponse.set(response);
                }, User.id, etNewJobTitle.getText()+"", etNewJobDesc.getText()+"",
                        etNewJobSalary.getText()+"", etNewJobType.getText()+"");
                finish();
            } else {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelJobPost.setOnClickListener(v -> {
            finish();
        });
    }

    boolean checkJobContents() {
        return !etNewJobTitle.getText().equals("") && !etNewJobEmail.getText().equals("") &&
                !etNewJobDesc.getText().equals("") && !etNewJobSalary.getText().equals("") &&
                !etNewJobType.getText().equals("");
    }
}