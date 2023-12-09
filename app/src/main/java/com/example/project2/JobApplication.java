package com.example.project2;

public class JobApplication {
    private int applicantID;
    private int jobID;

    private String applicantUsername;
    private String jobTitle;

    public JobApplication(int applicantID, String applicantUsername, int jobID, String jobTitle) {
        this.applicantID = applicantID;
        this.applicantUsername = applicantUsername;

        this.jobID = jobID;
        this.jobTitle = jobTitle;
    }

    // --------------<<<   GETTERS AND SETTERS   >>>-------------- \\


    @Override
    public String toString() {
        return "JobApplication{" +
                "applicantID=" + applicantID +
                ", jobID=" + jobID +
                ", applicantUsername='" + applicantUsername + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }

    public int getApplicantID() {
        return applicantID;
    }

    public void setApplicantID(int applicantID) {
        this.applicantID = applicantID;
    }

    public String getApplicantUsername() {
        return applicantUsername;
    }

    public void setApplicantUsername(String applicantUsername) {
        this.applicantUsername = applicantUsername;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
