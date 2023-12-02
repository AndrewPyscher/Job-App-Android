package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInPage extends AppCompatActivity {
    Button btnSignIn, btnCreateAccount;
    TextView tvError;
    RequestQueue queue;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    EditText etUsername, etPassword1;
    CheckBox chkStaySignedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);
        btnSignIn = findViewById(R.id.btnSignIn);
        queue = Volley.newRequestQueue(this);
        tvError = findViewById(R.id.tvError);
        etUsername = findViewById(R.id.etUsername);
        etPassword1 = findViewById(R.id.etPassword1);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        chkStaySignedIn = findViewById(R.id.chkStaySignedIn);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        ed = sp.edit();




//        if(sp.getBoolean("stay",false)){
//            String user = sp.getString("user","");
//            if(!user.equals("")){
//                Intent i = new Intent(this, MapActivity.class);
//                startActivity(i);
//            }
//        }
        btnCreateAccount.setOnClickListener(e->{
            Intent i = new Intent(this, CreateAccount.class);
            startActivity(i);
        });

        btnSignIn.setOnClickListener(e -> {
            UseServer use = UseServer.getInstance(this);
            use.login(response -> {
                Log.d("test", "onCreate: " + response);
                tvError.setText(response);
                if (response.equals("login")) {
                    if (chkStaySignedIn.isChecked()) {
                        ed.putBoolean("stay", true);
                    }
                    ed.putString("user", etUsername.getText().toString());
                    ed.commit();

                    Intent i = new Intent(this, MapActivity.class);
                    startActivity(i);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Incorrect username or password");
                }

            }, etUsername.getText().toString(), etPassword1.getText().toString());


        });





    }
}