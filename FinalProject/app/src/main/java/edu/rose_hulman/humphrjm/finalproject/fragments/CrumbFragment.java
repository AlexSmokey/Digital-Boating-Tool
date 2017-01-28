package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.MainPageOption;
import edu.rose_hulman.humphrjm.finalproject.MyLocationListener;
import edu.rose_hulman.humphrjm.finalproject.R;
import edu.rose_hulman.humphrjm.finalproject.adapters.CrumbAdapter;
import edu.rose_hulman.humphrjm.finalproject.adapters.ImageAdapter;

import static android.app.Activity.RESULT_OK;

/**
 * Created by goebelag on 1/15/2017.
 */
public class CrumbFragment extends Fragment {

    private BreadCrumb crumb;
    ImageView picture;

    private GridView gridView;
    private ImageAdapter imageAdapter;



    public CrumbFragment() {
    }

    public static CrumbFragment newInstance(BreadCrumb breadCrumb) {
        CrumbFragment breadCrumbsFragment = new CrumbFragment();
        breadCrumbsFragment.setCrumb(breadCrumb);
        Bundle args = new Bundle();
        breadCrumbsFragment.setArguments(args);
        return breadCrumbsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageAdapter = new ImageAdapter(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs_history, container, false);
        gridView = (GridView) view.findViewById(R.id.gvCrumbPictures);

        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(onGridClickListener);

//        this.crumb.getPictures().add(new CrumbPicture(null)); //HEYYYY THIS IS ONLY FOR THE DEV PROCESS
//        picture = (ImageView)view.findViewById(R.id.picture);
//        picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Fragment destFragment = crumb.getPictures().get(0).getSwitchTo();
//                if(destFragment != null) {
//                    FragmentActivity activity = (FragmentActivity) getActivity();
//                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, destFragment);
//                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
//                    fragmentTransaction.addToBackStack(crumb.getPictures().get(0).toString());
//                    fragmentTransaction.commit();
//                }
//            }
//        });


        return view;
    }


    public BreadCrumb getCrumb() {
        return crumb;
    }

    public void setCrumb(BreadCrumb crumb) {
        this.crumb = crumb;
    }


    public GridView.OnItemClickListener onGridClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("OnItemClick","Clicked " + position);
            if(gridView.getAdapter().getCount() == (position+1)){
                dispatchTakePictureIntent();
            } else {
                CrumbPicture crumbPicture = (CrumbPicture) gridView.getAdapter().getItem(position);
            }

        }
    };


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.e("CreateIMageFile",storageDir.getAbsolutePath());
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("Created Path",mCurrentPhotoPath);
        return image;
    }

        static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e("File Path again", photoFile.getAbsolutePath());
                Log.e("Context",String.valueOf(getContext()==null));
                Log.e("PhotoFile",String.valueOf(photoFile == null));
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "edu.rose_hulman.humphrjm.finalproject.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            File sd = Environment.getExternalStorageDirectory();
//            File image = new File(mCurrentPhotoPath);
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            imageAdapter.addItem(new CrumbPicture(null, mCurrentPhotoPath));


        }
    }


}
