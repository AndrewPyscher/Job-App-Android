package com.example.project2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class EmploymentAdapter extends BaseAdapter {
    ArrayList<Job> employmentHistory;
    Context context;

    public EmploymentAdapter(Context context, ArrayList<Job> employmentHistory) {
        this.employmentHistory = employmentHistory;
        this.context = context;
    }

    @Override
    public int getCount() {
        return employmentHistory.size();
    }

    @Override
    public Object getItem(int i) {
        return employmentHistory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view==null)
            view = LayoutInflater.from(context).inflate(R.layout.layout_employment,parent,false);
        Job myJob = employmentHistory.get(i);

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtOrganization = view.findViewById(R.id.txtOrganization);
        EditText etDateStart = view.findViewById(R.id.etDateStart);
        EditText etDateEnd = view.findViewById(R.id.etDateEnd);

        // Set Title
        txtTitle.setText(myJob.getTitle());
        // Set Organization name (if exists)
        if(!myJob.getOrganization().equals(""))
            txtOrganization.setText(myJob.getOrganization());
        // Set date started (if applicable)
        if(myJob.getDateStart() != null)
            etDateStart.setText(myJob.getDateStart().toString());
        // Set date ended (if applicable)
        if(myJob.getDateEnd() != null)
            etDateEnd.setText(myJob.getDateStart().toString());
        else
            etDateEnd.setText((new Date()).toString());

        return view;
    }
}