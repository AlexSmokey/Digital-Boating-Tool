package edu.rose_hulman.humphrjm.finalproject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import edu.rose_hulman.humphrjm.finalproject.fragments.BreadCrumbsFragment;

/**
 * Created by humphrjm on 1/22/2017.
 */

public class MyLocationListener implements LocationListener{

    private Location myLocation;
    private LocationHandler callback;

    public MyLocationListener(BreadCrumbsFragment c){
        callback = (LocationHandler) c;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
//            Log.e("Location",location.toString());
            this.myLocation = location;
            try {
                callback.onLocationUpdated(location);
            }catch (Exception e){
                Log.e("LocationChangedCall",e.toString());
            }
        }else {
            Log.e("Location","null");
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation() {
        return myLocation;
    }

    public interface LocationHandler{
        void onLocationUpdated(Location location);
    }
}
