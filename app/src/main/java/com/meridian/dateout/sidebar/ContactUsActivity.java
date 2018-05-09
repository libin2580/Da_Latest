package com.meridian.dateout.sidebar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.meridian.dateout.model.Contact_us_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;

public class ContactUsActivity extends Activity {
    ImageView back, phone1, mail1;
    TextView email,phone,address;
    ArrayList<Contact_us_Model> contactlist;
    String id1,email1,phone2,address1,name;
    Toolbar toolbar;
    LinearLayout lin_mail,lin_phone;
    ImageView cntctt_fb,cntctt_google,insta;
    double latitude=1.3525;
    double longitude=103.82226;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_us);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        analytics = FirebaseAnalytics.getInstance(ContactUsActivity.this);

        analytics.setCurrentScreen(ContactUsActivity.this, ContactUsActivity.this.getLocalClassName(), null /* class override */);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        back = (ImageView)  findViewById(R.id.img_crcdtlnam);
        lin_mail=findViewById(R.id.lin_mail);
        lin_phone=findViewById(R.id.lin_phone);
        cntctt_fb=findViewById(R.id.cntctt_fb);
        cntctt_google=findViewById(R.id.cntctt_google);
        insta= (ImageView) findViewById(R.id.cntctt_insta);

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dateout_official/?hl=en"));
                startActivity(browserIntent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ContactUsActivity.this, FrameLayoutActivity.class);
                startActivity(i);
                finish();
            }
        });
        toolbar = (Toolbar)findViewById(R.id.toolbar_tops);
        toolbar.setVisibility(View.VISIBLE);

        phone1 = (ImageView)findViewById(R.id.phone);
        mail1 = (ImageView) findViewById(R.id.mail);
        back = (ImageView) findViewById(R.id.img_crcdtlnam);
        email= (TextView)findViewById(R.id.txt_email);
        phone= (TextView) findViewById(R.id.txt_phone);
        address= (TextView) findViewById(R.id.txt_address);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri mapUri = Uri.parse("geo:0,0?q="+latitude+","+longitude+"(Singapore)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

                try
                {
                    startActivity(mapIntent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Uri mapUri1 = Uri.parse("geo:0,0?q="+latitude+","+longitude+"(Singapore)");
                        Intent mapIntent1 = new Intent(Intent.ACTION_VIEW, mapUri1);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent1);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(ContactUsActivity.this, "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        contact();
        cntctt_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uriUrl = Uri.parse("https://www.facebook.com/Date-Out-552484931628419");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }

        });
        cntctt_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }

        });
        lin_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone2));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        lin_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email1, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Enquire");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                //  emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses);
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });




    }






    private  void contact()
    {
        NetworkCheckingClass networkCheckingClass=new NetworkCheckingClass(ContactUsActivity.this);
        boolean i= networkCheckingClass.ckeckinternet();
        if(i==true) {


            String Schedule_url = Constants.URL+"contact.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);

                            if (response != null) {
                                try {
                                    JSONArray jsonarray = new JSONArray(response);

                                    contactlist = new ArrayList<>();


                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        Contact_us_Model contact_model = new Contact_us_Model();


                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                     id1 = jsonobject.getString("id");
                                      name = jsonobject.getString("name");
                                      email1 = jsonobject.getString("email");
                                         phone2 = jsonobject.getString("phone");
                                      address1 = jsonobject.getString("address");
                                     System.out.println("name"+name+"..."+"email"+email1+"..."+"address"+address1);
                                        contact_model.setId(id1);
                                        contact_model.setName(name);
                                        contact_model.setEmail(email1);
                                        contact_model.setPhone(phone2);
                                        contact_model.setAddress(address1);

                                        contactlist.add(contact_model);

                                        email.setText(email1);
                                        phone.setText(phone2);
                                        address.setText(address1);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(ContactUsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(ContactUsActivity.this);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        }
        else {


            final SweetAlertDialog dialog = new SweetAlertDialog(ContactUsActivity.this,SweetAlertDialog.NORMAL_TYPE);
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
        Intent i=new Intent(ContactUsActivity.this, FrameLayoutActivity.class);
        startActivity(i);
        finish();
    }

}
