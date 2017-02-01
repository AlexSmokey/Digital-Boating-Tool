package edu.rose_hulman.humphrjm.finalproject.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;

import edu.rose_hulman.humphrjm.finalproject.Constants;

/**
 * Created by humphrjm on 1/31/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Void>{
    private final long TEN_MEGABYTES = 1024 * 1024 * 10;

    private ImageConsumer imageConsumer;
    private String imageKey;

    public DownloadImageTask(ImageConsumer activity){
        imageConsumer = activity;
    }
    @Override
    protected Void doInBackground(String... params) {
        String imagePath = params[0];
        imageKey = params[1];
        try {

            StorageReference storageReference = Constants.DB_STORAGE_ROOT.child("/images/" + imagePath);

            Log.e("DoInBackground", storageReference.getPath());

            storageReference.getBytes(TEN_MEGABYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
        imageConsumer.onImageLoaded(bitmap, imageKey);
    }

    public interface ImageConsumer{
        void onImageLoaded(Bitmap bitmap, String key);
    }
}
