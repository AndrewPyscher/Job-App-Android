package com.example.project2;

import java.util.Date;

public class School {
    private String institution;
    private String department;
    private Date graduationDate;

    public School(String institution, String department) {
        this.institution = institution;
        this.department = department;
    }

    public School(String institution, String department, Date graduationDate) {
        this.institution = institution;
        this.department = department;
        this.graduationDate = graduationDate;
    }

    // --------------<<<   GETTERS AND SETTERS   >>>-------------- \\

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }
}
