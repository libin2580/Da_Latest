package com.meridian.dateout.sidebar;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.model.Sidebar_Model;

import java.util.ArrayList;

import static com.meridian.dateout.Constants.analytics;

public class RewardsActivity extends AppCompatActivity {
    ArrayList<Sidebar_Model> sidebarlist;
    String id,title,description1;
WebView  description;
LinearLayout back;
    ProgressBar progressBar;
   ImageView fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reward_sidebar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = (LinearLayout) findViewById(R.id.img_crcdtlnam);
        fb= (ImageView) findViewById(R.id.fb);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        description= (WebView) findViewById(R.id.description);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        analytics.setCurrentScreen(RewardsActivity.this, RewardsActivity.this.getLocalClassName(), null /* class override */);

        //about_us();
        description.getSettings().setLoadsImagesAutomatically(true);
        description.getSettings().setJavaScriptEnabled(true);
        description.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        description.setVisibility(View.GONE);
        String url_is= Constants.URL.substring(0, Constants.URL.length() - 9);
        url_is=url_is+"rewardsapps";
        description.loadUrl(url_is);


      //  System.out.println("url_is : "+url_is);



        WebViewClient client = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("MYAPP", "Page loaded");


            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                progressBar.setVisibility(ProgressBar.VISIBLE);
            }
        };
        description.setWebViewClient(client);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                description.setVisibility(View.VISIBLE);
                progressBar.setVisibility(ProgressBar.GONE);
            }
        }, 2000);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
