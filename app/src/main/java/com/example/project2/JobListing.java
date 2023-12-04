package com.example.project2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class JobListing {
    int id, employer_id;
    String title, description, category;
    String salary;
    LatLng location;

    // Parametrized constructor
    public JobListing(int id, int employer_id,String title, String description, String category, String salary,
                      LatLng location) {
        this.id = id;
        this.employer_id =employer_id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.salary = salary;
        this.location = location;
    }

    // Parametrized constructor for errors
    public JobListing(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getSalary() {
        return salary;
    }

    public LatLng getLocation() {
        return location;
    }

    public ArrayList<JobListing> getAllJobs(){
        return null;
    }
}
