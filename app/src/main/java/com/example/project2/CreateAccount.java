package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
    TextView lblError;
    RequestQueue queue;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    // class to create an account for both applicants and employers
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
        lblError = findViewById(R.id.lblError);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        ed = sp.edit();

        // listeners for radio buttons
        rdbApplicant.setOnClickListener(e->{
            if(rdbApplicant.isChecked()){
                rdbEmployer.setChecked(false);
            }
        });
        rdbEmployer.setOnClickListener(e->{
            if(rdbEmployer.isChecked()){
                rdbApplicant.setChecked(false);
            }
        });

        // when the user clicks the button verify all the info is filled out, if it is create their account
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
                lblError.setText(response);
                saveResponse.set(response);

                // after the account is made, sign the user in
                useServer.login(new HandleResponse() {
                    @Override
                    public void response(String response) {
                        String[] split = response.split("<><>");
                        ed.putString("session", split[1]);
                        Log.d("test", "response: " + response);
                        ed.putInt("id", Integer.parseInt(response.split("<><>")[0]));
                        ed.commit();
                        User.id = Integer.parseInt(response.split("<><>")[0]);
                        User.session = split[1];
                        User.username = etUsername1.getText().toString();
                    }
                }, etUsername1.getText().toString(), etConfirmPassword.getText().toString());
            }, role, etUsername1.getText().toString(), etConfirmPassword.getText().toString() );

            //determine which page to got next based on role
            if(role.equals("applicant")) {
                // Set user role
                User.role = "applicant";

                Intent createAccount = new Intent(this, EnterUserInfo.class);
                startActivity(createAccount);
            }
            if(role.equals("employer")){
                // Set user role
                User.role = "employer";

                Intent createAccount = new Intent(this, EnterEmployerInfo.class);
                startActivity(createAccount);
            }
        });
    }
}