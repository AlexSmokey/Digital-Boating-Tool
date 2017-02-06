package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.rose_hulman.humphrjm.finalproject.R;

public class etcFragment extends Fragment implements View.OnClickListener {

    public etcFragment(){}



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.etc_info, container, false);
        TextView mText = (TextView) view.findViewById(R.id.textView);
        mText.setOnClickListener(this);
        ImageView mImage = (ImageView) view.findViewById(R.id.imageView);
        mImage.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Snackbar.make(getView(), "Hit back to choose another feature", Snackbar.LENGTH_LONG).show();
    }
}
