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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class activity_jobs extends AppCompatActivity {

    BottomNavigationView botNavBar;
    SharedPreferences sp;
    RecyclerView rvJobs;
    ArrayList<JobListing> jobs;

    boolean loadFlag;
    JobListingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        sp = getSharedPreferences("user", MODE_PRIVATE);
        loadFlag = false;
        jobs = new ArrayList<>();
        rvJobs = findViewById(R.id.rvJobs);
        getData();
        adapter = new JobListingAdapter(this,jobs);
        rvJobs.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvJobs.setLayoutManager(manager);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(rvJobs);


        sp = getSharedPreferences("user", MODE_PRIVATE);

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
        loadFlag = true;
        UseServer useServer = new UseServer(this, sp.getString("session", ""));
        useServer.allJobs(response -> {
            if(response.equals(""))
                return;

            jobs = Formatting.recieveJob(response);
            adapter.notifyDataSetChanged();



        },"");

    }
}