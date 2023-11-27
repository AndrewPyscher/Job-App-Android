package com.example.project2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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

        // --------------<<<   GET VIEWS   >>>-------------- \\

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtOrganization = view.findViewById(R.id.txtOrganization);
        TextView txtDateStart = view.findViewById(R.id.txtDateStart);
        TextView txtDateEnd = view.findViewById(R.id.txtDateEnd);
        EditText etDateStart = view.findViewById(R.id.etDateStart);
        EditText etDateEnd = view.findViewById(R.id.etDateEnd);
        ImageButton btnSave = parent.getRootView().findViewById(R.id.btnEdit);    // Save button to update dates ;)

        // Remove start date fields if no start date exists
        if(myJob.getDateStart() == null) {
            txtDateStart.setVisibility(View.GONE);
            etDateStart.setVisibility(View.GONE);
        }

        // Remove end date fields if no start date exists
        if(myJob.getDateEnd() == null) {
            txtDateEnd.setVisibility(View.GONE);
            etDateEnd.setVisibility(View.GONE);
        }

        // --------------<<<   POPULATE VIEWS   >>>-------------- \\

        // Set Title
        txtTitle.setText(myJob.getTitle());
        // Set Organization name (if exists)
        if(!myJob.getOrganization().equals(""))
            txtOrganization.setText(myJob.getOrganization());
        // Set date started (if applicable)
        if(myJob.getDateStart() != null && txtDateStart.getVisibility() == View.VISIBLE) {
            if(etDateStart.getText().toString().equals(""))
                etDateStart.setText(getReadableDate(myJob.getDateStart()));
            txtDateStart.setText(etDateStart.getText());
        }
        // Set date ended (if applicable)
        if(myJob.getDateEnd() != null && txtDateEnd.getVisibility() == View.VISIBLE) {
            if(etDateEnd.getText().toString().equals(""))
                etDateEnd.setText(getReadableDate(myJob.getDateEnd()));
            txtDateEnd.setText(etDateEnd.getText());
        } else
            etDateEnd.setText((new Date()).toString());

        // --------------<<<   LISTENERS   >>>-------------- \\


        return view;
    }

    private String getReadableDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return  c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.YEAR);
    }
}