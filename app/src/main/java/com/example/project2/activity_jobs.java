package com.example.project2;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class activity_jobs extends AppCompatActivity {
    ArrayList<String> options;
    BottomNavigationView botNavBar;
    SharedPreferences sp;
    ListView lstJobs;
    ArrayList<JobListing> jobs;
    UseServer useServer;
    JobListingAdapter adapter;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        useServer = new UseServer(this, sp.getString("session", ""));
        jobs = new ArrayList<>();
        getData();
        adapter = new JobListingAdapter(this,jobs);
        lstJobs = findViewById(R.id.lstJobs);
        lstJobs.setAdapter(adapter);
        spinner = findViewById(R.id.spinner);
        botNavBar = findViewById(R.id.botNavBarJobSearch);
        options = new ArrayList<>();

        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                //You are here
                return true;
            } else if (id == R.id.search) {
                Intent i = new Intent(this, MapActivity.class);
                startActivity(i);
                return true;
            } else if (id == R.id.profile) {
                Intent i = new Intent(this, EmployerProfile.class);
                startActivity(i);
                return true;
            } else if(id == R.id.settings){
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        botNavBar.setSelectedItemId(R.id.home);
    }

    public void getData(){
        useServer.allJobs(response -> {
            if(response.equals(""))
                return;
            Log.d("test", "getData: " + response);
            ArrayList<JobListing> temp = Formatting.recieveJob(response);
            for (int i=0; i<temp.size(); i++){
                if(!options.contains(temp.get(i).category)){
                    options.add(temp.get(i).category);
                }
                jobs.add(temp.get(i));
                Log.d("test", "getData: " + i);

            }
            String[] optionArray = options.toArray(new String[0]);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, optionArray);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(adapter2);
            adapter.notifyDataSetChanged();
        },"");

    }

}