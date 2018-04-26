package com.meridian.dateout.explore.cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.CheckOutActivity;
import com.meridian.dateout.explore.address.Adddetails;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.Constants.analytics;

/**
 * Created by SIDDEEQ DESIGNER on 3/17/2018.
 */

public class Cart_details extends AppCompatActivity {

    private CartHistoryModel ohm;
    private ArrayList<CartHistoryModel>OrderHistoryArraylist=new ArrayList<CartHistoryModel>();
    RecyclerView recyclerView;
    private CartHistoryAdapter ohad;
    String userid,android_id;
    public static TextView Total_txt;
    LinearLayout Checkout,_close;
    List<Pair<String, String>> params;
    List<Pair<String, String>> params1;

    public static ProgressBar progress_bar_explore;
    int valuu=0;
    public static int totalprize_for_order=0;

    EditText promocode;
    String coupon_code;
    Button coupn_click;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progress_bar_explore = (ProgressBar)findViewById(R.id.progress_bar_explore);
        Checkout = (LinearLayout)findViewById(R.id.checkout);
        analytics = FirebaseAnalytics.getInstance(Cart_details.this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        promocode=findViewById(R.id.promocode);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_categorydeal);
        coupn_click = (Button)findViewById(R.id.coupn_click);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.scheduleLayoutAnimation();
        Total_txt= (TextView) findViewById(R.id.txt_explorename);
        _close = (LinearLayout) findViewById(R.id.img_crcdtlnam);
        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        coupon_code=promocode.getText().toString();
        SharedPreferences preferences_coupon_code =getApplicationContext().getSharedPreferences("coupon_code", MODE_PRIVATE);
        SharedPreferences.Editor editor1 =preferences_coupon_code.edit();
        editor1.putString("coupon_code",  coupon_code);
        editor1.apply();
        android_id = Settings.Secure.getString(Cart_details.this.getContentResolver(),Settings.Secure.ANDROID_ID);
        try {

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String  user_id = preferences.getString("user_id", null);

            if (user_id != null) {
                userid = user_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("myfbid", MODE_PRIVATE);
            String  profile_id = preferences1.getString("user_idfb", null);
            if (profile_id != null) {
                userid = profile_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("value_gmail", MODE_PRIVATE);
            String profileid_gmail = preferences2.getString("user_id", null);
            if (profileid_gmail != null) {
                userid = profileid_gmail;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences_user_id =getApplicationContext().getSharedPreferences("user_idnew", MODE_PRIVATE);
            SharedPreferences.Editor editor =preferences_user_id.edit();
            editor.putString("new_userid",  userid);
            editor.apply();

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        if(userid!=null)
        {
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("user_id",userid));
                add(new Pair<String, String>("device_token","0"));
            }};
        }
        else {
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("device_token",android_id));
                add(new Pair<String, String>("user_id","0"));
            }};
        }

