package com.belajar.posma.retrofitposma.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Posma on 9/22/2017.
 */

public class Credits implements Parcelable {

    @SerializedName("name")
    private String name;

    @SerializedName("character")
    private String character;

    @SerializedName("profile_path")
    private String profile_path;


    public Credits(String name, String character, String profile_path) {
        this.name = name;
        this.character = character;
        this.profile_path = profile_path;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }

    public String getProfile_path() {
        return profile_path;
    }
    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.character);
        dest.writeString(this.profile_path);
    }

    protected Credits(Parcel in) {
        this.name = in.readString();
        this.character = in.readString();
        this.profile_path = in.readString();
    }

    public static final Parcelable.Creator<Credits> CREATOR = new Parcelable.Creator<Credits>() {
        @Override
        public Credits createFromParcel(Parcel source) {
            return new Credits(source);
        }

        @Override
        public Credits[] newArray(int size) {
            return new Credits[size];
        }
    };
}
