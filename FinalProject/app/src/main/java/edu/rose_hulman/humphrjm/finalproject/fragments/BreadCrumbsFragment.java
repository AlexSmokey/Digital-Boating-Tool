package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.MyLocationListener;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumbsFragment extends Fragment {

    private final long TEN_SECONDS = 0; // frequency to pull location
    private final long METERS = 0; // difference in meters to pull location
    private final int GPS_REQUEST_CODE = 1;

    private ArrayList<BreadCrumb> crumbs;

    private EditText etLat, etLong;
    private Button bSave, bLoad, bAdd;

    LocationManager locationManager;
    LocationListener locationListener;

    public BreadCrumbsFragment() {
    }

    public static BreadCrumbsFragment newInstance(BreadCrumb breadCrumb) {
        BreadCrumbsFragment breadCrumbsFragment = new BreadCrumbsFragment();
        Bundle args = new Bundle();
        breadCrumbsFragment.setArguments(args);
        return breadCrumbsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("BreadCrumbs","Starting location");
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GPS_REQUEST_CODE);


        } else {
            Log.e("BreadCrumbs","Location stuff set");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TEN_SECONDS, METERS, locationListener);

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs, container, false);



        etLat = (EditText) view.findViewById(R.id.etLat);
        etLong = (EditText) view.findViewById(R.id.etLong);

        bAdd = (Button) view.findViewById(R.id.bAdd);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = ((MyLocationListener) locationListener).getLocation();
                if(location != null) {
                    etLat.setText(String.format("%.2f",location.getLatitude()));
                    etLong.setText(String.format("%.2f",location.getLongitude()));
                } else {
                    etLat.setText(String.valueOf(-1));
                    etLong.setText(String.valueOf(-1));
                }
            }
        });

        return view;
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.breadcrumbs);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//    }

    void initGPS(){

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GPS_REQUEST_CODE){
            // TODO: first time run
        }

    }

}