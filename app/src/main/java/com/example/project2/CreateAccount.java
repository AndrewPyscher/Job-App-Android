package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.atomic.AtomicReference;

public class CreateAccount extends AppCompatActivity {
    Button btnConfirmAccount;
    EditText etUsername1, etPassword2, etConfirmPassword,etCompanyName;
    RadioButton rdbEmployer,rdbApplicant;
    TextView lblCompany,lblError;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        queue = Volley.newRequestQueue(this);
        btnConfirmAccount = findViewById(R.id.btnConfirmAccount);
        etUsername1 = findViewById(R.id.etUsername1);
        etPassword2 = findViewById(R.id.etPassword2);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rdbEmployer = findViewById(R.id.rdbEmployer);
        rdbApplicant = findViewById(R.id.rdbApplicant);
        etCompanyName = findViewById(R.id.etCompanyName);
        lblCompany = findViewById(R.id.lblCompany);
        lblError = findViewById(R.id.lblError);

        rdbApplicant.setOnClickListener(e->{
            if(rdbApplicant.isChecked()){
                rdbEmployer.setChecked(false);
                lblCompany.setVisibility(View.INVISIBLE);
                etCompanyName.setVisibility(View.INVISIBLE);
            }
        });
        rdbEmployer.setOnClickListener(e->{
            if(rdbEmployer.isChecked()){
                rdbApplicant.setChecked(false);
                lblCompany.setVisibility(View.VISIBLE);
                etCompanyName.setVisibility(View.VISIBLE);
            }
        });

        btnConfirmAccount.setOnClickListener(e->{
            if(("" +etConfirmPassword.getText()).equals("") || (""+etUsername1.getText()).equals("")){
                lblError.setText("Fill out all fields!");
                lblError.setVisibility(View.VISIBLE);
                return;
            }
            if(!rdbEmployer.isChecked() && !rdbApplicant.isChecked()){
                lblError.setText("Fill out all fields!");
                lblError.setVisibility(View.VISIBLE);
                return;
            }
            if(!(""+etPassword2.getText()).equals((""+etConfirmPassword.getText()))){
                lblError.setText("Passwords don't match!");
                lblError.setVisibility(View.VISIBLE);
                return;
            }
            if(rdbEmployer.isChecked() && (""+etCompanyName.getText()).equals("")){
                lblError.setText("Fill out all fields!");
                lblError.setVisibility(View.VISIBLE);
                return;
            }

            String role = "";
            if(rdbApplicant.isChecked()){
                role = "applicant";
            }
            if(rdbEmployer.isChecked()){
                role = "employer";
            }
            UseServer useServer = new UseServer(this);
            AtomicReference<String> saveResponse = new AtomicReference<>("");
            useServer.createAccount(response -> {
                Log.d("test", "onCreate: " +response);
                saveResponse.set(response);
            }, role, etUsername1.getText().toString(), etConfirmPassword.getText().toString() );
        });


    }
}