package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * Created by goebelag on 1/15/2017.
 */
public class BreadCrumbsFragment extends Fragment{
    private ArrayList<BreadCrumb> crumbs;

    public BreadCrumbsFragment(){}

    public static BreadCrumbsFragment newInstance(BreadCrumb breadCrumb){
        BreadCrumbsFragment breadCrumbsFragment = new BreadCrumbsFragment();
        Bundle args = new Bundle();
        breadCrumbsFragment.setArguments(args);
        return  breadCrumbsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs, container, false);
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
}
