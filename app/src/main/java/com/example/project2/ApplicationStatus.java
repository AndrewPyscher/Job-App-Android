package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ApplicationStatus extends AppCompatActivity {

    BottomNavigationView botNavBar;
    RecyclerView rvAppStatus;
    ApplicationStatusAdapter adapter;
    ArrayList<AppStatusObj> appStatuses = new ArrayList<>();

    private BroadcastReceiver appStatusUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("ACTION_DATA_UPDATED")) {
                appStatuses = intent.getParcelableArrayListExtra("appStatuses");
                adapter = new ApplicationStatusAdapter(getApplicationContext(), appStatuses);
                rvAppStatus.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_status);

        botNavBar = findViewById(R.id.botNavBarNotifs);
        rvAppStatus = findViewById(R.id.rvAppStatus);

        adapter = new ApplicationStatusAdapter(this, appStatuses);
        rvAppStatus.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvAppStatus.setLayoutManager(manager);

        LocalBroadcastManager.getInstance(this).
                registerReceiver(appStatusUpdateReceiver, new IntentFilter("ACTION_DATA_UPDATED"));

        if (User.role.equals("applicant")) {
            Intent i = new Intent(this, applicationStatusService.class);
            Log.d("test","starting tracking service");
            startService(i);
        }

        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                Intent i = new Intent(this, activity_jobs.class);
                startActivity(i);
                return true;
            } else if (id == R.id.search) {
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);
                return true;
            } else if (id == R.id.profile) {
                Intent i = new Intent(this, (User.role.equals("applicant")) ? UserProfile.class : EmployerProfile.class);
                startActivity(i);
                return true;
            } else if (id == R.id.settings) {
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            } else if (id == R.id.Notifs) {
                //You are here
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        botNavBar.setSelectedItemId(R.id.Notifs);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(appStatusUpdateReceiver);
        super.onDestroy();
    }
}