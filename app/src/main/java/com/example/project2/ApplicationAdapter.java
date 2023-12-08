package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    public static final String TAG = "APPLICATION_ADAPTER";

    Context context;
    ArrayList<JobApplication> applications;

    public ApplicationAdapter(Context context, ArrayList<JobApplication> applications) {
        this.context = context;
        this.applications = applications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pending_application, parent, false);
        return new ApplicationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JobApplication myApplication = applications.get(position);

        holder.btnViewProfile.setOnClickListener(v -> {
            Intent i = new Intent(context, ((User.role.equals("applicant")) ? UserProfile.class : EmployerProfile.class));
            i.putExtra("username", myApplication.getApplicantUsername());
        });

    }

    @Override
    public int getItemCount() {
        return applications.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        Button btnViewProfile;

        ViewHolder(View itemView)  {
            super(itemView);

            // --------------<<<   GET VIEWS   >>>-------------- \\

            btnViewProfile = itemView.findViewById(R.id.btnViewProfile);

            // --------------<<<   SET VIEW VISIBILITY   >>>-------------- \\


        }
    }
}
