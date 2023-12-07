package com.example.project2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class JobInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context mapContext;
    ArrayList<JobListing> mapJobListings;
    Formatting formatting;

    public JobInfoWindow(Context mapContext, ArrayList<JobListing> jobListings) {
        this.mapContext = mapContext;
        this.mapJobListings = jobListings;

        formatting = new Formatting();
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
        TextView tvType = view.findViewById(R.id.tvInfoCategory);
        ImageView ivStar1 = view.findViewById(R.id.ivStar1);
        ImageView ivStar2 = view.findViewById(R.id.ivStar2);
        ImageView ivStar3 = view.findViewById(R.id.ivStar3);
        ImageView ivStar4 = view.findViewById(R.id.ivStar4);
        ImageView ivStar5 = view.findViewById(R.id.ivStar5);

        // Find JobListing from marker tag
        JobListing markerJobListing = (JobListing) marker.getTag();

        // Update text information
        tvTitle.setText(markerJobListing.getTitle());
        tvSalary.setText("$" + markerJobListing.getSalary());
        tvType.setText(markerJobListing.getCategory());

        // Find rating for employer and update star display from rating value
        switch (markerJobListing.getRating()) {
            case 5: {
                ivStar5.setImageResource(R.drawable.rating_star_on);
            }
            case 4: {
                ivStar4.setImageResource(R.drawable.rating_star_on);
            }
            case 3: {
                ivStar3.setImageResource(R.drawable.rating_star_on);
            }
            case 2: {
                ivStar2.setImageResource(R.drawable.rating_star_on);
            }
            case 1: {
                ivStar1.setImageResource(R.drawable.rating_star_on);
            }
            case 0: {
                break;
            }
        }
        return view;
    }

}
