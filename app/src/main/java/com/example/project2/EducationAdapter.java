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

public class EducationAdapter extends BaseAdapter {
    ArrayList<School> educationHistory;
    Context context;

    public EducationAdapter(Context context, ArrayList<School> educationHistory) {
        this.educationHistory = educationHistory;
        this.context = context;
    }

    @Override
    public int getCount() {
        return educationHistory.size();
    }

    @Override
    public Object getItem(int i) {
        return educationHistory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view==null)
            view = LayoutInflater.from(context).inflate(R.layout.layout_employment,parent,false);
        School mySchool = educationHistory.get(i);

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtOrganization = view.findViewById(R.id.txtOrganization);
        EditText etDateStart = view.findViewById(R.id.etDateStart);
        EditText etDateEnd = view.findViewById(R.id.etDateEnd);
        etDateEnd.setVisibility(View.GONE); // Reusing template. Only one date needed
        // Set institution
        txtTitle.setText(mySchool.getInstitution());
        // Set department name (if exists)
        txtOrganization.setText(mySchool.getDepartment());
        // Set date started (if applicable)
        if(mySchool.getGraduationDate() != null)
            etDateStart.setText(mySchool.getGraduationDate().toString());

        return view;
    }
}