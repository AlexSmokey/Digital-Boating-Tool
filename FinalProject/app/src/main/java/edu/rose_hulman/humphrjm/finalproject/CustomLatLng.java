package edu.rose_hulman.humphrjm.finalproject;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by humphrjm on 2/1/2017.
 */

public class CustomLatLng implements Comparable<CustomLatLng>{
    private LatLng latLng;
    private String key;
    private int index;
    private String name;

    public CustomLatLng(LatLng latLng, String key, int index, String name){
        this.latLng = latLng;
        this.key = key;
        this.index = index;
        this.name = name;
    }
    public CustomLatLng(double lat, double lng, String key, int index, String name){
        latLng = new LatLng(lat, lng);
        this.key = key;
        this.index = index;
        this.name = name;
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

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CustomLatLng o) {
        return (index - o.index);
    }
}
