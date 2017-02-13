package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.ImageProcessing.ImageHandler;
import edu.rose_hulman.humphrjm.finalproject.MainActivity;
import edu.rose_hulman.humphrjm.finalproject.PicDrawView;
import edu.rose_hulman.humphrjm.finalproject.R;

import static android.support.v4.content.PermissionChecker.PERMISSION_DENIED;

/**
 * Created by goebelag on 1/15/2017.
 */
public class PictureFragment extends Fragment {

    private CrumbPicture crumbPicture;
    private PicDrawView imageView;
    private Bitmap alteredBitmap;
    private EditText etPictureTitle;
    private EditText etNotes;

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 8;
    private boolean permissionGranted;

    private DatabaseReference imgRef;
    private String breadCrumbKey;

    private void initDB(){
        imgRef = FirebaseDatabase.getInstance().getReference().child(MainActivity.ANDROID_ID).child("crumbs").child(breadCrumbKey).child("pictures").child(crumbPicture.getKey());
        imgRef.addValueEventListener(new PictureValueEventListener());
    }

    public PictureFragment() {
    }

    public static PictureFragment newInstance(CrumbPicture crumbPicture, String breadCrumbKey) {
        PictureFragment pictureFragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putParcelable("crumbPicture", crumbPicture);
        args.putString("breadCrumbKey", breadCrumbKey);
        pictureFragment.setArguments(args);
        return pictureFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.crumbPicture = getArguments().getParcelable("crumbPicture");
            this.breadCrumbKey = getArguments().getString("breadCrumbKey");
        }
        initDB();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.breadcrumbs_edit, container, false);
        imageView = (PicDrawView) view.findViewById(R.id.ivEditImage);
        etPictureTitle = (EditText) view.findViewById(R.id.etEditTitle);
        etNotes = (EditText) view.findViewById(R.id.etEditNotes);
        if(crumbPicture != null){
            etPictureTitle.setText(crumbPicture.getPictureTitle());
            etNotes.setText(crumbPicture.getPictureNotes());
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // empty
            }

            @Override
            public void afterTextChanged(Editable s) {
                String title = etPictureTitle.getText().toString();
                String notes = etNotes.getText().toString();
                crumbPicture.setPictureTitle(title);
                crumbPicture.setPictureNotes(notes);
                imgRef.setValue(crumbPicture);

            }
        };

        etPictureTitle.addTextChangedListener(textWatcher);
        etNotes.addTextChangedListener(textWatcher);


        if(crumbPicture != null){
            etPictureTitle.setText(crumbPicture.getPictureTitle());
            Bitmap bmp = crumbPicture.getBitmap();
            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(),
                    bmp.getHeight(), bmp.getConfig());
            //imageView.setImageBitmap(bmp);
            imageView.setNewImage(alteredBitmap, bmp);
            etNotes.setText(crumbPicture.getPictureNotes());
        }


        permissionGranted = false;
        if (getActivity() == null) {
            Log.e(getString(R.string.boat_tag), getString(R.string.error_act));
            super.onStop();
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }else {
            permissionGranted = true;
        }
        super.onStart();


        return view;
    }



    @Override
    public void onStop() {
        if (getActivity() == null) {
            Log.e(getString(R.string.boat_tag), getString(R.string.error_act));
            super.onStop();
            return;
        }
        if (!permissionGranted) {
            Log.e(getString(R.string.boat_tag), getString(R.string.no_permission));
            super.onStop();
            return;
        }
        Bitmap bmp = overlay(imageView.getbitMap(), imageView.getAltBitMap());
























        Bitmap emptyBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        if (!bmp.sameAs(emptyBitmap)) {
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, crumbPicture.getPictureTitle(), "Edited Breadcrumb");
        }
        super.onStop();
    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                } else {
                    permissionGranted = false;
                }
                return;
            }
        }
    }

    private class PictureValueEventListener implements ValueEventListener{

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            CrumbPicture newPicture = dataSnapshot.getValue(CrumbPicture.class);
            crumbPicture.setValues(newPicture);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e("PictureFragmentDB", "Database error: " + databaseError.toString());
        }
    }


}
