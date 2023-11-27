package com.example.project2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
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

        // --------------<<<   GET VIEWS   >>>-------------- \\

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtOrganization = view.findViewById(R.id.txtOrganization);
        TextView txtDateGraduate = view.findViewById(R.id.txtDateStart);
        EditText etDateGraduate = view.findViewById(R.id.etDateStart);

        // Extra views that need to be removed
        TextView txtDateEnd = view.findViewById(R.id.txtDateEnd);
        EditText etDateEnd = view.findViewById(R.id.etDateEnd);
        txtDateEnd.setVisibility(View.GONE);
        etDateEnd.setVisibility(View.GONE);

        // Remove graduation date fields if no start date exists
        if(mySchool.getGraduationDate() == null) {
            txtDateGraduate.setVisibility(View.GONE);
            etDateGraduate.setVisibility(View.GONE);
        }

        // --------------<<<   POPULATE VIEWS   >>>-------------- \\

        // Set institution
        txtTitle.setText(mySchool.getInstitution());
        // Set department name (if exists)
        txtOrganization.setText(mySchool.getDepartment());
        // Set date started (if applicable)
        if(mySchool.getGraduationDate() != null && txtDateGraduate.getVisibility() == View.VISIBLE) {
            Log.d("WE_IN", "we should be in, ladies and gentlemen");
            if(etDateGraduate.getText().toString().equals(""))
                etDateGraduate.setText(getReadableDate(mySchool.getGraduationDate()));
            if(mySchool.getGraduationDate().after(new Date()))
                txtDateGraduate.setText("Anticipated: " + etDateGraduate.getText().toString());
            else
                txtDateGraduate.setText("Graduated: " + etDateGraduate.getText().toString());
        }

        return view;
    }

    private String getReadableDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return  c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.YEAR);
    }
}