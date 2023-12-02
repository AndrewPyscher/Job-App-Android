package com.example.project2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class JobListing {
    int id;
    String title, description, category;
    Double salary;
    LatLng location;

    // Parametrized constructor
    public JobListing(int id, String title, String description, String category, Double salary,
                      LatLng location) {
        this.id = id;
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

    public Double getSalary() {
        return salary;
    }

    public LatLng getLocation() {
        return location;
    }

    public ArrayList<JobListing> getAllJobs(){


        return null;
    }
}
