package edu.rose_hulman.humphrjm.finalproject;

import android.location.Location;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumb {
    private Location location;

    public BreadCrumb(){}

    public BreadCrumb(Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
