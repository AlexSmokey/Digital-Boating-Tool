package edu.rose_hulman.humphrjm.finalproject;

import android.graphics.Picture;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import edu.rose_hulman.humphrjm.finalproject.fragments.PictureFragment;

/**
 * Created by goebelag on 1/23/2017.
 */
public class CrumbPicture implements Parcelable{
//    private Picture picture;
    private String localPicturePath;
    private String remotePicturePath;
//    private Fragment switchTo;



    public CrumbPicture(String remotePicturePath, String localPicturePath) {
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


    public String getLocalPicturePath() {
        return localPicturePath;
    }

    public void setLocalPicturePath(String localPicturePath) {
        this.localPicturePath = localPicturePath;
    }

    public String getRemotePicturePath() {
        return remotePicturePath;
    }

    public void setRemotePicturePath(String remotePicturePath) {
        this.remotePicturePath = remotePicturePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
