package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.Constants;
import edu.rose_hulman.humphrjm.finalproject.CustomLatLng;
import edu.rose_hulman.humphrjm.finalproject.MainActivity;
import edu.rose_hulman.humphrjm.finalproject.MapProcessing.OnMapAndViewReadyListener;
import edu.rose_hulman.humphrjm.finalproject.MyLocationListener;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.adapters.CrumbAdapter;
import edu.rose_hulman.humphrjm.finalproject.CustomLocation;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumbsFragment extends Fragment implements SensorEventListener, OnMapReadyCallback, OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, GoogleMap.OnMarkerClickListener {

    private final long TEN_SECONDS = 0; // frequency to pull location
    private final long METERS = 0; // difference in meters to pull location

    private final String LIST_KEY = "List_Key";
    private final String MAP_FRAGMENT_TAG = "map";

    private TextView tvCurrentDistance;
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
    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    private HashMap<CustomLatLng, BreadCrumb> breadcrumbs = new HashMap<>();


    private void dbInit() {
        breadCrumbReference = FirebaseDatabase.getInstance().getReference().child("crumbs");
        breadCrumbReference.addChildEventListener(new CrumbsChildEventListener());
    }

    private void mapInit() {
//        supportMapFragment = SupportMapFragment.newInstance();
        breadcrumbs.clear();
        index = 0;
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);

        // We only create a fragment if it doesn't already exist.
        if (supportMapFragment == null) {
            // To programmatically add the map, we first create a SupportMapFragment.
            supportMapFragment = SupportMapFragment.newInstance();

            // Then we add it using a FragmentTransaction.
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.map_holder, supportMapFragment, MAP_FRAGMENT_TAG);
            fragmentTransaction.commit();

        }


//        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        Log.e("Map Setup", (supportMapFragment == null) ? "null" : "not null");
//        new OnMapAndViewReadyListener(supportMapFragment, this);
        supportMapFragment.getMapAsync(this);
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
        Log.e("BreadCrumbs", "Starting location");
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
            Log.e("BreadCrumbs", "Location stuff set");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MainActivity.incrementSeconds, MainActivity.incrementMeters, locationListener);

        }
        crumbAdapter = new CrumbAdapter(getContext());


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

        tvCurrentDistance = (TextView) view.findViewById(R.id.tvCurrentDistance);
        setCurrentDistance(0.0);

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
                    BreadCrumb c = new BreadCrumb(loc, lat + " " + lon);
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

        bSave = (Button) view.findViewById(R.id.bSave);
        bLoad = (Button) view.findViewById(R.id.bLoad);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recyclerView.setVisibility(View.GONE);

        mapInit();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.mapFragment));
        try {
            settingsMenuItem.setVisible(false);
        } catch (Exception e){}
        try {


            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(supportMapFragment);
            ft.commit();
        }catch (Exception e){}

    }

    MenuItem settingsMenuItem;
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        settingsMenuItem = menu.findItem(R.id.action_settings);
        settingsMenuItem.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.addToBackStack("settings");
            fragmentTransaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCurrentPosition() {
        Location location = ((MyLocationListener) locationListener).getLocation();
        if (location != null) {
            etLat.setText(String.format("%.4f", location.getLatitude()));
            etLong.setText(String.format("%.4f", location.getLongitude()));
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

    private void setCurrentDistance(double distance){
        String s;

        if(distance > 1000){
            String f = String.format("%.2f", (distance/1000));
            s = f + " Km";
        } else {
            s = String.valueOf(distance) + " m";
        }
        tvCurrentDistance.setText(getActivity().getResources().getString(R.string.current_distance, s));
    }

    void initGPS() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.GPS_REQUEST_CODE) {
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
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
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

        if (this.getUserVisibleHint()) {
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


    private Marker lastMarkerClicked = null;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                etLat.setText(String.valueOf(latLng.latitude));
                etLong.setText(String.valueOf(latLng.longitude));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.long_create_title)
                        .setMessage(R.string.long_create_message)
                        .setNegativeButton(R.string.add_cancel, null)
                        .setPositiveButton(R.string.long_create, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    float lat = (float)latLng.latitude;
                                    float lon = (float)latLng.longitude;
                                    Location loc = new Location("");
                                    loc.setLatitude(lat);
                                    loc.setLongitude(lon);
                                    loc.setTime(System.nanoTime());
                                    BreadCrumb c = new BreadCrumb(loc, lat + " " + lon);
                                    breadCrumbReference.push().setValue(c);
                                } catch (Exception e) {
                                    Log.e("BOAT", "Breadcrumb could not be made " + e.getMessage());
                                }
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(final Marker marker) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.delete_marker_title)
                        .setMessage(R.string.delete_breadcrumb_message)
                        .setNegativeButton(R.string.cancel_delete, null)
                        .setPositiveButton(R.string.yes_delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Collection<BreadCrumb> crumbSet = breadcrumbs.values();
                                BreadCrumb tempCrumb = null;
                                for (BreadCrumb c: crumbSet) {
                                    if (c.getKey().equals(marker.getSnippet())) {
                                        tempCrumb = c;
                                        break;
                                    }
                                }
                                deleteCrumb(marker.getSnippet());
                                marker.remove();
                                final BreadCrumb finalTempCrumb = tempCrumb;
                                Snackbar.make(getView(), R.string.after_deletion, Snackbar.LENGTH_LONG);
//                                        .setAction("UNDO", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                if (finalTempCrumb != null)
//                                                    breadCrumbReference.push().setValue(finalTempCrumb);
//                                            }
//                                        }).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (marker != null) {
                    LatLng loc = null;
                    Collection<BreadCrumb> crumbSet = breadcrumbs.values();
                    for (BreadCrumb c: crumbSet) {
                        if (c.getKey().equals(marker.getSnippet())) {
                            marker.setPosition(c.getCustomLatLng().getLatLng());
                            return;
                        }
                    }
                }
            }
        });
        mMap.getUiSettings().setZoomControlsEnabled(false);
