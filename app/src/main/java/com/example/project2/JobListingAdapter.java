package com.example.project2;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JobListingAdapter extends BaseAdapter {

    ArrayList<JobListing> jobs;
    Context context;

    public JobListingAdapter(Context context, ArrayList<JobListing> jobs){
        this.context = context;
        this.jobs = jobs;
    }

    @Override
    public int getCount() {
        return jobs.size();
    }

    @Override
    public Object getItem(int position) {
        return jobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_layout_jobs_item, parent, false);
        TextView tvJobName = view.findViewById(R.id.tvJobName);
        TextView tvSalary = view.findViewById(R.id.tvSalary);
        TextView tvType = view.findViewById(R.id.tvType);
        Button btnViewMore = view.findViewById(R.id.btnViewMore);
        Button btnApply = view.findViewById(R.id.btnApply);
        JobListing j = jobs.get(position);

        tvJobName.setText(j.title);
        tvSalary.setText(j.salary);
        tvType.setText(j.category);

        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        btnApply.setOnClickListener(e->{
            btnApply.setVisibility(View.INVISIBLE);
            Log.d("test", "getView: here" );
            UseServer server = new UseServer(context, sp.getString("session",""));
            server.insertApplication(new HandleResponse() {
                @Override
                public void response(String response) {
                    Toast.makeText(context, "Application Submitted!", Toast.LENGTH_LONG).show();
                }
            }, j.id,User.id,"");
        });

        btnViewMore.setOnClickListener(e->{
            Intent i = new Intent(context, JobProfile.class);
            i.putExtra("jobID", j.id);
            i.putExtra("employerID", j.employer_id);
            context.startActivity(i);
        });

        return view;
    }
}


