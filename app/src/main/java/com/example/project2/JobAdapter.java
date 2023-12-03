package com.example.project2;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    public static final String TAG = "JOB_ADAPTER";
    private ArrayList<Job> jobHistory;
    public static boolean isEditable = false;
    Job addedJob; // Ease of access getting 'invisible' information

    Context context;

    // data is passed into the constructor
    JobAdapter(Context context, ArrayList<Job> jobHistory) {
        this.context=context;
        this.jobHistory = jobHistory;
        addedJob = jobHistory.get(jobHistory.size() - 1);
        Log.d(TAG,"Added Job index: " + jobHistory.size());
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_employment, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        String animal = jobHistory.get(position);
//        holder.myTextView.setText(animal);
        //get category position and set text name

        Job myJob = jobHistory.get(position);

        Log.d(TAG, "New View in List: " + position);
//        Log.d("USER_PROFILE", "Count method: " + getCount());

        // Hide TextViews if newJob creation item
        if(!myJob.isVisible() && !isEditable) {
            holder.txtTitle.setVisibility(View.GONE);
            holder.txtOrganization.setVisibility(View.GONE);
            holder.txtDateStart.setVisibility(View.GONE);
            holder.txtDateEnd.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);
        } else holder.itemView.setVisibility(View.VISIBLE);

        // Remove start date fields if no start date exists
        if(myJob.getDateStart() == null && myJob.isVisible() && !isEditable) {
            holder.txtDateStart.setVisibility(View.GONE);
            holder.etDateStart.setVisibility(View.GONE);
        }

        // Remove end date fields if no start date exists
        if(myJob.getDateEnd() == null && myJob.isVisible() && !isEditable) {
            holder.txtDateEnd.setVisibility(View.GONE);
            holder.etDateEnd.setVisibility(View.GONE);
        }

        if(holder.getAdapterPosition() == jobHistory.size()-1 && !myJob.isVisible())
            holder.imgDelete.setVisibility(View.INVISIBLE);

        // --------------<<<   POPULATE VIEWS   >>>-------------- \\

        // Set Title
        holder.txtTitle.setText(myJob.getTitle());
        holder.etTitle.setText(myJob.getTitle());
        // Set Organization name (if exists)
        if(!myJob.getOrganization().equals("")) {
            holder.txtOrganization.setText(myJob.getOrganization());
            holder.etOrganization.setText(myJob.getOrganization());
        }
        // Set date started (if applicable)
        if(myJob.getDateStart() != null) {
            holder.txtDateStart.setText(getReadableDate(myJob.getDateStart()));
            holder.etDateStart.setText(getReadableDate(myJob.getDateStart()));
        }
        // Set date ended (if applicable)
        if(myJob.getDateEnd() != null) {
            holder.txtDateEnd.setText(getReadableDate(myJob.getDateEnd()));
            holder.etDateEnd.setText(getReadableDate(myJob.getDateEnd()));
        }
//        else
//            holder.etDateEnd.setText((new Date()).toString());


        // --------------<<<   LISTENERS   >>>-------------- \\

        // Listeners on all EditText fields
        // They do not update the myJob object directly
        //  - A temp job object is modified while a user edits the profile
        //  - On Save, the temp job overwrites the original job
        //  - TODO: Send saved profile to server
        //  - On Cancel, the temp job is reverted to the original job object.

        holder.imgDelete.setOnClickListener(v -> {
            int removedIdx = jobHistory.indexOf(myJob);
            jobHistory.remove(myJob);
            notifyItemRemoved(removedIdx);
        });

        holder.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                myJob.setTitle(s.toString());

                listenerMethod(holder, myJob);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        holder.etOrganization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                myJob.setOrganization(s.toString());
                listenerMethod(holder, myJob);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        holder.etDateStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    myJob.setDateStart(new SimpleDateFormat("MM/yyyy").parse(s.toString()));
                } catch (ParseException ignored) {}
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        holder.etDateEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    myJob.setDateEnd(new SimpleDateFormat("MM/yyyy").parse(s.toString()));
                } catch (ParseException ignored) {}
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        // --------------<<<   END MAIN   >>>-------------- \\
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return jobHistory.size();
    }

    Job getItem(int id) {
        return jobHistory.get(id);
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtOrganization;
        TextView txtDateStart;
        TextView txtDateEnd;
        EditText etTitle;
        EditText etOrganization;
        EditText etDateStart;
        EditText etDateEnd;
        ImageView imgDelete;

        ViewHolder(View itemView)  {
            super(itemView);

            // --------------<<<   GET VIEWS   >>>-------------- \\

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOrganization = itemView.findViewById(R.id.txtOrganization);
            txtDateStart = itemView.findViewById(R.id.txtDateStart);
            txtDateEnd = itemView.findViewById(R.id.txtDateEnd);
            etTitle = itemView.findViewById(R.id.etTitle);
            etOrganization = itemView.findViewById(R.id.etOrganization);
            etDateStart = itemView.findViewById(R.id.etDateStart);
            etDateEnd = itemView.findViewById(R.id.etDateEnd);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            // --------------<<<   SET VIEW VISIBILITY   >>>-------------- \\

            // Default visibility values
            int staticVisibility = View.VISIBLE;
            int editVisibility = View.INVISIBLE;

            // Flip visibility values if editing profile
            if(isEditable) {
                staticVisibility = View.INVISIBLE;
                editVisibility = View.VISIBLE;
            }

            // Set all static TextViews to the appropriate visibility
            txtTitle.setVisibility(staticVisibility);
            txtOrganization.setVisibility(staticVisibility);
            txtDateStart.setVisibility(staticVisibility);
            txtDateEnd.setVisibility(staticVisibility);

            // Set all EditTexts to the appropriate visibility
            etTitle.setVisibility(editVisibility);
            etOrganization.setVisibility(editVisibility);
            etDateStart.setVisibility(editVisibility);
            etDateEnd.setVisibility(editVisibility);
            // Also ImageView
            imgDelete.setVisibility(editVisibility);
        }
    }

    /**
     * Method used by EditText listeners to create/destroy RecyclerView cards
     * @param holder ViewHolder for given item
     * @param myJob Active job
     */
    private void listenerMethod(ViewHolder holder, Job myJob) {
        int arrIdx = holder.getAdapterPosition();

        if (holder.etTitle.getText().length() == 0 &&
                holder.etOrganization.getText().length() == 0 &&
                arrIdx != jobHistory.size() - 1
        ) {
            int removedIdx = jobHistory.indexOf(myJob);
            jobHistory.remove(myJob);
            notifyItemRemoved(removedIdx);
        }

        if ((holder.etTitle.getText().length() > 0 ||
                holder.etOrganization.getText().length() > 0)
                && arrIdx == jobHistory.size() - 1
        ) {
            myJob.setVisible(true);
            holder.imgDelete.setVisibility(View.VISIBLE);

            Job newJob = new Job(false);
            jobHistory.add(newJob);
            notifyItemInserted(jobHistory.indexOf(newJob));
//            notifyItemInserted(jobHistory.size()-1);
        }
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