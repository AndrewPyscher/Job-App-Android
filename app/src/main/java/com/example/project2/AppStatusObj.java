package com.example.project2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AppStatusObj implements Parcelable {
    String position, companyName, status;

    public AppStatusObj(String position, String companyName, String status) {
        this.position = position;
        this.companyName = companyName;
        this.status = status;
    }

    protected AppStatusObj(Parcel in) {
        position = in.readString();
        companyName = in.readString();
        status = in.readString();
    }

    public static final Creator<AppStatusObj> CREATOR = new Creator<AppStatusObj>() {
        @Override
        public AppStatusObj createFromParcel(Parcel in) {
            return new AppStatusObj(in);
        }

        @Override
        public AppStatusObj[] newArray(int size) {
            return new AppStatusObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(position);
        dest.writeString(companyName);
        dest.writeString(status);
    }
}
