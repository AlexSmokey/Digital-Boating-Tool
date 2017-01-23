package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.MainPageOption;
import edu.rose_hulman.humphrjm.finalproject.MyLocationListener;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.adapters.CrumbAdapter;

/**
 * Created by goebelag on 1/15/2017.
 */
public class CrumbFragment extends Fragment {

    private BreadCrumb crumb;
    ImageView picture;


    public CrumbFragment() {
    }

    public static CrumbFragment newInstance(BreadCrumb breadCrumb) {
        CrumbFragment breadCrumbsFragment = new CrumbFragment();
        breadCrumbsFragment.setCrumb(breadCrumb);
        Bundle args = new Bundle();
        breadCrumbsFragment.setArguments(args);
        return breadCrumbsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs_history, container, false);
        this.crumb.getPictures().add(new CrumbPicture(null)); //HEYYYY THIS IS ONLY FOR THE DEV PROCESS
        picture = (ImageView)view.findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment destFragment = crumb.getPictures().get(0).getSwitchTo();
                if(destFragment != null) {
                    FragmentActivity activity = (FragmentActivity) getActivity();
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, destFragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.addToBackStack(crumb.getPictures().get(0).toString());
                    fragmentTransaction.commit();
                }
            }
        });


        return view;
    }


    public BreadCrumb getCrumb() {
        return crumb;
    }

    public void setCrumb(BreadCrumb crumb) {
        this.crumb = crumb;
    }
}
