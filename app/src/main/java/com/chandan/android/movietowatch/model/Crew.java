package com.chandan.android.movietowatch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CHANDAN on 8/7/2017.
 */

public class Crew implements Parcelable{

    private String creditId;
    private String department;
    private int gender;
    private int id;
    private String job;
    private String name;
    private String profilePath;

    public Crew (String name, String job, String profilePath){
        this.name = name;
        this.job = job;
        this.profilePath = profilePath;
    }
    protected Crew(Parcel in) {
        creditId = in.readString();
        department = in.readString();
        gender = in.readInt();
        id = in.readInt();
        job = in.readString();
        name = in.readString();
        profilePath = in.readString();
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(creditId);
        parcel.writeString(department);
        parcel.writeInt(gender);
        parcel.writeInt(id);
        parcel.writeString(job);
        parcel.writeString(name);
        parcel.writeString(profilePath);
    }
}
