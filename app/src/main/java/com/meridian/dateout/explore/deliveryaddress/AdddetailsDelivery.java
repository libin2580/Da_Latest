package com.meridian.dateout.explore.deliveryaddress;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.explore.category_booking_detailspage.Booking_DetailsActivity;
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

public class AdddetailsDelivery extends AppCompatActivity {
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
    RecyclerView recyclerVieaddress;
    AddreesAdapter addreesAdapter;
    ArrayList<AddressModel> addresslist;
    AddressModel addressModel;
    LinearLayout add_address;
    View custompopup_view;
    PopupWindow address_popupwindow;
    LinearLayout coordinatorLayout;
    EditText addr_name,addr_phone,addr_city,addr_area,addrs_flat,addr_state,addr_pin,email;
    CheckBox addr_work,addr_home;
    LinearLayout save_data,plus_btn,_close;
    public  static LinearLayout place_order;
    JSONArray array;
    String adname,adphone,adcity,adarea,adflatads,adstate,adpin,ad_type;
    private String android_id;
    List<Pair<String, String>> params;
    List<Pair<String, String>> params1;
    public static String address_id;
    String message,device_token,bookingdate,bookingtime,ad_number,ch_number,people,str_comnt,amount,somejson;
    TextView condtiion_txt,txt_cart;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_address_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);
        analytics = FirebaseAnalytics.getInstance(AdddetailsDelivery.this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);
        progress=(ProgressBar)findViewById(R.id.progress);
        txt_cart=(TextView)findViewById(R.id.txt_cart);
        txt_cart.setText("Add To Cart");
        address = getIntent().getStringExtra("address");
        adult_tkt_price = getIntent().getIntExtra("adult_discnt_price", 0);
        booking_date = getIntent().getStringExtra("booking_date");
        booking_time = getIntent().getStringExtra("booking_time");
        currency = getIntent().getStringExtra("currency");
        checkout_deal_id= getIntent().getStringExtra("checkout_deal_id");
        ticket_disnt_price = getIntent().getIntExtra("ticket_disnt_price",0);
        title = getIntent().getStringExtra("title");
        child_tkt_price = getIntent().getIntExtra("child_discnt_price", 0);
        adult_number = getIntent().getIntExtra("adult_number", 0);
        child_number = getIntent().getIntExtra("child_number", 0);
        quantity= getIntent().getIntExtra("qty", 0);
        total_price = getIntent().getIntExtra("total_price", 0);
        total_number = getIntent().getIntExtra("total_number", 0);
        String pop = getIntent().getStringExtra("poupup");

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        str_fullname = preferences.getString("fullname", null);
        str_email = preferences.getString("email", null);

        SharedPreferences preferencesfb = getSharedPreferences("myfb", MODE_PRIVATE);
        String str_emails = preferencesfb.getString("emails", null);
        String str_names = preferencesfb.getString("names", null);
        System.out.println("emails" + str_emails);
        System.out.println("names" + str_names);

        SharedPreferences preferences1 = getSharedPreferences("value_google_user", MODE_PRIVATE);
        checkout_deal_id=getIntent().getStringExtra("deal_id");
        bookingdate= getIntent().getStringExtra("cust_selected_date" );
        bookingtime=getIntent().getStringExtra("cust_selected_time");
        ad_number= getIntent().getStringExtra("adult_number");
        ch_number= getIntent().getStringExtra("child_number");
        people=getIntent().getStringExtra("quantity");
        str_comnt=getIntent().getStringExtra("comment" );
        amount= getIntent().getStringExtra("amount");
        somejson= getIntent().getStringExtra("deal_optionsJSON ");
        String str_fullname1 = preferences1.getString("name", null);
        String str_email1 = preferences1.getString("email", null);
        String p= "By clicking on place holder you agree to our";
        System.out.println("textttt"+p);
        condtiion_txt = (TextView)findViewById(R.id.condtiion_txt);
        condtiion_txt.setText(Html.fromHtml(p +" "+
                "<a href='id.web.freelancer.example.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));

        condtiion_txt.setClickable(true);
        condtiion_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdddetailsDelivery.this,TermsOfUse.class);
                startActivity(i);
            }
        });
        condtiion_txt.setLinkTextColor(Color.BLACK);
        SharedPreferences preferencesuser_id = getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id = preferencesuser_id.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid" + userid);
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("user_id", user_id));
                add(new Pair<String, String>("guest_device_token", "0"));

            }};
            Fuel.post(URL1+"list_address.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
               /* try {
                    JSONArray jsonarray = new JSONArray(s);

*/
                    try {
                        addresslist = new ArrayList<>();
                        JSONObject jsonObj = new JSONObject(s);
                        System.out.println("_________sssssssssssssssssss_____________" + s);

                        String status = jsonObj.getString("status");
                        System.out.println("_________status_____________" + status);

                        if (status.equalsIgnoreCase("false")) {
                            Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                            i.putExtra("id", user_id);
                            startActivity(i);
                            finish();
                        } else {
                            if (jsonObj.has("message")) {
                                message = jsonObj.getString("message");
                                if (message.equalsIgnoreCase("No data")) {
                                    // display_add_address();
                                    Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                                    i.putExtra("id", user_id);
                                    startActivity(i);
                                    finish();
                                }
                            }
                            final String data = jsonObj.getString("data");
                            System.out.println("___________data___________" + data);

                            array = new JSONArray(data);
                            System.out.println("___________length___________" + array.length());

                            if (array.length() <= 0) {
                                // display_add_address();
                                Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                                i.putExtra("id", user_id);
                                startActivity(i);
                                finish();
                                plus_btn.setVisibility(View.GONE);
                            } else {
                                plus_btn.setVisibility(View.VISIBLE);

                                for (int i = 0; i < array.length(); i++) {
                                    addressModel = new AddressModel();
                                    JSONObject jsonobject = array.getJSONObject(i);


                                    id = jsonobject.getString("id");
                                    params1 = new ArrayList<Pair<String, String>>() {{
                                        add(new Pair<String, String>("deal_id", checkout_deal_id));
                                        add(new Pair<String, String>("user_id", userid));
                                        add(new Pair<String, String>("device_token", "0"));
                                        add(new Pair<String, String>("address_id",id));
                                        add(new Pair<String, String>("cust_selected_date", bookingdate));
                                        add(new Pair<String, String>("cust_selected_time", bookingtime));
                                        add(new Pair<String, String>("adult_number",ad_number));
                                        add(new Pair<String, String>("child_number",ch_number));
                                        add(new Pair<String, String>("quantity",people));
                                        add(new Pair<String, String>("comment", str_comnt));
                                        add(new Pair<String, String>("amount",amount));
                                        add(new Pair<String, String>("deal_optionsJSON ", somejson));
                                    }};
                                    System.out.println("******params1" + params1);
                                    String name = jsonobject.getString("name");
                                    String phone = jsonobject.getString("phone");
                                    String city = jsonobject.getString("city");
                                    String area = jsonobject.getString("street");
                                    String flat_address = jsonobject.getString("building");
                                    String state = jsonobject.getString("state");
                                    String pin = jsonobject.getString("pin");
                                    String work_home = jsonobject.getString("type");


                                    addressModel.setId(id);
                                    addressModel.setName(name);
                                    addressModel.setPhone(phone);
                                    addressModel.setCity(city);
                                    addressModel.setArea(area);
                                    addressModel.setFlat_address(flat_address);
                                    addressModel.setState(state);
                                    addressModel.setPin(pin);
                                    addressModel.setWork_home(work_home);

                                    addresslist.add(addressModel);


                                }
                            }
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            recyclerVieaddress.setLayoutManager(llm);
                            addreesAdapter = new AddreesAdapter(getApplicationContext(), addresslist);

                            recyclerVieaddress.scheduleLayoutAnimation();
                            recyclerVieaddress.setAdapter(addreesAdapter);
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
        else {
            android_id = Settings.Secure.getString(AdddetailsDelivery.this.getContentResolver(),Settings.Secure.ANDROID_ID);

            System.out.println("userid" + android_id);
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("user_id", "0"));
                add(new Pair<String, String>("guest_device_token", android_id));

            }};
            Fuel.post(URL1+"list_address.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
               /* try {
                    JSONArray jsonarray = new JSONArray(s);

*/
                    try {
                        addresslist = new ArrayList<>();
                        JSONObject jsonObj = new JSONObject(s);
                        System.out.println("_________sssssssssssssssssss_____________" + s);

                        String status = jsonObj.getString("status");
                        System.out.println("_________status_____________" + status);

                        if (status.equalsIgnoreCase("false")) {
                            Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                            i.putExtra("id", user_id);
                            startActivity(i);
                            finish();
                        } else {
                            if (jsonObj.has("message")) {
                                message = jsonObj.getString("message");
                                if (message.equalsIgnoreCase("No data")) {
                                    // display_add_address();
                                    Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                                    i.putExtra("id", user_id);
                                    startActivity(i);
                                    finish();
                                }
                            }
                            final String data = jsonObj.getString("data");
                            System.out.println("___________data___________" + data);

                            array = new JSONArray(data);
                            System.out.println("___________length___________" + array.length());

                            if (array.length() <= 0) {
                                // display_add_address();
                                Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                                i.putExtra("id", user_id);
                                startActivity(i);
                                finish();
                                plus_btn.setVisibility(View.GONE);
                            } else {
                                plus_btn.setVisibility(View.VISIBLE);

                                for (int i = 0; i < array.length(); i++) {
                                    addressModel = new AddressModel();
                                    JSONObject jsonobject = array.getJSONObject(i);


                                    id = jsonobject.getString("id");
                                    params1 = new ArrayList<Pair<String, String>>() {{
                                        add(new Pair<String, String>("deal_id", checkout_deal_id));
                                        add(new Pair<String, String>("user_id", "0"));
                                        add(new Pair<String, String>("device_token",android_id));
                                        add(new Pair<String, String>("address_id",id));
                                        add(new Pair<String, String>("cust_selected_date", bookingdate));
                                        add(new Pair<String, String>("cust_selected_time", bookingtime));
                                        add(new Pair<String, String>("adult_number",ad_number));
                                        add(new Pair<String, String>("child_number",ch_number));
                                        add(new Pair<String, String>("quantity",people));
                                        add(new Pair<String, String>("comment", str_comnt));
                                        add(new Pair<String, String>("amount",amount));
                                        add(new Pair<String, String>("deal_optionsJSON ", somejson));
                                    }};
                                    System.out.println("******params1" + params1);
                                    String name = jsonobject.getString("name");
                                    String phone = jsonobject.getString("phone");
                                    String city = jsonobject.getString("city");
                                    String area = jsonobject.getString("street");
                                    String flat_address = jsonobject.getString("building");
                                    String state = jsonobject.getString("state");
                                    String pin = jsonobject.getString("pin");
                                    String work_home = jsonobject.getString("type");


                                    addressModel.setId(id);
                                    addressModel.setName(name);
                                    addressModel.setPhone(phone);
                                    addressModel.setCity(city);
                                    addressModel.setArea(area);
                                    addressModel.setFlat_address(flat_address);
                                    addressModel.setState(state);
                                    addressModel.setPin(pin);
                                    addressModel.setWork_home(work_home);

                                    addresslist.add(addressModel);


                                }
                            }
                            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                            recyclerVieaddress.setLayoutManager(llm);
                            addreesAdapter = new AddreesAdapter(getApplicationContext(), addresslist);

                            recyclerVieaddress.scheduleLayoutAnimation();
                            recyclerVieaddress.setAdapter(addreesAdapter);
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

                }
            });

            System.out.println("******params1" + params1);

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

        recyclerVieaddress = (RecyclerView) findViewById(R.id.recycler_vertical1);
        add_address=(LinearLayout) findViewById(R.id.add_address);
        _close = (LinearLayout) findViewById(R.id.img_crcdtlnam);
        _close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"hiii",Toast.LENGTH_SHORT).show();
               // display_add_address();
                Intent i = new Intent(AdddetailsDelivery.this, EdittestActivityDelivery.class);
                i.putExtra("id",user_id);
                i.putExtra("edit","edit");

                startActivity(i);
                finish();

            }
        });

        System.out.println("title....add1.....:"+title);
        System.out.println("cuurncy...add1......:"+currency);
        System.out.println("book_date...add1......:"+booking_date);
        System.out.println("book_time....add1.....:"+booking_time);
        System.out.println("qtyyy2222...."+quantity);
        System.out.println("addddetails_in_.....booking_date" + booking_date);
        System.out.println("childnumber" + child_number);

        place_order=(LinearLayout) findViewById(R.id.place_order);
        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address_id==null){
                    final SweetAlertDialog dialog = new SweetAlertDialog(AdddetailsDelivery.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("Empty fields!")
                            .setContentText("Please select a Delivered address")
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
                }

                addto_cart();
            }
        });


        final LayoutInflater inflator = (LayoutInflater) AdddetailsDelivery.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        custompopup_view = inflator.inflate(R.layout.activity_add_new_address, null);

        coordinatorLayout=(LinearLayout) custompopup_view. findViewById(R.id.coordinatorLayout);
        addr_name=(EditText)custompopup_view.findViewById(R.id.address_name);
        email=(EditText)custompopup_view.findViewById(R.id.address_email);

        addr_phone=(EditText)custompopup_view.findViewById(R.id.address_phone);
        addr_city=(EditText)custompopup_view.findViewById(R.id.address_city);
        addr_area=(EditText)custompopup_view.findViewById(R.id.address_area);
        addrs_flat=(EditText)custompopup_view.findViewById(R.id.address_flatnumber);
        addr_state=(EditText)custompopup_view.findViewById(R.id.addressr_state);
        addr_pin=(EditText)custompopup_view.findViewById(R.id.address_pin);
        plus_btn=(LinearLayout)custompopup_view.findViewById(R.id.plus_btn);
        addr_work=(CheckBox)custompopup_view.findViewById(R.id.address_work);
        addr_home=(CheckBox)custompopup_view.findViewById(R.id.address_home);
         LinearLayout  close=(LinearLayout)custompopup_view.findViewById(R.id.close);
        if(getuser_id!=null&&getuser_id==userid)
        {

            email.setText(get_email);
            fullname.setText(get_name);
            phone.setText(get_phone);
            edit_address.setText(get_address);

        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address_popupwindow.dismiss();
                if(message.equalsIgnoreCase("No data")){

                    address_popupwindow.dismiss();

                }
            }
        });
        addr_work.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (addr_work.isChecked()) {
                    ad_type="work";

                    addr_work.setChecked(false);
                 //   addr_home.setChecked(false);
                    addr_work.setButtonDrawable(R.drawable.blue_tick);
                    addr_home.setButtonDrawable(R.drawable.gray_tick);
                }
                else {

                    ad_type="home";
                    addr_work.setChecked(true);
                    addr_work.setButtonDrawable(R.drawable.gray_tick);
                    addr_home.setButtonDrawable(R.drawable.blue_tick);
                }

            }
        });
        addr_home.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (addr_home.isChecked()) {

                    addr_home.setChecked(false);
                   // addr_work.setChecked(false);
                    ad_type="home";
                    addr_home.setButtonDrawable(R.drawable.blue_tick);
                    addr_work.setButtonDrawable(R.drawable.gray_tick);
                }
                else {
                    ad_type="work";

                    addr_home.setChecked(true);
                    addr_work.setButtonDrawable(R.drawable.blue_tick);
                    addr_home.setButtonDrawable(R.drawable.gray_tick);

                }
            }
        });

        save_data=(LinearLayout)custompopup_view.findViewById(R.id.saves);
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!AdddetailsDelivery.this.isFinishing()) {

                    NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());

                    boolean i = networkCheckingClass.ckeckinternet();
                    if (i) {
                        if (addr_name.getText().toString()== null) {
                            addr_name.setError("Enter Name");
                        } else if (addr_phone.getText().toString()== null) {
                            addr_phone.setError("Enter Phone");
                        } else if (addr_city.getText().toString().length() == 0) {
                            addr_city.setError("Enter City");
                        } else if (addr_area.getText().toString().length() == 0) {
                            addr_area.setError("Enter Area");
                        } else if (addrs_flat.getText().toString().length() == 0) {
                            addrs_flat.setError("Enter Flat Number");
                        } else if (addr_state.getText().toString().length() == 0) {
                            addr_state.setError("Enter State");
                        } else if (addr_pin.getText().toString().length() == 0) {
                            addr_pin.setError("Enter Pin");
                        } else {
                            adname=addr_name.getText().toString();
                            adphone=addr_phone.getText().toString();

                            adcity=addr_city.getText().toString();

                            adarea=addr_area.getText().toString();

                            adflatads=addrs_flat.getText().toString();
                            adstate=addr_state.getText().toString();
                            adpin=addr_pin.getText().toString();
                            new SendPostRequest().execute();
                        }

                    } else {
                        final SweetAlertDialog dialog = new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.NORMAL_TYPE);
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
                    }
                }
            }
        });




    }

    private void display_add_address() {
        try {
            plus_btn.setVisibility(View.GONE);

            address_popupwindow = new PopupWindow(custompopup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                address_popupwindow.setElevation(5.0f);
            }
            address_popupwindow.setFocusable(true);
            address_popupwindow.setAnimationStyle(R.style.AppTheme);
            address_popupwindow.showAtLocation(coordinatorLayout, Gravity.CENTER, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class getDetails extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler h = new HttpHandler();
            String s = h.makeServiceCall(Constants.URL+"view-profile.php?user_id="+user_id);
            if(s!=null){
                try {
                    JSONObject jobj=new JSONObject(s);
                    String status=jobj.getString("status");
                    if(status.equalsIgnoreCase("true")){
                        JSONObject dataObj=jobj.getJSONObject("data");
                        res_name=dataObj.getString("name");
                        res_email=dataObj.getString("email");
                        res_phone=dataObj.getString("phone");
                        res_image=dataObj.getString("image");
                        res_location=dataObj.getString("location");

                    }

                }
                catch (Exception e){
                    e.printStackTrace();


                }
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            email.setText(res_email);
            fullname.setText(res_name);
            phone.setText(res_phone);
            edit_address.setText(res_location);




        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        public void onPreExecute(){

        }

        public String doInBackground(String... arg0) {
            //String js=filename.replaceAll(" ","");
            try {

                URL url = new URL(URL1+"add_address.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();



                postDataParams.put("user_id",userid);
                postDataParams.put("name",adname);
                postDataParams.put("phone",adphone);
                postDataParams.put("city",adcity);
                postDataParams.put("street",adarea);
                postDataParams.put("building",adflatads);
                postDataParams.put("state",adstate);
                postDataParams.put("pin",adpin);
                postDataParams.put("type",ad_type);


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
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

            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
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
    private void addto_cart() {

        progress.setVisibility(View.VISIBLE);
        Fuel.post(URL1+"add_to_cart.php",params1).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                progress.setVisibility(View.GONE);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    System.out.println("*************result : " + s);
                    String status = jsonObj.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        final SweetAlertDialog dialog = new SweetAlertDialog(AdddetailsDelivery.this,SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitleText("Item Added Successfully")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();
                                        Intent i = new Intent(AdddetailsDelivery.this, Cart_details.class);

                                        startActivity(i);
                                        finish();

                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                    }
                    else {
                        String msg = jsonObj.getString("message");
                        final SweetAlertDialog dialog = new SweetAlertDialog(AdddetailsDelivery.this,SweetAlertDialog.WARNING_TYPE);
                        dialog.setTitleText(msg)
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();

                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

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
