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

import java.util.ArrayList;

import edu.rose_hulman.humphrjm.finalproject.MainPageOption;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.fragments.BreadCrumbsFragment;
import edu.rose_hulman.humphrjm.finalproject.fragments.etcFragment;

/**
 * Created by humphrjm on 1/16/2017.
 */

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder> {

    private ArrayList<MainPageOption> mainPageOptions = new ArrayList<>();
    private Context context;

    public MainPageAdapter(Context context) {
        this.context = context;
        mainPageOptions.add(new MainPageOption("Bread Crumbs","Tool for making digital markers on a map with descriptions and pictures in order to find one's way back", R.mipmap.ic_launcher, R.layout.breadcrumbs, new BreadCrumbsFragment()));
        mainPageOptions.add(new MainPageOption("TBA","Coming Soon", R.mipmap.ic_launcher, R.layout.etc_info, new etcFragment()));
        notifyDataSetChanged();
    }

    @Override
    public MainPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MainPageOption pageOption = mainPageOptions.get(position);
        holder.imageView.setImageResource(pageOption.getImg());
        holder.tvName.setText(pageOption.getName());
        holder.tvDesc.setText(pageOption.getDescription());

    }

    @Override
    public int getItemCount() {
        return mainPageOptions.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvDesc;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivImage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            RelativeLayout relativeLayout = (RelativeLayout) itemView.findViewById(R.id.main_option);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainPageOption mainPageOption = mainPageOptions.get(getAdapterPosition());

                    Fragment destFragment = mainPageOption.getSwitchTo();
                    if(destFragment != null) {
                        FragmentActivity activity = (FragmentActivity) context;
                        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, destFragment);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                        fragmentTransaction.addToBackStack(mainPageOption.getName());
                        fragmentTransaction.commit();
                    }

                }
            });

        }
    }
}
