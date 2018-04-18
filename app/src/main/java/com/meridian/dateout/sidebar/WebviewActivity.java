package com.meridian.dateout.sidebar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.meridian.dateout.R;

import static com.meridian.dateout.Constants.analytics;

public class WebviewActivity extends AppCompatActivity {
String url;
    WebView wv;
    ProgressDialog pd;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv= (WebView) findViewById(R.id.webvw);
        progress = (ProgressBar)findViewById(R.id.progress_bar);
        Bundle extras = getIntent().getExtras();
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        if (extras != null) {
            url=extras.getString("url");
            progress.setVisibility(ProgressBar.VISIBLE);

            wv.setWebViewClient(new WebViewClient());
            wv.loadUrl(url);
            wv.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    progress.setVisibility(ProgressBar.GONE);
                }


                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {


                }
            });
        }



    }
}
