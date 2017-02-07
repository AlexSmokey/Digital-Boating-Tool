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
import android.widget.CheckBox;
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
    private CheckBox cbAuto;
    private SharedPreferences sharedPreferences;
    private boolean imperial;

    private int seconds;
    private float meters;
    private boolean autoCrumb;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
        imperial = sharedPreferences.getBoolean(Constants.KEY_IMPERIAL, false);
        seconds = sharedPreferences.getInt(Constants.KEY_TIME, 0);
        meters = sharedPreferences.getFloat(Constants.KEY_DISTANCE,0);
        autoCrumb = sharedPreferences.getBoolean(Constants.KEY_AUTO, false);
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
        cbAuto = (CheckBox) view.findViewById(R.id.cbAuto);
        cbAuto.setChecked(autoCrumb);
        cbAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoCrumb = isChecked;
            }
        });
        etTime.setText(String.valueOf(seconds));
        float dist = meters;
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
    public void onDestroyView() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.KEY_AUTO, autoCrumb);
        editor.putBoolean(Constants.KEY_IMPERIAL, imperial);
        editor.putFloat(Constants.KEY_DISTANCE, meters);
        editor.putInt(Constants.KEY_TIME, seconds);
        editor.apply();
        super.onDestroyView();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            float f = Float.valueOf(etDistance.getText().toString());
            if(imperial){
                f /= Constants.FEET_PER_METER;
            }
            meters = f;
        } catch( Exception ex){
            if(imperial) {
                etDistance.setText(String.format("%.2f",meters * Constants.FEET_PER_METER));
            } else {
                etDistance.setText(String.valueOf(meters));
            }
        }
        try{
            seconds = Integer.valueOf(etTime.getText().toString());
        } catch (Exception e){
            etTime.setText(String.valueOf(seconds));
        }
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

    }
}
