package edu.rose_hulman.humphrjm.finalproject;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

import edu.rose_hulman.humphrjm.finalproject.fragments.SettingsFragment;
import edu.rose_hulman.humphrjm.finalproject.fragments.SplashscreenFragment;

public class MainActivity extends AppCompatActivity {



    public static String ROOT_DIRECTORY;
    public static HashMap<String, String> savedLocalImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ROOT_DIRECTORY =  getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//        savedLocalImages = new HashMap<>();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user == null){
//            mAuth.signInAnonymously();
//        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new SplashscreenFragment());
        fragmentTransaction.commit();




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new SettingsFragment());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.addToBackStack("settings");
            fragmentTransaction.commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }






}