coupn_click.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        coupon_code=promocode.getText().toString();
        {

          //  final String coupn_value= edt_coupon_code.getText().toString();
          //  System.out.println("check_____   checkout_deal_id" +   checkout_deal_id+"........couponvalue"+coupn_value+"amount...."+checkout_total_price);
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
                                        System.out.println("s**********" + response);
                                        String status = jsonObj.getString("status");

                                        if( status.contentEquals("failed")) {
                                            //  lin_newamt.setVisibility(View.VISIBLE);
                                            final SweetAlertDialog dialog = new SweetAlertDialog(Cart_details.this,SweetAlertDialog.NORMAL_TYPE);
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
                                         //   ntxt_amount.setText("Please enter a valid coupon code");

                                        }
                                        else {
                                            String amount = jsonObj.getString("amount");
                                            String discount_amount = jsonObj.getString("discount_amount");
                                            String new_amount = jsonObj.getString("new_amount");
                                            //lin_newamt.setVisibility(View.VISIBLE);
                                            Total_txt.setText("$" +new_amount);
                                            promocode.setText("");
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
                        if(userid!=null)
                        {
                            android_id="0";

                        }else {
                           userid="0" ;
                        }
                        Map<String, String> params = new HashMap<String, String>();
                        //meridian.net.in/demo/etsdc/response.php?fid=1&email=" + email + "&phone=" + phon + "&name=" + fulnam + "&occupation=" + occ + "&location=" + loc + "&password=" + pass
                        params.put("user_id",userid);
                        params.put("coupon",coupon_code);
                        params.put("device_id",android_id);
                        System.out.println("params1111111111" + params);
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

                final SweetAlertDialog dialog = new SweetAlertDialog(Cart_details.this,SweetAlertDialog.NORMAL_TYPE);
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
       /* if(userid!=null)
        {
            params1 = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("user_id",userid));
                add(new Pair<String, String>("device_id ","0"));
                add(new Pair<String, String>("coupon",coupon_code));


            }};
        }
        else {
            params1 = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("device_token",android_id));
                add(new Pair<String, String>("user_id","0"));
                add(new Pair<String, String>("coupon",coupon_code));


            }};
        }

        coupon_check();
        System.out.println("<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>"+params1);*/
    }
});
        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u=new Intent(Cart_details.this, Adddetails.class);

                startActivity(u);
            }
        });
        view_cart();

    }

    private void coupon_check() {
        {


            progress_bar_explore.setVisibility(View.VISIBLE);
            Fuel.post(URL1 +"applycoupon.php?", params1).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                    progress_bar_explore.setVisibility(View.GONE);
                    System.out.println("s**********11111111111111" + response.toString());

                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        System.out.println("s**********" + s);
                        String status = jsonObj.getString("status");
                        String amount = jsonObj.getString("amount");
                        String discount_amount = jsonObj.getString("discount_amount");
                        String new_amount = jsonObj.getString("new_amount");



                        System.out.println("s**********" + status);
                        System.out.println("s**********" + amount);
                        System.out.println("s**********" + discount_amount);

                        System.out.println("s**********" + new_amount);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

                }
            });







        }
    }

    private void view_cart() {
        OrderHistoryArraylist.clear();

        progress_bar_explore.setVisibility(View.VISIBLE);
        Fuel.post(URL1 + "view_cart.php", params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                progress_bar_explore.setVisibility(View.GONE);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    System.out.println("s**********" + s);
                    String status = jsonObj.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                       if(jsonObj.has("data")){
                           JSONArray dataArray = jsonObj.getJSONArray("data");
                           for (int i = 0; i < dataArray.length(); i++) {
                               JSONObject obj = dataArray.getJSONObject(i);
                               ohm = new CartHistoryModel();
                               ohm.setTitle(obj.getString("title"));
                               ohm.setImage(obj.getString("image"));
                               ohm.setAmount(obj.getString("amount"));
                               ohm.setBooking_date(obj.getString("date_added"));
                               ohm.setQuantity(obj.getString("quantity"));
                               ohm.setcart_item_id(obj.getString("cart_item_id"));
                               ohm.setDeal_id(obj.getString("deal_id"));
                               OrderHistoryArraylist.add(ohm);

                               Log.e("##########", String.valueOf(OrderHistoryArraylist));

                           }
                           if (OrderHistoryArraylist.size() > 0) {
                               totalprize_for_order -= totalprize_for_order;
                               ohad = new CartHistoryAdapter(getApplicationContext(), OrderHistoryArraylist);
                               recyclerView.setAdapter(ohad);
                               for (int i = 0; i < OrderHistoryArraylist.size(); i++) {
                                   valuu = Integer.parseInt(OrderHistoryArraylist.get(i).getamount());
                                   totalprize_for_order += valuu;
                                   Total_txt.setText("$" + String.valueOf(totalprize_for_order));

                               }
                           }
                       }
                       else {
                           final SweetAlertDialog dialog = new SweetAlertDialog(Cart_details.this,SweetAlertDialog.WARNING_TYPE);
                           dialog.setTitleText("Cart is empty!")
                                   .setConfirmText("OK")
                                   .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sDialog) {
                                           dialog.dismissWithAnimation();
                                           finish();

                                       }
                                   })
                                   .show();
                           dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                       }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

            }
        });







            }



}
