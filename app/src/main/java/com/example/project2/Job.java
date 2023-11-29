package com.example.project2;

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