//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        mMap.setOnMarkerClickListener(this);
        dbInit();
//        drawMarkers();
    }

//    private Double latMin = null, latMax = null, longMin = null, longMax = null;

    private void drawMarkers() {

        for (CustomLatLng latLng : breadcrumbs.keySet()) {

            drawMarker(latLng);

        }
    }

    private void drawMarker(CustomLatLng latLng) {
//        double latVal = latLng.getLatitude();
//        double longVal = latLng.getLongitude();
//        if(latMin == null || latMin > latVal){
//            latMin = latVal;
//        }
//        if(latMax == null || latMax < latVal){
//            latMax = latVal;
//        }
//        if(longMin == null || longMin > longVal){
//            longMin = longVal;
//        }
//        if(longMax == null || longMax < longVal){
//            longMax = longVal;
//        }
        lastMarkerClicked = null;
        mMap.addMarker(new MarkerOptions().position(latLng.getLatLng()).draggable(true).title(latLng.getName()).snippet(latLng.getKey()));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (CustomLatLng customLatLng : breadcrumbs.keySet()) {
            builder.include(customLatLng.getLatLng());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 20);
        mMap.animateCamera(cameraUpdate);

    }

    private Marker lastDrawnMarker = null;

    private double drawLines() { // returns total distance
        double totalDistance = 0.0;
        CustomLatLng thisLatLong = null, lastLatLong = null;
        ArrayList<CustomLatLng> list = new ArrayList<>(breadcrumbs.keySet());
        Collections.sort(list);
        Log.e("List",list.toString());
        for(CustomLatLng c : list){
            lastLatLong = thisLatLong;
            thisLatLong = c;

            totalDistance += drawLine(lastLatLong, thisLatLong);
        }
        return totalDistance;
    }

    private double getDistance(LatLng start, LatLng end){
        Location loc1 = new Location("");
        loc1.setLatitude(start.latitude);
        loc1.setLongitude(start.longitude);
        Location loc2 = new Location("");
        loc2.setLatitude(end.latitude);
        loc2.setLongitude(end.longitude);
        return loc1.distanceTo(loc2);
    }

    private double drawLine(CustomLatLng lastL, CustomLatLng thisL) { // returns distance between
        if(lastL != null && thisL != null) {
            LatLng lastLatLng = lastL.getLatLng(), thisLatLng = thisL.getLatLng();
            Log.e("DrawLine","Drawing line between " + lastL.getIndex() + " & " + thisL.getIndex());
            mMap.addPolyline(new PolylineOptions().add(lastLatLng, thisLatLng).width(5).color(Color.RED));
            return getDistance(lastLatLng, thisLatLng);
        }
        return 0;
    }

    private void redrawMarkers() {
        mMap.clear();
        drawMarkers();
        setCurrentDistance(drawLines());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(lastMarkerClicked)) {
            CustomLatLng customLatLng = new CustomLatLng(marker.getPosition(), marker.getSnippet(), 0, "");
            BreadCrumb breadCrumb = null;
            for (CustomLatLng c : breadcrumbs.keySet()) {
                if (c.equalTo(customLatLng)) {
                    breadCrumb = breadcrumbs.get(c);
                    break;
                }
            }

            if (breadCrumb != null) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, CrumbFragment.newInstance(breadCrumb));
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.addToBackStack(breadCrumb.getName());
                fragmentTransaction.commit();
            } else {
                Log.e("onmarkerclick", customLatLng.toString());
            }
            return true;
        }
        Log.e("LastMarkerClicked", (lastMarkerClicked == null) ? "null" : lastMarkerClicked.toString());
        Log.e("This Marker", (marker == null) ? "null" : marker.toString());
        lastMarkerClicked = marker;
        return false; // false = camera centers on marker, true will not center
    }


    private int index = 0;

    public void deleteCrumb(String c) {
        breadCrumbReference.child(c).removeValue();
    }

    private class CrumbsChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            BreadCrumb breadCrumb = dataSnapshot.getValue(BreadCrumb.class);
            breadCrumb.setKey(dataSnapshot.getKey());
            CustomLocation customLocation = breadCrumb.getLocation();
            CustomLatLng latLng = new CustomLatLng(customLocation.getLatitude(), customLocation.getLongitude(), breadCrumb.getKey(), index++, breadCrumb.getName());
            crumbAdapter.addCrumb(breadCrumb);
            crumbAdapter.notifyDataSetChanged();
            breadcrumbs.put(latLng, breadCrumb);
