package com.meridian.dateout.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.R;

import static com.meridian.dateout.Constants.analytics;


public class MainActivityNw extends FragmentActivity {


    FragmentManager fragmentManager;
    FrameLayout container;
    Button log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity_nw);
        analytics = FirebaseAnalytics.getInstance(MainActivityNw.this);

        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);
        container = (FrameLayout) findViewById(R.id.frame_container);
        execute();
    }

    private void execute() {
        String tag = "lib";
        Fragment fragment = new FaceBukFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, tag);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        transaction.addToBackStack(tag).commit();


    }


}
