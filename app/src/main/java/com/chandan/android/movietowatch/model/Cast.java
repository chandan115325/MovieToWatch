package com.chandan.android.movietowatch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CHANDAN on 8/7/2017.
 */

public class Cast implements Parcelable{

    private int castId;
    private String character;
    private String creditId;
    private int gender;
    private int id;
    private String name;
    private int order;
    private String profilePath;

    public Cast( String profilePath,String name, String character){
        this.name = name;
        this.character = character;
        this.profilePath = profilePath;
    }

    protected Cast(Parcel in) {
        castId = in.readInt();
        character = in.readString();
        creditId = in.readString();
        gender = in.readInt();
        id = in.readInt();
        name = in.readString();
        order = in.readInt();
        profilePath = in.readString();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
        parcel.writeInt(castId);
        parcel.writeString(character);
        parcel.writeString(creditId);
        parcel.writeInt(gender);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(order);
        parcel.writeString(profilePath);
    }
}