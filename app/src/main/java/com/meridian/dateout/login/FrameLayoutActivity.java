package com.meridian.dateout.login;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.account.AccountFragment;
import com.meridian.dateout.account.ChangePassword;
import com.meridian.dateout.account.Faqfragment;
import com.meridian.dateout.account.FavrtFragment;
import com.meridian.dateout.account.NotificationFragment;
import com.meridian.dateout.account.UpdateProfile;
import com.meridian.dateout.account.WalletFragment;
import com.meridian.dateout.account.order.OrderHistory;
import com.meridian.dateout.account.order.Pasthistory;
import com.meridian.dateout.account.order.Upcominghistory;
import com.meridian.dateout.chat.ChatFragment;
import com.meridian.dateout.chat.ChatListingFragment;
import com.meridian.dateout.chat.Email_Submit_Fragment;
import com.meridian.dateout.collections.CategoryDealFragment;
import com.meridian.dateout.collections.CategoryDealFragment1;
import com.meridian.dateout.collections.CollectionsFragment;
import com.meridian.dateout.coupon.CouponFragment;
import com.meridian.dateout.explore.ExploreFragment;
import com.meridian.dateout.explore.RecyclerAdapterCategory;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.fcm.Config;
import com.meridian.dateout.model.CategoryModel;
import com.meridian.dateout.model.DealsModel;
import com.meridian.dateout.model.FacebookModel;
import com.meridian.dateout.nearme.NearMeFragment;
import com.meridian.dateout.reminder.ReminderAddFragment_new;
import com.meridian.dateout.reminder.ReminderMainFragment;
import com.meridian.dateout.rewards.AllRewardsFragment;
import com.meridian.dateout.rewards.MembershipFragment;
import com.meridian.dateout.rewards.MyRewardFragment;
import com.meridian.dateout.rewards.PointHistory;
import com.meridian.dateout.rewards.RewardsMainFragment;
import com.meridian.dateout.sidebar.AboutActivity;
import com.meridian.dateout.sidebar.ContactUsActivity;
import com.meridian.dateout.sidebar.PrivacyActivity;
import com.meridian.dateout.sidebar.TermsOfUseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.analytics;

public class FrameLayoutActivity extends AppCompatActivity implements Pasthistory.OnFragmentInteractionListener,Upcominghistory.OnFragmentInteractionListener,ReminderAddFragment_new.OnFragmentInteractionListener,ReminderMainFragment.OnFragmentInteractionListener,Email_Submit_Fragment.OnFragmentInteractionListener,OrderHistory.OnFragmentInteractionListener,PointHistory.OnFragmentInteractionListener,MembershipFragment.OnFragmentInteractionListener,CouponFragment.OnFragmentInteractionListener,ChangePassword.OnFragmentInteractionListener,FavrtFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener, RewardsMainFragment.OnFragmentInteractionListener, ChatListingFragment.OnFragmentInteractionListener, ChatFragment.OnFragmentInteractionListener, WalletFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, CategoryDealFragment.OnFragmentInteractionListener, CategoryDealFragment1.OnFragmentInteractionListener, ExploreFragment.OnFragmentInteractionListener, CollectionsFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, Faqfragment.OnFragmentInteractionListener,NearMeFragment.OnFragmentInteractionListener,MyRewardFragment.OnFragmentInteractionListener,AllRewardsFragment.OnFragmentInteractionListener,UpdateProfile.OnFragmentInteractionListener {
    RecyclerView recyclerView;
    String id, id1, category, background, icon;
    String str_fullname, str_email, str_fullname1, str_email1, photo, str_pic,str_emails;
    Boolean doubleBackToExitPressedOnce=false;
    ArrayList<CategoryModel> categoryModelArrayList;
    ArrayList<DealsModel> dealsModelArrayList;
    public static ImageView img_account, img_collections, img_explore, img_chat,img_nearme,img_rewrd_name;
    public static LinearLayout lay_account, lay_chat, lay_explore, lay_collections,lay_nearme,lay_rewrd_name;
    String title;
    String image;
    String description;
    String discount;
    GoogleApiClient mGoogleApiClient;
    String timing;
    String delivery;
    String category1;
    String tags;
    String seller_id;
    View customView;
    static TextView img_top_places;
    ArrayList<String> all_background;
    static ImageView back;
    PopupWindow  mPopupWindow ;
    ProgressBar progress;
    TextView txt_name;
    public static TextView img_toolbar_crcname;
    public static ImageView img_top_cal;
    public static ImageView img_top_faq;
    public static TextView txt_explorenam, txt_chat_name, txt_collctnz_nam, txt_accnt_name,txt_nearme,txt_rewrdname;
    RecyclerAdapterCategory recyclerAdapterCategory;
    ImageView img_country;
    LinearLayout lin_recycler;
    public static LinearLayout tabbar;
    public static Toolbar toolbar;
    SharedPreferences sharedfb;
    String str_fulname_fb,personName,email,profileid,personPhoto,go,fb,personName_fb,email_fb,profileid_fb,personPhoto_fb;
    public static BitmapDrawable bitmapdraw_home;
    public static BitmapDrawable bitmapdraw_me;
    public static BitmapDrawable bitmapdraw_search;
    public static String to_notification="false";
    FragmentManager fragmentManager;
    BroadcastReceiver    mReceiver;
    String token;
    String extras,remindtype,remindtitle,reminddetails,rem_type,rem_title,rem_date,rem_time;
    ImageView  popup_close_details;
    TextView  popup_txt_title,popup_txt_description;
    public static LinearLayout activity_frame_layout;
    public static LinearLayout explore_toolbar_lay,nearby_toolbar_lay;
    public static ImageView search_nearby,my_location,filter,cart;
    public static PlaceAutocompleteFragment places;
    LinearLayout linear_popup_ok;
    TextView txt_popup_date,txt_popup_time,txt_popup_type,txt_popup_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        analytics = FirebaseAnalytics.getInstance(FrameLayoutActivity.this);
        analytics.setCurrentScreen(FrameLayoutActivity.this,"Homepage",FrameLayoutActivity.this.getLocalClassName() /* class override */);

