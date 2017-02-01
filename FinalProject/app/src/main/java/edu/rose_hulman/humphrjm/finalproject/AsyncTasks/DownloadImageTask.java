package edu.rose_hulman.humphrjm.finalproject.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;

import edu.rose_hulman.humphrjm.finalproject.Constants;

/**
 * Created by humphrjm on 1/31/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Void>{
    private final long FOUR_MEGABYTES = 1024 * 1024 * 4;

    private ImageConsumer imageConsumer;
    private String imageKey, imagePath;

    public DownloadImageTask(ImageConsumer activity){
        imageConsumer = activity;
    }
    @Override
    protected Void doInBackground(String... params) {
        Log.e("DOWNLOADING","Downloading file : " + params[0]);
        imagePath = params[0];
        imageKey = params[1];
        if(imagePath == null || imageKey == null){
            return null;
        }
        try {

            StorageReference dbRoot = FirebaseStorage.getInstance().getReferenceFromUrl("gs://digital-boating-tool.appspot.com").child("images");
            StorageReference storageReference = dbRoot.child(imagePath);

            Log.e("DoInBackground","Image Path: " + imagePath);

            Log.e("DoInBackground", "Full path: " + storageReference.getPath());

            storageReference.getBytes(FOUR_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    customPostExecute(bytes);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("DoInBackground","Failed to download.");
                }
            });

        } catch (Exception e) {
            Log.d("DownloadImageTask", "Error: " + e.toString());
        }
        return null;
    }

    private void customPostExecute(byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageConsumer.onImageLoaded(bitmap, imagePath, imageKey);
    }

    public interface ImageConsumer{
        void onImageLoaded(Bitmap bitmap, String imageName, String key);
    }
}
