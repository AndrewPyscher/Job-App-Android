package com.example.project2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import kotlinx.coroutines.Job;

public class JobInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context mapContext;
    ArrayList<JobListing> mapJobListings;

    public JobInfoWindow(Context mapContext, ArrayList<JobListing> jobListings) {
        this.mapContext = mapContext;
        this.mapJobListings = jobListings;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        // Inflate info window with custom job layout and find required views
        View view = LayoutInflater.from(mapContext).inflate(R.layout.job_info_window, null);
        TextView tvTitle = view.findViewById(R.id.tvInfoTitle);
        TextView tvSalary = view.findViewById(R.id.tvInfoSalary);
        TextView tvType = view.findViewById(R.id.tvInfoType);

        // Find JobListing from marker tag
        JobListing markerJobListing = (JobListing) marker.getTag();

        // Update info window with JobListing information
        tvTitle.setText(markerJobListing.getTitle());
        tvSalary.setText("$" + markerJobListing.getSalary());
        tvType.setText(markerJobListing.getType());

        return view;
    }
}
