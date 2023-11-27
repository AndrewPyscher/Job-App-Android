package com.example.project2;

import java.util.Date;

public class Job {
    private String title;
    private String organization;
    private Date dateStart;
    private Date dateEnd;

    public Job(String title, String organization) {
        this.title = title;
        this.organization = organization;
    }

    public Job(String title, String organization, Date dateStart, Date dateEnd) {
        this.title = title;
        this.organization = organization;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }


    // --------------<<<   GETTERS AND SETTERS   >>>-------------- \\


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
