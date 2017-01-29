package edu.rose_hulman.humphrjm.finalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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

import edu.rose_hulman.humphrjm.finalproject.BreadCrumb;
import edu.rose_hulman.humphrjm.finalproject.CrumbPicture;
import edu.rose_hulman.humphrjm.finalproject.R;

/**
 * Created by goebelag on 1/15/2017.
 */
public class PictureFragment extends Fragment {

    private CrumbPicture crumbPicture;
    private ImageView imageView;
    private EditText etPictureTitle;
    private EditText etNotes;

    private DatabaseReference imgRef;
    private String breadCrumbKey;

    private void initDB(){
        imgRef = FirebaseDatabase.getInstance().getReference().child("crumbs").child(breadCrumbKey).child("pictures").child(crumbPicture.getKey());
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
        imageView = (ImageView) view.findViewById(R.id.ivEditImage);
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
            imageView.setImageBitmap(crumbPicture.getBitmap());
            etNotes.setText(crumbPicture.getPictureNotes());
        }


        return view;
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
