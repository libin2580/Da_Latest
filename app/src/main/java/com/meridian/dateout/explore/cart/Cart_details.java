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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.address.Adddetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    public static ProgressBar progress_bar_explore;
    int valuu=0;
    public static int totalprize_for_order=0;

    EditText promocode;
    String coupon_code;
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
            }};
        }
        else {
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("device_token",android_id));
            }};
        }


        Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u=new Intent(Cart_details.this, Adddetails.class);

                startActivity(u);
            }
        });
        view_cart();

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
