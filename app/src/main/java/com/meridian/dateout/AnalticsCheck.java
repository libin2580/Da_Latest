package com.meridian.dateout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import static com.meridian.dateout.Constants.analytics;

/**
 * Created by Anvin on 5/18/2017.
 */

public class AnalticsCheck extends AppCompatActivity {
    public AnalticsCheck(Context appContext){
        try {
           // analytics.setCurrentScreen(appContext, appContext.getLocalClassName() , null /* class override */);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
