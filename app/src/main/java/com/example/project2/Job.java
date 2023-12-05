package com.example.project2;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Job {
    private String title;
    private String organization;
    private Date dateStart;
    private Date dateEnd;
    private boolean isVisible;
    private Job temp;

    // Used for creating clones
    public Job() {
        this.title = "";
        this.organization = "";
        this.dateStart = null;
        this.dateEnd = null;
    }

    public Job(String title, String organization) {
        this.title = title;
        this.organization = organization;
        this.isVisible = true;
        this.temp = cloneJob(this);
    }

    public Job(String title, String organization, Date dateStart, Date dateEnd) {
        this(title, organization);
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Job(boolean isVisible) {
        this("","");
        this.isVisible = isVisible;
    }

    // --------------<<<   GETTERS AND SETTERS   >>>-------------- \\


    private Job cloneJob(Job parent) {
        Job newJob = new Job();
        newJob.title = parent.title;
        newJob.organization = parent.organization;
        if(parent.dateStart != null)
            newJob.dateStart = parent.dateStart;
        if(parent.dateEnd != null)
            newJob.dateEnd = parent.dateEnd;

        return newJob;
    }

    public void saveChanges() {
        this.title = temp.title;
        this.organization = temp.organization;
        this.dateStart = temp.dateStart;
        this.dateEnd = temp.dateEnd;
        this.isVisible = true;
    }

    public void cancelChanges() {
        this.temp = cloneJob(this);
    }

    // ---

    @NonNull
    @Override
    public String toString() {
       String result = "";

       result+=this.title;
       result+="@#@";
       result+=this.organization;
       result+="@#@";
       result+=this.getDateStartString();
       result+="@#@";
       result+=this.getDateEndString();

       return result;
    }


    // ---

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        temp.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        temp.organization = organization;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        temp.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }
    public String getDateStartString() {
        return getDateString(this.getDateStart());
    }
    public String getDateEndString() {
        return getDateString(this.getDateEnd());
    }

    private String getDateString(Date date) {
        if(date == null)
            return "";

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return  c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.YEAR);
    }

    public void setDateEnd(Date dateEnd) {
        temp.dateEnd = dateEnd;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isEmpty() {
        return (this.title == null && this.organization == null);
    }
}
