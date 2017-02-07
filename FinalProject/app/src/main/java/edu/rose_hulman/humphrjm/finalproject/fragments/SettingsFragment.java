package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import edu.rose_hulman.humphrjm.finalproject.Constants;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * Created by humphrjm on 2/3/2017.
 */

public class SettingsFragment extends Fragment implements TextWatcher, CompoundButton.OnCheckedChangeListener {

    private EditText etDistance;
    private EditText etTime;
    private TextView tvDistance;
    private Switch sUnits;
    private SharedPreferences sharedPreferences;
    private boolean imperial;
    private MenuItem miSettings;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        imperial = sharedPreferences.getBoolean(Constants.KEY_IMPERIAL, false);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvDistance = (TextView) view.findViewById(R.id.tvSettingsDistance);
        etTime = (EditText) view.findViewById(R.id.etTime);
        etDistance = (EditText) view.findViewById(R.id.etDistance);
        sUnits = (Switch) view.findViewById(R.id.sUnits);
        float dist = sharedPreferences.getInt(Constants.KEY_DISTANCE, 0);
        etTime.setText(String.valueOf(sharedPreferences.getInt(Constants.KEY_TIME, 0)));
        if(imperial){
            dist *= Constants.FEET_PER_METER;
            tvDistance.setText("Distance (feet)");
        } else {
            tvDistance.setText("Distance (meters)");
        }
        etDistance.setText(String.valueOf(dist));
        sUnits.setChecked(imperial);
        sUnits.setOnCheckedChangeListener(this);
        etTime.addTextChangedListener(this);
        etDistance.addTextChangedListener(this);
//        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        return view;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        float dist = sharedPreferences.getInt(Constants.KEY_DISTANCE,0);
        if(imperial){
            dist *= Constants.FEET_PER_METER;
        }
        int time = sharedPreferences.getInt(Constants.KEY_TIME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            dist = Integer.parseInt(etDistance.getText().toString());
            if(imperial){
                dist /= Constants.FEET_PER_METER;
            }
            editor.putFloat(Constants.KEY_DISTANCE, dist);

        } catch (Exception e){
            etDistance.setText(String.valueOf(dist));
        }
        try {
            time = Integer.parseInt(etTime.getText().toString());
            editor.putInt(Constants.KEY_TIME, time);
        } catch (Exception ex){
            etTime.setText(String.valueOf(time));
        }
        editor.apply();

    }

    private void updateDist(){

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        imperial = isChecked;
        if(imperial){
            tvDistance.setText("Distance (feet)");
        } else {
            tvDistance.setText("Distance (meters)");
        }
        sharedPreferences.edit().putBoolean(Constants.KEY_IMPERIAL, isChecked).apply();
    }
}
