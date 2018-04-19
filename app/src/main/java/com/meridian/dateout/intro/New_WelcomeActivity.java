package com.meridian.dateout.intro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

import static com.meridian.dateout.Constants.analytics;


public class New_WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;

    private int[] layouts;
    private Button btnSkip, btnNext;

    private PrefManager prefManager;
    private ProgressDialog pdlog;
    JSONArray array1;
    private New_ViewPagerAdapter va;
    private ArrayList<BannerModel> imageURL;
    ProgressBar progress;

    double latitude;
    double longitude;
    String flag_id;
    Button getstarted;
    ProgressBar progressBar;

    private static ViewPager mPager;
    private static int currentPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.new_activity_welcome);
        getstarted= (Button) findViewById(R.id.getstarted);
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        analytics = FirebaseAnalytics.getInstance(New_WelcomeActivity.this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        mPager = (ViewPager) findViewById(R.id.pager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        SharedPreferences myPrefs = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen2();
            finish();
        }
        new FetchImage().execute();
        flag_id = myPrefs.getString("flag_id", null);
        if (flag_id != null && !flag_id.isEmpty()) {
            Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i1);
            finish();
        }
        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i1);
                finish();
            }
        });

        imageURL = new ArrayList();

        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });


            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchHomeScreen();
                }
            });




        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int current = getItem(+1);
                    if (current < array1.length()) {
                        // move to next screen
                        viewPager.setCurrentItem(current);
                    } else {
                        launchHomeScreen();
                    }
                }catch (NullPointerException e){

                }
            }
        });


    }
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(New_WelcomeActivity.this, LoginActivity.class));
        finish();
    }
    private void launchHomeScreen2() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(New_WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    class FetchImage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            New_HttpHandler h = new New_HttpHandler();
            String jsonString = h.makeServiceCall(Constants.URL+"introbanners.php?key=100");

            System.out.println("responseeee.........."+jsonString);
            if (jsonString != null && !jsonString.isEmpty() && !jsonString.equals("null")) {
                try {
                    array1 = new JSONArray(jsonString);

                    for (int i = 0; i < array1.length(); i++) {


                        BannerModel b=new BannerModel();
                        JSONObject obj = array1.getJSONObject(i);
                        b.setId(obj.getString("id"));
                        String banner=obj.getString("banner");
                        b.setBanner(banner);
                        imageURL.add(b);



                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("nodots");
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mPager.setAdapter(new New_ViewPagerAdapter(getApplicationContext(),imageURL));
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == imageURL.size()) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };


            changeStatusBarColor();

            getstarted.setVisibility(View.VISIBLE);
            progress.setVisibility(ProgressBar.GONE);

        }

    }



}