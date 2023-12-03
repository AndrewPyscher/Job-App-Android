package com.example.project2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Formatting {
    public Formatting() {
    }
    String delimiter = "!@#";
    String delimiter2 = "\\$%\\^";
    public ArrayList<JobListing> recieveJob(String input){
        ArrayList<JobListing> jobListings = new ArrayList<>();

        String[] jobs = input.split(delimiter2);
        for(int i=0; i<jobs.length; i++){
            String[] split = jobs[i].split(delimiter);
            String[] location = split[6].split(",");
            jobListings.add(
                    new JobListing(
                            Integer.parseInt(split[0]), // job id
                            Integer.parseInt(split[1]), // employer_id
                            split[2], // title
                            split[3], // description
                            split[4], // salary
                            split[5], // type
                            new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])) // location
                            )
            );


        }

        return jobListings;
    }
}
