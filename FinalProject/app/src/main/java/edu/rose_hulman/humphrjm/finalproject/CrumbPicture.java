package edu.rose_hulman.humphrjm.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.File;

import edu.rose_hulman.humphrjm.finalproject.ImageProcessing.ImageHandler;

/**
 * Created by goebelag on 1/23/2017.
 */
public class CrumbPicture implements Parcelable{
//    private Picture picture;
    private String key;

//    private String localPicturePath;
    private String picturePath;
//
//    private String remotePicturePath;
    private String pictureTitle;
    private String pictureNotes;

//    private Fragment switchTo;

    public CrumbPicture(){}


    public CrumbPicture(String picturePath) {
        this.pictureTitle = "title";
        this.pictureNotes = "notes";
//        this.remotePicturePath = remotePicturePath;
//        this.localPicturePath = localPicturePath;
        this.picturePath = picturePath;
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

//    @Exclude
//    public String getLocalPicturePath() {
//        return localPicturePath;
//    }

    protected CrumbPicture(Parcel in) {
        key = in.readString();
        picturePath = in.readString();
        pictureTitle = in.readString();
        pictureNotes = in.readString();
    }

    public static final Creator<CrumbPicture> CREATOR = new Creator<CrumbPicture>() {
        @Override
        public CrumbPicture createFromParcel(Parcel in) {
            return new CrumbPicture(in);
        }

        @Override
        public CrumbPicture[] newArray(int size) {
            return new CrumbPicture[size];
        }
    };

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


//    @Exclude
//    public void setLocalPicturePath(String localPicturePath) {
//        this.localPicturePath = localPicturePath;
//
//    }

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

//    public String getRemotePicturePath() {
//        return remotePicturePath;
//    }
//
//    public void setRemotePicturePath(String remotePicturePath) {
//        this.remotePicturePath = remotePicturePath;
//    }

    @Exclude
    public Bitmap getBitmap(){ // do not pass bitmaps through fragments, bad bad bad
        if(picturePath == null){
            return null;
        }

        return ImageHandler.getImage(picturePath);


//        File image = new File(picturePath);
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
//        return bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(picturePath);
//        dest.writeString(localPicturePath);
//        dest.writeString(remotePicturePath);
        dest.writeString(pictureTitle);
        dest.writeString(pictureNotes);

    }

    public void setValues(CrumbPicture otherPicture){
        if(otherPicture != null) {
            if (otherPicture.picturePath != null && !otherPicture.picturePath.isEmpty()) {
                this.picturePath = otherPicture.picturePath;
            }
//            this.remotePicturePath = otherPicture.remotePicturePath;
            this.pictureNotes = otherPicture.pictureNotes;
            this.pictureTitle = otherPicture.pictureTitle;
        }
    }
}
