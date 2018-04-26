package com.meridian.dateout.explore.category_booking_detailspage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.address.Adddetails;
import com.meridian.dateout.explore.calendar.CaldroidSampleCustomFragment;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.ScheduleModel;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.Constants.analytics;


public class Booking_DetailsActivity extends AppCompatActivity {


    ArrayList<ScheduleModel> scheduleModelArrayList;
    ArrayList<String> arrayListtime;

    String booking_date="",booking_time="";
    LinearLayout linearLayout_schedule;

    ImageView back;
    String checkout_deal_id;
    static String time;
    String title;
    TextView v1, v3, v, v2;
    TextView txt_title;

    private PopupWindow mPopupWindow,  mPopupWindow_cmnt;
    TextView adult_plus, adult_minus, adult_number, adult_price, adult_age, adult_discnt_price, child_discnt_price;
    TextView child_plus, child_minus, child_number, child_price, child_age, total_price;

    int deal_id;
    Button next,add_to_cart;
    int s = 0;
    LinearLayout linear_adult, linear_child;
    int ad_number = 0;
    int ch_number = 0;
    int people_number = 0;
    int tkt = 0;
    int ad_total = 0;
    int ch_total = 0;
    int ad_price = 0;
    int ch_price = 0;
    int adult_discount_tkt_price = 0;
    int child_discount_tkt_price = 0;
    int total = 0;
    TextView total_num;
    int people = 0, tot_num_qty = 0, tot_num_ad_ch = 0;
    String adult_tkt_price = null, child_tkt_price = null, adult_age_range, child_age_range, deal_type;
    String address;
    ViewPager Tab;
    String str_time = null;
    LinearLayout linear_people, linear_peopl_total, layout_other;
    String price, ticket_disnt_price = null;
    TextView price_people, price_people_discnt, minus_people, number_people, plus_peopl, people_total, scheduled_date, scheduled_time;

    public static ArrayList<ScheduleModel> DisableModelArrayList;
    ArrayList<Date> disabledDates_fromstart, disabledDates_fromend, disabledDates_fromjson;
    ArrayList<Date> disabledDates_addall;
    Date minDate, maxDate;
    String FORMATTED_MINDATE, FORMATTED_MAXDATE;
    CaldroidFragment caldroidFragment;
    CaldroidFragment dialogCaldroidFragment;
    String minimumdate, maximumdate;
    String mnth_remov_zero_min;
    String mnth_remov_zero_max;
    Calendar cal;
    String checkout_info, calendar_instruction;
    String currency;
    String ad, cd, timing;
    String userid,str_comnt;
    List<Pair<String, String>> params;
    List<Pair<String, String>> params_item;
    EditText edt_comment;
    Button but_comnt;
    LinearLayout layout_selected_date, layout_selected_time;
    View  customView,   customView_comment;
    ImageView close_point;
    String str_commnt;
    LinearLayout calendar2,booking_details_new;
    TextView  calendar_information,txt_book_currnt_pts,txt_book_deal_pts,txt_book_total_pts,txt_points_nextlevel;
    String calendar_show,android_id;
    ImageView img_book;
    LinearLayout  linear_info_no_cal,linear_booking_button;
    String deal_point,available_point,points_nextlevel;
    ImageView Home,cart;
    RecyclerView recyclerView;
    ItemModel Im;
    ArrayList<ItemModel>item_list=new ArrayList<>();
    ItemAdapter itemAdapter;
    EditText Comment;
    ProgressBar progress_bar_explore;
    public  static  String somejson="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__details_new);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        android_id = Settings.Secure.getString(Booking_DetailsActivity.this.getContentResolver(),Settings.Secure.ANDROID_ID);
        analytics = FirebaseAnalytics.getInstance(this);
        analytics.setCurrentScreen(this,this.getLocalClassName(), null /* class override */);

        //displayPopup_comment();

        progress_bar_explore=(ProgressBar) findViewById(R.id.progress_bar_explore);
        caldroidFragment = new CaldroidSampleCustomFragment();
        schedule();
        booking_details_new=(LinearLayout)findViewById(R.id.booking_details_new);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager llm = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(llm);

