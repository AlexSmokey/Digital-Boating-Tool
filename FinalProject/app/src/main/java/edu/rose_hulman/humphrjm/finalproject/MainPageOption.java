package edu.rose_hulman.humphrjm.finalproject;

import android.support.v4.app.Fragment;

/**
 * Created by humphrjm on 1/16/2017.
 */

public class MainPageOption {
    private String name;
    private String description;
    private int img;
    private int layoutId;
    private Fragment switchTo;

    public MainPageOption(String name, String description, int img, int layoutId, Fragment switchTo){
        this.name = name;
        this.description = description;
        this.img = img;
        this.layoutId = layoutId;
        this.switchTo = switchTo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImg() {
        return img;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public Fragment getSwitchTo() {
        return switchTo;
    }
}
