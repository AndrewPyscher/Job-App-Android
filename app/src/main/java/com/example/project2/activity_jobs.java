package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class activity_jobs extends AppCompatActivity {

    BottomNavigationView botNavBar;
    SharedPreferences sp;
    ListView lstJobs;
    ArrayList<JobListing> jobs;
    UseServer useServer;
    JobListingAdapter adapter;


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

        botNavBar = findViewById(R.id.botNavBarJobSearch);


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
                Intent i = new Intent(this, UserProfile.class);
                startActivity(i);
                return true;
                // TODO need to add the rest of the navbar buttons to their respective activities
            } else {
                return false;
            }
        });

    }


    public void getData(){
        useServer.allJobs(response -> {
            if(response.equals(""))
                return;
            Log.d("test", "getData: " + response);
            ArrayList<JobListing> temp = Formatting.recieveJob(response);
            for (int i=0; i<temp.size(); i++){
                jobs.add(temp.get(i));
                Log.d("test", "getData: " + i);
            }
            adapter.notifyDataSetChanged();
        },"");

    }
}