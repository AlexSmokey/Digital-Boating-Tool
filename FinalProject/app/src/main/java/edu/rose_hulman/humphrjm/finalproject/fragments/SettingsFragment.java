package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.rose_hulman.humphrjm.finalproject.MainActivity;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * Created by humphrjm on 2/3/2017.
 */

public class SettingsFragment extends Fragment implements TextWatcher {

    private EditText etDistance;
    private EditText etTime;

    public SettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        etTime = (EditText) view.findViewById(R.id.etTime);
        etDistance = (EditText) view.findViewById(R.id.etDistance);
        etTime.setText(String.valueOf(MainActivity.incrementSeconds));
        etDistance.setText(String.valueOf(MainActivity.incrementMeters));
        etTime.addTextChangedListener(this);
        etDistance.addTextChangedListener(this);
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
        try {
            MainActivity.incrementMeters = Integer.parseInt(etDistance.getText().toString());
        } catch (Exception e){
            etDistance.setText(String.valueOf(MainActivity.incrementMeters));
        }
        try {
            MainActivity.incrementSeconds = Integer.parseInt(etTime.getText().toString());
        } catch (Exception ex){
            etTime.setText(String.valueOf(MainActivity.incrementSeconds));
        }

    }
}