        Bundle args = new Bundle();
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE,false);
        args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        caldroidFragment.setArguments(args);

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String   user_id = preferences.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences1 =getSharedPreferences("myfbid", MODE_PRIVATE);
        String  profile_id = preferences1.getString("user_idfb", null);
        if (profile_id != null) {
            userid = profile_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences2 = getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences2.getString("user_id", null);
        if (profileid_gmail != null) {
            userid = profileid_gmail;
            System.out.println("userid" + userid);
        }
        System.out.println("useridmypreflogin" + userid);
        System.out.println("userrrr" + user_id);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar2, caldroidFragment);
        t.commit();
        LayoutInflater inflater_comment = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        customView_comment = inflater_comment.inflate(R.layout.booking_details_comment_pop, null);
        close_point=(ImageView) customView_comment.findViewById(R.id.close_point_comment);
        close_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow_cmnt.dismiss();
                edt_comment.setText("");
            }
        });
        Home= (ImageView) findViewById(R.id.home);
        cart= (ImageView) findViewById(R.id.cart);
        Comment= (EditText) findViewById(R.id.comment);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  =new Intent(Booking_DetailsActivity.this, FrameLayoutActivity.class);
                startActivity(i);
            }
        });
        but_comnt= (Button)customView_comment. findViewById(R.id.but_cmnt_ok);
        edt_comment= (EditText) customView_comment. findViewById(R.id.editText_comment);

        but_comnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_commnt=edt_comment.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("shr_comment", MODE_PRIVATE).edit();
                editor.putString("comment",str_commnt);
                editor.commit();
                edt_comment.setText("");
                mPopupWindow_cmnt.dismiss();



            }
        });

        LayoutInflater inflater2 = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater2.inflate(R.layout.booking_details_point_popup, null);
        close_point=(ImageView)customView.findViewById(R.id.close_point_book);
        linear_booking_button= (LinearLayout)customView.findViewById(R.id.linear_booking_button);
        txt_book_currnt_pts= (TextView) customView.findViewById(R.id.txt_book_currnt_pts);
        txt_book_deal_pts= (TextView) customView.findViewById(R.id.txt_book_deal_pts);
        txt_book_total_pts= (TextView) customView.findViewById(R.id.txt_book_total_pts);
        txt_points_nextlevel= (TextView) customView.findViewById(R.id.nextlevel);

        close_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPopupWindow.dismiss();


            }


        });
        linear_booking_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Adddetails.class);
                i.putExtra("adult_price", ad_price);
                i.putExtra("child_price", ch_price);
                i.putExtra("adult_number", ad_number);
                i.putExtra("child_number", ch_number);
                i.putExtra("timee", str_time);
                i.putExtra("booking_date",booking_date);
                i.putExtra("booking_time", booking_time);

                System.out.println("quantityys" + people_number + tot_num_qty);
                i.putExtra("currency", currency);
                i.putExtra("ticket_disnt_price", tkt);
                i.putExtra("checkout_deal_id", checkout_deal_id);
                i.putExtra("title", title);
                i.putExtra("total_number", people);
                i.putExtra("qty", people_number);
                i.putExtra("total_price", total);
                i.putExtra("address", address);
                i.putExtra("child_discnt_price", child_discount_tkt_price);
                i.putExtra("adult_discnt_price", adult_discount_tkt_price);

                System.out.println("currency....boo2.....:"+currency);
                System.out.println("title....boo2.....:"+title);

                System.out.println("book_date.........:"+booking_date);
                System.out.println("book_time.........:"+booking_time);

                System.out.println("discount" + adult_discount_tkt_price);
                System.out.println("qtyyyy1................." + people_number);
                startActivity(i);

            }
        });

        txt_title = (TextView) findViewById(R.id.txt_book_title);
        deal_id = getIntent().getIntExtra("deal_id", 0);
        checkout_deal_id = String.valueOf(deal_id);
        System.out.println("****checkout_deal_id"+checkout_deal_id);
        params_item = new ArrayList<Pair<String, String>>() {{
            add(new Pair<String, String>("deal_id", checkout_deal_id));

        }};

        item();
        time = getIntent().getStringExtra("time");
        linearLayout_schedule = (LinearLayout) findViewById(R.id.linear_sch);
        layout_other = (LinearLayout) findViewById(R.id.layout_other);
        linear_people = (LinearLayout) findViewById(R.id.linear_peopl);
        linear_peopl_total = (LinearLayout) findViewById(R.id.layout_people_total);
        price_people = (TextView) findViewById(R.id.price_people);
        price_people_discnt = (TextView) findViewById(R.id.price_people_discnt);
        minus_people = (TextView) findViewById(R.id.minusS_people);
        number_people = (TextView) findViewById(R.id.numberS_people);
        plus_peopl = (TextView) findViewById(R.id.plusS_people);
        people_total = (TextView) findViewById(R.id.txt_people_total);
        scheduled_date = (TextView) findViewById(R.id.scheduled_date);
        linear_info_no_cal = (LinearLayout) findViewById(R.id.cal);
        scheduled_time = (TextView) findViewById(R.id.scheduled_time);
        linear_adult = (LinearLayout) findViewById(R.id.linear_adult);
        linear_child = (LinearLayout) findViewById(R.id.linear_child);

        img_book = (ImageView) findViewById(R.id.imageview_book);
        calendar_information = (TextView) findViewById(R.id.calendar_information);
        linear_adult.setVisibility(View.VISIBLE);
        adult_plus = (TextView) findViewById(R.id.plus);
        adult_minus = (TextView) findViewById(R.id.minus);
        adult_number = (TextView) findViewById(R.id.number);
        adult_price = (TextView) findViewById(R.id.price);
        adult_age = (TextView) findViewById(R.id.age);
        total_num = (TextView) findViewById(R.id.total_num);
        adult_discnt_price = (TextView) findViewById(R.id.adult_discnt_price);
        child_discnt_price = (TextView) findViewById(R.id.child_discnt_price);
        layout_selected_date = (LinearLayout) findViewById(R.id.layout_selected_date);
        layout_selected_time = (LinearLayout) findViewById(R.id.layout_selected_time);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Cart_details.class);
                startActivity(i);
            }
        });

        child_plus = (TextView) findViewById(R.id.plusS);
        child_minus = (TextView) findViewById(R.id.minusS);
        child_number = (TextView) findViewById(R.id.numberS);
        child_price = (TextView) findViewById(R.id.price1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        View view1 = getLayoutInflater().inflate(R.layout.tab_custom_view, null);
        v = (TextView) view1.findViewById(R.id.txt1);
        v1 = (TextView) view1.findViewById(R.id.txt2);

        View view2 = getLayoutInflater().inflate(R.layout.tab_custom_view, null);
        v2 = (TextView) view2.findViewById(R.id.txt1);
        v3 = (TextView) view2.findViewById(R.id.txt2);

        child_age = (TextView) findViewById(R.id.age1);
        total_price = (TextView) findViewById(R.id.total_price);

        linearLayout_schedule = (LinearLayout) findViewById(R.id.linear_sch);
        calendar2 = (LinearLayout) findViewById(R.id.calendar2);
        if (getIntent().getStringExtra("calendar_instruction") != null) {
            calendar_instruction = getIntent().getStringExtra("calendar_instruction");
            System.out.println("calendar_instructionnnn" + calendar_instruction);
        }
        if (getIntent().getStringExtra("calendar_show") != null)
        {
            calendar_show = getIntent().getStringExtra("calendar_show");
            if (calendar_show.contentEquals("no"))
            {

                layout_selected_date.setVisibility(View.GONE);
                layout_selected_time.setVisibility(View.GONE);
                linearLayout_schedule.setVisibility(View.GONE);
                calendar2.setVisibility(View.GONE);
                calendar_information.setVisibility(View.VISIBLE);
                calendar_information.setText(calendar_instruction);

                linear_info_no_cal.setVisibility(View.VISIBLE);
                img_book.setVisibility(View.VISIBLE);

            }
            else
            {
                calendar_information.setVisibility(View.GONE);
                linearLayout_schedule.setVisibility(View.VISIBLE);
                layout_selected_date.setVisibility(View.VISIBLE);
                layout_selected_time.setVisibility(View.VISIBLE);
                calendar2.setVisibility(View.VISIBLE);
                linear_info_no_cal.setVisibility(View.GONE);

                img_book.setVisibility(View.GONE);


            }

        }
        if (getIntent().getStringExtra("deal_point") != null) {
            deal_point = getIntent().getStringExtra("deal_point");

            System.out.println("deall..pointttt.....from..deal.....detaill"+deal_point);
        }
        if (getIntent().getStringExtra("available_point") != null) {
            available_point = getIntent().getStringExtra("available_point");
            txt_book_currnt_pts.setText(available_point+" Points");
        }
        if (getIntent().getStringExtra("points_nextlevel") != null) {
            points_nextlevel= getIntent().getStringExtra("points_nextlevel");

        }



        if (getIntent().getStringExtra("timing") != null) {
            timing = getIntent().getStringExtra("timing");
        }
        if (getIntent().getStringExtra("deal_type") != null) {
            deal_type = getIntent().getStringExtra("deal_type");
        }
        if (getIntent().getStringExtra("address") != null) {
            address = getIntent().getStringExtra("address");
        }
        System.out.println("deaaalstypeee" + deal_type);
        if (getIntent().getStringExtra("price") != null) {
            price = getIntent().getStringExtra("price");
        }
        if (getIntent().getStringExtra("tickt_discnt_price") != null) {
            ticket_disnt_price = getIntent().getStringExtra("tickt_discnt_price");
            System.out.println("date out..............tickett...discounted priceeeeeeee"+ticket_disnt_price);
        }
        if (getIntent().getStringExtra("adult_price") != null) {
            adult_tkt_price = getIntent().getStringExtra("adult_price");

            if (adult_tkt_price.contentEquals("") || adult_tkt_price.isEmpty() || adult_tkt_price.equals(0)) {
                System.out.println("adult_priceee1......" + adult_tkt_price);

                v.setText(R.string.adult);
                System.out.println("adult_priceee2......" + v.getText().toString());
                v1.setText("0");
                tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
                view1.setVisibility(View.INVISIBLE);
                linear_adult.setVisibility(View.GONE);
                linear_people.setVisibility(View.GONE);

            } else {
                System.out.println("adult_priceee2......" + adult_tkt_price);
                v.setText(R.string.adult);
                System.out.println("adult_priceee2......" + v.getText().toString());
                v1.setText("0");
                tabLayout.addTab(tabLayout.newTab().setCustomView(view1));
            }
        }


        if (getIntent().getStringExtra("child_price") != null) {
            child_tkt_price = getIntent().getStringExtra("child_price");
            System.out.println("child_priceee0......" + child_tkt_price);
            if (child_tkt_price.contentEquals("") || child_tkt_price.isEmpty() || child_tkt_price.equals(0))
            {
                System.out.println("child_priceee1......" + child_tkt_price);
                v2.setText(R.string.child);
                v3.setText("0");
                linear_child.setVisibility(View.GONE);
                linear_people.setVisibility(View.GONE);

            }
            else
            {
                System.out.println("child_priceee2......" + child_tkt_price);
                v2.setText(R.string.child);
                v3.setText("0");
                tabLayout.addTab(tabLayout.newTab().setCustomView(view2));
            }


        }


        if (getIntent().getStringExtra("adult_discnt_price") != null)
        {
            ad = getIntent().getStringExtra("adult_discnt_price");
        }
        if (getIntent().getStringExtra("child_discnt_price") != null)
        {
            cd = getIntent().getStringExtra("child_discnt_price");
        }
        if (getIntent().getStringExtra("adult_age") != null)
        {
            adult_age_range = getIntent().getStringExtra("adult_age");
        }
        if (getIntent().getStringExtra("child_age") != null)
        {
            child_age_range = getIntent().getStringExtra("child_age");
        }
        if (getIntent().getStringExtra("checkout_info") != null)
        {
            checkout_info = getIntent().getStringExtra("checkout_info");
        }
        if (getIntent().getStringExtra("currency") != null)
        {
            currency = getIntent().getStringExtra("currency");
        }

        System.out.println("*************************************************ticket_disnt_pricefirst" + ticket_disnt_price + "priceee..." + price);
        System.out.println("*************************************************adultt_disnt_pricefirst" + adult_discount_tkt_price + "priceee..." + adult_tkt_price);

        try
        {
            adult_discount_tkt_price = Integer.parseInt(ad);
        }
        catch (NumberFormatException ex)
        {

        }

        try
        {
            child_discount_tkt_price = Integer.parseInt(cd);
        }
        catch (NumberFormatException ex)
        {

        }
        child_number.setText(String.valueOf(ad_number));
        adult_number.setText(String.valueOf(ch_number));

        child_age.setText(child_age_range);
        adult_age.setText(adult_age_range);
        if (checkout_info != null)
        {

        }
        System.out.println("pricesssss....." + ad + "ssss....." + adult_tkt_price);


        if (adult_tkt_price != null) {
            if (adult_discount_tkt_price != 0) {
                System.out.println("pricesssss....." + adult_discount_tkt_price + "ssss....." + adult_tkt_price);
                adult_discnt_price.setText(currency + " " + adult_discount_tkt_price);
                System.out.println("pricesssss....." + adult_discount_tkt_price + "ssss....." + adult_tkt_price);
                final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
                adult_price.setText(currency + " " + adult_tkt_price, TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) adult_price.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, adult_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            } else {
                try {
                    adult_discount_tkt_price = Integer.parseInt(adult_tkt_price);
                } catch (NumberFormatException e) {

                }
                adult_discnt_price.setText(currency + " " + adult_tkt_price);
                adult_price.setVisibility(View.INVISIBLE);
            }
        }


        if (child_tkt_price != null) {
            if (child_discount_tkt_price != 0) {
                System.out.println("pricesssss....." + child_discount_tkt_price + "ssss....." + child_tkt_price);
                child_discnt_price.setText(currency + " " + child_discount_tkt_price);
                System.out.println("pricesssss....." + adult_discount_tkt_price + "ssss....." + adult_tkt_price);
                final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
                child_price.setText(currency + " " + child_tkt_price, TextView.BufferType.SPANNABLE);
                Spannable spannable = (Spannable) child_price.getText();
                spannable.setSpan(STRIKE_THROUGH_SPAN, 0, child_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            } else {
                try {
                    child_discount_tkt_price = Integer.parseInt(child_tkt_price);
                } catch (NumberFormatException e) {

                }
                child_discnt_price.setText(currency + " " + child_tkt_price);
                child_price.setVisibility(View.INVISIBLE);
            }
        }

        try {
            if (deal_type.contentEquals("gifts") || deal_type.contentEquals("food_and_beverages")) {
                tabLayout.setVisibility(View.GONE);
                linear_people.setVisibility(View.VISIBLE);
                linear_adult.setVisibility(View.GONE);
                linear_child.setVisibility(View.GONE);
                linear_peopl_total.setVisibility(View.GONE);
                layout_other.setVisibility(View.GONE);


                    System.out.println("*************************************************ticket_disnt_price" + ticket_disnt_price);
                    if (ticket_disnt_price.isEmpty() || ticket_disnt_price.equals(0) || ticket_disnt_price.equals("")) {
                        price_people_discnt.setText(currency + " " + price);
                        try
                        {
                            tkt = Integer.parseInt(price);
                        }
                        catch (NumberFormatException ex)
                        {

                        }
                        price_people.setVisibility(View.INVISIBLE);

                    }
                    else
                        {
                        final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
                        price_people.setText(currency + " " + price, TextView.BufferType.SPANNABLE);
                        Spannable spannable1 = (Spannable) price_people.getText();
                        spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, price_people.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        try {
                            tkt = Integer.parseInt(ticket_disnt_price);
                        } catch (NumberFormatException ex) { // handle your exception

                        }
                        price_people_discnt.setText(currency + " " + ticket_disnt_price);
                    }

            } else {
                tabLayout.setVisibility(View.VISIBLE);
                linear_people.setVisibility(View.GONE);
                linear_adult.setVisibility(View.VISIBLE);
                linear_child.setVisibility(View.GONE);
                linear_peopl_total.setVisibility(View.VISIBLE);
                layout_other.setVisibility(View.VISIBLE);

            }
        }
        catch (NullPointerException e)
        {

        }
        try
        {
            if (child_tkt_price.equals(null) || child_tkt_price.equals("") || child_tkt_price.isEmpty())
            {
                ch_price = 0;

            }
            else
            {
                ch_price = Integer.parseInt(child_tkt_price);

            }

        }
        catch (Exception e)
        {

        }




        /*caldroid>>>>>>>>>>>.........................................*/


        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("MM");
        final SimpleDateFormat formatterday = new SimpleDateFormat("dd");
        final SimpleDateFormat formattermnth = new SimpleDateFormat("MM");
        final SimpleDateFormat formatteryear = new SimpleDateFormat("yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****


        cal = Calendar.getInstance();
        minDate = cal.getTime();

        minimumdate = String.valueOf(minDate);
        String removezero_min = formatter1.format(minDate);
        FORMATTED_MINDATE = formatter.format(minDate);
        if (removezero_min.startsWith("0")) {
            mnth_remov_zero_min = removezero_min.replaceFirst("0", "");
        } else {
            mnth_remov_zero_min = removezero_min;
        }
        System.out.println("minimumdate..........." + minimumdate);
        System.out.println("remove zero...formatted..minimumdate..........." + mnth_remov_zero_min);
        System.out.println("formatter....minimumdate..........." + FORMATTED_MINDATE);

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 3); // Jump two months ahead
        cal.add(Calendar.DATE, -1);
        maxDate = cal.getTime();
        FORMATTED_MAXDATE = formatter.format(maxDate);
        maximumdate = String.valueOf(maxDate);
        System.out.println("maximumdate..........." + maximumdate);
        System.out.println("formatted..maximumdate..........." + formatter1.format(maxDate));
        System.out.println("formatter....maximumdate..........." + FORMATTED_MAXDATE);
        String removezero_max = formatter1.format(maxDate);

        if (removezero_max.startsWith("0"))
        {
            mnth_remov_zero_max = removezero_max.replaceFirst("0", "");
        } else {
            mnth_remov_zero_max = removezero_max;
        }
        System.out.println("remove zero...formatted..maximumdate..........." + mnth_remov_zero_max);
        caldroidFragment.setMinDate(minDate);
        caldroidFragment.setMaxDate(maxDate);
        caldroidFragment.refreshView();
        v.setTextColor(Color.parseColor("#0A7197"));
        v2.setTextColor(Color.parseColor("#6C6D6D"));
        v1.setTextColor(Color.parseColor("#0A7197"));
        v3.setTextColor(Color.parseColor("#6C6D6D"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.black), ContextCompat.getColor(getApplicationContext(), R.color.com_facebook_blue));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //  viewPager.setCurrentItem(tab.getPosition());
                int position = tab.getPosition();
                // linear_child.setVisibility(View.VISIBLE);
                if (position == 0) {
                    System.out.println("Oneeeee");

                    if (adult_tkt_price.contentEquals("") || adult_tkt_price.isEmpty() || adult_tkt_price.equals(0)) {
                        linear_adult.setVisibility(View.GONE);
                    } else {
                        linear_adult.setVisibility(View.VISIBLE);
                        linear_child.setVisibility(View.GONE);
                        child_discnt_price.setVisibility(View.GONE);
                        adult_discnt_price.setVisibility(View.VISIBLE);
                        linear_people.setVisibility(View.GONE);

                        v.setTextColor(Color.parseColor("#0A7197"));
                        v2.setTextColor(Color.parseColor("#6C6D6D"));

                        v1.setTextColor(Color.parseColor("#0A7197"));
                        v3.setTextColor(Color.parseColor("#6C6D6D"));
                    }
                }
                if (position == 1) {
                    System.out.println("Oneeeee.......");

                    if (child_tkt_price.contentEquals("") || child_tkt_price.isEmpty() || child_tkt_price.equals(0)) {
                        linear_child.setVisibility(View.GONE);
                    } else {
                        linear_child.setVisibility(View.VISIBLE);
                        linear_adult.setVisibility(View.GONE);
                        child_discnt_price.setVisibility(View.VISIBLE);
                        adult_discnt_price.setVisibility(View.GONE);
                        linear_people.setVisibility(View.GONE);
                        v.setTextColor(Color.parseColor("#6C6D6D"));
                        v2.setTextColor(Color.parseColor("#0A7197"));

                        v1.setTextColor(Color.parseColor("#6C6D6D"));
                        v3.setTextColor(Color.parseColor("#0A7197"));
                    }


                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //   final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        title = getIntent().getStringExtra("title");
        txt_title.setText(title);
        //  txt_time.setText(time);
        System.out.println("deal_id" + deal_id);
        checkout_deal_id = String.valueOf(deal_id);
        next = (Button) findViewById(R.id.next);
        add_to_cart = (Button) findViewById(R.id.add_to_cart);
        System.out.println("boooking__in details" + booking_date);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u=new Intent(Booking_DetailsActivity.this, Cart_details.class);
                startActivity(u);

            }
        });

        plus_peopl.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                if (people_number >= 0) {
                    people_number++;
                    number_people.setText(String.valueOf(people_number));
                    ad_total = tkt * people_number;
                    total = ad_total;
                    people = people_number;
                    people_total.setText(currency + " " + total);
                    tot_num_qty = people_number; try {
                        int total_deal_point = Integer.parseInt(deal_point) * people_number;
                        txt_book_deal_pts.setText(total_deal_point+" Points");
                        int s = Integer.parseInt(available_point) + total_deal_point;
                        txt_book_total_pts.setText("" + s+" Points");
                        System.out.println("summmmm"+s+"....."+total_deal_point );
                        if(total_deal_point>=Integer.parseInt(points_nextlevel))
                        {
                            txt_points_nextlevel.setText("You have reached the next stage");

                        }
                        else
                        {
                            int total_nextlevel=Integer.parseInt(points_nextlevel)-total_deal_point;
                            System.out.println("summmmm....."+total_nextlevel );
                            txt_points_nextlevel.setText(""+total_nextlevel+" Points");
                        }

                    }
                    catch (Exception e)
                    {

                    }

                }


            }


        });

        minus_people.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (people_number > 0) {
                    people_number--;
                    number_people.setText(String.valueOf(people_number));
                    ad_total = tkt * people_number;
                    total = ad_total;
                    people_total.setText(currency + " " + total);
                    tot_num_qty = people_number;
                    people = people_number;
                    try {
                        int total_deal_point = Integer.parseInt(deal_point) * people_number;
                        txt_book_deal_pts.setText(total_deal_point+" Points");
                        int s = Integer.parseInt(available_point) + total_deal_point;
                        txt_book_total_pts.setText("" + s+" Points");
                        System.out.println("summmmm"+s+"....."+total_deal_point );
                        if(total_deal_point>=Integer.parseInt(points_nextlevel))
                        {
                            txt_points_nextlevel.setText("You have reached the next stage");

                        }
                        else
                        {
                            int total_nextlevel=Integer.parseInt(points_nextlevel)-total_deal_point;
                            System.out.println("summmmm....."+total_nextlevel );
                            txt_points_nextlevel.setText(""+total_nextlevel+" Points");
                        }

                    }
                    catch (Exception e)
                    {

                    }


                }


            }
        });

        adult_plus.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                if (ad_number >= 0) {
                    ad_number++;
                    adult_number.setText(String.valueOf(ad_number));
                    v1.setText(String.valueOf(ad_number));
                    ad_total = adult_discount_tkt_price * ad_number;
                    total = ad_total + ch_total;
                    total_price.setText(currency + " " + total);
                    people = ad_number + ch_number;
                    tot_num_ad_ch = ad_number + ch_number;
                    total_num.setText("" + people);

                    try {
                        int total_deal_point = Integer.parseInt(deal_point) * people;
                        txt_book_deal_pts.setText(total_deal_point+" Points");


                        int s = Integer.parseInt(available_point) + total_deal_point;
                        txt_book_total_pts.setText("" + s+" Points");
                        System.out.println("summmmm"+s+"....."+total_deal_point );
                        if(total_deal_point>=Integer.parseInt(points_nextlevel))
                        {
                            txt_points_nextlevel.setText("You have reached the next stage");

                        }
                        else
                        {
                            int total_nextlevel=Integer.parseInt(points_nextlevel)-total_deal_point;
                            System.out.println("summmmm....."+total_nextlevel );
                            txt_points_nextlevel.setText(""+total_nextlevel+" Points");
                        }


                    }
                    catch (Exception e)
                    {

                    }

                }



            }


        });

        adult_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (ad_number > 0) {
                    ad_number--;
                    adult_number.setText(String.valueOf(ad_number));
                    v1.setText(String.valueOf(ad_number));
                    ad_total = adult_discount_tkt_price * ad_number;
                    total = ad_total + ch_total;
                    total_price.setText(currency + " " + total);
                    people = ad_number + ch_number;
                    tot_num_ad_ch = ad_number + ch_number;
                    total_num.setText("" + people);
                    try {
                        int total_deal_point = Integer.parseInt(deal_point) * people;
                        txt_book_deal_pts.setText(total_deal_point+" Points");

                        int s = Integer.parseInt(available_point) + total_deal_point;
                        txt_book_total_pts.setText("" + s+" Points");
                        System.out.println("summmmm"+s+"....."+total_deal_point );
                        if(total_deal_point>=Integer.parseInt(points_nextlevel))
                        {
                            txt_points_nextlevel.setText("You have reached the next stage");

                        }
                        else
                        {
                            int total_nextlevel=Integer.parseInt(points_nextlevel)-total_deal_point;
                            System.out.println("summmmm....."+total_nextlevel );
                            txt_points_nextlevel.setText(""+total_nextlevel+" Points");
                        }
                    }
                    catch (Exception e)
                    {

                    }


                }


            }
        });
        child_plus.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                if (ch_number >= 0) {
                    ch_number++;
                    child_number.setText(String.valueOf(ch_number));
                    v3.setText(String.valueOf(ch_number));
                    ch_total = child_discount_tkt_price * ch_number;
                    total = ad_total + ch_total;
                    total_price.setText(currency + " " + total);
                    people = ad_number + ch_number;
                    tot_num_ad_ch = ad_number + ch_number;
                    total_num.setText("" + people);
                    try {
                        int total_deal_point = Integer.parseInt(deal_point) * people;
                        txt_book_deal_pts.setText(total_deal_point+" Points");
                        int s = Integer.parseInt(available_point) + total_deal_point;
                        txt_book_total_pts.setText("" + s+" Points");
                        System.out.println("summmmm"+s+"....."+total_deal_point );
                        if(total_deal_point>=Integer.parseInt(points_nextlevel))
                        {
                            txt_points_nextlevel.setText("You have reached the next stage");

                        }
                        else
                        {
                            int total_nextlevel=Integer.parseInt(points_nextlevel)-total_deal_point;
                            System.out.println("summmmm....."+total_nextlevel );
                            txt_points_nextlevel.setText(""+total_nextlevel+" Points");
                        }

                    }
                    catch (Exception e)
                    {

                    }

                }


            }


        });

        child_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ch_number > 0) {
                    ch_number--;
                    child_number.setText(String.valueOf(ch_number));
                    v3.setText(String.valueOf(ch_number));
                    ch_total = child_discount_tkt_price * ch_number;
                    total = ad_total + ch_total;
                    total_price.setText(currency + " " + total);
                    people = ad_number + ch_number;
                    tot_num_ad_ch = ad_number + ch_number;
                    total_num.setText("" + people);
                    try {
                        int total_deal_point = Integer.parseInt(deal_point) * people;
                        txt_book_deal_pts.setText(total_deal_point+" Points");

                        int s = Integer.parseInt(available_point) + total_deal_point;
                        txt_book_total_pts.setText("" + s+" Points");
                        System.out.println("summmmm"+s+"....."+total_deal_point );
                        if(total_deal_point>=Integer.parseInt(points_nextlevel))
                        {
                            txt_points_nextlevel.setText("You have reached the next stage");

                        }
                        else
                        {
                            int total_nextlevel=Integer.parseInt(points_nextlevel)-total_deal_point;
                            System.out.println("summmmm....."+total_nextlevel );
                            txt_points_nextlevel.setText(""+total_nextlevel+" Points");
                        }

                    }
                    catch (Exception e)
                    {

                    }

                }


            }
        });
        System.out.println("summmmm"+s+"....."+available_point);
        System.out.println("summmmm"+s+"....."+available_point+"deal___pointtt"+deal_point+people_number+tot_num_qty+"peoplee"+people);


        back = (ImageView) findViewById(R.id.img_crcdtlnam);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onBackPressed();

            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Booking.......deaaalstypeee_click...." + deal_type);
                System.out.println("Booking..... datee..." + booking_date);
                System.out.println("Booking........calendar_show" + calendar_show);
                System.out.println("Booking........People" + people);


                if (booking_date == "" && calendar_show.contentEquals("yes"))
                {
                    final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Please select an Available date")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));



                } else if (people == 0)
                {

                    if (deal_type.contentEquals("gifts") || deal_type.contentEquals("food_and_beverages")) {
                        final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("")
                                .setContentText("Please select the quantity of deal you would like to purchase")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                    } else {
                        final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("")
                                .setContentText("Please select adult number or child number")
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
                else {

                    if(userid!=null)
                    {
                        // displayPopup();
                        params = new ArrayList<Pair<String, String>>() {{
                            add(new Pair<String, String>("deal_id", checkout_deal_id));
                            add(new Pair<String, String>("user_id",userid));
                            add(new Pair<String, String>("device_token","0"));
                            add(new Pair<String, String>("cust_selected_date", booking_date));
                            add(new Pair<String, String>("cust_selected_time", booking_time));
                            add(new Pair<String, String>("adult_number", String.valueOf(ad_number)));
                            add(new Pair<String, String>("child_number",String.valueOf(ch_number)));
                            add(new Pair<String, String>("quantity", String.valueOf(people)));
                            add(new Pair<String, String>("comment", str_comnt));
                            add(new Pair<String, String>("amount", String.valueOf(total/people)));
                            add(new Pair<String, String>("deal_optionsJSON ",somejson));
                        }};

                        System.out.println("params********"+params);
                        addto_cart();

                    }
                    else {
                        params = new ArrayList<Pair<String, String>>() {{
                            add(new Pair<String, String>("deal_id", checkout_deal_id));
                            add(new Pair<String, String>("user_id","0"));
                            add(new Pair<String, String>("device_token",android_id));
                            add(new Pair<String, String>("cust_selected_date", booking_date));
                            add(new Pair<String, String>("cust_selected_time", booking_time));
                            add(new Pair<String, String>("adult_number", String.valueOf(ad_number)));
                            add(new Pair<String, String>("child_number",String.valueOf(ch_number)));
                            add(new Pair<String, String>("quantity", String.valueOf(people)));
                            add(new Pair<String, String>("comment", str_comnt));
                            add(new Pair<String, String>("amount", String.valueOf(total/people)));
                            add(new Pair<String, String>("deal_optionsJSON ",somejson));

                        }};
                        System.out.println("params********"+params);
                        addto_cart1();
                    }



                }

            }
        });

        schedule();

        Calendar instance1 = Calendar.getInstance();

        Calendar endOfNextMonth = Calendar.getInstance();
        endOfNextMonth.set(Calendar.DAY_OF_MONTH, 1);
        endOfNextMonth.add(Calendar.MONTH, 3); // Jump two months ahead
        endOfNextMonth.add(Calendar.DATE, -1);



        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");


                if (scheduleModelArrayList != null && !scheduleModelArrayList.isEmpty()) {


                    for (ScheduleModel scheduleModel : scheduleModelArrayList) {
                        System.out.println("date......" + originalFormat.format(date) + "&&&&&" + scheduleModel.getDate());


                        if (originalFormat.format(date).equals(scheduleModel.getDate()))
                        {
                            System.out.println("dateequalll......" + originalFormat.format(date) + "&&&&&" + scheduleModel.getDate());
                            booking_date = scheduleModel.getDate();
                            String str = scheduleModel.getTime().toString().replaceAll("\\[", "").replaceAll("\\]", "");

                            scheduled_date.setText(": "+booking_date);

                            final ArrayList<String> colors= scheduleModel.getTime();
                            final CharSequence cs[] = colors.toArray(new CharSequence[colors.size()]);
                            System.out.println(Arrays.toString(cs)); // [foo, bar, waa]

                            AlertDialog.Builder builder = new AlertDialog.Builder(Booking_DetailsActivity.this);
                            builder.setTitle("Pick a Time");

                            builder.setItems(cs, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // the user clicked on colors[which]
                                    System.out.println("colorr"+cs[which]);
                                    scheduled_time.setText(": "+cs[which]);
                                    booking_time= String.valueOf(cs[which]);
                                }
                            });
                            builder.show();


                        }
                        else
                        {
                            scheduled_date.setText("Not Currently Available");

                        }


                    }
                    scheduled_date.setText(": "+booking_date);
                }
                else {

                }


            }

            @Override
            public void onChangeMonth(int month, int year) {

                String text = "month: " + month + " year: " + year;
                String mnth = String.valueOf(month);

                if (mnth.equals(mnth_remov_zero_max)) {

                    caldroidFragment.getRightArrowButton().setVisibility(View.INVISIBLE);
                    caldroidFragment.getLeftArrowButton().setVisibility(View.VISIBLE);

                } else if (mnth.equals(mnth_remov_zero_min)) {
                    caldroidFragment.getLeftArrowButton().setVisibility(View.INVISIBLE);
                    caldroidFragment.getRightArrowButton().setVisibility(View.VISIBLE);
                } else if (!mnth.equals(mnth_remov_zero_max) && !mnth.equals(mnth_remov_zero_min)) {
                    caldroidFragment.getLeftArrowButton().setVisibility(View.VISIBLE);
                    caldroidFragment.getRightArrowButton().setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {

                }
            }

        };

        caldroidFragment.setCaldroidListener(listener);



    }
    private void addto_cart() {

        progress_bar_explore.setVisibility(View.VISIBLE);
        Fuel.post(URL1+"add_to_cart.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                progress_bar_explore.setVisibility(View.GONE);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    System.out.println("*************result : " + s);
                    String status = jsonObj.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitleText("Item Added Successfully")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();
                                        View v = findViewById(R.id.add_to_cart);
                                        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360f);
                                        animation.setDuration(2000);
                                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation.start();
                                        View v1 = findViewById(R.id.next);
                                        ObjectAnimator animation1 = ObjectAnimator.ofFloat(v1, "rotationX", 0.0f, 360f);
                                        animation1.setDuration(1000);
                                        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation1.start();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                next.setVisibility(View.VISIBLE);
                                                add_to_cart.setVisibility(View.GONE);

                                            }
                                        }, 1000);

                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                    }
                    else {
                        String msg = jsonObj.getString("message");
                        final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.WARNING_TYPE);
                        dialog.setTitleText(msg)
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();
                                        View v = findViewById(R.id.add_to_cart);
                                        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360f);
                                        animation.setDuration(2000);
                                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation.start();
                                        View v1 = findViewById(R.id.next);
                                        ObjectAnimator animation1 = ObjectAnimator.ofFloat(v1, "rotationX", 0.0f, 360f);
                                        animation1.setDuration(1000);
                                        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation1.start();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                next.setVisibility(View.VISIBLE);
                                                add_to_cart.setVisibility(View.GONE);

                                            }
                                        }, 1000);

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
    private void addto_cart1() {

        progress_bar_explore.setVisibility(View.VISIBLE);
        Fuel.post(URL1+"add_to_cart.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                progress_bar_explore.setVisibility(View.GONE);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    System.out.println("*************result : " + s);
                    String status = jsonObj.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitleText("Item Added Successfully")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();
                                        View v = findViewById(R.id.add_to_cart);
                                        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360f);
                                        animation.setDuration(2000);
                                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation.start();
                                        View v1 = findViewById(R.id.next);
                                        ObjectAnimator animation1 = ObjectAnimator.ofFloat(v1, "rotationX", 0.0f, 360f);
                                        animation1.setDuration(1000);
                                        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation1.start();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                next.setVisibility(View.VISIBLE);
                                                add_to_cart.setVisibility(View.GONE);

                                            }
                                        }, 1000);

                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                    }
                    else {
                        String msg = jsonObj.getString("message");
                        final SweetAlertDialog dialog = new SweetAlertDialog(Booking_DetailsActivity.this,SweetAlertDialog.WARNING_TYPE);
                        dialog.setTitleText(msg)
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();
                                /*        View v = findViewById(R.id.add_to_cart);
                                        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "rotationX", 0.0f, 360f);
                                        animation.setDuration(2000);
                                        animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation.start();
                                        View v1 = findViewById(R.id.next);
                                        ObjectAnimator animation1 = ObjectAnimator.ofFloat(v1, "rotationX", 0.0f, 360f);
                                        animation1.setDuration(1000);
                                        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
                                        animation1.start();
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {
                                                next.setVisibility(View.VISIBLE);
                                                add_to_cart.setVisibility(View.GONE);

                                            }
                                        }, 1000);*/

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

    private void item() {

        progress_bar_explore.setVisibility(View.VISIBLE);
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i) {
            Fuel.post(URL1+"deal-options.php",params_item).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                    progress_bar_explore.setVisibility(View.GONE);
                    System.out.println("*************s: " + s);
                    try {

                        JSONObject  jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("true")) {
                            String data = jsonObject.getString("data");
                            JSONArray jsonArray = new JSONArray(data);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject obj = jsonArray.getJSONObject(j);
                                String opt_label = obj.getString("opt_label");
                                String opt_values = obj.getString("opt_values");
                                System.out.println("Upcoming resulttype : " + opt_values);
                                Im=new ItemModel();
                                Im.setname(opt_label);
                                Im.settype(opt_values);
                                item_list.add(Im);
                                itemAdapter = new ItemAdapter(item_list, getApplicationContext());
                                recyclerView.scheduleLayoutAnimation();
                                recyclerView.setAdapter(itemAdapter);
                                recyclerView.setHasFixedSize(true);

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

          /*  Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/deal-options.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Upcoming result : " + response);
                            try {
                                   Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

                                    JSONObject  jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("true")) {
                                        String data = jsonObject.getString("data");
                                        JSONArray jsonArray = new JSONArray(data);
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject obj = jsonArray.getJSONObject(j);
                                            String opt_label = obj.getString("opt_label");
                                            String opt_values = obj.getString("opt_values");
                                            System.out.println("Upcoming resulttype : " + opt_values);
                                            Im=new ItemModel();
                                            Im.setname(opt_label);
                                            Im.settype(opt_values);
                                            item_list.add(Im);
                                            itemAdapter = new ItemAdapter(item_list, getApplicationContext());
                                            recyclerView.scheduleLayoutAnimation();
                                            recyclerView.setAdapter(itemAdapter);
                                            recyclerView.setHasFixedSize(true);

                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deal_id", checkout_deal_id);
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);*/
        }
        else {
            final SweetAlertDialog dialog = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
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


    public void schedule() {
        progress_bar_explore.setVisibility(View.VISIBLE);
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {


            String Schedule_url = com.meridian.dateout.Constants.URL+"dealschedule.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null && !response.isEmpty()) {

                                System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);

                                progress_bar_explore.setVisibility(View.GONE);
                                try {


                                    JSONObject obj = new JSONObject(response);
                                    {
                                        scheduleModelArrayList = new ArrayList<ScheduleModel>();
                                        DisableModelArrayList = new ArrayList<ScheduleModel>();

                                        if (obj.has("Enabled Dates") && !obj.getString("Enabled Dates").equals("null")) {
                                            try {
                                                JSONArray jsonarray = obj.getJSONArray("Enabled Dates");


                                                for (int i = 0; i < jsonarray.length(); i++) {
                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                    ScheduleModel scheduleModel = new ScheduleModel();
                                                    arrayListtime = new ArrayList<>();


                                                    String date = jsonobject.getString("date");
                                                    System.out.println("date...enabled......+" + date);
                                                    String day = jsonobject.getString("day");
                                                    String month = jsonobject.getString("month");
                                                    String year = jsonobject.getString("year");
                                                    if (jsonobject.has("dayscount")) {
                                                        String dayscount = jsonobject.getString("dayscount");
                                                        scheduleModel.setDayscount(dayscount);
                                                    }
                                                    JSONArray time = jsonobject.getJSONArray("time");
                                                    for (int j = 0; j < time.length(); j++) {

                                                        scheduleModel.setStr_time(time.getString(j));
                                                        arrayListtime.add(time.getString(j));
                                                    }
                                                    scheduleModel.setDate(date);
                                                    scheduleModel.setDay(day);
                                                    scheduleModel.setMonth(month);
                                                    scheduleModel.setYear(year);
                                                    scheduleModel.setTime(arrayListtime);

                                                    scheduleModelArrayList.add(scheduleModel);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else

                                        {
                                            caldroidFragment.refreshView();
                                            ArrayList<Date> disabledDates_fromstart1 = new ArrayList<>();
                                            disabledDates_fromstart1 = getDates(FORMATTED_MINDATE, FORMATTED_MAXDATE);
                                            caldroidFragment.setDisableDates(disabledDates_fromstart1);
                                        }
                                        if (obj.has("Disabled Dates") && !obj.getString("Disabled Dates").equals("null")) {
                                            try {

                                                JSONArray jsonarray = obj.getJSONArray("Disabled Dates");

                                                for (int i = 0; i < jsonarray.length(); i++) {
                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                    ScheduleModel scheduleModel = new ScheduleModel();
                                                    arrayListtime = new ArrayList<>();


                                                    String date = jsonobject.getString("date");
                                                    System.out.println("date...disabled........+" + date);

                                                    String day = jsonobject.getString("day");
                                                    String month = jsonobject.getString("month");
                                                    String year = jsonobject.getString("" +
                                                            "year");
                                                    if (jsonobject.has("dayscount"))
                                                    {
                                                        String dayscount = jsonobject.getString("dayscount");
                                                        scheduleModel.setDayscount(dayscount);
                                                    }
                                                    scheduleModel.setDate(date);
                                                    DisableModelArrayList.add(scheduleModel);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }


                                    if (scheduleModelArrayList != null && !scheduleModelArrayList.isEmpty()) {


                                        //disabledDates_addall = new ArrayList<Date>();


                                        System.out.println("schedulemodel...getdate....." + scheduleModelArrayList.get(0).getDate() + "FORMATTED_MINDATE...." + FORMATTED_MINDATE);
                                        System.out.println("schedulemodel...getdatelast....." + scheduleModelArrayList.get(scheduleModelArrayList.size() - 1).getDate() + "FORMATTED_MAXDATE....." + FORMATTED_MAXDATE);

                                        String dateconvert_part1 = formatDate(scheduleModelArrayList.get(0).getDate(), "yyyy-MM-dd", "EE MMM dd HH:mm:ss z yyyy");
                                        DateFormat formatter = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                        Date date = formatter.parse(dateconvert_part1);
                                        String dateconvert_part2 = formatDate(scheduleModelArrayList.get(scheduleModelArrayList.size() - 1).getDate(), "yyyy-MM-dd", "EE MMM dd HH:mm:ss z yyyy");
                                        DateFormat formatter2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                        Date date2 = formatter2.parse(dateconvert_part2);


                                        System.out.println("dateconvertd.................." + date);


                                        System.out.println("dateconvertd1.................." + date2);


                                        //  if(DisableModelArrayList!=null&&DisableModelArrayList.isEmpty()){


                                        disabledDates_addall = new ArrayList<Date>();
                                        disabledDates_fromstart = new ArrayList<Date>();
                                        disabledDates_fromend = new ArrayList<Date>();
                                        disabledDates_fromjson = new ArrayList<Date>();

                                        System.out.println("schedulemodel...getdate....." + scheduleModelArrayList.get(0).getDate() + "FORMATTED_MINDATE...." + FORMATTED_MINDATE);

                                        String[] partsfrst = scheduleModelArrayList.get(0).getDate().split("-");


                                        String year0 = partsfrst[0]; // 004
                                        String mnth0 = partsfrst[1];
                                        String day0 = partsfrst[2];

                                        int s = Integer.parseInt(day0) - 1;

                                        String p = year0 + "-" + mnth0 + "-" + s;

                                        String[] partsland = scheduleModelArrayList.get(scheduleModelArrayList.size() - 1).getDate().split("-");


                                        String year1 = partsland[0]; // 004
                                        String mnth1 = partsland[1];
                                        String day1 = partsland[2];
                                        String mnthremov_zero= mnth1.replaceFirst("0","");
                                        int s1 = Integer.parseInt(day1) + 1;

                                        String p1 = year1 + "-" + mnth1 + "-" + s1;


                                        System.out.println("schedulemodel...getdatelast2222....." +p+ "...." + scheduleModelArrayList.get(scheduleModelArrayList.size() - 1).getDate() + "FORMATTED_MAXDATE....." + FORMATTED_MAXDATE);
                                        disabledDates_fromstart = getDates(FORMATTED_MINDATE, p);
                                        disabledDates_fromend = getDates(p1, FORMATTED_MAXDATE);
                                        System.out.println("disabledDates_fromstart.........." + disabledDates_fromstart);
                                        System.out.println("disabledDates_fromend.........." + disabledDates_fromend);
                                        disabledDates_addall.addAll(disabledDates_fromstart);
                                        disabledDates_addall.addAll(disabledDates_fromend);
                                        if (DisableModelArrayList != null && !DisableModelArrayList.isEmpty()) {
                                            for (ScheduleModel scheduleModel : DisableModelArrayList) {
                                                String dateconvert_part3 = formatDate(scheduleModel.getDate(), "yyyy-MM-dd", "EE MMM dd HH:mm:ss z yyyy");
                                                DateFormat formatter3 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                                Date dates = formatter3.parse(dateconvert_part3);
                                                System.out.println("date____disabled in json.................." + dates);

                                                disabledDates_fromjson.add(dates);
                                                caldroidFragment.setTextColorForDate(R.color.caldroid_darker_gray, dates);
                                            }
                                            if (disabledDates_fromjson != null) {
                                                disabledDates_addall.addAll(disabledDates_fromjson);
                                            }
                                        }

                                        caldroidFragment.refreshView();

                                        caldroidFragment.setSelectedDates(date, date2);
                                        if (disabledDates_addall != null) {
                                            caldroidFragment.setDisableDates(disabledDates_addall);
                                        }




                                    }




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deal_id", String.valueOf(deal_id));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws
            ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    public static ArrayList<Date> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void displayPopup() {

        try {
            System.out.println("inside display popup");
            mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);


            mPopupWindow.showAtLocation(booking_details_new, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayPopup_comment() {

        try {
            System.out.println("inside display popup");
            mPopupWindow_cmnt = new PopupWindow(customView_comment, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow_cmnt.setElevation(5.0f);
            }
            mPopupWindow_cmnt.setFocusable(true);
            mPopupWindow_cmnt.setAnimationStyle(R.style.popupAnimation);


            mPopupWindow_cmnt.showAtLocation(booking_details_new, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
