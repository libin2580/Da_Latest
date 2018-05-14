package com.meridian.dateout.explore.promo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.explore.StripeCheck;
import com.meridian.dateout.explore.address.AddreesAdapter;
import com.meridian.dateout.explore.address.AddressModel;
import com.meridian.dateout.explore.address.EdittestActivity;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.login.LoginActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.login.TermsOfUse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.Constants.analytics;
import static com.meridian.dateout.explore.cart.Cart_details.Total_txt;
import static com.meridian.dateout.explore.cart.Cart_details.promocode;

public class Promo extends AppCompatActivity {
    Button add_details;
    EditText fullname, phone,edit_address;
    String user_id,userid,profile_id;
    ImageView back;
    ProgressBar progress;
    String address,booking_date,booking_time,title;
    public static TextView toolbar_CRCNAM;
    String currency,checkout_deal_id,getuser_id,get_email,get_address,get_phone,get_name;
    int adult_tkt_price, child_tkt_price, adult_number,quantity, child_number, total_price, total_number, ticket_disnt_price;
    String regexStr ="^[+]?[0-9]{10,13}$";
    String res_name,res_email,res_phone,res_image,res_location;
    String str_userid,str_fullname,str_username,str_email,str_photo,str_phone,str_location;
    RecyclerView recyclerViepromo;
    TextView add_address_txt;
    PromoAdapter promoAdapter;
    ArrayList<AddressModel> addresslist;
    AddressModel addressModel;
    ArrayList<PromoModel> promolist;
    PromoModel promoModel;
    LinearLayout add_address;
    View custompopup_view;
    PopupWindow address_popupwindow;
    LinearLayout coordinatorLayout,apply;
    EditText addr_name,addr_phone,addr_city,addr_area,addrs_flat,addr_state,addr_pin,email;
    CheckBox addr_work,addr_home;
    LinearLayout save_data,plus_btn,_close,continue_shopping;
    public  static LinearLayout place_order;
    JSONArray array;
    String adname,adphone,adcity,adarea,adflatads,adstate,adpin,ad_type;
    private String android_id;
    List<Pair<String, String>> params;
    public static String promo_id1;
    String message;
    TextView condtiion_txt;
    CheckBox terms;
    public static String terms_txt;
    String coupon_id, coupon_code, coupon_name, cpn_valid_from, cpn_valid_till, deal_id, deal_name, offer;
public static String coupn_value;
EditText promocode_nw;
    String     promocode_nw_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promocode);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);
        analytics = FirebaseAnalytics.getInstance(Promo.this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);


        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        str_fullname = preferences.getString("fullname", null);
        str_email = preferences.getString("email", null);

        SharedPreferences preferencesfb = getSharedPreferences("myfb", MODE_PRIVATE);
        String str_emails = preferencesfb.getString("emails", null);
        String str_names = preferencesfb.getString("names", null);
        System.out.println("emails" + str_emails);
        System.out.println("names" + str_names);

        SharedPreferences preferences1 = getSharedPreferences("value_google_user", MODE_PRIVATE);

        String str_fullname1 = preferences1.getString("name", null);
        String str_email1 = preferences1.getString("email", null);
        String p= "I AGREE";
        System.out.println("textttt"+p);
        promocode_nw= (EditText) findViewById(R.id.promocode_nw);
        progress= (ProgressBar) findViewById(R.id.progress);
        continue_shopping= (LinearLayout) findViewById(R.id.continue_shopping);
        SharedPreferences preferencesuser_id = getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id = preferencesuser_id.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid" + userid);
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("user_id", user_id));
                add(new Pair<String, String>("guest_device_token", "0"));

            }};
        }
        else {
            android_id = Settings.Secure.getString(Promo.this.getContentResolver(),Settings.Secure.ANDROID_ID);

            System.out.println("userid" + android_id);
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("user_id", "0"));
                add(new Pair<String, String>("guest_device_token", android_id));

            }};

        }
        SharedPreferences preferences_fb_id = getSharedPreferences("myfbid", MODE_PRIVATE);
        profile_id = preferences_fb_id.getString("user_idfb", null);
        if (profile_id != null) {
            userid = profile_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences_gmail_id =getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences_gmail_id.getString("user_id", null);
        if (profileid_gmail != null) {
            userid = profileid_gmail;
            System.out.println("userid" + userid);
        }
        System.out.println("useridmypreflogin" + userid);

        SharedPreferences preferences_add = getApplicationContext().getSharedPreferences("adddetails", MODE_PRIVATE);
        getuser_id=  preferences_add.getString("userid",null);
        if(getuser_id!=null&&getuser_id==userid)
        {
            get_email=  preferences_add.getString("email",null);
            get_address= preferences_add.getString("address",null);
            get_phone= preferences_add.getString("phone",null);
            get_name= preferences_add.getString("name",null);
            email.setText(get_email);
            fullname.setText(get_name);
            phone.setText(get_phone);
            edit_address.setText(get_address);

        }

        recyclerViepromo = (RecyclerView) findViewById(R.id.recycler_vertical1);
        add_address=(LinearLayout) findViewById(R.id.add_address);
        _close = (LinearLayout) findViewById(R.id.img_crcdtlnam);
        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Promo.this, Cart_details.class);
                startActivity(i);
                finish();

            }
        });
        continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Promo.this, Cart_details.class);
                startActivity(i);
                finish();

            }
        });
        view_coupon();





        apply=(LinearLayout) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("address_id" + promo_id1);
                promocode_nw_str=promocode_nw.getText().toString();
                System.out.println("<<<<<<<<<<<<<<<<coupn_value>>>>>>>>>>>>>>>>>>"+coupn_value);

                if(promo_id1==null&&promocode_nw_str==null){
                    System.out.println("<<<<<<<<<<<<<<<<00000000000000000>>>>>>>>>>>>>>>>>>"+"000000000000000000000");

                    final SweetAlertDialog dialog = new SweetAlertDialog(Promo.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("!")
                            .setContentText("Please select a coupon")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                    return;
                }else if(promocode_nw_str.equalsIgnoreCase("")) {
                    System.out.println("<<<<<<<<<<<<<<<<111111111111111111>>>>>>>>>>>>>>>>>>"+promocode_nw_str);

                    System.out.println("<<<<<<<<<<<<<<<<111111111111111111>>>>>>>>>>>>>>>>>>"+"1111111111111111");


new SendPostRequest().execute();

                }
                else if(promo_id1!=null){
                    System.out.println("<<<<<<<<<<<<<<<<222222222222222222222>>>>>>>>>>>>>>>>>>"+"222222222222222222222");

                    new SendPostRequest().execute();

                }
                else if(promocode_nw_str!=null){
                    System.out.println("<<<<<<<<<<<<<<<<111111111111111111>>>>>>>>>>>>>>>>>>"+promocode_nw_str);
                    new SendPostRequest().execute();

                }
            }
        });






    }

    private void view_coupon() {
        progress.setVisibility(View.VISIBLE);
        Fuel.post(URL1+"coupon-list-public.php  ",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {

                progress.setVisibility(View.GONE);
                try {
                    promolist = new ArrayList<>();

                    System.out.println("<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>"+s);
                    JSONObject jobj=new JSONObject(s);
                    String status=jobj.getString("status");
                    if(status.equalsIgnoreCase("true")){
                        final String data = jobj.getString("data");
                        System.out.println("<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>"+data);

                        array = new JSONArray(data);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            promoModel = new PromoModel();


                            coupon_id = jsonobject.getString("coupon_id");
                            coupon_code = jsonobject.getString("coupon_code");
                            coupon_name = jsonobject.getString("coupon_name");
                            cpn_valid_from = jsonobject.getString("cpn_valid_from");
                            cpn_valid_till = jsonobject.getString("cpn_valid_till");
                            deal_id = jsonobject.getString("deal_id");
                            deal_name = jsonobject.getString("deal_name");
                            offer = jsonobject.getString("offer");


                            promoModel.setCoupon_id(coupon_id);
                            promoModel.setCoupon_code(coupon_code);
                            promoModel.setCoupon_name(coupon_name);
                            promoModel.setCpn_valid_from(cpn_valid_from);
                            promoModel.setCpn_valid_till(cpn_valid_till);
                            promoModel.setDeal_id(deal_id);
                            promoModel.setDeal_name(deal_name);
                            promoModel.setOffer(offer);

                            promolist.add(promoModel);



                        }
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        recyclerViepromo.setLayoutManager(llm);
                        promoAdapter = new PromoAdapter(getApplicationContext(), promolist);

                        recyclerViepromo.scheduleLayoutAnimation();
                        recyclerViepromo.setAdapter(promoAdapter);
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

            }
        });

    }




   public class SendPostRequest extends AsyncTask<String, Void, String> {

        public void onPreExecute(){

        }

        public String doInBackground(String... arg0) {
            //String js=filename.replaceAll(" ","");
            try {

                URL url = new URL(URL1+"applycoupon.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();

                if (user_id != null) {
                    userid = user_id;
                    System.out.println("userid" + userid);
                    postDataParams.put("user_id",user_id);
                    postDataParams.put("device_id","0");
                    if (promocode_nw_str.equalsIgnoreCase("")){
                        postDataParams.put("coupon",coupn_value);

                    }else {
                        postDataParams.put("coupon",promocode_nw_str);

                    }
                   /* params = new ArrayList<Pair<String, String>>() {{
                        add(new Pair<String, String>("user_id", user_id));
                        add(new Pair<String, String>("guest_device_token", "0"));

                    }};*/

                }
                else {
                    android_id = Settings.Secure.getString(Promo.this.getContentResolver(),Settings.Secure.ANDROID_ID);

                    System.out.println("userid" + android_id);
                    postDataParams.put("user_id","0");
                    postDataParams.put("device_id",android_id);
                    if (promocode_nw_str.equalsIgnoreCase("")){
                        postDataParams.put("coupon",coupn_value);

                    }else {
                        postDataParams.put("coupon",promocode_nw_str);

                    }                   /* params = new ArrayList<Pair<String, String>>() {{
                        add(new Pair<String, String>("user_id", "0"));
                        add(new Pair<String, String>("guest_device_token", android_id));

                    }};*/

                }




                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000  /*milliseconds*/ );
                conn.setConnectTimeout(15000  /*milliseconds*/ );
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        public void onPostExecute(String result) {
            System.out.println("<<<<<<<<<<<<<<<<result>>>>>>>>>>>>>>>>>>"+result);
            String amount,discount_amount,new_amount;

           /* Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();*/
            try {
                JSONObject jobj1=new JSONObject(result);
                String status=jobj1.getString("status");
                if(status.equalsIgnoreCase("failed")){
                    String message=jobj1.getString("message");
                    final SweetAlertDialog dialog = new SweetAlertDialog(Promo.this,SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText(message)
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismissWithAnimation();
                                    //finish();

                                }
                            })
                            .show();
                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


            }
            else if(status.equalsIgnoreCase("success")) {
                    amount=jobj1.getString("amount");
                    discount_amount=jobj1.getString("discount_amount");
                    new_amount=jobj1.getString("new_amount");
                    Total_txt.setText(new_amount);
                    if(promocode_nw_str.equalsIgnoreCase("")){

                        promocode.setText(coupn_value);
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("coupon_code", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
//
                        editor.putString("coupon_code", coupn_value);
                        editor.commit();
                        Intent u = new Intent(Promo.this, Cart_details.class);
                        u.putExtra("new_amount",new_amount);
                        startActivity(u);
                        finish();
                    }else {



                        promocode.setText(promocode_nw_str);
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("coupon_code", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
//
                        editor.putString("coupon_code", promocode_nw_str);
                        editor.commit();
                        Intent u = new Intent(Promo.this, Cart_details.class);
                        u.putExtra("new_amount",new_amount);
                        startActivity(u);
                        finish();
                    }

              ;

                }
        } catch(JSONException e){
           e.printStackTrace();
       }

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
