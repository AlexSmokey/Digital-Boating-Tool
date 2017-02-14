package edu.rose_hulman.humphrjm.finalproject;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by humphrjm on 1/29/2017.
 */

public class CustomLocation implements Parcelable{

    String key;
    double latitude;
    double longitude;

    public CustomLocation(){}

    public CustomLocation(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }


    protected CustomLocation(Parcel in) {
        key = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<CustomLocation> CREATOR = new Creator<CustomLocation>() {
        @Override
        public CustomLocation createFromParcel(Parcel in) {
            return new CustomLocation(in);
        }

        @Override
        public CustomLocation[] newArray(int size) {
            return new CustomLocation[size];
        }
    };

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(key);
    }
}
