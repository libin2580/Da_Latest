package com.meridian.dateout.sidebar;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.login.TermsOfUse;
import com.meridian.dateout.model.Sidebar_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;

public class AboutActivity  extends Activity {
    ArrayList<Sidebar_Model> sidebarlist;
    String id,title,description1;
    TextView description;
   LinearLayout back;
   ImageView fb,insta;
    Toolbar toolbar;
    ProgressBar progressBar;
   /* public static AboutActivity newInstance() {
     AboutActivity fragment = new AboutActivity();

        return fragment;
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar= (ProgressBar)findViewById(R.id.progress_bar);
        toolbar = (Toolbar)findViewById(R.id.toolbar_tops);
       toolbar.setVisibility(View.VISIBLE);
        back = (LinearLayout)findViewById(R.id.img_crcdtlnam);
        analytics = FirebaseAnalytics.getInstance(AboutActivity.this);

        analytics.setCurrentScreen(AboutActivity.this, AboutActivity.this.getLocalClassName(), null /* class override */);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AboutActivity.this, FrameLayoutActivity.class);
                startActivity(i);
                finish();

            }
        });
        fb= (ImageView) findViewById(R.id.fb);

        description= (TextView)findViewById(R.id.description);
        about_us();
        insta= (ImageView) findViewById(R.id.imageView6);

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dateout_official/?hl=en"));
                startActivity(browserIntent);
                /*Intent i=new Intent(AboutActivity.this, WebviewActivity.class);
                i.putExtra("url","https://www.instagram.com/dateout_official/?hl=en");

                startActivity(i);*/
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(AboutActivity.this, WebviewActivity.class);
                i.putExtra("url","https://www.facebook.com/Date-Out-552484931628419/");

                startActivity(i);

            }
        });
    }


    private  void about_us()
    {
        progressBar.setVisibility(View.VISIBLE);
        NetworkCheckingClass networkCheckingClass=new NetworkCheckingClass(AboutActivity.this);
        boolean i= networkCheckingClass.ckeckinternet();
        if(i==true) {


            String Schedule_url = Constants.URL+"pages.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);

                            if (response != null) {
                                try {
                                    JSONArray jsonarray = new JSONArray(response);

                                    sidebarlist = new ArrayList<>();


                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        Sidebar_Model sidebar_model = new Sidebar_Model();
                                        JSONObject jsonobject = jsonarray.getJSONObject(0);

                                            id = jsonobject.getString("id");
                                            title = jsonobject.getString("title");
                                            description1 = jsonobject.getString("description");
                                            System.out.println("description" + description1);

                                            sidebar_model.setId(id);
                                            sidebar_model.setTitle(title);
                                            sidebar_model.setDescription(description1);
                                            sidebarlist.add(sidebar_model);
                                            description.setText(description1);
                                            progressBar.setVisibility(View.GONE);
                                        }




                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {

                                Toast.makeText(AboutActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(1));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(AboutActivity.this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        }
        else {


            final SweetAlertDialog dialog = new SweetAlertDialog(AboutActivity.this,SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("")
                    .setContentText("Oops Your Connection Seems Off..s")

                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


        }

    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(AboutActivity.this, FrameLayoutActivity.class);
        startActivity(i);
        finish();
    }


}
