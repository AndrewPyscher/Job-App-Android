package com.example.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextParams;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

            String url = "http://162.243.172.218:5000/createUser";
            StringRequest createAccountRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Log.d("test", "onCreate: " + response);
                        if(response.equals("Username Already Exists!")){
                            Log.d("tests", "onCreate: user exists" );
                        }

                        //account created successfully

                    },
                    error -> Log.d("test", "onErrorResponse: " + error)
            ) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    JSONObject jsonParams = new JSONObject();
                    String role = "";
                    if(rdbApplicant.isChecked()){
                        role = "applicant";
                    }
                    if(rdbEmployer.isChecked()){
                        role = "employer";
                    }
                    try {
                        jsonParams.put("role", role);
                        jsonParams.put("username", etUsername1.getText()+"");
                        jsonParams.put("password", etConfirmPassword.getText()+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return jsonParams.toString().getBytes();
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
        queue.add(createAccountRequest);
        });


    }
}