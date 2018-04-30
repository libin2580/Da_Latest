package com.meridian.dateout.explore.category_booking_detailspage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
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
import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.login.CaldroidSampleActivity;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.LoginActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.CategoryDealModel;
import com.meridian.dateout.model.ScheduleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.analytics;

public class CategoryDealDetail extends AppCompatActivity implements OnMapReadyCallback {
    int deal_id;
    String id;
    String title;
    String description;
    String discount;
    String timing;
    String delivery;
    String category;
    String tags;
    String seller_id,highlights,latitude,logitude;
    ImageView back;
    AppBarLayout appbar;
    TextView txt_more;
    String tkt_discounted_price;
    String points_to_next_level;
    CollapsingToolbarLayout collapsingToolbar;
    ArrayList<ScheduleModel> scheduleModelArrayList = new ArrayList<>();
    String checkout_info;
    ArrayList<String> arrayListtime, arrayListimage;
    TextView txt_sun, txt_mon, txt_tue, txt_wed, txt_thurs, txt_fri, txt_sat;
    TextView txt_title;
    String deal_type,adult_age_range, adult_tkt_price, child_age_range, child_tkt_price, adult_discount_tkt_price, child_discount_tkt_price,comment_option;
    WebView txt_address;
    WebView txt_description2;
    String  available_point ,deal_points;
    String needtoknow, noteon_tickets, currency;
    SliderLayout sliderShow;
    public static LinearLayout checkout;
    Double lat, longitude;
    String calendar_instruction;
    TextView txt_dtl_price, txt_actual_price,need_to_know_txt;
    String address,inclusion,exclusion;
    LinearLayout schedule_click;
    LinearLayout share;
    ImageView wish;
    ProgressBar progressBar;
    WebView txt_description,need_to_know;
    LinearLayout lin_wish,lin_wish_selected;
    String schedule_status,deeplink;
    ArrayList<CategoryDealModel> categoryDealModelArrayList;
    String price;
    WebView pager_text;
    ImageView imag_new;
    TabLayout tabLayout;
    String userid, deal_slug;
    WebView txt_inclusion, txt_exclusion;
    CoordinatorLayout layout_visible;
    LinearLayout layout_loader;
    TextView deal_title;
    FrameLayout frame_map;
    LinearLayout layout_image,layout_address,layout_needtoknow;
    String REGISTER_URL = Constants.URL+"dealsbyid.php?";
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    NestedScrollView nestedScrollView;

    LinearLayout linear_navigate_me,Cart;
    ImageView Home;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        analytics = FirebaseAnalytics.getInstance(this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        deal_id = getIntent().getIntExtra("deal_id", 0);
        deal_slug = getIntent().getStringExtra("deal_slug");
        deeplink = getIntent().getStringExtra("deeplink");
        if(deeplink!=null){
            REGISTER_URL=deeplink;
            System.out.println("deeplink" + deeplink);
            recycler_inflate1();
        }
        else {
            recycler_inflate();
        }
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        System.out.println("deal_id" + deal_id);
        lin_wish = (LinearLayout) findViewById(R.id.lin_wish);
        lin_wish_selected = (LinearLayout) findViewById(R.id.lin_wish_selected);
        layout_visible= (CoordinatorLayout) findViewById(R.id.main_content_category_detail);
        Cart= (LinearLayout) findViewById(R.id.cart);
        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Cart_details.class);
                startActivity(i);
            }
        });

        layout_loader= (LinearLayout) findViewById(R.id.layout_loader);
        layout_address= (LinearLayout) findViewById(R.id.lay_address);
        layout_image= (LinearLayout) findViewById(R.id.layout_image);
        layout_needtoknow= (LinearLayout) findViewById(R.id.lay_need_to_knw);
        wish = (ImageView) findViewById(R.id.wish_list1);
        progress = (ProgressBar) findViewById(R.id.progress);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_categry_detail);
        need_to_know = (WebView) findViewById(R.id.need_to_know);
        need_to_know_txt = (TextView) findViewById(R.id.need_to_know_txt);
        nestedScrollView= (NestedScrollView) findViewById(R.id.nested_scroll);
        txt_sun = (TextView) findViewById(R.id.txt_sun);
        txt_mon = (TextView) findViewById(R.id.txt_mon);
        txt_tue = (TextView) findViewById(R.id.txt_tue);
        txt_wed = (TextView) findViewById(R.id.txt_wed);
        txt_thurs = (TextView) findViewById(R.id.txt_thurs);
        txt_fri = (TextView) findViewById(R.id.txt_fri);
        txt_sat = (TextView) findViewById(R.id.txt_sat);
        txt_more = (TextView) findViewById(R.id.txt_more);
        checkout = (LinearLayout) findViewById(R.id.checkout);
        pager_text = (WebView) findViewById(R.id.txt_tickt);
        txt_inclusion = (WebView) findViewById(R.id.txt_inclusion);
        txt_exclusion = (WebView) findViewById(R.id.txt_exclusion);
        share = (LinearLayout) findViewById(R.id.share);
        imag_new = (ImageView) findViewById(R.id.img_new);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appbar = (AppBarLayout) findViewById(R.id.appBarLayout);
        scheduleModelArrayList = new ArrayList<>();
        arrayListtime = new ArrayList<>();
        arrayListimage = new ArrayList<>();
        arrayListimage = new ArrayList<>();
        categoryDealModelArrayList = new ArrayList<>();
        txt_description = (WebView) findViewById(R.id.txt_description1);
        txt_description2 = (WebView) findViewById(R.id.txt_description3);
        txt_title = (TextView) findViewById(R.id.txt_title1);
        frame_map= (FrameLayout) findViewById(R.id.frame_map);

        txt_address = (WebView) findViewById(R.id.txt_address);
