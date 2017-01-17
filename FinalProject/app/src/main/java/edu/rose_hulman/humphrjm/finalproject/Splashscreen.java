package edu.rose_hulman.humphrjm.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by humphrjm on 12/18/2016.
 */

public class Splashscreen extends AppCompatActivity {

    public static final int REQUEST_CODE_INPUT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        final ImageView imageView = (ImageView)this.findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp();
            }
        });
    }

    private void openApp() {
        Intent MainIntent = new Intent(this, MainActivity.class);
        startActivity(MainIntent);
    }
}
