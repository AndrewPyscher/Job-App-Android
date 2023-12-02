package com.example.project2;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    public static final String TAG = "EDUCATION_ADAPTER";
    public static boolean isEditable = false;
    ArrayList<School> educationHistory;
    School addedSchool;
    Context context;


    // data is passed into the constructor
    public EducationAdapter(Context context, ArrayList<School> educationHistory) {
        this.context = context;
        this.educationHistory = educationHistory;
        addedSchool = educationHistory.get(educationHistory.size() - 1);
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

        School mySchool = educationHistory.get(position);

        Log.d(TAG, "New View in List: " + position);
//        Log.d("USER_PROFILE", "Count method: " + getCount());

        // Hide TextViews if newJob creation item
        if(!mySchool.isVisible() && !isEditable) {
            holder.txtInstitution.setVisibility(View.GONE);
            holder.txtDepartment.setVisibility(View.GONE);
            holder.txtDateGraduate.setVisibility(View.GONE);
            holder.txtDateEnd.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);
        } else holder.itemView.setVisibility(View.VISIBLE);

        // Remove graduation date field if no start date exists
        if(mySchool.getGraduationDate() == null && mySchool.isVisible()) {
            holder.txtDateGraduate.setVisibility(View.GONE);
            holder.etDateGraduate.setVisibility(View.GONE);
        }

        // These should be gone always. Only one date for schools
        holder.txtDateEnd.setVisibility(View.GONE);
        holder.txtDateEnd.setVisibility(View.GONE);

        // --------------<<<   POPULATE VIEWS   >>>-------------- \\

        // Set institution
        holder.txtInstitution.setText(mySchool.getInstitution());
        holder.etInstitution.setText(mySchool.getInstitution());
        // Set department name (if exists)
        holder.txtDepartment.setText(mySchool.getDepartment());
        holder.txtDepartment.setText(mySchool.getDepartment());
        // Set date started (if applicable)
        if(mySchool.getGraduationDate() != null && holder.etDateGraduate.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "we should be in, ladies and gentlemen");
            if(holder.etDateGraduate.getText().toString().equals(""))
                holder.etDateGraduate.setText(getReadableDate(mySchool.getGraduationDate()));
            if(mySchool.getGraduationDate().after(new Date()))
                holder.txtDateGraduate.setText("Anticipated: " + holder.etDateGraduate.getText().toString());
            else
                holder.txtDateGraduate.setText("Graduated: " + holder.etDateGraduate.getText().toString());
        }


        // --------------<<<   LISTENERS   >>>-------------- \\

        // Listeners on all EditText fields
        // They do not update the mySchool object directly
        //  - A temp job object is modified while a user edits the profile
        //  - On Save, the temp job overwrites the original job
        //  - TODO: Send saved profile to server
        //  - On Cancel, the temp job is reverted to the original job object.

        holder.etInstitution.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                mySchool.setInstitution(s.toString());
                Log.d(TAG, "institution: {" +mySchool.getInstitution()+"}");
                listenerMethod(holder, mySchool);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        holder.etDepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                mySchool.setDepartment(s.toString());
                listenerMethod(holder, mySchool);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        holder.etDateGraduate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mySchool.setGraduationDate(new SimpleDateFormat("MM/yyyy").parse(s.toString()));
                } catch (ParseException ignored) {}
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        // --------------<<<   END MAIN   >>>-------------- \\
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return educationHistory.size();
    }

    School getItem(int id) {
        return educationHistory.get(id);
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
//        TextView myTextView;

//        ViewHolder(View itemView) {
//            super(itemView);
//            myTextView = itemView.findViewById(R.id.tvAnimalName);
//
//        }


        TextView txtInstitution;
        TextView txtDepartment;
        TextView txtDateGraduate;

        EditText etInstitution;
        EditText etDepartment;
        EditText etDateGraduate;

        TextView txtDateEnd;
        EditText etDateEnd;


        ViewHolder(View itemView)  {
            super(itemView);

            // --------------<<<   GET VIEWS   >>>-------------- \\


            txtInstitution = itemView.findViewById(R.id.txtTitle);
            txtDepartment = itemView.findViewById(R.id.txtOrganization);
            txtDateGraduate = itemView.findViewById(R.id.txtDateStart);
            etInstitution = itemView.findViewById(R.id.etTitle);
            etDepartment = itemView.findViewById(R.id.etOrganization);
            etDateGraduate = itemView.findViewById(R.id.etDateStart);

            txtDateEnd = itemView.findViewById(R.id.txtDateEnd);
            etDateEnd = itemView.findViewById(R.id.etDateEnd);


            // --------------<<<   SET VIEW VISIBILITY   >>>-------------- \\

            int txtVisibility = View.VISIBLE;
            int etVisibility = View.INVISIBLE;

            if(isEditable) {
                txtVisibility = View.INVISIBLE;
                etVisibility = View.VISIBLE;
            }

            txtInstitution.setVisibility(txtVisibility);
            txtDepartment.setVisibility(txtVisibility);
            txtDateGraduate.setVisibility(txtVisibility);

            etInstitution.setVisibility(etVisibility);
            etDepartment.setVisibility(etVisibility);
            etDateGraduate.setVisibility(etVisibility);

            // Extra views that need to be removed
            txtDateEnd.setVisibility(View.INVISIBLE);
            etDateEnd.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Method used by EditText listeners to create/destroy RecyclerView cards
     * @param holder ViewHolder for given item
     * @param mySchool Active school
     */
    private void listenerMethod(ViewHolder holder, School mySchool) {
        int arrIdx = holder.getAdapterPosition();

        if (holder.etInstitution.getText().length() == 0 &&
                holder.etDepartment.getText().length() == 0 &&
                arrIdx != educationHistory.size() - 1
        ) {
            int removedIdx = educationHistory.indexOf(mySchool);
            educationHistory.remove(mySchool);
            notifyItemRemoved(removedIdx);
        }

        if ((holder.etInstitution.getText().length() > 0 ||
                holder.etDepartment.getText().length() > 0)
                && arrIdx == educationHistory.size() - 1
        ) {
            mySchool.setVisible(true);

            School newSchool = new School(false);
            educationHistory.add(newSchool);
            notifyItemInserted(educationHistory.indexOf(newSchool));
        }
    }

    /**
     * Save changes to user's job history
     */
    public void saveSchoolData() {
        for (School mySchool : educationHistory)
            mySchool.saveChanges();

        // Remove empty invisible job so that it can be repopulated
        if(addedSchool.isEmpty())
            educationHistory.remove(addedSchool);
    }

    /**
     * Cancel changes to user's job history
     */
    public void cancelChanges() {
        for (School mySchool : educationHistory)
            mySchool.cancelChanges();
        // Remove invisible job (will be repopulated)
        educationHistory.remove(addedSchool);
    }
    private String getReadableDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return  c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.YEAR);
    }

}