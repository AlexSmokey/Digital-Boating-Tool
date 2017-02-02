package edu.rose_hulman.humphrjm.finalproject;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumb implements Parcelable{
    private String key;
    private CustomLocation customLocation;
    private String name;
    private ArrayList<CrumbPicture> pictures;
    private String notes;

    public BreadCrumb(){}

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BreadCrumb(Location location, String name){
        this.customLocation = new CustomLocation(location);
        this.name = name;

        notes = "";
        pictures = new ArrayList<>();
    }

    public CustomLocation getLocation() {
        return customLocation;
    }

    public void setLocation(CustomLocation customLocation) {
        this.customLocation = customLocation;
    }

    @Exclude
    public void setLocation(Location location){
        this.customLocation = new CustomLocation(location);
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

    @Exclude
    public void setPictures(ArrayList<CrumbPicture> pictures) {
        this.pictures = pictures;
    }

    public void setPictures(HashMap<String, CrumbPicture> pictures){
        this.pictures = new ArrayList<>(pictures.values());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(customLocation);
        parcel.writeString(name);
        parcel.writeList(pictures);
        parcel.writeString(notes);
        parcel.writeString(key);
//        parcel.writeValue(switchTo);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setValues(BreadCrumb otherCrumb){
        this.customLocation = otherCrumb.customLocation;
        this.name = otherCrumb.name;
        this.pictures = otherCrumb.pictures;
        this.notes = otherCrumb.notes;
    }

    @Exclude
    public CustomLatLng getCustomLatLng(){
        return new CustomLatLng(this.getLocation().getLatitude(), this.getLocation().getLongitude(), key, 0, name);
    }
}
