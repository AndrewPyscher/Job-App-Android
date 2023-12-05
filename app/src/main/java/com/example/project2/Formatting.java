package com.example.project2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Formatting {
    public Formatting() {
    }
    public static final String DELIMITER_1 = "!@#";

    public static final String DELIMITER_2 = "\\$%\\^";
    public static ArrayList<JobListing> recieveJob(String input){
        ArrayList<JobListing> jobListings = new ArrayList<>();

        String[] jobs = input.split(DELIMITER_2);
        for(int i=0; i<jobs.length; i++){
            String[] split = jobs[i].split(DELIMITER_1);
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



    // ----------<<< USER PROFILE ROUTES >>>---------- \\

    public static String[] parseUserProfileInfo(String input)
    {

        String[] splitInput = input.split(DELIMITER_2);
        String[] jobHistory = splitInput[0].split(DELIMITER_1);   // wrong idx
        String[] educationHistory = splitInput[0].split(DELIMITER_1); // wrong idx


        return splitInput;
    }
}
