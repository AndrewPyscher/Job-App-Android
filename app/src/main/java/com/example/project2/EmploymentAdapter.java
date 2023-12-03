package com.example.project2;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EmploymentAdapter extends BaseAdapter {
    public static final String TAG = "EMPLOYMENT_ADAPTER";
    public static boolean isEditable = false;
    ArrayList<Job> jobHistory;
    Job addedJob; // Ease of access getting 'invisible' information
    Context context;

    public EmploymentAdapter(Context context, ArrayList<Job> jobHistory) {
        this.jobHistory = jobHistory;
        addedJob = jobHistory.get(jobHistory.size() - 1);
        this.context = context;
    }

    @Override
    public int getCount() {
        return jobHistory.size();
    }

    @Override
    public Object getItem(int i) {
        return jobHistory.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if(view==null)
            view = LayoutInflater.from(context).inflate(R.layout.layout_employment,parent,false);
        Job myJob = jobHistory.get(i);

        Log.d(TAG, "New View in List: " + i);
        Log.d(TAG, "Count method: " + getCount());

        // --------------<<<   GET VIEWS   >>>-------------- \\

        TextView txtTitle = view.findViewById(R.id.txtTitle);
        TextView txtOrganization = view.findViewById(R.id.txtOrganization);
        TextView txtDateStart = view.findViewById(R.id.txtDateStart);
        TextView txtDateEnd = view.findViewById(R.id.txtDateEnd);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etOrganization = view.findViewById(R.id.etOrganization);
        EditText etDateStart = view.findViewById(R.id.etDateStart);
        EditText etDateEnd = view.findViewById(R.id.etDateEnd);
        ImageButton btnSave = parent.getRootView().findViewById(R.id.btnEdit);    // Save button to update dates ;)


        // --------------<<<   SET VIEW VISIBILITY   >>>-------------- \\

        // Default visibility values
        int txtVisibility = View.VISIBLE;
        int etVisibility = View.INVISIBLE;

        // Flip visibility values if editing profile
        if(isEditable) {
            txtVisibility = View.INVISIBLE;
            etVisibility = View.VISIBLE;
        }

        // Set all static TextViews to the appropriate visibility
        txtTitle.setVisibility(txtVisibility);
        txtOrganization.setVisibility(txtVisibility);
        txtDateStart.setVisibility(txtVisibility);
        txtDateEnd.setVisibility(txtVisibility);

        // Set all EditTexts to the appropriate visibility
        etTitle.setVisibility(etVisibility);
        etOrganization.setVisibility(etVisibility);
        etDateStart.setVisibility(etVisibility);
        etDateEnd.setVisibility(etVisibility);


        // Hide TextViews if newJob creation item
        if(!myJob.isVisible() && !isEditable) {
            txtTitle.setVisibility(View.GONE);
            txtOrganization.setVisibility(View.GONE);
            txtDateStart.setVisibility(View.GONE);
            txtDateEnd.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else view.setVisibility(View.VISIBLE);

        // Remove start date fields if no start date exists
        if(myJob.getDateStart() == null && myJob.isVisible()) {
            txtDateStart.setVisibility(View.GONE);
            etDateStart.setVisibility(View.GONE);
        }

        // Remove end date fields if no start date exists
        if(myJob.getDateEnd() == null && myJob.isVisible()) {
            txtDateEnd.setVisibility(View.GONE);
            etDateEnd.setVisibility(View.GONE);
        }

        // --------------<<<   POPULATE VIEWS   >>>-------------- \\

        // Set Title
        txtTitle.setText(myJob.getTitle());
        etTitle.setText(myJob.getTitle());
        // Set Organization name (if exists)
        if(!myJob.getOrganization().equals("")) {
            txtOrganization.setText(myJob.getOrganization());
            etOrganization.setText(myJob.getOrganization());
        }
        // Set date started (if applicable)
        if(myJob.getDateStart() != null && txtDateStart.getVisibility() == View.VISIBLE) {
//            if(etDateStart.getText().toString().equals(""))
//                etDateStart.setText(getReadableDate(myJob.getDateStart()));
//            txtDateStart.setText(etDateStart.getText());
            txtDateStart.setText(getReadableDate(myJob.getDateStart()));
            etDateStart.setText(getReadableDate(myJob.getDateStart()));
        }
        // Set date ended (if applicable)
        if(myJob.getDateEnd() != null && txtDateEnd.getVisibility() == View.VISIBLE) {
//            if(etDateEnd.getText().toString().equals(""))
//                etDateEnd.setText(getReadableDate(myJob.getDateEnd()));
//            txtDateEnd.setText(etDateEnd.getText());
            txtDateEnd.setText(getReadableDate(myJob.getDateStart()));
            etDateEnd.setText(getReadableDate(myJob.getDateStart()));
        } else
            etDateEnd.setText((new Date()).toString());


        // --------------<<<   LISTENERS   >>>-------------- \\

        // Listeners on all EditText fields
        // They do not update the myJob object directly
        //  - A temp job object is modified while a user edits the profile
        //  - On Save, the temp job overwrites the original job
        //  - TODO: Send saved profile to server
        //  - On Cancel, the temp job is reverted to the original job object.

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myJob.setTitle(s.toString());
            }
        });

        etOrganization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myJob.setOrganization(s.toString());
            }
        });

        etDateStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    myJob.setDateStart(new SimpleDateFormat("MM/yyyy").parse(s.toString()));
                } catch (ParseException ignored) {}
            }
        });

        etDateEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    myJob.setDateEnd(new SimpleDateFormat("MM/yyyy").parse(s.toString()));
                } catch (ParseException ignored) {}
            }
        });
        // --------------<<<   END MAIN   >>>-------------- \\

        return view;
    }

    /**
     * Save changes to user's job history
     */
    public void saveJobData() {
        for (Job job : jobHistory)
            job.saveChanges();

        // Remove empty invisible job so that it can be repopulated
        if(addedJob.isEmpty())
            jobHistory.remove(addedJob);
    }

    /**
     * Cancel changes to user's job history
     */
    public void cancelChanges() {
        for (Job job : jobHistory)
            job.cancelChanges();
        // Remove invisible job (will be repopulated)
        jobHistory.remove(addedJob);
    }

    private String getReadableDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return  c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.YEAR);
    }
}