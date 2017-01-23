package edu.rose_hulman.humphrjm.finalproject;

import android.graphics.Picture;
import android.support.v4.app.Fragment;

import edu.rose_hulman.humphrjm.finalproject.fragments.PictureFragment;

/**
 * Created by goebelag on 1/23/2017.
 */
public class CrumbPicture {
    private Picture picture;
    private Fragment switchTo;

    public CrumbPicture(Picture picture) {
        this.picture = picture;
        this.switchTo = new PictureFragment();
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Fragment getSwitchTo() {
        return switchTo;
    }

    public void setSwitchTo(Fragment switchTo) {
        this.switchTo = switchTo;
    }
}
