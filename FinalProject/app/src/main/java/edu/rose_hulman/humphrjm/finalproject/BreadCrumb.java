package edu.rose_hulman.humphrjm.finalproject;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import edu.rose_hulman.humphrjm.finalproject.fragments.CrumbFragment;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumb implements Parcelable{
    private Location location;
    private Fragment switchTo;
    private String name;
    private ArrayList<CrumbPicture> pictures;

    public BreadCrumb(){}

    public BreadCrumb(Location location, String name){
        this.location = location;
        this.name = name;
        this.switchTo = CrumbFragment.newInstance(this);
        pictures = new ArrayList<CrumbPicture>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public Fragment getSwitchTo() {
        return switchTo;
    }

    public void setSwitchTo(Fragment switchTo) {
        this.switchTo = switchTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CrumbPicture> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<CrumbPicture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<BreadCrumb> CREATOR
            = new Parcelable.Creator<BreadCrumb>() {
        public BreadCrumb createFromParcel(Parcel in) {
            return new BreadCrumb(in);
        }

        public BreadCrumb[] newArray(int size) {
            return new BreadCrumb[size];
        }
    };

    private BreadCrumb(Parcel in) {
        this.name = in.readString();
        this.switchTo = (Fragment)in.readParcelable(getClass().getClassLoader());
        this.location = (Location)in.readParcelable(getClass().getClassLoader());
        this.pictures = in.readArrayList(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(location);
        parcel.writeString(name);
        parcel.writeList(pictures);
        parcel.writeValue(switchTo);
    }
}
