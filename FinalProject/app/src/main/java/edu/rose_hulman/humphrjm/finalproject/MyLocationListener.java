package edu.rose_hulman.humphrjm.finalproject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by humphrjm on 1/22/2017.
 */

public class MyLocationListener implements LocationListener{

    private Location myLocation;

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            Log.e("Location",location.toString());
            this.myLocation = location;
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
}
