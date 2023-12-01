package com.example.project2;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class JobListing {
    String title, company, type;
    Double salary, rating;
    LatLng location;

    public JobListing(String title, String company, LatLng location, String type, Double salary,
                      Double rating) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.type = type;
        this.salary = salary;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getType() {
        return type;
    }

    public Double getSalary() {
        return salary;
    }

    public Double getRating() {
        return rating;
    }

    public LatLng getLocation() {
        return location;
    }

    public ArrayList<JobListing> getAllJobs(){


        return null;
    }
}
