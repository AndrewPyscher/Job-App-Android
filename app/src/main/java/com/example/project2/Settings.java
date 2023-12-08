package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    BottomNavigationView botNavBar;
    SeekBar seekRadius;
    TextView txtRadius;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    Switch switchNotifications;
    Button btnChangePass;
    TextView etNewPass, etConfirm;
    int radius;
    boolean notifications;

    TextView txtErrorNew;

    /*
    to get the settings you need used SharedPreferences!
     for notifications use  ->   notifications = sp.getBoolean("notifications", true);
     radius for jobs use -> radius = sp.getInt("radius", 10);

     The default for notifications is true,
     the default for radius is 10 miles
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        seekRadius = findViewById(R.id.seekRadius);
        botNavBar = findViewById(R.id.botNavBar);
        txtRadius = findViewById(R.id.txtRadius);
        txtErrorNew =findViewById(R.id.txtErrorNew);
        btnChangePass = findViewById(R.id.btnChangePass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfirm = findViewById(R.id.etConfirm);

        switchNotifications = findViewById(R.id.switchNotifications);
        sp = getSharedPreferences("settings", MODE_PRIVATE);
        ed = sp.edit();
        notifications = sp.getBoolean("notifications", true);
        radius = sp.getInt("radius", 10);
        txtRadius.setText(radius + " miles");
        seekRadius.setProgress(radius);
        switchNotifications.setChecked(notifications);

        switchNotifications.setOnClickListener(e->{
            notifications = switchNotifications.isChecked();
            ed.putBoolean("notifications", notifications);
            ed.commit();
        });
        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                Intent i = new Intent(this, activity_jobs.class);
                startActivity(i);
                return true;
            } else if (id == R.id.search) {
                //Use start activity with intents to start that particular activity
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);
                return true;
            }
            else if (id == R.id.profile) {
                Intent i = new Intent(this, UserProfile.class);
                startActivity(i);
                return true;
            } else if(id == R.id.settings){
                return true;
            }
            else{
                return false;
            }
            //Etc etc etc, you can modify this however you want to change its behavior
        });

        btnChangePass.setOnClickListener(e->{
            if(etNewPass.getText().toString().equals(etConfirm.getText().toString())){
                UseServer server = new UseServer(this, User.session);
                server.changePassword(new HandleResponse() {
                    @Override
                    public void response(String response) {
                        txtErrorNew.setVisibility(View.VISIBLE);
                        txtErrorNew.setText("Password changed!");
                    }
                }, User.username, etConfirm.getText().toString());
            }
            else{
                txtErrorNew.setVisibility(View.VISIBLE);
                txtErrorNew.setText("Passwords Don't Match!");
            }
        });

        seekRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRadius.setText(progress + " miles");
                radius = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ed.putInt("radius", radius);
                ed.commit();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        //Set default selected item when in this activity in onStart
        //This will guarantee that we have the correct item selected when the activity starts
        botNavBar.setSelectedItemId(R.id.home);
    }
}