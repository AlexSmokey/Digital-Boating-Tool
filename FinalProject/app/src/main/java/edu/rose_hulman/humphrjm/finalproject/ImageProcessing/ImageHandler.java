package edu.rose_hulman.humphrjm.finalproject.ImageProcessing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.rose_hulman.humphrjm.finalproject.Constants;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.MainActivity;

/**
 * Created by humphrjm on 1/31/2017.
 */

public class ImageHandler {

    private Context context;
    public static StorageReference dbStorage ;

    public ImageHandler(Context context){
        this.context = context;
    }

    public static void uploadImage(final CrumbPicture crumbPicture, final ImageUploadConsumer activity){

        dbStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://digital-boating-tool.appspot.com");
        Uri file = Uri.fromFile(new File(MainActivity.ROOT_DIRECTORY, crumbPicture.getPicturePath()));
//        Log.e("Upload Path",Constants.DB_STORAGE_ROOT.getPath() + "/images/" + file.getLastPathSegment());
        StorageReference riversRef = dbStorage.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("Upload Failed",exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                crumbPicture.setRemotePicturePath(downloadUrl.getPath());
                activity.onImageUploaded(crumbPicture);

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("Progress",taskSnapshot.getBytesTransferred() + " / " + taskSnapshot.getTotalByteCount());
            }
        });
    }

    public interface ImageUploadConsumer{
        void onImageUploaded(CrumbPicture crumbPicture);
    }

    public static Bitmap getImage(String imageName){
        File storageDir = new File(MainActivity.ROOT_DIRECTORY);
        File image = new File(storageDir, imageName);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
        return bitmap;
    }

    public String imageName(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CrumbPic_");
        stringBuilder.append(timeStamp);
        return null;
    }

//    public String saveImage(Bitmap bitmap){
//        FileOutputStream out = null;
//        try {
//            out = new FileOutputStream(filename);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPG_" + timeStamp + "_";
//        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        Log.e("CreateImageFile",storageDir.getAbsolutePath());
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        Log.e("Created Path",mCurrentPhotoPath);
//        return image;
//    }
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Log.e("File Path again", photoFile.getAbsolutePath());
//                Log.e("Context",String.valueOf(getContext()==null));
//                Log.e("PhotoFile",String.valueOf(photoFile == null));
//                Uri photoURI = FileProvider.getUriForFile(getContext(),
//                        "edu.rose_hulman.humphrjm.finalproject.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }



}