        token  =pref.getString("regId", null);
        System.out.println("tokennnnn"+token);
        activity_frame_layout=(LinearLayout)findViewById(R.id.activity_frame_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        img_toolbar_crcname = (TextView) findViewById(R.id.toolbar_CRCNAM);
        img_top_faq = (ImageView) findViewById(R.id.img_top_faq);
        txt_accnt_name = (TextView) findViewById(R.id.txt_accntname);
        txt_rewrdname = (TextView) findViewById(R.id.txt_rewrdname);
        txt_chat_name = (TextView) findViewById(R.id.txt_chatname);
        txt_collctnz_nam = (TextView) findViewById(R.id.txt_collectnsname);
        txt_explorenam = (TextView) findViewById(R.id.txt_explorename);
        txt_nearme=(TextView)findViewById(R.id.txt_nearme);
        explore_toolbar_lay=(LinearLayout)findViewById(R.id.explore_toolbar_lay);
        search_nearby=(ImageView) findViewById(R.id.search_nearby);
        my_location=(ImageView)findViewById(R.id.my_location);
        filter=(ImageView)findViewById(R.id.filter);
        cart=(ImageView)findViewById(R.id.cart);
        places=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        img_account = (ImageView) findViewById(R.id.img_account);
        img_chat = (ImageView) findViewById(R.id.img_chat);
        img_nearme=(ImageView)findViewById(R.id.img_nearme);
        img_collections = (ImageView) findViewById(R.id.img_collections);
        img_rewrd_name = (ImageView) findViewById(R.id.img_rewrd);
        lay_collections = (LinearLayout) findViewById(R.id.lay_collections);
        lay_rewrd_name= (LinearLayout) findViewById(R.id.lay_reward);
        tabbar = (LinearLayout) findViewById(R.id.tabbar);
        img_explore = (ImageView) findViewById(R.id.img_explore);
        personName=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        profileid=getIntent().getStringExtra("profileid");
        personPhoto=getIntent().getStringExtra("personPhoto");
        personName_fb=getIntent().getStringExtra("name_fb");
        email_fb=getIntent().getStringExtra("email_fb");
        profileid_fb=getIntent().getStringExtra("profileid_fb");
        personPhoto_fb=getIntent().getStringExtra("personPhoto_fb");

        LayoutInflater inflater2 = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater2.inflate(R.layout.layout_details_reminder, null);
        popup_close_details=(ImageView)customView.findViewById(R.id.close_point_converter);
        txt_popup_date=(TextView) customView.findViewById(R.id.txt_popup_date);
        txt_popup_time=(TextView)customView.findViewById(R.id.txt_popup_time);
        txt_popup_type=(TextView)customView.findViewById(R.id.txt_popup_type);
        txt_popup_title=(TextView)customView.findViewById(R.id.txt_popup_title);
        linear_popup_ok=(LinearLayout) customView.findViewById(R.id.linear_popup_ok);
        popup_txt_title=(TextView) customView.findViewById(R.id.popup_txt_title);
        popup_txt_description=(TextView)customView.findViewById(R.id.popup_txt_description);
        if(getIntent().getStringExtra("type")!=null)
        {  rem_type=getIntent().getStringExtra("type");
            txt_popup_type.setText(rem_type);


            if(getIntent().getStringExtra("date")!=null)
            {  rem_date=getIntent().getStringExtra("date");
                txt_popup_date.setText("Date : "+rem_date);
            }
            if(getIntent().getStringExtra("time")!=null)
            {  rem_time=getIntent().getStringExtra("time");
                txt_popup_time.setText("Time : "+rem_time);
            }
            if((getIntent().getStringExtra("details")!=null))
            {
                reminddetails=getIntent().getStringExtra("details");
                popup_txt_description.setText(reminddetails);
                if(reminddetails.trim().length()==0){
                    popup_txt_description.setHint("No Details Given");
                }

            }


            if(getIntent().getStringExtra("title")!=null)
            {  rem_title=getIntent().getStringExtra("title");
                txt_popup_title.setText(rem_title);
            }

            System.out.println("usergmail___details...."+reminddetails);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    displayPopup();
                }
            }, 1000);
        }



        linear_popup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });




        popup_close_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });


        go=getIntent().getStringExtra("go");
        fb=getIntent().getStringExtra("fb");
        System.out.println("usergmail___details...."+personName+email+personPhoto+profileid);
        Intent notifyIntent = getIntent();
        extras = getIntent().getStringExtra("key");
        img_top_faq.setVisibility(View.GONE);

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        bitmapdraw_home=(BitmapDrawable)getResources().getDrawable(R.drawable.gift);
        bitmapdraw_me=(BitmapDrawable)getResources().getDrawable(R.drawable.meeeee);
        bitmapdraw_search=(BitmapDrawable)getResources().getDrawable(R.drawable.marker1);


        SharedPreferences preferences_user = getSharedPreferences("MyPref", MODE_PRIVATE);
        String  user_id=preferences_user.getString("user_id",null);
        System.out.println("=================== userid : "+user_id);
        SharedPreferences preferences =getSharedPreferences("MyPref", MODE_PRIVATE);
        str_fullname = preferences.getString("fullname", null);
        str_email = preferences.getString("email", null);
        photo = preferences.getString("photo", null);

        SharedPreferences preferencesfb = getSharedPreferences("myfb", MODE_PRIVATE);
        str_emails = preferencesfb.getString("emails", null);
        String  str_names = preferencesfb.getString("names", null);


        SharedPreferences preferences1 = getSharedPreferences("value_google_user", MODE_PRIVATE);

        str_fullname1 = preferences1.getString("name", null);
        str_email1 = preferences1.getString("email", null);
        String google_photo = preferences1.getString("pic", null);



        FacebookSdk.sdkInitialize(getApplicationContext());
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {

            }
        };
        profileTracker.startTracking();
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            String firstName = profile.getFirstName();
            String lastName = profile.getLastName();
            str_fulname_fb = firstName + lastName;
            System.out.println("name" + firstName + lastName);
            photo=personPhoto_fb;
        }


        if (getIntent().getStringExtra("fragment") != null) {

            String S = getIntent().getStringExtra("fragment");
            if (S != null && S.contentEquals("category") && !S.isEmpty()) {

            }

        } else {
            if (personName != null | email != null | profileid != null | personPhoto != null) {
                if (personPhoto == null) {
                    personPhoto = "";
                }
                photo = personPhoto;

                recycler_inflate_google(personName, email, profileid, personPhoto);
                System.out.println("usergmail info___details...." + personName + email + personPhoto + profileid);
            }
            if (personName_fb != null | email_fb != null | profileid_fb != null | personPhoto_fb != null) {
                photo = personPhoto_fb;

                recycler_inflate_fb(personName_fb, email_fb, profileid_fb, personPhoto_fb);
                System.out.println("userfb info___details...." + personName_fb + email_fb + personPhoto_fb + profileid_fb);
            }

            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (mReceiver != null) {
                        if (extras != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                          //  transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                            Fragment fragment = NotificationFragment.newInstance();
                            Bundle args = new Bundle();
                            args.putString("not", "not");
                            fragment.setArguments(args);
                            transaction.replace(R.id.flFragmentPlaceHolder, fragment, "not").addToBackStack("not");
                            transaction.commit();
                            System.out.println("broad....extrasssnulll" +extras);

                        }
                        else {

                            replacefragment(ExploreFragment.newInstance(), "e");
                            System.out.println("broad....extrasss...not..nulll"+extras );
                        }
                    }
                    else {

                        replacefragment(ExploreFragment.newInstance(), "e");
                        System.out.println("broad....mreceiever..notnull"+extras );
                    }


                }
            };
            LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
                    new IntentFilter("broadcaster"));
        }

        if(mReceiver!=null) {
            if (extras != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

                Fragment fragment = NotificationFragment.newInstance();
                Bundle args = new Bundle();
                args.putString("not", "not");
                fragment.setArguments(args);
                transaction.replace(R.id.flFragmentPlaceHolder, fragment, "not").addToBackStack("not");
                transaction.commit();
                System.out.println("broad....extrasssnulll"+extras );

            }
            else {

                replacefragment(ExploreFragment.newInstance(), "e");
                System.out.println("broad....extrasss...not..nulll"+extras );
            }

            System.out.println("broad....mreceiever outsidee..notnull" );
        }
        else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            Fragment fragment = NotificationFragment.newInstance();
            Bundle args = new Bundle();
            args.putString("not", "not");
            fragment.setArguments(args);
            transaction.replace(R.id.flFragmentPlaceHolder, fragment, "not").addToBackStack("not");
            transaction.commit();

            System.out.println("broad....mreceiever..outside.null"+extras );
        }


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        if ((str_fulname_fb!= null)||(str_fullname != null && str_email != null)||(str_fullname1 != null && str_email1 != null ))
        {


            menu.findItem(R.id.nav_LOGIN).setTitle("LOGOUT");
            menu.findItem(R.id.nav_LOGIN).setIcon(R.drawable.sign_out);
            menu.findItem(R.id.nav_REWARDS).setVisible(true);

        }
        else
        {
            menu.findItem(R.id.nav_LOGIN).setIcon(R.drawable.log_in);
            menu.findItem(R.id.nav_REWARDS).setVisible(true);

            menu.findItem(R.id.nav_LOGIN).setTitle("LOGIN");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        lay_rewrd_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_rewrd_name.performClick();

            }
        });

        lay_collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_collections.performClick();

            }
        });
        lay_explore = (LinearLayout) findViewById(R.id.lay_explore);
        lay_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_explore.performClick();

            }
        });
        lay_chat = (LinearLayout) findViewById(R.id.lay_chat);
        lay_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_chat.performClick();

            }
        });
        lay_account = (LinearLayout) findViewById(R.id.lay_account);
        lay_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_account.performClick();

            }
        });

        lay_nearme=(LinearLayout)findViewById(R.id.lay_nearme);
        lay_nearme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                img_nearme.performClick();
            }
        });


        img_account.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {



                if ((str_fulname_fb!= null && str_emails != null)||(str_fullname != null && str_email != null)||(str_fullname1 != null && str_email1 != null )) {
                    try {

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        System.out.println("fragment by id : " + fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder));
                        String current_fragment = fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().substring(0, fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().indexOf("{"));
                        System.out.println("current_fragment : " + current_fragment);

                        if (!current_fragment.trim().equalsIgnoreCase("AccountFragment")) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                            anim.setDuration(500); // Duration in milliseconds
                            anim.setInterpolator(new LinearInterpolator()); // E.g. Linear, Accelerate, Decelerate
                            anim.start();
                            img_account.setBackgroundResource(R.drawable.account);
                            txt_accnt_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                            img_chat.setBackgroundResource(R.drawable.email_blue);
                            txt_chat_name.setTextColor(getResources().getColor(R.color.black));
                            img_explore.setBackgroundResource(R.drawable.explore_click);
                            txt_explorenam.setTextColor(getResources().getColor(R.color.black));
                            img_collections.setBackgroundResource(R.drawable.collections_click);
                            txt_collctnz_nam.setTextColor(getResources().getColor(R.color.black));
                            img_nearme.setBackgroundResource(R.drawable.near_by);
                            img_rewrd_name.setBackgroundResource(R.drawable.reward_blue);
                            txt_rewrdname.setTextColor(getResources().getColor(R.color.black));
                            txt_nearme.setTextColor(getResources().getColor(R.color.black));
                            replacefragment(AccountFragment.newInstance(), "ac");

                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        img_account.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                                    }
                                }
                            };

                            img_account.animate().rotationBy(360).withEndAction(runnable).setDuration(1000).setInterpolator(new LinearInterpolator()).cancel();

                        }

                    }
                    catch (Exception e)
                    {

                    }
                }


                else {
                    try {


                        final SweetAlertDialog dialog = new SweetAlertDialog(FrameLayoutActivity.this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("LOGIN").
                                setContentText("Please login to avail this feature")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        dialog.dismiss();
                                        Intent i = new Intent(FrameLayoutActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }
                                })
                                .setCancelText("CANCEL")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                    }
                    catch (Exception e)
                    {

                    }
                }




            }
        });
        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    System.out.println("fragment by id : " + fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder));
                    String current_fragment = fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().substring(0, fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().indexOf("{"));
                    System.out.println("current_fragment : " + current_fragment);


                    if (!current_fragment.trim().equalsIgnoreCase("ChatFragment")) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        anim.setDuration(500); // Duration in milliseconds
                        anim.setInterpolator(new LinearInterpolator()); // E.g. Linear, Accelerate, Decelerate
                        anim.start();
                        img_chat.setBackgroundResource(R.drawable.email_black);
                        txt_chat_name.setTextColor(getResources().getColor(R.color.black));
                        img_explore.setBackgroundResource(R.drawable.explore);
                        txt_explorenam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_collections.setBackgroundResource(R.drawable.collections);
                        txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_account.setBackgroundResource(R.drawable.account);
                        txt_accnt_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_nearme.setBackgroundResource(R.drawable.near_by_blue);
                        img_rewrd_name.setBackgroundResource(R.drawable.reward_blue);
                        txt_rewrdname.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        txt_nearme.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        replacefragment(ChatFragment.newInstance(), "ch");
                        img_top_faq.setVisibility(View.GONE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                    }
                }
                catch (Exception e)
                {

                }

            }
        });
        img_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    System.out.println("fragment by id : " + fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder));
                    String current_fragment = fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().substring(0, fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().indexOf("{"));
                    System.out.println("current_fragment : " + current_fragment);

                    if (!current_fragment.trim().equalsIgnoreCase("ExploreFragment")) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        anim.setDuration(500); // Duration in milliseconds
                        anim.setInterpolator(new LinearInterpolator()); // E.g. Linear, Accelerate, Decelerate
                        anim.start();
                        img_explore.setBackgroundResource(R.drawable.explore);
                        txt_explorenam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_collections.setBackgroundResource(R.drawable.collections_click);
                        txt_collctnz_nam.setTextColor(getResources().getColor(R.color.black));
                        img_account.setBackgroundResource(R.drawable.account_click);
                        txt_accnt_name.setTextColor(getResources().getColor(R.color.black));
                        img_chat.setBackgroundResource(R.drawable.email_blue);
                        txt_chat_name.setTextColor(getResources().getColor(R.color.black));
                        img_nearme.setBackgroundResource(R.drawable.near_by);
                        txt_nearme.setTextColor(getResources().getColor(R.color.black));
                        img_rewrd_name.setBackgroundResource(R.drawable.reward_blue);
                        txt_rewrdname.setTextColor(getResources().getColor(R.color.black));
                        img_top_faq.setVisibility(View.GONE);
                        replacefragment(ExploreFragment.newInstance(), "e");
                        System.out.println("framelayout_shared_clickimage" + str_fulname_fb);
                    }
                }
                catch (Exception e)

                {

                }

            }
        });
        img_rewrd_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    System.out.println("fragment by id : " + fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder));
                    String current_fragment = fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().substring(0, fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().indexOf("{"));
                    System.out.println("current_fragment : " + current_fragment);


                    if (!current_fragment.trim().equalsIgnoreCase("RewardsMainFragment")) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        anim.setDuration(500); // Duration in milliseconds
                        anim.setInterpolator(new LinearInterpolator()); // E.g. Linear, Accelerate, Decelerate
                        anim.start();
                        img_rewrd_name.setBackgroundResource(R.drawable.reward);
                        txt_rewrdname.setTextColor(getResources().getColor(R.color.black));
                        img_collections.setBackgroundResource(R.drawable.collections);
                        txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_account.setBackgroundResource(R.drawable.account);
                        txt_accnt_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_chat.setBackgroundResource(R.drawable.email_blue);
                        txt_chat_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_explore.setBackgroundResource(R.drawable.explore);
                        txt_explorenam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_nearme.setBackgroundResource(R.drawable.near_by_blue);
                        txt_nearme.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_top_faq.setVisibility(View.GONE);
                        replacefragment(RewardsMainFragment.newInstance(), "reward");
                    }
                }
                catch ( Exception e)
                {

                }

            }
        });
        img_collections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    System.out.println("fragment by id : " + fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder));
                    String current_fragment = fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().substring(0, fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().indexOf("{"));
                    System.out.println("current_fragment : " + current_fragment);

                    if (!current_fragment.trim().equalsIgnoreCase("CollectionsFragment")) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        anim.setDuration(500); // Duration in milliseconds
                        anim.setInterpolator(new LinearInterpolator()); // E.g. Linear, Accelerate, Decelerate
                        anim.start();
                        img_collections.setBackgroundResource(R.drawable.collections);
                        txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_account.setBackgroundResource(R.drawable.account_click);
                        txt_accnt_name.setTextColor(getResources().getColor(R.color.black));
                        img_chat.setBackgroundResource(R.drawable.email_blue);
                        txt_chat_name.setTextColor(getResources().getColor(R.color.black));
                        img_explore.setBackgroundResource(R.drawable.explore_click);
                        txt_explorenam.setTextColor(getResources().getColor(R.color.black));
                        img_nearme.setBackgroundResource(R.drawable.near_by);
                        txt_nearme.setTextColor(getResources().getColor(R.color.black));
                        img_rewrd_name.setBackgroundResource(R.drawable.reward_blue);
                        txt_rewrdname.setTextColor(getResources().getColor(R.color.black));
                        img_top_faq.setVisibility(View.GONE);
                        replacefragment(CollectionsFragment.newInstance(), "co");
                    }
                }
                catch ( Exception e)
                {

                }

            }
        });
        img_nearme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    System.out.println("fragment by id : " + fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder));
                    String current_fragment = fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().substring(0, fragmentManager.findFragmentById(R.id.flFragmentPlaceHolder).toString().indexOf("{"));
                    System.out.println("current_fragment : " + current_fragment);

                    if (!current_fragment.trim().equalsIgnoreCase("NearMeFragment")) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
                        anim.setDuration(500); // Duration in milliseconds
                        anim.setInterpolator(new LinearInterpolator()); // E.g. Linear, Accelerate, Decelerate
                        anim.start();
                        img_nearme.setBackgroundResource(R.drawable.near_by_blue);
                        txt_nearme.setTextColor(getResources().getColor(R.color.txtcolor_icons));
                        img_explore.setBackgroundResource(R.drawable.explore_click);
                        txt_explorenam.setTextColor(getResources().getColor(R.color.black));
                        img_collections.setBackgroundResource(R.drawable.collections_click);
                        txt_collctnz_nam.setTextColor(getResources().getColor(R.color.black));
                        img_account.setBackgroundResource(R.drawable.account_click);
                        txt_accnt_name.setTextColor(getResources().getColor(R.color.black));
                        img_chat.setBackgroundResource(R.drawable.email_blue);
                        img_rewrd_name.setBackgroundResource(R.drawable.reward_blue);
                        txt_rewrdname.setTextColor(getResources().getColor(R.color.black));
                        txt_chat_name.setTextColor(getResources().getColor(R.color.black));
                        replacefragment(NearMeFragment.newInstance(), "ne");
                        img_top_faq.setVisibility(View.GONE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                    }
                }
                catch (Exception e)
                {

                }
            }
        });



        toolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View item = toolbar.findViewById(R.id.img_top_faq);

                if (item != null) {
                    toolbar.removeOnLayoutChangeListener(this);

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            img_toolbar_crcname.setText("FAQ");
                            ObjectAnimator animator = ObjectAnimator
                                    .ofFloat(v, "rotation", v.getRotation() + 360);
                            animator.start();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

                            transaction.replace(R.id.flFragmentPlaceHolder, Faqfragment.newInstance(), "f").addToBackStack("f");
                            transaction.commit();

                        }
                    });
                }
            }
        });

    }

    public void replacefragment(Fragment fragment, String s) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.flFragmentPlaceHolder, fragment, s);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_about_us) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.flFragmentPlaceHolder, AboutActivity.newInstance(), "about").addToBackStack("about");
            transaction.commit();

        } else if (id == R.id.nav_contact) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.flFragmentPlaceHolder, ContactUsActivity.newInstance(), "contact").addToBackStack("contact");
            transaction.commit();
        } else if (id == R.id.nav_terms) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.flFragmentPlaceHolder, TermsOfUseActivity.newInstance(), "termsofuse").addToBackStack("termsofuse");
            transaction.commit();

        } else if (id == R.id.nav_privacy) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.flFragmentPlaceHolder,PrivacyActivity.newInstance(), "privacy").addToBackStack("privacy");
            transaction.commit();
        }
        else if (id == R.id.nav_LOGIN) {
            if ((str_fulname_fb!= null)||(str_fullname != null && str_email != null)||(str_fullname1 != null && str_email1 != null )) {
                final SweetAlertDialog dialog = new SweetAlertDialog(FrameLayoutActivity.this, SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Alert!")
                        .setContentText("Do you want to logout?")

                        .setConfirmText("OK")

                        .setCancelText("CANCEL")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismiss();
                            }
                        })


                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                item.setTitle("Login");
                                dialog.dismiss();
                                SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                                preferences.edit().clear().commit();
                                SharedPreferences preferences_fb = getSharedPreferences("myfbid", MODE_PRIVATE);
                                preferences_fb.edit().clear().commit();

                                SharedPreferences preferences2 = getSharedPreferences("myfb", MODE_PRIVATE);
                                preferences2.edit().clear().commit();
                                LoginManager.getInstance().logOut();
                                SharedPreferences preferences_google_id = getSharedPreferences("value_gmail", MODE_PRIVATE);
                                preferences_google_id.edit().clear().commit();
                                SharedPreferences  preferences_google = getSharedPreferences("value_google_user", MODE_PRIVATE);
                                preferences_google.edit().clear().commit();
                                try {

                                    if (str_fullname1 != null && str_email1 != null || Googlelogin.personName != null || Googlelogin.email != null) {
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                                new ResultCallback<Status>() {
                                                    @Override
                                                    public void onResult(Status status) {



                                                    }
                                                });
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .show();

                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));




            }
            else
            {
                item.setTitle("Login");
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }

        }
        else if (id == R.id.nav_REWARDS) {

            if ((str_fulname_fb!= null && str_emails != null)||(str_fullname != null && str_email != null)||(str_fullname1 != null && str_email1 != null )) {
                try {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

                    transaction.replace(R.id.flFragmentPlaceHolder, RewardsMainFragment.newInstance(), "reward").addToBackStack("reward");
                    transaction.commit();
                } catch (Exception e) {

                }
            }
            else {
                try {


                    final SweetAlertDialog dialog = new SweetAlertDialog(FrameLayoutActivity.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("LOGIN").
                            setContentText("Please login to avail this feature")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    dialog.dismiss();
                                    Intent i = new Intent(FrameLayoutActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .setCancelText("CANCEL")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                }
                catch (Exception e)
                {

                }
            }

        }

        else if (id == R.id.nav_CHAT) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.flFragmentPlaceHolder, ChatFragment.newInstance(), "chat").addToBackStack("chat");
            transaction.commit();
        }
        else if (id == R.id.nav_FAQ) {
            img_toolbar_crcname.setText("FAQ");

            FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.flFragmentPlaceHolder, Faqfragment.newInstance(), "f").addToBackStack("f");
            transaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int stackCount = fragmentManager.getBackStackEntryCount();
        if( fragmentManager.getFragments() != null )
            return fragmentManager.getFragments().get( stackCount > 0 ? stackCount-1 : stackCount );
        else
            return null;
    }

    @Override
    public void onBackPressed() {
        System.out.println("open drawerrrrrr");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = getVisibleFragment();
        System.out.println("fragment__"+currentFragment+"tag...."+currentFragment.getTag()+"id..."+currentFragment.getId());
        if(currentFragment.getTag().contentEquals("e")||currentFragment.getTag().contentEquals("reward")||currentFragment.getTag().contentEquals("ch")||currentFragment.getTag().contentEquals("ac")||currentFragment.getTag().contentEquals("co")||currentFragment.getTag().contentEquals("ne"))
        {
            System.out.println("dont pop--------fragment__"+currentFragment+"tag...."+currentFragment.getTag());

            {

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    finish();

                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                com.nispok.snackbar.Snackbar.with(FrameLayoutActivity.this) // context
                        .text("Press again to EXIT")
                        .color(Color.parseColor("#4797c4"))
                        .show(FrameLayoutActivity.this);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                        //MusicService.mp.stop();
                    }
                }, 2000);

            }
        }
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();

            }
        }


    }
    public void recycler_inflate_google(final String name,final String email, final String profile_id,final String photo) {

        String REGISTER_URL = Constants.URL+"registration.php?";
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealdetail_google==== :" + response);


                            try {

                                JSONObject jsonobject = new JSONObject(response);




                                String user_id = jsonobject.getString("user_id");
                                String fullname = jsonobject.getString("fullname");
                                String username = jsonobject.getString("username");
                                String photo = jsonobject.getString("photo");
                                String email = jsonobject.getString("email");
                                String phone = jsonobject.getString("phone");
                                String log_status = jsonobject.getString("log_status");
                                String google_id = jsonobject.getString("google_id");
                                SharedPreferences.Editor editorgm = getApplicationContext().getSharedPreferences("value_gmail", MODE_PRIVATE).edit();
                                editorgm.putString("user_id", user_id);
                                System.out.println("user_id..." + user_id);

                                editorgm.commit();


                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_id", user_id);
                                editor.putString("fullname", fullname);
                                editor.putString("email", email);
                                editor.putString("username", username);
                                editor.putString("photo", photo);
                                editor.putString("phone",phone);
                                editor.putString("device_token",token);
                                editor.commit();


                                SharedPreferences prefs =getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                                boolean Islogin = Boolean.parseBoolean("true");
                                prefs.edit().putBoolean("Islogin", Islogin).commit();

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



                    System.out.println("google_name.........."+name+"profil"+profile_id+"firstname");

                    params.put("name", name);
                    params.put("username", name);
                    params.put("email", email);
                    params.put("password", "nil");
                    params.put("profile_pic",photo);
                    params.put("g_id", profile_id);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        } else {
            final SweetAlertDialog dialog = new SweetAlertDialog(FrameLayoutActivity.this,SweetAlertDialog.NORMAL_TYPE);
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


    public void recycler_inflate_fb( final String names, final String email,final String profile_idfinal,final String photo) {

        String REGISTER_URL = Constants.URL+"registration.php?";
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealdetail :" + response);


                            try {


                                JSONObject jsonobject = new JSONObject(response);


                                FacebookModel facebookModel = new FacebookModel();
                                String  user_id = jsonobject.getString("user_id");
                                String fullname = jsonobject.getString("fullname");
                                String username = jsonobject.getString("username");
                                String photo = jsonobject.getString("photo");
                                String email = jsonobject.getString("email");
                                String phone = jsonobject.getString("phone");
                                String log_status = jsonobject.getString("log_status");
                                String facebook_id = jsonobject.getString("facebook_id");
                                facebookModel.setEmail(email);
                                facebookModel.setFullname(fullname);
                                facebookModel.setFacebook_id(facebook_id);
                                facebookModel.setLog_status(log_status);
                                facebookModel.setUser_id(user_id);
                                facebookModel.setUsername(username);
                                facebookModel.setPhoto(photo);
                                facebookModel.setPhone(phone);

                                System.out.println("user_id" + user_id + fullname);

                                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("user_id", user_id);
                                editor.putString("fullname", fullname);
                                editor.putString("email", email);
                                editor.putString("username", username);
                                editor.putString("photo", photo);
                                editor.putString("phone",phone);
                                editor.putString("device_token",token);
                                editor.commit();



                                SharedPreferences preferencesfb = getSharedPreferences("myfbid", MODE_PRIVATE);
                                SharedPreferences.Editor editorfb = preferencesfb.edit();
                                if (user_id != null) {

                                    editorfb.putString("user_idfb", user_id);
                                }

                                editor.commit();


                                SharedPreferences prefs =getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                                boolean Islogin = Boolean.parseBoolean("true");
                                prefs.edit().putBoolean("Islogin", Islogin).commit();



                            }
                            catch (JSONException e) {
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


                    System.out.println("name" + names + "profil" + profile_idfinal + "firstname" + email);

                    params.put("name",names.replaceAll("\\s+", ""));
                    params.put("username",names.replaceAll("\\s+", ""));
                    params.put("email", email);
                    params.put("password", "nil");
                    params.put("profile_pic", photo);
                    params.put("fb_id", profile_idfinal);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            requestQueue.add(stringRequest);
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(FrameLayoutActivity.this,SweetAlertDialog.NORMAL_TYPE);
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

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = FrameLayoutActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private android.app.FragmentManager.OnBackStackChangedListener getListener()
    {
        android.app.FragmentManager.OnBackStackChangedListener result = new android.app.FragmentManager.OnBackStackChangedListener()
        {
            public void onBackStackChanged()
            {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {

                }
            }
        };

        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            Fragment currentFragment = getVisibleFragment();
            System.out.println("currentFragment : " + currentFragment);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commit();
            }

            Intent intent=getIntent();
            Bundle extras = intent.getExtras();
            String tabNumber;


            if(extras != null) {
                tabNumber = extras.getString("open");
                String    deal_id = extras.getString("deal_id");
                if(deal_id!=null)
                {
                    Intent i = new Intent(getApplicationContext(), CategoryDealDetail.class);

                    i.putExtra("deal_id", deal_id);
                    startActivity(i);
                }
                else {
                    Log.d("TEMP", "Tab Number: " + tabNumber);
                    if (tabNumber.equalsIgnoreCase("account")) {
                        new Handler().post(new Runnable() {
                            public void run() {
                                to_notification = "true";
                                System.out.println("to_notification (in framely activi): " + to_notification);
                                img_account.performClick();
                            }
                        });
                    }
                }
            }
            else {
                Log.d("TEMP", "Extras are NULL");
            }
        }
        catch (Exception e) {

        }

    }
    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
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


            mPopupWindow.showAtLocation(FrameLayoutActivity.activity_frame_layout, Gravity.CENTER, 0, 0);
        }
        catch (InflateException e)
        {
            e.printStackTrace();
        }

    }


}
