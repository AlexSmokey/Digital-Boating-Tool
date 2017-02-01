package edu.rose_hulman.humphrjm.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.google.firebase.database.Exclude;

import java.io.File;

import edu.rose_hulman.humphrjm.finalproject.fragments.PictureFragment;

/**
 * Created by goebelag on 1/23/2017.
 */
public class CrumbPicture implements Parcelable{
//    private Picture picture;
    private String key;

    private String localPicturePath;

    private String remotePicturePath;
    private String pictureTitle;
    private String pictureNotes;
//    private Fragment switchTo;

    public CrumbPicture(){}


    public CrumbPicture(String remotePicturePath, String localPicturePath) {
        this.pictureTitle = "title";
        this.pictureNotes = "notes";
        this.remotePicturePath = remotePicturePath;
        this.localPicturePath = localPicturePath;
//        this.picture = picture;
//        this.switchTo = new PictureFragment();
    }

//    public Picture getPicture() {
//        return picture;
//    }
//
//    public void setPicture(Picture picture) {
//        this.picture = picture;
//    }

//    public Fragment getSwitchTo() {
//        return switchTo;
//    }
//
//    public void setSwitchTo(Fragment switchTo) {
//        this.switchTo = switchTo;
//    }

    @Exclude
    public String getLocalPicturePath() {
        return localPicturePath;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Exclude
    public void setLocalPicturePath(String localPicturePath) {
        this.localPicturePath = localPicturePath;

    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getPictureNotes() {
        return pictureNotes;
    }

    public void setPictureNotes(String pictureNotes) {
        this.pictureNotes = pictureNotes;
    }

    public String getRemotePicturePath() {
        return remotePicturePath;
    }

    public void setRemotePicturePath(String remotePicturePath) {
        this.remotePicturePath = remotePicturePath;
    }

    @Exclude
    public Bitmap getBitmap(){ // do not pass bitmaps through fragments, bad bad bad
        if(getLocalPicturePath() == null){
            return null;
        }
        File image = new File(this.getLocalPicturePath());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(localPicturePath);
        dest.writeString(remotePicturePath);
        dest.writeString(pictureTitle);
        dest.writeString(pictureNotes);
    }

    public void setValues(CrumbPicture otherPicture){
        if(otherPicture != null) {
            if (otherPicture.localPicturePath != null && !otherPicture.localPicturePath.isEmpty()) {
                this.localPicturePath = otherPicture.localPicturePath;
            }
            this.remotePicturePath = otherPicture.remotePicturePath;
            this.pictureNotes = otherPicture.pictureNotes;
            this.pictureTitle = otherPicture.pictureTitle;
        }
    }
}
