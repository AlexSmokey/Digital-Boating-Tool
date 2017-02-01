package edu.rose_hulman.humphrjm.finalproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.rose_hulman.humphrjm.finalproject.AsyncTasks.DownloadImageTask;
import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.ImageProcessing.ImageHandler;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.views.SquareImageView;

/**
 * Created by humphrjm on 1/27/2017.
 */

public class ImageAdapter extends BaseAdapter implements DownloadImageTask.ImageConsumer, ImageHandler.ImageUploadConsumer{

    private DatabaseReference pictureRef;

    private ArrayList<CrumbPicture> pictureList;
    private HashMap<String, String> savedLocalImages;
    private Context mContext;

    private String breadCrumbKey;

    public ImageAdapter(Context c, String breadCrumbKey) {
        this.breadCrumbKey = breadCrumbKey;
        mContext = c;
        pictureList = new ArrayList<>();
        savedLocalImages = new HashMap<>();
        pictureList.add(null);
        notifyDataSetChanged();
        pictureRef = FirebaseDatabase.getInstance().getReference().child("crumbs").child(breadCrumbKey).child("pictures");
        pictureRef.addChildEventListener(new CrumbChildEventListener());
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

        if (crumbPicture != null) {
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
            if (bitmap == null) {
//                downloadImage(crumbPicture);
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

    private void downloadImage(CrumbPicture crumbPicture) {
        (new DownloadImageTask(this)).execute(crumbPicture.getRemotePicturePath(), crumbPicture.getKey());
    }


    public void removeItem(int location){
        pictureRef.child(pictureList.get(location).getKey()).removeValue();
    }


    public void addItem(CrumbPicture picture) {
        ImageHandler.uploadImage(picture, this);
//        notifyDataSetChanged();
    }

    public Object getLastItem() {
        return pictureList.get(pictureList.size() - 1);
    }

    @Override
    public void onImageLoaded(Bitmap bitmap, String imageKey) {
        for(CrumbPicture crumbPicture : pictureList){
            if(crumbPicture.getKey().equals(imageKey)){

            }
        }
    }

    @Override
    public void onImageUploaded(CrumbPicture crumbPicture) {
        savedLocalImages.put(crumbPicture.getRemotePicturePath(), crumbPicture.getLocalPicturePath());
        pictureRef.push().setValue(crumbPicture);
    }


    private class CrumbChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            CrumbPicture crumbPicture = dataSnapshot.getValue(CrumbPicture.class);
            crumbPicture.setKey(dataSnapshot.getKey());
            if(savedLocalImages.containsKey(crumbPicture.getRemotePicturePath())){
                crumbPicture.setLocalPicturePath(savedLocalImages.get(crumbPicture.getRemotePicturePath()));
//                savedLocalImages.remove(crumbPicture.getRemotePicturePath());
            }
            pictureList.add(0, crumbPicture);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            CrumbPicture crumbPicture = dataSnapshot.getValue(CrumbPicture.class);
            String key = dataSnapshot.getKey();
            for(CrumbPicture c : pictureList){
                if(c.getKey().equals(key)){
                    c.setValues(crumbPicture);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            CrumbPicture crumbPicture = dataSnapshot.getValue(CrumbPicture.class);
            String key = dataSnapshot.getKey();
            for(CrumbPicture c : pictureList){
                if(c.getKey().equals(key)){
                    pictureList.remove(c);
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("ImageAdapterDB", "Database error: " + databaseError.toString());
        }
    }






}