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
    UseServer use;
    String TAG = "Test";
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
        use = UseServer.getInstance(this);


        /*try{
            if(sp.getBoolean("stay",false)){
                String user = sp.getString("user","");
                Log.d(TAG, "user: " + user);
                if(!user.equals("")){
                    signIn(sp.getString("user", ""), sp.getString("password", ""));
                }
            }
        }catch (Exception e){
            ed.putBoolean("stay",false);
            ed.commit();
        }*/

        btnCreateAccount.setOnClickListener(e->{
            Intent m = new Intent(this, CreateAccount.class);
            startActivity(m);
        });

        btnSignIn.setOnClickListener(e -> {
            signIn(etUsername.getText().toString(), etPassword1.getText().toString());
        });
    }
    public void signIn(String username, String password){
        try {
            use.login(response -> {
                if (response == null) return;
                Log.d("test", "onCreate: " + response);
                tvError.setText(response);
                if (!response.equals("Username or Password is incorrect!")) {
                    String[] split = response.split("<><>");
                    Log.d("Garbage",split[1]);
                    ed.putString("session", split[1]);
                    ed.commit();
                    if (chkStaySignedIn.isChecked()) {
                        ed.putBoolean("stay", true);
                        if (sp.getString("user", "").equals("")) {
                            ed.putString("user", etPassword1.getText().toString());
                        }
                        if (sp.getString("password", "").equals("")) {
                            ed.putString("password", etPassword1.getText().toString());
                        }
                    } else {
                        ed.putBoolean("stay", false);
                    }

                    ed.putInt("id", Integer.parseInt(split[0]));
                    ed.commit();

                    Intent i = new Intent(this, activity_jobs.class);
                    startActivity(i);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Incorrect username or password");
                }

            }, username, password);
        }catch (Exception e){

        }
    }
    @Override
    protected void onDestroy() {
        use.logout(new HandleResponse() {
            @Override
            public void response(String response) {
                Log.d("test", "response: " + response);
            }
        });
        super.onDestroy();
    }


}