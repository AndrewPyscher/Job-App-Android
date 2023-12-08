package com.example.project2;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class School {
    private String institution;
    private String department;
    private Date graduationDate;
    private boolean isVisible;
    private School temp;

    // Used for creating clones
    public School() {
        this.institution = "";
        this.department = "";
        this.graduationDate = null;
    }

    public School(boolean isVisible) {
        this("","");
        this.isVisible = isVisible;
    }

    public School(String institution, String department) {
        this.institution = institution;
        this.department = department;
        this.isVisible = true;
        this.temp = cloneSchool(this);
    }

    public School(String institution, String department, Date graduationDate) {
        this(institution, department);
        this.graduationDate = graduationDate;
    }



    private School cloneSchool(School parent) {
        School newSchool = new School();
        newSchool.institution = parent.institution;
        newSchool.department = parent.department;
        if(parent.graduationDate != null)
            newSchool.graduationDate = parent.graduationDate;

        return newSchool;
    }

    public void saveChanges() {
        this.institution = temp.institution;
        this.department = temp.department;
        this.graduationDate = temp.graduationDate;
        this.isVisible = true;
    }

    public void cancelChanges() {
        this.temp = cloneSchool(this);
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";

        result+=this.institution;
        result+="@#@";
        result+=this.department;
        result+="@#@";
        result+=this.getGradDateString();

        return result;
    }

    // --------------<<<   GETTERS AND SETTERS   >>>-------------- \\

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        temp.institution = institution;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        temp.department = department;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        temp.graduationDate = graduationDate;
    }

    public String getGradDateString() {
        if(this.graduationDate == null)
            return "";

        Calendar c = Calendar.getInstance();
        c.setTime(this.graduationDate);

        return  c.get(Calendar.MONTH) + "/" +
                c.get(Calendar.YEAR);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    public boolean isEmpty() {
        return (this.institution == null && this.department == null);
    }
}
