package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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
    UseServer use;
    String TAG = "Test";
    public static final int LOCATION_REQUEST_CODE = 321;
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
        sp = getSharedPreferences("user", MODE_PRIVATE);
        ed = sp.edit();
        use = UseServer.getInstance(this);

        // Check permission settings for location access
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Location access permitted...");
        }

        btnCreateAccount.setOnClickListener(e->{
            // Update shared preference values for current location
            updateCurrentLocation();

            Intent m = new Intent(this, CreateAccount.class);
            startActivity(m);
        });

        btnSignIn.setOnClickListener(e -> {
            signIn(etUsername.getText().toString(), etPassword1.getText().toString());
        });
    }
    // method to sign in
    public void signIn(String username, String password){
        if(etUsername.getText().toString().equals("") || etPassword1.getText().toString().equals("")){
            tvError.setText("Fill out all Fields!");
            tvError.setVisibility(View.VISIBLE);
        }
        try {
            use.login(response -> {
                if (response == null) return;
                Log.d("test", "onCreate: " + response);
                tvError.setText(response);
                if (!response.equals("Username or Password is incorrect!")) {
                    String[] split = response.split("<><>");
                    ed.putString("session", split[1]);
                    ed.commit();
                    User.username = etUsername.getText().toString();
                    User.id = Integer.parseInt(split[0]);
                    User.session = split[1];
                    //User.role = "";
                    ed.putInt("id", Integer.parseInt(split[0]));
                    ed.commit();

                    // Get user role and set it
                    // TODO
                    use.getRole(new HandleResponse() {
                        @Override
                        public void response(String response) {
                            User.role = response;
                        }
                    }, User.id);

                    // Update shared preference values for current location
                    updateCurrentLocation();

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

    public void startTrackingService() {
        Intent i = new Intent(this, applicationStatusService.class);
        Log.d("test","starting tracking service");
        startService(i);
    }

    @Override
    protected void onDestroy() {
        use.logout(response -> Log.d("test", "response: " + response));
        super.onDestroy();
    }

    // Checks location permissions before
    private void updateCurrentLocation() {
        // Create fused location provider object and check permissions for using locations
        FusedLocationProviderClient flpClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Location access was denied...");
            return;
        }

        // Set on success and on failure listeners for location request
        flpClient.getLastLocation().addOnSuccessListener(location -> {
            // On success update shared preferences value for location as coordinates string
            ed.putString("location", location.getLatitude() + "," + location.getLongitude());
            ed.commit();
        });
        flpClient.getLastLocation().addOnFailureListener(e -> {
            // On failure update shared preferences value for location as error string
            ed.putString("location", "error");
            ed.commit();
        });
    }

    // Pulls user role info from database and sets user's role to it


}