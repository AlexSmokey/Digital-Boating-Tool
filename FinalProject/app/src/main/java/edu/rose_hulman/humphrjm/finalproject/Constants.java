package edu.rose_hulman.humphrjm.finalproject;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by humphrjm on 1/28/2017.
 */

public class Constants {
    public static final int RESULT_CODE_PHOTO = 1;
    public static final int GPS_REQUEST_CODE = 1;

    public static StorageReference DB_STORAGE_ROOT = FirebaseStorage.getInstance().getReference("gs://digital-boating-tool.appspot.com");
}
