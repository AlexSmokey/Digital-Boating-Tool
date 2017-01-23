package edu.rose_hulman.humphrjm.finalproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rose_hulman.humphrjm.finalproject.adapters.MainPageAdapter;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {

    MainPageAdapter mainPageAdapter;


    public MainPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainPageAdapter = new MainPageAdapter(getContext());
        recyclerView.setAdapter(mainPageAdapter);

//        RelativeLayout breadCrumbsButton = (RelativeLayout) view.findViewById(R.id.breadCrumbs);
//
//        breadCrumbsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openBreadCrumbs();
//            }
//        });
        return view;
    }
    private void openBreadCrumbs() {
//        Intent MainIntent = new Intent(this, BreadCrumbsFragment.class);
//        startActivity(MainIntent);


        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new BreadCrumbsFragment());
        fragmentTransaction.addToBackStack("breadcrumbs");
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }


}
