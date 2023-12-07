package com.example.project2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JobListingAdapter extends RecyclerView.Adapter<JobListingAdapter.JobListingViewHolder> {

    ArrayList<JobListing> jobs;
    Context context;

    public JobListingAdapter(Context context, ArrayList<JobListing> jobs){
        this.context = context;
        this.jobs = jobs;
    }
    @NonNull
    @Override
    public JobListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_layout_jobs_item,parent,false);
        return new JobListingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobListingViewHolder holder, int position) {
        JobListing job = jobs.get(position);
        holder.tvJobName.setText(job.title);
        holder.tvSalary.setText(job.salary);
        holder.tvType.setText(job.category);
        Log.d("test", "onBindViewHolder: "  +jobs.size());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class JobListingViewHolder extends RecyclerView.ViewHolder{
        TextView tvJobName;
        TextView tvSalary;
        TextView tvType;
        Button btnViewMore;
        Button btnApply;

        public JobListingViewHolder(@NonNull View view) {
            super(view);
            tvJobName = view.findViewById(R.id.tvJobName);
            tvSalary = view.findViewById(R.id.tvSalary);
            tvType = view.findViewById(R.id.tvType);
            btnViewMore = view.findViewById(R.id.btnViewMore);
            btnApply = view.findViewById(R.id.btnApply);




        }
    }
}


