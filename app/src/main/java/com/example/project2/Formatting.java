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
                            split[5], // category
                            new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])) // location
                            )
            );

        }

        return jobListings;
    }

    // TODO NEED TO CHECK IF WILL BREAK
    public int receiveRating(String input) {
        double ratingCounter = 0;

        // Split input by delimiter value then loop through ratings summing them together
        String[] splitRatings = input.split(delimiter);
        for (int i = 0; i < splitRatings.length; i++) {
            // Add up all ratings
            ratingCounter = ratingCounter + Integer.parseInt(splitRatings[i]);
        }

        // Divide by number of ratings, round, and return value
        return (int) Math.round(ratingCounter/splitRatings.length);
    }
}
