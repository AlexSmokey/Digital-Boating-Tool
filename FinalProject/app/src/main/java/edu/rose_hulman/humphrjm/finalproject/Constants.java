package edu.rose_hulman.humphrjm.finalproject;

import android.content.Context;
import android.os.Environment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by humphrjm on 1/28/2017.
 */

public class Constants {
    public static final int RESULT_CODE_PHOTO = 1;
    public static final int GPS_REQUEST_CODE = 1;

    public static final String SHARED_PREF = "DIGITAL_BOATING_TOOL";
    public static final String KEY_IMPERIAL = "IMPERIAL";
    public static final String KEY_DISTANCE = "DISTANCE";
    public static final String KEY_TIME = "TIME";
    public static final String KEY_AUTO = "AUTO_MODE";

    public static final float FEET_PER_METER = 3.28084f;


    public static StorageReference DB_STORAGE_ROOT = FirebaseStorage.getInstance().getReferenceFromUrl("gs://digital-boating-tool.appspot.com");


}
