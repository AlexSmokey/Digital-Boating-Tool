package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * Created by goebelag on 1/15/2017.
 */
public class PictureFragment extends Fragment {

    private CrumbPicture crumbPicture;

    public PictureFragment() {
    }

    public static PictureFragment newInstance(CrumbPicture crumbPicture) {
        PictureFragment pictureFragment = new PictureFragment();
        Bundle args = new Bundle();
        pictureFragment.setArguments(args);
        return pictureFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){

        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs_edit, container, false);



        return view;
    }


}
