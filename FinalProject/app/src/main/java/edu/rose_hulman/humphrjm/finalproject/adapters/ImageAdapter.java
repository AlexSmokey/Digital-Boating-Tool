package edu.rose_hulman.humphrjm.finalproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.views.SquareImageView;

/**
 * Created by humphrjm on 1/27/2017.
 */

public class ImageAdapter extends BaseAdapter {

    private FirebaseDatabase crumbRef;

    private ArrayList<CrumbPicture> pictureList;
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
        pictureList = new ArrayList<>();
        pictureList.add(null);
        notifyDataSetChanged();
    }

    public int getCount() {
        return pictureList.size();
    }

    public Object getItem(int position) {
        return pictureList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imageView;
        CrumbPicture crumbPicture = pictureList.get(position);
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new SquareImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(8, 8, 8, 8);


        } else {
            imageView = (SquareImageView) convertView;
        }
        int pWidth = GridView.LayoutParams.MATCH_PARENT;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(pWidth, pWidth);
        imageView.setLayoutParams(params);
        int wid = imageView.getWidth();
        imageView.setMinimumHeight(wid);
        imageView.setMaxHeight(wid);

        if(crumbPicture != null) {
//            if (crumbPicture.getLocalPicturePath() == null) {
//                if (crumbPicture.getRemotePicturePath() != null) {
//                    downloadImage();
//                }
//                imageView.setImageResource(R.mipmap.ic_launcher);
//            } else {
//
//                imageView.setImageBitmap(crumbPicture.getBitmap());
//            }
            Bitmap bitmap = crumbPicture.getBitmap();
            if(bitmap == null){
                downloadImage();
                imageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                imageView.setImageBitmap(bitmap);
            }
        } else {
            imageView.setImageResource(android.R.drawable.btn_plus);
        }


        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);


//        Picture picture = crumbPicture.getPicture();
//        Bitmap bm = pictureDrawable2Bitmap(picture);

//        imageView.setImageBitmap(bm);

//        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    private void downloadImage(){

    }


    private static Bitmap pictureDrawable2Bitmap(Picture picture) {
        PictureDrawable pd = new PictureDrawable(picture);
        Bitmap bitmap = Bitmap.createBitmap(pd.getIntrinsicWidth(), pd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pd.getPicture());
        return bitmap;
    }

    public void addItem(CrumbPicture picture){
        pictureList.add(0, picture);
        notifyDataSetChanged();
    }

    public Object getLastItem(){
        return pictureList.get(pictureList.size()-1);
    }


}