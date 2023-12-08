package com.example.project2;

public class JobApplication {
    private int applicantID;
    private String applicantUsername;

    public JobApplication(int applicantID, String applicantUsername) {
        this.applicantID = applicantID;
        this.applicantUsername = applicantUsername;
    }

    // --------------<<<   GETTERS AND SETTERS   >>>-------------- \\


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
}
