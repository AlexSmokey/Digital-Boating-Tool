package edu.rose_hulman.humphrjm.finalproject.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.MainPageOption;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.fragments.BreadCrumbsFragment;
import edu.rose_hulman.humphrjm.finalproject.fragments.CrumbFragment;

/**
 * Created by goebelag on 1/15/2017.
 */

public class CrumbAdapter extends RecyclerView.Adapter<CrumbAdapter.ViewHolder> {

    private ArrayList<BreadCrumb> crumbs;
    private Context context;
    private DatabaseReference crumbsReference;

    public CrumbAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
        crumbs = new ArrayList<>();
    }

    @Override
    public CrumbAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crumb, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.Name.setText(crumbs.get(position).getName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                crumbs.remove(position);
                notifyDataSetChanged();
                return true;
            }
        });

    }

    public void setCrumbs(ArrayList<BreadCrumb> list) {
        this.crumbs = list;
    }

    @Override
    public int getItemCount() {
        return crumbs.size();
    }

    public void addCrumb(BreadCrumb c) {
        crumbs.add(c);
        notifyDataSetChanged();
    }

    public void updateCrumb(BreadCrumb c){
        for(BreadCrumb breadCrumb : crumbs){
            if(c.getKey().equals(breadCrumb.getKey())){
                breadCrumb.setValues(c);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void removeCrumb(BreadCrumb c){
        for(BreadCrumb breadCrumb : crumbs){
            if(c.getKey().equals(breadCrumb.getKey())){
                crumbs.remove(breadCrumb);
                notifyDataSetChanged();
                return;
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;

        public ViewHolder(final View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.Name);
            RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.crumb_layout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BreadCrumb c = crumbs.get(getAdapterPosition());

                    if(c != null) {
                        FragmentActivity activity = (FragmentActivity) context;
                        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, CrumbFragment.newInstance(c));
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.addToBackStack(c.getName());
                        fragmentTransaction.commit();
                    }

                }
            });

        }
    }
}
