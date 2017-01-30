package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.Constants;
import edu.rose_hulman.humphrjm.finalproject.MyLocationListener;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.adapters.CrumbAdapter;
import edu.rose_hulman.humphrjm.finalproject.adapters.MainPageAdapter;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumbsFragment extends Fragment implements SensorEventListener{

    private final long TEN_SECONDS = 0; // frequency to pull location
    private final long METERS = 0; // difference in meters to pull location

    private final String LIST_KEY = "List_Key";


    private EditText etLat, etLong;
    private Button bSave, bLoad, bAdd;
    private CrumbAdapter crumbAdapter;

    LocationManager locationManager;
    LocationListener locationListener;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private DatabaseReference breadCrumbReference;

    private void dbInit(){
        breadCrumbReference = FirebaseDatabase.getInstance().getReference().child("crumbs");
        breadCrumbReference.addChildEventListener(new CrumbsChildEventListener());
    }

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
        mSensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        locationListener = new MyLocationListener();
//        if (savedInstanceState != null)
//            crumbs = savedInstanceState.getParcelableArrayList(LIST_KEY);
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
                    Constants.GPS_REQUEST_CODE);


        } else {
            Log.e("BreadCrumbs","Location stuff set");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TEN_SECONDS, METERS, locationListener);

        }
        crumbAdapter = new CrumbAdapter(getContext());
        dbInit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.map);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        crumbAdapter.setCrumbs((ArrayList<BreadCrumb>)crumbs.clone());
        recyclerView.setAdapter(crumbAdapter);


        etLat = (EditText) view.findViewById(R.id.etLat);
        etLong = (EditText) view.findViewById(R.id.etLong);

        bAdd = (Button) view.findViewById(R.id.bAdd);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    float lat = Float.parseFloat(etLat.getText().toString());
                    float lon = Float.parseFloat(etLong.getText().toString());
                    Location loc = new Location("");
                    loc.setLatitude(lat);
                    loc.setLongitude(lon);
                    loc.setTime(System.nanoTime());
                    BreadCrumb c = new BreadCrumb(loc, 0 + "");
                    //                    crumbs.add(c);
                    breadCrumbReference.push().setValue(c);
                } catch (NumberFormatException e) {
                    Snackbar.make(getView(), R.string.invalid_lat_long, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.more), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new AlertDialog.Builder(getContext())
                                            .setTitle(R.string.invalid_lat_long_title)
                                            .setMessage(R.string.invalid_lat_long_message)
                                            .setNegativeButton(R.string.cont, null)
                                            .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    etLat.setText("");
                                                    etLong.setText("");
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            })
                            .show();
                } catch (Exception e) {
                    Log.e("BOAT", "Breadcrumb could not be made " + e.getMessage());
                }
            }
        });
        ImageButton refreshButton = (ImageButton) view.findViewById(R.id.ibRefresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentPosition();
            }
        });

        bSave = (Button)view.findViewById(R.id.bSave);
        bLoad = (Button)view.findViewById(R.id.bLoad);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public void setCurrentPosition() {
        Location location = ((MyLocationListener) locationListener).getLocation();
        if(location != null) {
            etLat.setText(String.format("%.4f",location.getLatitude()));
            etLong.setText(String.format("%.4f",location.getLongitude()));
        } else {
            etLat.setText(String.valueOf(-1));
            etLong.setText(String.valueOf(-1));
        }
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
        if(requestCode == Constants.GPS_REQUEST_CODE){
            // TODO: first time run
        }

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        saveInstance(outState);
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        if (mAccel > 48) {
            Snackbar.make(getView(), "", Snackbar.LENGTH_LONG)
                    .setAction(R.string.shake_message, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setCurrentPosition();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onStart() {
        super.onStart();

        if(this.getUserVisibleHint()) {
            this.registerSensorListener();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.unregisterSensorListener();
    }

    private void registerSensorListener() {
        mSensorManager.registerListener(this, mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterSensorListener() {
        mSensorManager.unregisterListener(this);
    }

//    private void saveInstance(Bundle data) {
//        data.putParcelableArrayList(LIST_KEY, this.crumbs);
//    }



    private class CrumbsChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            BreadCrumb breadCrumb = dataSnapshot.getValue(BreadCrumb.class);
            breadCrumb.setKey(dataSnapshot.getKey());
            crumbAdapter.addCrumb(breadCrumb);
            crumbAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            BreadCrumb newCrumb = dataSnapshot.getValue(BreadCrumb.class);
            newCrumb.setKey(key);
            crumbAdapter.updateCrumb(newCrumb);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            BreadCrumb newCrumb = dataSnapshot.getValue(BreadCrumb.class);
            newCrumb.setKey(key);
            crumbAdapter.removeCrumb(newCrumb);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("BreadCrumbsFragmentDB", "Database error: " + databaseError.toString());
        }
    }

}