//            drawMarker(latLng);
            redrawMarkers();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            BreadCrumb newCrumb = dataSnapshot.getValue(BreadCrumb.class);
            newCrumb.setKey(key);
            crumbAdapter.updateCrumb(newCrumb);
            CustomLatLng customLatLng = newCrumb.getCustomLatLng();
            for(CustomLatLng c: breadcrumbs.keySet()){
                if(customLatLng.equalTo(c)){
                    BreadCrumb b = breadcrumbs.get(c);
                    if(breadcrumbs.remove(c) != null){
                        Log.e("Remove Breadcrumb","Breadcrumb removed");
                    }
                    Log.e("Update Crumb","Index : " + c.getIndex());
                    c.setName(b.getName());
                    breadcrumbs.put(c, b);
                    redrawMarkers();
                    return;
                }
            }
//            if (breadcrumbs.containsKey(customLatLng)) {
//                BreadCrumb b = breadcrumbs.get(customLatLng);
//                CustomLatLng oldKey = breadcrumbs.
//                customLatLng.setName(b.getName());
//                breadcrumbs.remove(customLatLng);
//                breadcrumbs.put(customLatLng, b);
//                redrawMarkers();
//            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            BreadCrumb newCrumb = dataSnapshot.getValue(BreadCrumb.class);
            newCrumb.setKey(key);
            crumbAdapter.removeCrumb(newCrumb);
            CustomLatLng customLatLng = newCrumb.getCustomLatLng();
            if (breadcrumbs.containsKey(customLatLng)) {
                breadcrumbs.remove(customLatLng);
                redrawMarkers();
            }

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
