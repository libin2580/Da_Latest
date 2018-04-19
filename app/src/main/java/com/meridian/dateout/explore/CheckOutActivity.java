package com.meridian.dateout.explore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.address.Adddetails;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.analytics;

public class CheckOutActivity extends AppCompatActivity {
    ImageView back;
   static String addphone, addemail, addfulname,address_add;
    TextView txtaddfullname, txtaddemail, txtaddphone;
    ImageView edit;
    LinearLayout lin_child, lin_people_number;
    int adult_tkt_price, child_tkt_price, adult_number, child_number, total_price, total_number, ticket_disnt_price,quantity;
    TextView textaddress,txtadt_num, txtchld_num, txtadt_pric, txtch_pric, txt_totalpric, txt_address, txt_totalnum, txt_noofpeople, txtpeople_price;
    String address;
    String booking_date="",booking_time="",titles;
    LinearLayout lin_adult, lin_people;
    TextView textchk_address,txt_addaddress;
    Button button_check_out,title;
    EditText edt_coupon_code;
    LinearLayout lin_newamt;
    Button butt_coupn_submit;
    LinearLayout coupn_pop_up;
    String currency,checkout_deal_id,tickt_discnt_price,user_id,user_id_check;
    String checkout_currency,checkout_title,checkout_userid="",checkout_dealid,checkout_sel_date,checkout_total_number,checkout_sel_time,check_out_delvry_address,checkout_booking_address,checkout_adultprice,checkout_adult_number,checkout_child_number,checkout_child_price,checkout_total_price;
    TextView ntxt_amount;
    String new_amont,status,amount,discount_amount,message;
    ImageView Home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_pay);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        analytics = FirebaseAnalytics.getInstance(CheckOutActivity.this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        address = getIntent().getStringExtra("address");
        addphone = getIntent().getStringExtra("addphone");
        addemail = getIntent().getStringExtra("addemail");
        user_id_check= getIntent().getStringExtra("user_id_check");
        titles = getIntent().getStringExtra("title");
        booking_date = getIntent().getStringExtra("booking_date");
        booking_time = getIntent().getStringExtra("booking_time");
        address_add = getIntent().getStringExtra("address_add");
        addfulname = getIntent().getStringExtra("addfullname");
        adult_tkt_price = getIntent().getIntExtra("adult_discnt_price", 0);
        child_tkt_price = getIntent().getIntExtra("child_discnt_price", 0);
        adult_number = getIntent().getIntExtra("adult_number", 0);
        child_number = getIntent().getIntExtra("child_number", 0);
        total_price = getIntent().getIntExtra("total_price", 0);
        total_number = getIntent().getIntExtra("total_number", 0);
        quantity = getIntent().getIntExtra("qty", 0);
        ntxt_amount= (TextView) findViewById(R.id.ntxt_amount);
        currency = getIntent().getStringExtra("currency");
        System.out.println("title....chec1.....:"+titles);
        System.out.println("currency...chck1......:"+currency);
        System.out.println("book_date...chck1......:"+booking_date);
        System.out.println("book_time....chck1.....:"+booking_time);
        checkout_deal_id= getIntent().getStringExtra("checkout_deal_id");
        lin_newamt= (LinearLayout) findViewById(R.id.lin_newamt);
        lin_newamt.setVisibility(View.GONE);
        ticket_disnt_price = getIntent().getIntExtra("ticket_disnt_price", 0);
        edit = (ImageView) findViewById(R.id.imageView15);
        ImageView img = (ImageView) findViewById(R.id.imageView12);
        lin_adult = (LinearLayout) findViewById(R.id.lay_adult);
        lin_people = (LinearLayout) findViewById(R.id.lay_people);
        lin_people_number = (LinearLayout) findViewById(R.id.lay_people_number);
        txtaddfullname = (TextView) findViewById(R.id.txt_chk_name);
        textchk_address= (TextView) findViewById(R.id.txt_chk_address);
        txtaddemail = (TextView) findViewById(R.id.txt_chk_email);
        txtaddphone = (TextView) findViewById(R.id.txt_chk_phone);
        txtadt_num = (TextView) findViewById(R.id.txt_noofadult);
        txt_noofpeople = (TextView) findViewById(R.id.txt_noofpeople);
        txtpeople_price = (TextView) findViewById(R.id.txtpeople_price);
        butt_coupn_submit= (Button) findViewById(R.id.butt_coupn_submit);
        coupn_pop_up= (LinearLayout)findViewById(R.id.popup1);
        txtchld_num = (TextView) findViewById(R.id.txt_noofchild);
        txtadt_pric = (TextView) findViewById(R.id.txt_adultprice);
        txtch_pric = (TextView) findViewById(R.id.txt_childprice);
        txt_totalpric = (TextView) findViewById(R.id.txt_total_pric);
        txt_totalnum = (TextView) findViewById(R.id.total_number);
        txt_addaddress = (TextView) findViewById(R.id.textaddress);
        button_check_out= (Button) findViewById(R.id.button_check_out);
        edt_coupon_code= (EditText) findViewById(R.id.edt_coupon_code);
        Home= (ImageView) findViewById(R.id.home);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  =new Intent(CheckOutActivity.this, FrameLayoutActivity.class);
                startActivity(i);
            }
        });
        checkout_title=titles;
        checkout_currency=currency;

        edt_coupon_code.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {


            }
             public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });


        butt_coupn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String coupn_value= edt_coupon_code.getText().toString();
                System.out.println("check_____   checkout_deal_id" +   checkout_deal_id+"........couponvalue"+coupn_value+"amount...."+checkout_total_price);
                String url= Constants.URL+"applycoupon.php?";

                NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
                boolean i = networkCheckingClass.ckeckinternet();
                if (i) {

                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(final String response) {
                                    // progress.setVisibility(ProgressBar.GONE);
                                    System.out.println("response"+response);




                                    if(response != null && !response.isEmpty() && !response.equals("null")) {

                                        System.out.println("responseeeee" + response);
                                        JSONObject jsonObj = null;
                                        try {
                                            jsonObj = new JSONObject(response);
                                            if(jsonObj.has("status")) {
                                                status = jsonObj.getString("status");
                                            }
                                            if(jsonObj.has("new_amount")) {
                                                new_amont = jsonObj.getString("new_amount");
                                            }

                                            if(jsonObj.has("amount")) {
                                                amount = jsonObj.getString("amount");
                                            }
                                            if(jsonObj.has("discount_amount")) {
                                                discount_amount = jsonObj.getString("discount_amount");
                                            }
                                            if(jsonObj.has("message")) {
                                             message = jsonObj.getString("message");
                                            }

                                            System.out.println("transaction json......" +status);
                                            if( status.contentEquals("failed")) {
                                            //  lin_newamt.setVisibility(View.VISIBLE);
                                                final SweetAlertDialog dialog = new SweetAlertDialog(CheckOutActivity.this,SweetAlertDialog.NORMAL_TYPE);
                                                dialog.setTitleText("COUPON")
                                                        .setContentText("Please enter a valid coupon code")

                                                        .setConfirmText("OK")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                dialog.dismiss();
                                                            }
                                                        })
                                                        .show();
                                                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                                                ntxt_amount.setText("Please enter a valid coupon code");

                                            }
                                            else {
                                                lin_newamt.setVisibility(View.VISIBLE);
                                                ntxt_amount.setText(new_amont);
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

                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError
                        {
                            Map<String, String> params = new HashMap<String, String>();
                            //meridian.net.in/demo/etsdc/response.php?fid=1&email=" + email + "&phone=" + phon + "&name=" + fulnam + "&occupation=" + occ + "&location=" + loc + "&password=" + pass
                            params.put("deal_id",checkout_deal_id);
                            params.put("coupon",coupn_value);
                            params.put("amount",checkout_total_price);

                            return params;
                        }

                    };

                    RequestQueue requestQueue2= Volley.newRequestQueue(getApplicationContext());
                    int socketTimeout = 30000;//30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest2.setRetryPolicy(policy);
                    requestQueue2.add(stringRequest2);
                }
                else
                {

                    final SweetAlertDialog dialog = new SweetAlertDialog(CheckOutActivity.this,SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Oops Your Connection Seems Off..")

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
        });
        button_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),StripeCheck.class);
                i.putExtra("check_address",address_add);
                String s= String.valueOf(total_number);
                if(new_amont!=null) {
                    i.putExtra("check_price",new_amont);
                }
                else {
                    i.putExtra("check_price",checkout_total_price);
                }
                i.putExtra("check_title",titles);
                i.putExtra("booking_date",booking_date);
                i.putExtra("booking_time",booking_time);
                i.putExtra("check_currency",currency);
                i.putExtra("total_number",String.valueOf(total_number));
                i.putExtra("user_id",checkout_userid);
                i.putExtra("deal_id",checkout_deal_id);
                i.putExtra("adult_number",checkout_adult_number);
                i.putExtra("child_number",checkout_child_number);
                i.putExtra("qty",quantity);
                System.out.println("title....check2.....:"+titles);
                System.out.println("qtyyy..22:"+quantity);
                System.out.println("currency...chck2......:"+currency);
                System.out.println("book_date....chck2.....:"+booking_date);
                System.out.println("book_time.....chck2....:"+booking_time);

                startActivity(i);
                finish();
            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adddetails.toolbar_CRCNAM.setText("EDIT DETAILS");
                onBackPressed();
            }
        });
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);
        back = (ImageView) findViewById(R.id.img_crcdtlnam);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });



        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String user_id = preferences.getString("user_id", null);

        if (user_id != null) {
            checkout_userid = user_id;
            System.out.println("userid" + checkout_userid);
        }
        SharedPreferences preferences1 =getSharedPreferences("myfbid", MODE_PRIVATE);
        String profile_id = preferences1.getString("user_idfb", null);
        if (profile_id != null) {
            checkout_userid = profile_id;
            System.out.println("userid" + checkout_userid);
        }
        SharedPreferences preferences2 = getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences2.getString("user_id", null);
        if (profileid_gmail != null) {
            checkout_userid = profileid_gmail;
            System.out.println("userid" + checkout_userid);
        }
        SharedPreferences preferenceswsh = getApplicationContext().getSharedPreferences("wishk", MODE_PRIVATE);
        String images = preferenceswsh.getString("image", null);
        Glide
                .with(getApplicationContext())
                .load(images)
                .centerCrop()

                .crossFade()
                .into(img);

        int tot = adult_number + child_number;
        txt_totalnum.setText("" + tot);
        textchk_address.setText(titles);
        lin_child = (LinearLayout) findViewById(R.id.lay_child);
        txtadt_num.setText("" + adult_number);
        txtadt_pric.setText(checkout_currency+" "+ adult_tkt_price);
        //  booking_date=Booking_DetailsActivity.booking_date;
        if(quantity!=0)
        {
            lin_people.setVisibility(View.VISIBLE);
            lin_people_number.setVisibility(View.GONE);
        }
        else {
           lin_people.setVisibility(View.GONE);
           lin_people_number.setVisibility(View.GONE);
        }

        if (child_number != 0 && child_tkt_price != 0) {

            lin_child.setVisibility(View.VISIBLE);
            txtch_pric.setText(checkout_currency+" "+ child_tkt_price);
            txtchld_num.setText("" + child_number);
//            lin_people.setVisibility(View.GONE);
        }
        else
        {

            lin_child.setVisibility(View.GONE);
            txt_noofpeople.setText("" + total_number);
            txtpeople_price.setText(checkout_currency+" "+ ticket_disnt_price);
        }
        if (adult_number != 0 && adult_tkt_price != 0)
        {
            lin_adult.setVisibility(View.VISIBLE);

            txtadt_pric.setText(checkout_currency+" " + adult_tkt_price);
            txtadt_num.setText("" + adult_number);

        }
        else
        {
            lin_adult.setVisibility(View.GONE);

            txt_noofpeople.setText("" + total_number);

            txtpeople_price.setText(checkout_currency+" "+ ticket_disnt_price);
            checkout_total_price= String.valueOf(ticket_disnt_price);
            checkout_total_number= String.valueOf(total_number);
        }
        txt_totalpric.setText(checkout_currency+" " + total_price);
        txtaddfullname.setText(addfulname);
        txtaddemail.setText(addemail);
        txtaddphone.setText(addphone);
        txt_addaddress.setText(address_add);
        checkout_total_price= String.valueOf(total_price);

        ////////////////////checkout detailsss/////////////////////////////
        checkout_booking_address=address_add;

        checkout_dealid=checkout_deal_id;
        checkout_adult_number= String.valueOf(adult_number);
        checkout_child_number= String.valueOf(child_number);
        checkout_adultprice= String.valueOf(adult_tkt_price);
        checkout_child_price= String.valueOf(child_tkt_price);
        checkout_sel_date=booking_date;
        checkout_total_price= String.valueOf(total_price);
        checkout_total_number= String.valueOf(tot);

        System.out.println("checkout_userid............."+checkout_userid);
        System.out.println("checkout_booking_address............."+checkout_booking_address);
        System.out.println("checkout_booking_address............."+checkout_booking_address);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
