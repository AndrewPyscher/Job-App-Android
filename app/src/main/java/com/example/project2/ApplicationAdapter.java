package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    UseServer serverDAO;

    public ApplicationAdapter(Context context, ArrayList<JobApplication> applications) {
        this.context = context;
        this.applications = applications;
        serverDAO = UseServer.getInstance(context);
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

        holder.txtApplicantName.setText(myApplication.getApplicantUsername());

        holder.txtApplicantJob.setText(myApplication.getJobTitle());

        holder.btnViewProfile.setOnClickListener(v -> {
            Intent i = new Intent(context, UserProfile.class);
            i.putExtra("username", myApplication.getApplicantUsername());
            context.startActivity(i);
        });

        holder.btnHireApplicant.setOnClickListener(v -> {
            serverDAO.updateApplication(response -> {
                Log.d(TAG, "UpdateApplication:"+response);
                int removedIDX = applications.indexOf(myApplication);
                applications.remove(myApplication);
                notifyItemRemoved(removedIDX);
            }, myApplication.getJobID(), myApplication.getApplicantID(), "You're Hired!","accepted");
        });

        holder.btnRejectApplicant.setOnClickListener(v -> {
            serverDAO.updateApplication(response -> {
                Log.d(TAG, "UpdateApplication:"+response);
                applications.remove(myApplication);
                int removedIDX = applications.indexOf(myApplication);
                applications.remove(myApplication);
                notifyItemRemoved(removedIDX);
            }, myApplication.getJobID(), myApplication.getApplicantID(), "You're Hired!","accepted");
        });
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtApplicantName;
        TextView txtApplicantJob;
        Button btnViewProfile;
        ImageButton btnHireApplicant;
        ImageButton btnRejectApplicant;

        ViewHolder(View itemView)  {
            super(itemView);

            // --------------<<<   GET VIEWS   >>>-------------- \\

            txtApplicantName = itemView.findViewById(R.id.txtApplicantName);
            txtApplicantJob = itemView.findViewById(R.id.txtApplicantJob);
            btnViewProfile = itemView.findViewById(R.id.btnViewProfile);
            btnHireApplicant = itemView.findViewById(R.id.btnHireApplicant);
            btnRejectApplicant = itemView.findViewById(R.id.btnRejectApplicant);

            // --------------<<<   SET VIEW VISIBILITY   >>>-------------- \\


        }
    }
}
