package edu.rose_hulman.humphrjm.finalproject;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by humphrjm on 2/1/2017.
 */

public class CustomLatLng implements Comparable<CustomLatLng>{
    private LatLng latLng;
    private String key;
    private int index;

    public CustomLatLng(LatLng latLng, String key, int index){
        this.latLng = latLng;
        this.key = key;
        this.index = index;
    }
    public CustomLatLng(double lat, double lng, String key, int index){
        latLng = new LatLng(lat, lng);
        this.key = key;
        this.index = index;
    }

    public boolean equalTo(CustomLatLng other){
        return (other.key.equals(this.key) && other.latLng.equals(this.latLng));
    }

    public double getLatitude(){
        return latLng.latitude;
    }

    public double getLongitude(){
        return latLng.longitude;
    }

    public LatLng getLatLng(){
        return latLng;
    }

    @Override
    public String toString() {
        return key + " " + latLng.toString();
    }

    public String getKey(){
        return key;
    }

    public int getIndex(){
        return index;
    }

    @Override
    public int compareTo(CustomLatLng o) {
        return (index - o.index);
    }
}