//        txt_timing = (TextView) findViewById(R.id.txt_timimg1);
        txt_dtl_price = (TextView) findViewById(R.id.txt_dtl_price);
        txt_actual_price = (TextView) findViewById(R.id.txt_actual_price);
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);
        deal_title = (TextView) findViewById(R.id.toolbar_CRCNAM);
        back = (ImageView) findViewById(R.id.img_crcdtlnam);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        Home= (ImageView) findViewById(R.id.home);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  =new Intent(CategoryDealDetail.this, FrameLayoutActivity.class);
                startActivity(i);
            }
        });
        linear_navigate_me=(LinearLayout)findViewById(R.id.linear_navigate_me);


        nestedScrollView.scrollTo(0, 0);


        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String user_id = preferences.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid.....login" + userid);
        }

        SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("myfbid", MODE_PRIVATE);
        String profile_id = preferences1.getString("user_idfb", null);

        if (profile_id != null) {
            userid = profile_id;
            System.out.println("userid......fblogin" + userid);
        }

        SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences2.getString("user_id", null);
        if (profileid_gmail != null) {
            userid = profileid_gmail;
            System.out.println("userid......googlelogin" + userid);
        }
        System.out.println("logged userid........" + userid);

        if (userid != null) {
            lin_wish.setVisibility(View.VISIBLE);

        } else
        {
            lin_wish.setVisibility(View.VISIBLE);
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.white), ContextCompat.getColor(getApplicationContext(), R.color.white));

        schedule();

        progressBar.setVisibility(View.GONE);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                if (tab.getText().equals("MY TICKETS")) {

                    pager_text.setVisibility(View.VISIBLE);
                    txt_inclusion.setVisibility(View.GONE);
                    txt_exclusion.setVisibility(View.GONE);


                } else if (tab.getText().equals("INCLUSION")) {
                    pager_text.setVisibility(View.GONE);
                    txt_inclusion.setVisibility(View.VISIBLE);
                    txt_exclusion.setVisibility(View.GONE);
                }


            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareIt();
            }

            private void shareIt() {
              /*  Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Date Out");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, " ");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out "+"'"+title+"'"+" Deal in DateOut website \n\n" +
                                "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/"+deal_slug);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));*/


               /* Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/null"))
                        .setDynamicLinkDomain("rkag5.app.goo.gl")
                        // Set parameters
                        // ...
                        .buildShortDynamicLink()
                        .addOnCompleteListener(CategoryDealDetail.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();

                                    Log.e("aaaaaaaaaaaaaaa", String.valueOf(shortLink));
                                    Log.e("aaaaaaaaaaaaaaa", String.valueOf(flowchartLink));

                                } else
                                    {
                                    Log.e("aaaaaaaaaaaaaaa","errorloading");
                                }
                            }
                        });*/

                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                       // .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/null"))
                        .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/dealsbyid.php?deal_id="+deal_id))
                        .setDynamicLinkDomain("rkag5.app.goo.gl")
                        // Set parameters
                        // ...
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.meridian.dateout").setFallbackUrl(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/"+deal_slug)).
                                build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder("com.meridian.wildlyyours")
                                .setFallbackUrl(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/"+deal_slug)).
                                        setCustomScheme("shareDeal").
                                build())

                       /* .setGoogleAnalyticsParameters(
                                new DynamicLink.GoogleAnalyticsParameters.Builder()
                                        .setSource("orkut")
                                        .setMedium("social")
                                        .setCampaign("example-promo")
                                        .build())
                        .setItunesConnectAnalyticsParameters(
                                new DynamicLink.ItunesConnectAnalyticsParameters.Builder()
                                        .setProviderToken("123456")
                                        .setCampaignToken("example-promo")
                                        .build())

                        .setSocialMetaTagParameters(
                                new DynamicLink.SocialMetaTagParameters.Builder()
                                        .setTitle("Example of a Dynamic Link")
                                        .setDescription("This link works whether the app is installed or not!")
                                        //.setImageUrl()
                                        .build())*/
                        .buildShortDynamicLink()
                        .addOnCompleteListener(CategoryDealDetail.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();

                                    Log.e("aaaaaaaaaaaaaaa14", String.valueOf(shortLink));
                                    Log.e("aaaaaaaaaaaaaaaa15",String.valueOf(flowchartLink));

                                    Intent in=new Intent();
                                    String msg=flowchartLink.toString();
                                    in.setAction(Intent.ACTION_SEND);
                                    in.putExtra(Intent.EXTRA_TEXT,msg);
                                    in.setType("text/plain");
                                    startActivity(in);

                                } else
                                {
                                    Log.e("aaaaaaaaaaaaaaa16","errorloading");
                                }
                            }
                        });


               /* DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/null"))
                        .setDynamicLinkDomain("rkag5.app.goo.gl")
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.meridian.dateout").build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder("com.meridian.wildlyyours").build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();

                Log.e("aaaaaaaaaaa", String.valueOf(dynamicLinkUri));*/

            }
        });


        schedule_click = (LinearLayout) findViewById(R.id.sch_click);
        schedule_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CaldroidSampleActivity.class);
                i.putExtra("deal_id", deal_id);
                i.putExtra("user_id", userid);
                i.putExtra("time", timing);
                i.putExtra("title", title);
                i.putExtra("deal_type",deal_type);
                i.putExtra("timing",timing);
                i.putExtra("calendar_show",schedule_status);
                i.putExtra("calendar_instruction",calendar_instruction);

                i.putExtra("price", price);
                i.putExtra("tickt_discnt_price", tkt_discounted_price);

                i.putExtra("checkout_info", checkout_info);

                i.putExtra("adult_price", adult_tkt_price);

                i.putExtra("currency", currency);

                i.putExtra("child_price", child_tkt_price);
                i.putExtra("address", address);
                i.putExtra("adult_age", adult_age_range);
                i.putExtra("child_age", child_age_range);

                i.putExtra("adult_discnt_price", adult_discount_tkt_price);

                i.putExtra("child_discnt_price", child_discount_tkt_price);
                startActivity(i);
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Booking_DetailsActivity.class);
                i.putExtra("deal_id", deal_id);
                i.putExtra("user_id", userid);
                i.putExtra("time", timing);
                i.putExtra("title", title);
                i.putExtra("deal_type",deal_type);
                i.putExtra("timing",timing);
                i.putExtra("deal_point",deal_points);
                i.putExtra("available_point",available_point);
                i.putExtra("points_nextlevel",points_to_next_level);
                i.putExtra("calendar_show",schedule_status);
                i.putExtra("comment_option",comment_option);
                i.putExtra("calendar_instruction",calendar_instruction);
                i.putExtra("price", price);
                i.putExtra("tickt_discnt_price", tkt_discounted_price);
                i.putExtra("checkout_info", checkout_info);
                i.putExtra("adult_price", adult_tkt_price);
                i.putExtra("currency", currency);
                i.putExtra("child_price", child_tkt_price);
                i.putExtra("address", address);
                i.putExtra("adult_age", adult_age_range);
                i.putExtra("child_age", child_age_range);
                i.putExtra("adult_discnt_price", adult_discount_tkt_price);
                i.putExtra("child_discnt_price", child_discount_tkt_price);
                startActivity(i);
            }
        });


        setupWindowAnimations();


        linear_navigate_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("google.navigation:q="+latitude+","+longitude);
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        lin_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                SharedPreferences preferenceswsh = getApplicationContext().getSharedPreferences("wishk", MODE_PRIVATE);
                if (userid != null) {

                    loadtowish();


                } else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(CategoryDealDetail.this,SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Wish List")
                            .setContentText("Please login")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
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
        lin_wish_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userid != null) {

                    progress.setVisibility(View.VISIBLE);
                    loadtowish_selected();

                } else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(CategoryDealDetail.this,SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Wish List")
                            .setContentText("Please login")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
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

    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            getWindow().setReturnTransition(fade);
        }
    }

    public void loadtowish() {


        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i) {
            String REGISTER_URL1 = Constants.URL+"creat_wishlist.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {


                            System.out.println("responseeeee....." + response);

                            if (response.contentEquals("\"success\"")) {
                                progressBar.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                                lin_wish.setVisibility(View.GONE);
                                lin_wish_selected.setVisibility(View.VISIBLE);


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

                    params.put("user_id", userid);
                    params.put("deal_id", String.valueOf(deal_id));
                    params.put("sflag", "1");

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
    public void loadtowish_selected() {


        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i) {
            String REGISTER_URL1 = Constants.URL+"creat_wishlist.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {


                            System.out.println("responseeeee...........whishlistttt..........." + response);


                            if (response.contentEquals("\"success\"")) {
                                progressBar.setVisibility(View.GONE);
                                progress.setVisibility(View.GONE);
                                lin_wish.setVisibility(View.VISIBLE);
                                lin_wish_selected.setVisibility(View.GONE);


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

                    params.put("user_id", userid);
                    params.put("deal_id", String.valueOf(deal_id));
                    params.put("sflag","0");

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

    private void schedule() {
        layout_loader.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {


            String Schedule_url = Constants.URL+"dealschedule.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);


                            if (!response.isEmpty()) {
                                System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    {


                                        if (obj.has("Enabled Dates")) {

                                            if(obj.getString("Enabled Dates") != null &&  !obj.getString("Enabled Dates").equals("null")) {

                                                JSONArray jsonarray = obj.getJSONArray("Enabled Dates");


                                                for (int i = 0; i < jsonarray.length(); i++) {
                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                    ScheduleModel scheduleModel = new ScheduleModel();



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
                                            }
                                        }
                                        if(obj.has("Disabled Dates")) {
                                            if (obj.getString("Disabled Dates") != null && !obj.getString("Disabled Dates").equals("null"))
                                            {
                                                try
                                                {
                                                    JSONArray jsonarray = obj.getJSONArray("Disabled Dates");

                                                    for (int i = 0; i < jsonarray.length(); i++) {
                                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                        ScheduleModel scheduleModel = new ScheduleModel();
                                                        // arrayListtime = new ArrayList<>();


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



                                                    }
                                                }
                                                catch (Exception e)
                                                {

                                                }
                                            }
                                        }

                                    }
                                    SimpleDateFormat myFormat = new SimpleDateFormat("DD-mm-yyyy");
                                    Date date = new Date();
                                    Date date1, date2;
                                    int day = 0;

                                    Calendar cal = Calendar.getInstance();


                                    for (int j = 0; j < scheduleModelArrayList.size(); j++) {


                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Fri")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_fri.setTextColor(getResources().getColor(R.color.white));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Sat")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_sat.setTextColor(getResources().getColor(R.color.white));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Sun")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_sun.setTextColor(getResources().getColor(R.color.white));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Mon")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_mon.setTextColor(getResources().getColor(R.color.white));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Tue")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_tue.setTextColor(getResources().getColor(R.color.white));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Wed")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_wed.setTextColor(getResources().getColor(R.color.white));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Thu")) {
                                            System.out.println("dayy...."+scheduleModelArrayList.get(j).getDay());
                                            txt_thurs.setTextColor(getResources().getColor(R.color.white));

                                        }

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

    public void recycler_inflate() {


        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealdetail :" + response);
                            String need_toknow = null;
                            if(response!=null&&!response.isEmpty()) {

                                try {
                                    JSONArray jsonarray = new JSONArray(response);

                                    for (int i = 0; i < jsonarray.length(); i++)
                                    {
                                        CategoryDealModel categoryDealModel = new CategoryDealModel();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        id = jsonobject.getString("id");
                                        title = jsonobject.getString("title");

                                        JSONArray image = jsonobject.getJSONArray("image");
                                        System.out.println("imagessss......."+image);

                                        if(image!=null&&!image.equals("null")&&image.length()!=0)
                                        {
                                            System.out.println("imagessss1......."+image);
                                            layout_image.setVisibility(View.VISIBLE);

                                            for (int j = 0; j < image.length(); j++)
                                            {
                                                System.out.println("timee+" + image.getString(j));
                                                arrayListimage.add(image.getString(j));

                                                categoryDealModel.setArray_image(arrayListimage);
                                                Glide
                                                        .with(getApplicationContext())
                                                        .load(image.getString(0))
                                                        .centerCrop()
                                                        .crossFade()
                                                        .into(imag_new);

                                                categoryDealModel.setImage(image.getString(0));

                                            }
                                        }
                                        else
                                        {
                                            System.out.println("imagessss2......."+image);
                                            layout_image.setVisibility(View.GONE);
                                        }
                                        if (jsonobject.has("description"))
                                        {
                                            description = jsonobject.getString("description");
                                        }
                                        if (jsonobject.has("points_to_next_level"))
                                        {
                                            points_to_next_level = jsonobject.getString("points_to_next_level");
                                        }
                                        if (jsonobject.has("deal_slug"))
                                        {
                                            deal_slug = jsonobject.getString("deal_slug");
                                        }
                                        if (jsonobject.has("discount"))
                                        {
                                            discount = jsonobject.getString("discount");
                                        }
                                        if (jsonobject.has("timing"))
                                        {
                                            timing = jsonobject.getString("timing");
                                        }
                                        if (jsonobject.has("delivery"))
                                        {
                                            delivery = jsonobject.getString("delivery");
                                        }
                                        if (jsonobject.has("category"))
                                        {
                                            category = jsonobject.getString("category");
                                        }
                                        if (jsonobject.has("tags"))
                                        {
                                            tags = jsonobject.getString("tags");
                                        }
                                        if (jsonobject.has("available_points"))
                                        {
                                            available_point = jsonobject.getString("available_points");
                                        }
                                        if (jsonobject.has("deal_points"))
                                        {
                                            deal_points = jsonobject.getString("deal_points");
                                        }
                                        if (jsonobject.has("highlights"))
                                        {
                                            highlights = jsonobject.getString("highlights");
                                        }
                                        if (jsonobject.has("address"))
                                        {

                                            address = jsonobject.getString("address");
                                            System.out.println("address..............."+address);
                                            if(address!=null&&!address.isEmpty()&&!address.equals(null))
                                            {
                                                layout_address.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                layout_address.setVisibility(View.GONE);
                                            }
                                        }
                                        if (jsonobject.has("latitude"))
                                        {
                                            latitude = jsonobject.getString("latitude");
                                        }
                                        if (jsonobject.has("logitude"))
                                        {
                                            logitude = jsonobject.getString("logitude");
                                        }
                                        schedule_status = jsonobject.getString("schedule_status");
                                        calendar_instruction = jsonobject.getString("calendar_instruction");
                                        if (jsonobject.has("need_toknow"))
                                        {

                                            needtoknow = jsonobject.getString("need_toknow");
                                            System.out.println("need to knowww  contenttt........................."+ needtoknow);
                                            if(needtoknow.contentEquals(""))
                                            {

                                                System.out.println("need to knowww1........................."+ needtoknow);
                                                layout_needtoknow.setVisibility(View.GONE);
                                                need_to_know.setVisibility(View.GONE);
                                                need_to_know_txt.setVisibility(View.GONE);
                                            }
                                           else if(needtoknow!=null||!needtoknow.equals(null))
                                            {
                                                String text2 = "<html><body style='text-align:justify'> %s </body></Html>";
                                                need_to_know.setBackgroundColor(Color.TRANSPARENT);
                                                need_to_know.loadData(String.format(text2,needtoknow), "text/html", "utf-8");
                                                WebSettings webSettings = need_to_know.getSettings();
                                                webSettings.setDefaultFontSize(12);
                                                System.out.println("need to knowww........................."+ needtoknow);
                                                need_to_know_txt.setVisibility(View.VISIBLE);
                                                layout_needtoknow.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                System.out.println("need to knowww1........................."+ needtoknow);
                                                layout_needtoknow.setVisibility(View.GONE);
                                                need_to_know.setVisibility(View.GONE);
                                                need_to_know_txt.setVisibility(View.GONE);
                                            }

                                        }

                                        if (jsonobject.has("checkout_info"))
                                        {
                                            checkout_info = jsonobject.getString("checkout_info");

                                        }
                                        String wishlist_flag = jsonobject.getString("wishlist_flag");
                                        if (jsonobject.has("noteon_tickets"))
                                        {
                                            noteon_tickets = jsonobject.getString("noteon_tickets");
                                            System.out.println("notee..on.tickets........................."+ noteon_tickets);
                                            if (noteon_tickets != null && !noteon_tickets.isEmpty() && !noteon_tickets.equals("null"))
                                            {
                                                tabLayout.addTab(tabLayout.newTab().setText("MY TICKETS"));
                                                pager_text.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                pager_text.setVisibility(View.GONE);
                                            }


                                        }
                                        if (jsonobject.has("inclusion"))
                                        {
                                            inclusion = jsonobject.getString("inclusion");
                                            tabLayout.addTab(tabLayout.newTab().setText("INCLUSION"));
                                        }

                                        if (jsonobject.has("exclusion"))
                                        {
                                            exclusion = jsonobject.getString("exclusion");

                                        }


                                        if (jsonobject.has("currency"))
                                        {
                                            currency = jsonobject.getString("currency");
                                        }
                                        if (jsonobject.has("price"))
                                        {
                                            price = jsonobject.getString("price");


                                        }

                                        if (jsonobject.has("tkt_discounted_price"))
                                        {
                                            tkt_discounted_price = jsonobject.getString("tkt_discounted_price");

                                        }
                                        if (jsonobject.has("adult_age_range"))
                                        {
                                            adult_age_range = jsonobject.getString("adult_age_range");
                                        }
                                        if (jsonobject.has("adult_tkt_price"))
                                        {

                                            adult_tkt_price = jsonobject.getString("adult_tkt_price");
                                        }
                                        if (jsonobject.has("deal_type"))
                                        {
                                            deal_type = jsonobject.getString("deal_type");
                                        }
                                        if (jsonobject.has("child_age_range"))
                                        {
                                            child_age_range = jsonobject.getString("child_age_range");
                                        }
                                        if (jsonobject.has("child_tkt_price"))
                                        {
                                            child_tkt_price = jsonobject.getString("child_tkt_price");
                                        }
                                        if (jsonobject.has("child_discount_tkt_price"))
                                        {
                                            child_discount_tkt_price = jsonobject.getString("child_discount_tkt_price");
                                        }
                                        if (jsonobject.has("adult_discount_tkt_price"))
                                        {
                                            adult_discount_tkt_price = jsonobject.getString("adult_discount_tkt_price");
                                        }
                                        if(jsonobject.has("comment_option")) {
                                            comment_option = jsonobject.getString("comment_option");
                                        }
                                        System.out.println("description1" + description);

                                        categoryDealModel.setWishlist(wishlist_flag);
                                        txt_title.setText(title);
                                        deal_title.setText(title);


                                        categoryDealModel.setcomment_option(comment_option);
                                        categoryDealModel.setAdult_age_range(adult_age_range);
                                        categoryDealModel.setInclusion(inclusion);
                                        categoryDealModel.setExclusion(exclusion);
                                        categoryDealModel.setChild_age_range(child_age_range);
                                        categoryDealModel.setAdult_tkt_price(adult_tkt_price);
                                        categoryDealModel.setChild_tkt_price(child_tkt_price);
                                        categoryDealModel.setChild_discount_tkt_price(child_discount_tkt_price);
                                        categoryDealModel.setAdult_discount_tkt_price(adult_discount_tkt_price);
                                        categoryDealModel.setTitle(title);
                                        categoryDealModel.setAddress(address);
                                        categoryDealModel.setLatitude(latitude);
                                        categoryDealModel.setAddress(address);
                                        categoryDealModel.setLogitude(logitude);
                                        categoryDealModel.setNoteon_tickets(noteon_tickets);
                                        categoryDealModel.setCurrency(currency);
                                        categoryDealModel.setPrice(price);
                                        categoryDealModel.setHighlights(highlights);
                                        categoryDealModel.setDescription(description);
                                        categoryDealModel.setDiscount(tkt_discounted_price);
                                        categoryDealModel.setDelivery(delivery);
                                        categoryDealModel.setTiming(timing);
                                        categoryDealModel.setSeller_id(seller_id);
                                        categoryDealModelArrayList.add(categoryDealModel);


                                    }
                                    for (int k = 0; k < categoryDealModelArrayList.size(); k++)
                                    {
                                        String str4 = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getDescription() + "</div";
                                        txt_description.setBackgroundColor(Color.TRANSPARENT);
                                        txt_description.getSettings().setJavaScriptEnabled(true);
                                        txt_description.loadDataWithBaseURL("",str4,"text/html","UTF-8","");
                                        WebSettings settings_hig1 = txt_description.getSettings();
                                        settings_hig1.setTextZoom(80);

                                        String str3 = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getHighlights() + "</div";
                                        txt_description2.setBackgroundColor(Color.TRANSPARENT);
                                        txt_description2.getSettings().setJavaScriptEnabled(true);
                                        txt_description2.loadDataWithBaseURL("",str3,"text/html","UTF-8","");
                                        WebSettings settings_hig = txt_description2.getSettings();
                                        settings_hig.setTextZoom(80);

                                        System.out.println("description2........." + categoryDealModelArrayList.get(k).getDescription());
                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("wishk", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("wish_select", categoryDealModelArrayList.get(k).getWishlist());
                                        editor.putString("sflag", categoryDealModelArrayList.get(k).getWishlist());
                                        editor.putString("image", categoryDealModelArrayList.get(k).getImage());
                                        editor.commit();

                                        if (categoryDealModelArrayList.get(k).getDiscount().isEmpty() || categoryDealModelArrayList.get(k).getDiscount().equals(0) || categoryDealModelArrayList.get(k).getDiscount().equals("")) {


                                            txt_dtl_price.setText(categoryDealModelArrayList.get(k).getCurrency() + " " + categoryDealModelArrayList.get(k).getPrice());


                                        }
                                        else
                                        {

                                            txt_dtl_price.setText(categoryDealModelArrayList.get(k).getCurrency() + " " + categoryDealModelArrayList.get(k).getDiscount());
                                            final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
                                            txt_actual_price.setText(categoryDealModelArrayList.get(k).getCurrency() + " " + categoryDealModelArrayList.get(k).getPrice(), TextView.BufferType.SPANNABLE);
                                            Spannable spannable1 = (Spannable) txt_actual_price.getText();
                                            spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, txt_actual_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        }

                                        String txtinc = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getInclusion() + "</div";
                                        txt_inclusion.loadUrl("file:///android_asset/demo.html");

                                        txt_inclusion.setBackgroundColor(Color.TRANSPARENT);
                                        Resources res1 = getResources();
                                        float fontSize;
                                        fontSize = res1.getDimension(R.dimen.textsize);
                                        txt_inclusion.getSettings().setJavaScriptEnabled(true);
                                        txt_inclusion.loadDataWithBaseURL("", txtinc, "text/html", "UTF-8", "");
                                        Resources res3 = getResources();
                                        WebSettings settings = txt_inclusion.getSettings();
                                        settings.setTextZoom(80);

                                        String txtexc = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getExclusion() + "</div";
                                        txt_exclusion.loadUrl("file:///android_asset/demo.html");
                                        txt_exclusion.setBackgroundColor(Color.TRANSPARENT);
                                        Resources resexc = getResources();
                                        float fontSize_ex;
                                        fontSize_ex = resexc.getDimension(R.dimen.textsize);
                                        txt_exclusion.getSettings().setJavaScriptEnabled(true);
                                        txt_exclusion.loadDataWithBaseURL("", txtexc, "text/html", "UTF-8", "");

                                        WebSettings settings_exe = txt_exclusion.getSettings();
                                        settings_exe.setTextZoom(80);


                                        String txtpager = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getNoteon_tickets() + "</div";
                                        pager_text.loadUrl("file:///android_asset/demo.html");
                                        pager_text.setBackgroundColor(Color.TRANSPARENT);
                                        Resources res = getResources();
                                        float fontSize_tickt;
                                        fontSize_tickt = res1.getDimension(R.dimen.textsize);
                                        pager_text.getSettings().setJavaScriptEnabled(true);
                                        pager_text.loadDataWithBaseURL("",txtpager, "text/html", "UTF-8", "");

                                        WebSettings settings_tickt = pager_text.getSettings();
                                        settings_tickt.setTextZoom(80);


                                        if (categoryDealModelArrayList.get(k).getWishlist().contentEquals(String.valueOf(1)))
                                        {

                                            lin_wish.setVisibility(View.GONE);
                                            lin_wish_selected.setVisibility(View.VISIBLE);

                                        }
                                        else
                                        {

                                            lin_wish.setVisibility(View.VISIBLE);
                                            lin_wish_selected.setVisibility(View.GONE);

                                        }

                                        System.out.println("wish_recyclerdata............." + categoryDealModelArrayList.get(k).getWishlist());

                                        String text2 = "<html><body style='text-align:justify'> %s </body></Html>";
                                        txt_address.setBackgroundColor(Color.TRANSPARENT);
                                        txt_address.loadData(String.format(text2,categoryDealModelArrayList.get(k).getAddress()), "text/html", "utf-8");
                                        WebSettings webSettings = txt_address.getSettings();
                                        webSettings.setDefaultFontSize(12);
                                        collapsingToolbar.setTitle(categoryDealModelArrayList.get(k).getTitle());
                                    }

                                    if (arrayListimage != null && !arrayListimage.isEmpty())
                                    {
                                        for (int k = 0; k < arrayListimage.size(); k++)
                                        {  String pic1=arrayListimage.get(k).replaceAll(" ", "%20");
                                            final DefaultSliderView defaultSliderView = new DefaultSliderView(getApplicationContext());
                                            final int finalK = k;


                                            System.out.println("timee...................." + arrayListimage.get(finalK));
                                            defaultSliderView.image(pic1);


                                            sliderShow.addSlider(defaultSliderView);


                                        }
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    layout_visible.setVisibility(View.VISIBLE);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                {
                                    final SweetAlertDialog dialog = new SweetAlertDialog(CategoryDealDetail.this,SweetAlertDialog.NORMAL_TYPE);
                                    dialog.setTitleText("")
                                            .setContentText("No data to diplay")

                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    dialog.dismiss();
                                                    Intent i  =new Intent(CategoryDealDetail.this, FrameLayoutActivity.class);
                                                    startActivity(i);

                                                }
                                            })
                                            .show();
                                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
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
                    params.put("user_id", String.valueOf(userid));

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
    public void recycler_inflate1() {


        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE111111111111+++++++++++++++   dealdetail :" + response);
                            String need_toknow = null;
                            if(response.equalsIgnoreCase("")){{
                                {
                                    final SweetAlertDialog dialog = new SweetAlertDialog(CategoryDealDetail.this,SweetAlertDialog.NORMAL_TYPE);
                                    dialog.setTitleText("")
                                            .setContentText("No data to diplay")

                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    dialog.dismiss();
                                                    Intent i  =new Intent(CategoryDealDetail.this, FrameLayoutActivity.class);
                                                    startActivity(i);

                                                }
                                            })
                                            .show();
                                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                                }
                            }}
                           else if(response!=null&&!response.isEmpty()) {

                                try {
                                    JSONArray jsonarray = new JSONArray(response);

                                    for (int i = 0; i < jsonarray.length(); i++)
                                    {
                                        CategoryDealModel categoryDealModel = new CategoryDealModel();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        id = jsonobject.getString("id");
                                        title = jsonobject.getString("title");

                                        JSONArray image = jsonobject.getJSONArray("image");
                                        System.out.println("imagessss......."+image);

                                        if(image!=null&&!image.equals("null")&&image.length()!=0)
                                        {
                                            System.out.println("imagessss1......."+image);
                                            layout_image.setVisibility(View.VISIBLE);

                                            for (int j = 0; j < image.length(); j++)
                                            {
                                                System.out.println("timee+" + image.getString(j));
                                                arrayListimage.add(image.getString(j));

                                                categoryDealModel.setArray_image(arrayListimage);
                                                Glide
                                                        .with(getApplicationContext())
                                                        .load(image.getString(0))
                                                        .centerCrop()
                                                        .crossFade()
                                                        .into(imag_new);

                                                categoryDealModel.setImage(image.getString(0));

                                            }
                                        }
                                        else
                                        {
                                                                                     System.out.println("imagessss2......."+image);
                                            layout_image.setVisibility(View.GONE);
                                        }
                                        if (jsonobject.has("description"))
                                        {
                                            description = jsonobject.getString("description");
                                        }
                                        if (jsonobject.has("points_to_next_level"))
                                        {
                                            points_to_next_level = jsonobject.getString("points_to_next_level");
                                        }
                                        if (jsonobject.has("deal_slug"))
                                        {
                                            deal_slug = jsonobject.getString("deal_slug");
                                        }
                                        if (jsonobject.has("discount"))
                                        {
                                            discount = jsonobject.getString("discount");
                                        }
                                        if (jsonobject.has("timing"))
                                        {
                                            timing = jsonobject.getString("timing");
                                        }
                                        if (jsonobject.has("delivery"))
                                        {
                                            delivery = jsonobject.getString("delivery");
                                        }
                                        if (jsonobject.has("category"))
                                        {
                                            category = jsonobject.getString("category");
                                        }
                                        if (jsonobject.has("tags"))
                                        {
                                            tags = jsonobject.getString("tags");
                                        }
                                        if (jsonobject.has("available_points"))
                                        {
                                            available_point = jsonobject.getString("available_points");
                                        }
                                        if (jsonobject.has("deal_points"))
                                        {
                                            deal_points = jsonobject.getString("deal_points");
                                        }
                                        if (jsonobject.has("highlights"))
                                        {
                                            highlights = jsonobject.getString("highlights");
                                        }
                                        if (jsonobject.has("address"))
                                        {

                                            address = jsonobject.getString("address");
                                            System.out.println("address..............."+address);
                                            if(address!=null&&!address.isEmpty()&&!address.equals(null))
                                            {
                                                layout_address.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                layout_address.setVisibility(View.GONE);
                                            }
                                        }
                                        if (jsonobject.has("latitude"))
                                        {
                                            latitude = jsonobject.getString("latitude");
                                        }
                                        if (jsonobject.has("logitude"))
                                        {
                                            logitude = jsonobject.getString("logitude");
                                        }
                                        schedule_status = jsonobject.getString("schedule_status");
                                        calendar_instruction = jsonobject.getString("calendar_instruction");
                                        if (jsonobject.has("need_toknow"))
                                        {

                                            needtoknow = jsonobject.getString("need_toknow");
                                            System.out.println("need to knowww  contenttt........................."+ needtoknow);
                                            if(needtoknow.contentEquals(""))
                                            {

                                                System.out.println("need to knowww1........................."+ needtoknow);
                                                layout_needtoknow.setVisibility(View.GONE);
                                                need_to_know.setVisibility(View.GONE);
                                                need_to_know_txt.setVisibility(View.GONE);
                                            }
                                            else if(needtoknow!=null||!needtoknow.equals(null))
                                            {
                                                String text2 = "<html><body style='text-align:justify'> %s </body></Html>";
                                                need_to_know.setBackgroundColor(Color.TRANSPARENT);
                                                need_to_know.loadData(String.format(text2,needtoknow), "text/html", "utf-8");
                                                WebSettings webSettings = need_to_know.getSettings();
                                                webSettings.setDefaultFontSize(12);
                                                System.out.println("need to knowww........................."+ needtoknow);
                                                need_to_know_txt.setVisibility(View.VISIBLE);
                                                layout_needtoknow.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                System.out.println("need to knowww1........................."+ needtoknow);
                                                layout_needtoknow.setVisibility(View.GONE);
                                                need_to_know.setVisibility(View.GONE);
                                                need_to_know_txt.setVisibility(View.GONE);
                                            }

                                        }

                                        if (jsonobject.has("checkout_info"))
                                        {
                                            checkout_info = jsonobject.getString("checkout_info");

                                        }
                                        String wishlist_flag = jsonobject.getString("wishlist_flag");
                                        if (jsonobject.has("noteon_tickets"))
                                        {
                                            noteon_tickets = jsonobject.getString("noteon_tickets");
                                            System.out.println("notee..on.tickets........................."+ noteon_tickets);
                                            if (noteon_tickets != null && !noteon_tickets.isEmpty() && !noteon_tickets.equals("null"))
                                            {
                                                tabLayout.addTab(tabLayout.newTab().setText("MY TICKETS"));
                                                pager_text.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                pager_text.setVisibility(View.GONE);
                                            }


                                        }
                                        if (jsonobject.has("inclusion"))
                                        {
                                            inclusion = jsonobject.getString("inclusion");
                                            tabLayout.addTab(tabLayout.newTab().setText("INCLUSION"));
                                        }

                                        if (jsonobject.has("exclusion"))
                                        {
                                            exclusion = jsonobject.getString("exclusion");

                                        }


                                        if (jsonobject.has("currency"))
                                        {
                                            currency = jsonobject.getString("currency");
                                        }
                                        if (jsonobject.has("price"))
                                        {
                                            price = jsonobject.getString("price");


                                        }

                                        if (jsonobject.has("tkt_discounted_price"))
                                        {
                                            tkt_discounted_price = jsonobject.getString("tkt_discounted_price");

                                        }
                                        if (jsonobject.has("adult_age_range"))
                                        {
                                            adult_age_range = jsonobject.getString("adult_age_range");
                                        }
                                        if (jsonobject.has("adult_tkt_price"))
                                        {

                                            adult_tkt_price = jsonobject.getString("adult_tkt_price");
                                        }
                                        if (jsonobject.has("deal_type"))
                                        {
                                            deal_type = jsonobject.getString("deal_type");
                                        }
                                        if (jsonobject.has("child_age_range"))
                                        {
                                            child_age_range = jsonobject.getString("child_age_range");
                                        }
                                        if (jsonobject.has("child_tkt_price"))
                                        {
                                            child_tkt_price = jsonobject.getString("child_tkt_price");
                                        }
                                        if (jsonobject.has("child_discount_tkt_price"))
                                        {
                                            child_discount_tkt_price = jsonobject.getString("child_discount_tkt_price");
                                        }
                                        if (jsonobject.has("adult_discount_tkt_price"))
                                        {
                                            adult_discount_tkt_price = jsonobject.getString("adult_discount_tkt_price");
                                        }
                                        if(jsonobject.has("comment_option")) {
                                            comment_option = jsonobject.getString("comment_option");
                                        }
                                        System.out.println("description1" + description);

                                        categoryDealModel.setWishlist(wishlist_flag);
                                        txt_title.setText(title);
                                        deal_title.setText(title);


                                        categoryDealModel.setcomment_option(comment_option);
                                        categoryDealModel.setAdult_age_range(adult_age_range);
                                        categoryDealModel.setInclusion(inclusion);
                                        categoryDealModel.setExclusion(exclusion);
                                        categoryDealModel.setChild_age_range(child_age_range);
                                        categoryDealModel.setAdult_tkt_price(adult_tkt_price);
                                        categoryDealModel.setChild_tkt_price(child_tkt_price);
                                        categoryDealModel.setChild_discount_tkt_price(child_discount_tkt_price);
                                        categoryDealModel.setAdult_discount_tkt_price(adult_discount_tkt_price);
                                        categoryDealModel.setTitle(title);
                                        categoryDealModel.setAddress(address);
                                        categoryDealModel.setLatitude(latitude);
                                        categoryDealModel.setAddress(address);
                                        categoryDealModel.setLogitude(logitude);
                                        categoryDealModel.setNoteon_tickets(noteon_tickets);
                                        categoryDealModel.setCurrency(currency);
                                        categoryDealModel.setPrice(price);
                                        categoryDealModel.setHighlights(highlights);
                                        categoryDealModel.setDescription(description);
                                        categoryDealModel.setDiscount(tkt_discounted_price);
                                        categoryDealModel.setDelivery(delivery);
                                        categoryDealModel.setTiming(timing);
                                        categoryDealModel.setSeller_id(seller_id);
                                        categoryDealModelArrayList.add(categoryDealModel);


                                    }
                                    for (int k = 0; k < categoryDealModelArrayList.size(); k++)
                                    {
                                        String str4 = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getDescription() + "</div";
                                        txt_description.setBackgroundColor(Color.TRANSPARENT);
                                        txt_description.getSettings().setJavaScriptEnabled(true);
                                        txt_description.loadDataWithBaseURL("",str4,"text/html","UTF-8","");
                                        WebSettings settings_hig1 = txt_description.getSettings();
                                        settings_hig1.setTextZoom(80);

                                        String str3 = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getHighlights() + "</div";
                                        txt_description2.setBackgroundColor(Color.TRANSPARENT);
                                        txt_description2.getSettings().setJavaScriptEnabled(true);
                                        txt_description2.loadDataWithBaseURL("",str3,"text/html","UTF-8","");
                                        WebSettings settings_hig = txt_description2.getSettings();
                                        settings_hig.setTextZoom(80);

                                        System.out.println("description2........." + categoryDealModelArrayList.get(k).getDescription());
                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("wishk", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("wish_select", categoryDealModelArrayList.get(k).getWishlist());
                                        editor.putString("sflag", categoryDealModelArrayList.get(k).getWishlist());
                                        editor.putString("image", categoryDealModelArrayList.get(k).getImage());
                                        editor.commit();

                                        if (categoryDealModelArrayList.get(k).getDiscount().isEmpty() || categoryDealModelArrayList.get(k).getDiscount().equals(0) || categoryDealModelArrayList.get(k).getDiscount().equals("")) {


                                            txt_dtl_price.setText(categoryDealModelArrayList.get(k).getCurrency() + " " + categoryDealModelArrayList.get(k).getPrice());


                                        }
                                        else
                                        {

                                            txt_dtl_price.setText(categoryDealModelArrayList.get(k).getCurrency() + " " + categoryDealModelArrayList.get(k).getDiscount());
                                            final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
                                            txt_actual_price.setText(categoryDealModelArrayList.get(k).getCurrency() + " " + categoryDealModelArrayList.get(k).getPrice(), TextView.BufferType.SPANNABLE);
                                            Spannable spannable1 = (Spannable) txt_actual_price.getText();
                                            spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, txt_actual_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        }

                                        String txtinc = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getInclusion() + "</div";
                                        txt_inclusion.loadUrl("file:///android_asset/demo.html");

                                        txt_inclusion.setBackgroundColor(Color.TRANSPARENT);
                                        Resources res1 = getResources();
                                        float fontSize;
                                        fontSize = res1.getDimension(R.dimen.textsize);
                                        txt_inclusion.getSettings().setJavaScriptEnabled(true);
                                        txt_inclusion.loadDataWithBaseURL("", txtinc, "text/html", "UTF-8", "");
                                        Resources res3 = getResources();
                                        WebSettings settings = txt_inclusion.getSettings();
                                        settings.setTextZoom(80);

                                        String txtexc = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getExclusion() + "</div";
                                        txt_exclusion.loadUrl("file:///android_asset/demo.html");
                                        txt_exclusion.setBackgroundColor(Color.TRANSPARENT);
                                        Resources resexc = getResources();
                                        float fontSize_ex;
                                        fontSize_ex = resexc.getDimension(R.dimen.textsize);
                                        txt_exclusion.getSettings().setJavaScriptEnabled(true);
                                        txt_exclusion.loadDataWithBaseURL("", txtexc, "text/html", "UTF-8", "");

                                        WebSettings settings_exe = txt_exclusion.getSettings();
                                        settings_exe.setTextZoom(80);


                                        String txtpager = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + categoryDealModelArrayList.get(k).getNoteon_tickets() + "</div";
                                        pager_text.loadUrl("file:///android_asset/demo.html");
                                        pager_text.setBackgroundColor(Color.TRANSPARENT);
                                        Resources res = getResources();
                                        float fontSize_tickt;
                                        fontSize_tickt = res1.getDimension(R.dimen.textsize);
                                        pager_text.getSettings().setJavaScriptEnabled(true);
                                        pager_text.loadDataWithBaseURL("",txtpager, "text/html", "UTF-8", "");

                                        WebSettings settings_tickt = pager_text.getSettings();
                                        settings_tickt.setTextZoom(80);


                                        if (categoryDealModelArrayList.get(k).getWishlist().contentEquals(String.valueOf(1)))
                                        {

                                            lin_wish.setVisibility(View.GONE);
                                            lin_wish_selected.setVisibility(View.VISIBLE);

                                        }
                                        else
                                        {

                                            lin_wish.setVisibility(View.VISIBLE);
                                            lin_wish_selected.setVisibility(View.GONE);

                                        }

                                        System.out.println("wish_recyclerdata............." + categoryDealModelArrayList.get(k).getWishlist());

                                        String text2 = "<html><body style='text-align:justify'> %s </body></Html>";
                                        txt_address.setBackgroundColor(Color.TRANSPARENT);
                                        txt_address.loadData(String.format(text2,categoryDealModelArrayList.get(k).getAddress()), "text/html", "utf-8");
                                        WebSettings webSettings = txt_address.getSettings();
                                        webSettings.setDefaultFontSize(12);
                                        collapsingToolbar.setTitle(categoryDealModelArrayList.get(k).getTitle());
                                    }

                                    if (arrayListimage != null && !arrayListimage.isEmpty())
                                    {
                                        for (int k = 0; k < arrayListimage.size(); k++)
                                        {  String pic1=arrayListimage.get(k).replaceAll(" ", "%20");
                                            final DefaultSliderView defaultSliderView = new DefaultSliderView(getApplicationContext());
                                            final int finalK = k;


                                            System.out.println("timee...................." + arrayListimage.get(finalK));
                                            defaultSliderView.image(pic1);


                                            sliderShow.addSlider(defaultSliderView);


                                        }
                                    }

                                    progressBar.setVisibility(View.GONE);
                                    layout_visible.setVisibility(View.VISIBLE);


                                } catch (JSONException e) {
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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {


            super.onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null&&!response.isEmpty()) {
                                System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealdetail :" + response);


                                try {
                                    JSONArray jsonarray = new JSONArray(response);


                                    for (int i = 0; i < jsonarray.length(); i++)
                                    {
                                        CategoryDealModel categoryDealModel = new CategoryDealModel();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        id = jsonobject.getString("id");
                                        title = jsonobject.getString("title");
                                        description = jsonobject.getString("description");
                                        discount = jsonobject.getString("discount");
                                        deal_slug = jsonobject.getString("deal_slug");
                                        timing = jsonobject.getString("timing");
                                        delivery = jsonobject.getString("delivery");
                                        category = jsonobject.getString("category");
                                        tags = jsonobject.getString("tags");
                                        String highlights = jsonobject.getString("highlights");
                                        String address = jsonobject.getString("address");
                                        String latitude = jsonobject.getString("latitude");
                                        String logitudes = jsonobject.getString("logitude");
                                        String noteon_tickets = jsonobject.getString("noteon_tickets");
                                        String currency = jsonobject.getString("currency");
                                        String price = jsonobject.getString("price");
                                        String adult_price = jsonobject.getString("adult_tkt_price");
                                        String adult_age_range = jsonobject.getString("adult_age_range");
                                        String child_age_range = jsonobject.getString("child_age_range");
                                        String child_tkt_price = jsonobject.getString("child_tkt_price");
                                        String adult_discount_tkt_price = jsonobject.getString("adult_discount_tkt_price");
                                        String child_discount_tkt_price = jsonobject.getString("child_discount_tkt_price");
                                        System.out.println("description1" + description);
                                        JSONArray image = jsonobject.getJSONArray("image");
                                        for (int j = 0; j < image.length(); j++)
                                        {
                                            System.out.println("timee+" + image.getString(j));
                                            arrayListimage.add(image.getString(j));
                                            categoryDealModel.setArray_image(arrayListimage);

                                        }

                                        if (latitude != null && logitudes != null&&!latitude.isEmpty()&&!logitudes.isEmpty()&&!latitude.equals("null")&&!logitudes.equals("null"))
                                        {
                                            frame_map.setVisibility(View.VISIBLE);

                                            try
                                            {
                                                lat = Double.valueOf(latitude);
                                            }
                                            catch (NumberFormatException e)
                                            {
                                                lat = Double.valueOf(0);
                                            }
                                            try
                                            {
                                                longitude = Double.valueOf(logitudes);
                                            }
                                            catch (NumberFormatException e)
                                            {
                                                longitude = Double.valueOf(0);
                                            }


                                            System.out.println("lattt1:" + lat + longitude);
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longitude), 20.0f));
                                            LatLng sydney = new LatLng(lat, longitude);
                                            mMap.addMarker(new MarkerOptions().position(sydney).title(title));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                        }
                                        else
                                        {
                                            frame_map.setVisibility(View.GONE);

                                        }


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
        }
        else
        {

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


    private void ticket_id() {
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {


            String Schedule_url = Constants.URL+"dealtickets.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);
                            if (response != null&&!response.isEmpty()) {
                                System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);


                                try {
                                    JSONArray jsonarray = new JSONArray(response);


                                    for (int i = 0; i < jsonarray.length(); i++) {

                                        ScheduleModel scheduleModel = new ScheduleModel();
                                        arrayListtime = new ArrayList<>();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        String date = jsonobject.getString("tkt_id");
                                        String day = jsonobject.getString("deal_id");
                                        String month = jsonobject.getString("month");
                                        String year = jsonobject.getString("year");
                                        String dayscount = jsonobject.getString("dayscount");
                                        JSONArray time = jsonobject.getJSONArray("time");
                                        for (int j = 0; j < time.length(); j++) {
                                            System.out.println("timee+" + time.getString(j));
                                            scheduleModel.setStr_time(time.getString(j));
                                            arrayListtime.add(time.getString(j));
                                        }
                                        scheduleModel.setDate(date);
                                        scheduleModel.setDay(day);
                                        scheduleModel.setMonth(month);
                                        scheduleModel.setYear(year);
                                        scheduleModel.setTime(arrayListtime);
                                        scheduleModel.setDayscount(dayscount);
                                        scheduleModelArrayList.add(scheduleModel);


                                    }
                                    SimpleDateFormat myFormat = new SimpleDateFormat("DD-mm-yyyy");
                                    Date date = new Date();
                                    Date date1, date2;
                                    int day = 0;

                                    Calendar cal = Calendar.getInstance();
                                    for (int j = 0; j < scheduleModelArrayList.size(); j++) {



                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Fri")) {
                                            txt_fri.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Sat")) {
                                            txt_sat.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Sun")) {
                                            txt_sun.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Mon")) {
                                            txt_mon.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Tue")) {
                                            txt_tue.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Wed")) {
                                            txt_wed.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }
                                        if (scheduleModelArrayList.get(j).getDay().contentEquals("Thu")) {
                                            txt_thurs.setBackgroundColor(getResources().getColor(R.color.calendarback_colr));

                                        }


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
    protected void onResume() {
        super.onResume();
        nestedScrollView.scrollTo(0, 0);

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