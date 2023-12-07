package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class EnterEmployerInfo extends AppCompatActivity {
    EditText txtCompanyName, txtStreetName, txtCity, txtState;
    Button btnEmployer;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_employer_info);

        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtStreetName = findViewById(R.id.txtStreetName);
        btnEmployer = findViewById(R.id.btnEmployer);
        txtCity = findViewById(R.id.txtCity);
        txtState = findViewById(R.id.txtState);
        sp = getSharedPreferences("user", MODE_PRIVATE);


        UseServer server = new UseServer(this, sp.getString("session", ""));
        btnEmployer.setOnClickListener(e->{
            String address = txtStreetName.getText().toString() + ", " + txtCity.getText().toString() + ", " + txtState.getText().toString();
            String coords = ConvertAddressToLngLat.convert(this, address);
            Log.d("test", "onCreate: " + sp.getString("session", ""));
            Log.d("test", "onCreate: " + coords);

            server.insertEmployer(response -> {
                Log.d("test", "onCreate: "+ response);
            }, sp.getInt("id", -1),txtCompanyName.getText().toString(), coords);
        });
    }

}
