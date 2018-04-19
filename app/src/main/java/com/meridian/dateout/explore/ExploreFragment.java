package com.meridian.dateout.explore;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.CategoryModel;
import com.meridian.dateout.model.DealsModel;
import com.squareup.picasso.Picasso;
import com.squareup.timessquare.CalendarPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.meridian.dateout.Constants.analytics;
import static com.meridian.dateout.explore.CollectionsAdapter1.jsonlist;
import static com.meridian.dateout.explore.CollectionsAdapter2.str_sorted_by;

public class ExploreFragment extends Fragment implements View.OnClickListener {
    static RecyclerView recyclerView, recyclerView1;
    String id, category, background, icon, _18plusOnly,str_fullname_gmail, str_email_gmail,deal_slug;
    ArrayList<CategoryModel> categoryModelArrayList;
    ArrayList<DealsModel> dealsModelArrayList;
    ArrayList<DealsModel> alldeals_categryModelArrayList;
    String title,image,description,timing,delivery,id_deal,category1, currency,tags, category_id, categorys,seller_id, tkt_discounted_price, price;
    ArrayList<String> all_background;
    static TextView search_empty,txt_name,experience;
    RecyclerAdapterCategory1 recyclerAdapterCategory1;
    ImageView img_country;
    LinearLayout lin_recycler;
    RecyclerAdapterCategory recyclerAdapterCategory;
    RecyclerAdapterCategory_after_filter recyclerAdapterCategory_after_filter;
    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout mySwipeRefreshLayout, mySwipeRefreshLayout1;
    AppBarLayout appBarLayout;
    PopupWindow filter_popupwindow;
    View custompopup_view;
    RelativeLayout topLayout,range_layout;
    private OnFragmentInteractionListener mListener;
    Button sort_by, categry, location, prce, schedule, duration;
    CrystalRangeSeekbar rangeSeekbar;
    RecyclerView recyclerViewpopup, recyclerview_sort_by;
    LinearLayout catagories_list_main, apply_btn,popup_close,sort_by_layout;
    String str_range_from, str_range_to;
    TextView tvMin, tvMax,clear_txt;
    ProgressBar progress, progress1;
    ArrayList<Spinner_model1> SpinList1;
    ArrayList<Spinner_model1> location_list;
    ArrayList<Spinner_model1> sort_by_list;
    Spinner_model1 spinnerModel1;
    CollectionsAdapter1 collectionsAdapter1;
    CollectionsAdapter2 collectionsAdapter2;
    CalendarPickerView calendar_view;
    String filter_by;
    NetworkCheckingClass networkCheckingClass;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.explore1, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.explore_coordintr);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        appBarLayout = (AppBarLayout) view.findViewById(R.id.MyAppbars);
        appBarLayout.setExpanded(true);
        search_empty = (TextView) view.findViewById(R.id.search_empty);
        dealsModelArrayList = new ArrayList<>();
        alldeals_categryModelArrayList = new ArrayList<>();
        categoryModelArrayList = new ArrayList<>();
        all_background = new ArrayList<>();
        topLayout = (RelativeLayout) view.findViewById(R.id.top_layout);
        txt_name = (TextView) view.findViewById(R.id.name_guest);
        experience = (TextView) view.findViewById(R.id.experience);
        img_country = (ImageView) view.findViewById(R.id.lin_country);
        lin_recycler = (LinearLayout) view.findViewById(R.id.lin_recycler);
        progress1 = (ProgressBar) view.findViewById(R.id.progress_bar_explore);
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mySwipeRefreshLayout1 = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer1);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_vertical);
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_vertical1);
        mySwipeRefreshLayout.setRefreshing(false);
        mySwipeRefreshLayout.setEnabled(false);
        mySwipeRefreshLayout1.setRefreshing(false);
        mySwipeRefreshLayout1.setEnabled(false);
        FrameLayoutActivity.toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_toolbar_crcname.setVisibility(View.VISIBLE);
        FrameLayoutActivity.tabbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        FrameLayoutActivity.search_nearby.setVisibility(View.GONE);
        FrameLayoutActivity.my_location.setVisibility(View.GONE);
        FrameLayoutActivity.filter.setVisibility(View.VISIBLE);
        FrameLayoutActivity.cart.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_toolbar_crcname.setText("SINGAPORE");
        FrameLayoutActivity.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaypopup_filter_popupwindow();

            }
        });
        FrameLayoutActivity.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), Cart_details.class);
                startActivity(i);
            }
        });

        final LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        custompopup_view = inflator.inflate(R.layout.filterlayout, null);
        recyclerview_sort_by = (RecyclerView) custompopup_view.findViewById(R.id.recyclerview_sort_by);
        progress = (ProgressBar) custompopup_view.findViewById(R.id.progress);
        catagories_list_main = (LinearLayout) custompopup_view.findViewById(R.id.catagories_list_main);
        popup_close = (LinearLayout) custompopup_view.findViewById(R.id.img_crcdtlnam);
        categry = (Button) custompopup_view.findViewById(R.id.button1);
        location = (Button) custompopup_view.findViewById(R.id.button2);
        prce = (Button) custompopup_view.findViewById(R.id.button3);
        schedule = (Button) custompopup_view.findViewById(R.id.button4);
        duration = (Button) custompopup_view.findViewById(R.id.button5);
        sort_by = (Button) custompopup_view.findViewById(R.id.button6);
        apply_btn = (LinearLayout) custompopup_view.findViewById(R.id.apply_btn);
        clear_txt = (TextView) custompopup_view.findViewById(R.id.clear);
        calendar_view = (CalendarPickerView) custompopup_view.findViewById(R.id.calendar_view);
        clear_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_popupwindow.dismiss();
                if (networkCheckingClass.ckeckinternet()) {
                    // new GetContacts2().execute();
                    get_explore();

                } else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
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
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        List<Date> dates = calendar_view.getSelectedDates();
        rangeSeekbar = (CrystalRangeSeekbar) custompopup_view.findViewById(R.id.rangeSeekbar3);
        range_layout = (RelativeLayout) custompopup_view.findViewById(R.id.range_layout);
        sort_by_layout = (LinearLayout) custompopup_view.findViewById(R.id.sort_by_layout);
        tvMin = (TextView) custompopup_view.findViewById(R.id.textMin1);
        tvMax = (TextView) custompopup_view.findViewById(R.id.textMax1);
        recyclerViewpopup = (RecyclerView) custompopup_view.findViewById(R.id.recycler_vertical1);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(" $ "+String.valueOf(minValue));
                tvMax.setText(" $ "+String.valueOf(maxValue));

            }
        });
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
        calendar_view.setVisibility(View.GONE);
        popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_popupwindow.dismiss();
            }
        });
        sort_by.setOnClickListener(this);
        categry.setOnClickListener(this);
        location.setOnClickListener(this);
        prce.setOnClickListener(this);
        schedule.setOnClickListener(this);
        duration.setOnClickListener(this);

        SharedPreferences sharedfb = getActivity().getSharedPreferences("myfb", getActivity().MODE_PRIVATE);
        String str_fulname_fbs = sharedfb.getString("names", null);
        System.out.println("explorefragment_sghared" + str_fulname_fbs);
        SharedPreferences preferences_user = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        String fullname_user = preferences_user.getString("fullname", null);
        SharedPreferences preferences_gmail = getActivity().getSharedPreferences("value_google_user", getActivity().MODE_PRIVATE);
        str_fullname_gmail = preferences_gmail.getString("name", null);
        str_email_gmail = preferences_gmail.getString("email", null);
        if (str_fulname_fbs != null) {
            String upperString = str_fulname_fbs.substring(0, 1).toUpperCase() + str_fulname_fbs.substring(1);
            txt_name.setText("Hey " + upperString + ",");
        } else if (fullname_user != null) {
            String upperString = fullname_user.substring(0, 1).toUpperCase() + fullname_user.substring(1);
            txt_name.setText("Hey " + upperString + ",");
        } else if (str_fullname_gmail != null) {
            String upperString = str_fullname_gmail.substring(0, 1).toUpperCase() + str_fullname_gmail.substring(1);
            txt_name.setText("Hey " + upperString + ",");
        }
        networkCheckingClass = new NetworkCheckingClass(getActivity());
        final boolean i = networkCheckingClass.ckeckinternet();
        if (i) {
            //new GetContacts2().execute();
            get_explore();

        } else {
            final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
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
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_popupwindow.dismiss();
                if (filter_by.equalsIgnoreCase("catagories")) {

                    get_filter_list_by_catagories(jsonlist);
                }
                if (filter_by.equalsIgnoreCase("location")) {

                    get_filter_list_by_location(jsonlist);
                }
                if (filter_by.equalsIgnoreCase("price")) {
                    str_range_from = tvMin.getText().toString();
                    str_range_to = tvMax.getText().toString();

                    get_filter_list_by_price(str_range_from, str_range_to);
                }
                if (filter_by.equalsIgnoreCase("schedule")) {
                    Date start_date = calendar_view.getSelectedDates().get(0);
                    Date end_date = calendar_view.getSelectedDates().get((calendar_view.getSelectedDates().size()) - 1);
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String start_date1 = formatter.format(start_date);
                    String end_date1 = formatter.format(end_date);

                    get_filter_list_by_schedule(start_date1, end_date1);
                }
                if (filter_by.equalsIgnoreCase("sort_by")) {

                    get_filter_list_sort_by(str_sorted_by);
                }
            }
        });
        get_filter();
        return view;
    }

    private void get_filter() {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/filter_values.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress.setVisibility(View.GONE);
                        JSONObject jsonObj = null;
                        if (response != null) {
                            try {
                                jsonObj = new JSONObject(response);
                                String status = jsonObj.getString("status");
                                String filter_categories = jsonObj.getJSONObject("data").getString("filter_categories");
                                String filter_locations = jsonObj.getJSONObject("data").getString("filter_locations");
                                String filter_price = jsonObj.getJSONObject("data").getString("filter_price");
                                sort_by_list = new ArrayList<>();
                                String sort_by = jsonObj.getJSONObject("data").getString("sort_by");
                                JSONArray sortarray = new JSONArray(sort_by);
                                sort_by_list = new ArrayList<>();
                                for (int i = 0; i < sortarray.length(); i++) {
                                    String aa = sortarray.get(i).toString();
                                    System.out.println("-----------sxort_by " + aa);
                                    spinnerModel1 = new Spinner_model1();
                                    spinnerModel1.setCategory(aa);
                                    sort_by_list.add(spinnerModel1);
                                }
                                JSONArray jsonarray = new JSONArray(filter_categories);
                                SpinList1 = new ArrayList<>();
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    String id = jsonobject.getString("id");
                                    String category = jsonobject.getString("category");
                                    System.out.println("-----------category " + category);
                                    spinnerModel1 = new Spinner_model1();
                                    spinnerModel1.setId(id);
                                    spinnerModel1.setCategory(category);
                                    SpinList1.add(spinnerModel1);
                                }
                                JSONArray jsonarray1 = new JSONArray(filter_locations);
                                location_list = new ArrayList<>();
                                for (int i = 0; i < jsonarray1.length(); i++) {
                                    JSONObject jsonobject = jsonarray1.getJSONObject(i);
                                    String id = jsonobject.getString("id");
                                    String location = jsonobject.getString("location");
                                    System.out.println("-----------location " + location);
                                    spinnerModel1 = new Spinner_model1();
                                    spinnerModel1.setId(id);
                                    spinnerModel1.setCategory(location);
                                    location_list.add(spinnerModel1);

                                }

                                String minimum = jsonObj.getJSONObject("data").getJSONObject("filter_price").getString("minimum");
                                String maximum = jsonObj.getJSONObject("data").getJSONObject("filter_price").getString("maximum");
                                String currency = jsonObj.getJSONObject("data").getJSONObject("filter_price").getString("currency");
                                String icon = jsonObj.getJSONObject("data").getJSONObject("filter_price").getString("icon");
                                tvMin.setText(" $ "+minimum);
                                tvMax.setText(" $ "+maximum);
                                rangeSeekbar.setMinValue(Float.parseFloat(minimum));
                                rangeSeekbar.setMaxValue(Float.parseFloat(maximum));
                                System.out.println("-----------currency " + currency);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.setVisibility(View.GONE);
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
        requestQueue.getCache().clear();
    }


    private void get_filter_list_sort_by(final String str_sorted_by) {
        progress1.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/explore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress1.setVisibility(View.GONE);
                        if (response != null) {
                            System.out.println("****************************response********************** : " + response);
                            try {
                                JSONObject jsonObjec = new JSONObject(response);
                                String status=jsonObjec.getString("status");
                                String data=jsonObjec.getString("data");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    if (jsonObject.has("categories")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("categories");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                CategoryModel categoryModel = new CategoryModel();
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    category_id = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("category")) {
                                                    categorys = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("background")) {
                                                    background = jsonobject.getString("background");
                                                }
                                                if (jsonobject.has("_18plusOnly")) {
                                                    _18plusOnly = jsonobject.getString("_18plusOnly");
                                                }
                                                if (jsonobject.has("icon")) {
                                                    icon = jsonobject.getString("icon");
                                                }
                                                dealsModel.setCategory_background(background);
                                                dealsModel.setTitle(categorys);
                                                dealsModel.setCategory_name(categorys);
                                                dealsModel.setCategory_icon(icon);
                                                dealsModel.setCategory_id(category_id);
                                                dealsModel.setCategory_type("category");
                                                dealsModel.setType("category");
                                                dealsModel.setDelivery("");
                                                dealsModel.setDescription("");
                                                dealsModel.setDiscount("");
                                                dealsModel.setImage(background);
                                                dealsModel.setTags("");
                                                dealsModel.setSeller_id("");
                                                dealsModel.setTiming("");
                                                dealsModel.setCurrency("");
                                                dealsModel.setPrice("");
                                                categoryModel.setBackground(background);
                                                categoryModel.setCategory(categorys);
                                                categoryModel.setIcon(icon);
                                                categoryModel.setId(category_id);
                                                categoryModel.set_18plusOnly(_18plusOnly);
                                                categoryModel.setType("category");
                                                categoryModelArrayList.add(categoryModel);
                                                all_background.add(background);
                                                alldeals_categryModelArrayList.add(dealsModel);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (jsonObject.has("deals")) {
                                        dealsModelArrayList = new ArrayList<>();
                                        dealsModelArrayList.clear();

                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("deals");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    id_deal = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("title")) {
                                                    title = jsonobject.getString("title");
                                                }
                                                if (jsonobject.has("image")) {
                                                    image = jsonobject.getString("image");
                                                }
                                                if (jsonobject.has("description")) {
                                                    description = jsonobject.getString("description");
                                                }
                                                if (jsonobject.has("timing")) {
                                                    timing = jsonobject.getString("timing");
                                                }
                                                if (jsonobject.has("delivery")) {
                                                    delivery = jsonobject.getString("delivery");
                                                }
                                                if (jsonobject.has("category")) {
                                                    category1 = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("tags")) {
                                                    tags = jsonobject.getString("tags");
                                                }
                                                if (jsonobject.has("price")) {
                                                    price = jsonobject.getString("price");
                                                }
                                                if (jsonobject.has("tkt_discounted_price")) {
                                                    tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                                }
                                                if (jsonobject.has("seller_id")) {
                                                    seller_id = jsonobject.getString("seller_id");
                                                }
                                                if (jsonobject.has("currency")) {
                                                    currency = jsonobject.getString("currency");
                                                }
                                                dealsModel.setId(id_deal);
                                                dealsModel.setCategory_id(category);
                                                dealsModel.setTitle(title);
                                                dealsModel.setType("deal");
                                                dealsModel.setDelivery(delivery);
                                                dealsModel.setDescription(description);
                                                dealsModel.setDiscount(tkt_discounted_price);
                                                dealsModel.setImage(image);
                                                dealsModel.setTags(tags);
                                                dealsModel.setSeller_id(seller_id);
                                                dealsModel.setTiming(timing);
                                                dealsModel.setCurrency(currency);
                                                System.out.println("----------- title : " + title);
                                                if (price == null || price.equals(0)) {
                                                    dealsModel.setPrice("0");
                                                } else {
                                                    dealsModel.setPrice(price);
                                                }
                                                alldeals_categryModelArrayList.add(dealsModel);
                                                dealsModel.setType("deal");
                                                dealsModelArrayList.add(dealsModel);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (jsonObject.has("banner")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("banner");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                String imageid = jsonobject.getString("id");
                                                String caption = jsonobject.getString("caption");
                                                experience.setText(caption);
                                                image = jsonobject.getString("image");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (getActivity() != null) {
                                if (image != null && !image.isEmpty()) {
                                    Picasso.with(getActivity()).load(image).fit().into(img_country);
                                }
                            }
                            PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemViewCacheSize(20);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (categoryModelArrayList != null && dealsModelArrayList != null) {
                                recyclerAdapterCategory_after_filter = new RecyclerAdapterCategory_after_filter(categoryModelArrayList, dealsModelArrayList, getActivity());
                                recyclerView.setAdapter(recyclerAdapterCategory_after_filter);

                            }
                            PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                            recyclerView1.setLayoutManager(layoutManager1);
                            recyclerView1.setNestedScrollingEnabled(false);
                            recyclerView1.setHasFixedSize(true);
                            recyclerView1.setItemViewCacheSize(20);
                            recyclerView1.setDrawingCacheEnabled(true);
                            recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (alldeals_categryModelArrayList != null) {
                                recyclerAdapterCategory1 = new RecyclerAdapterCategory1(alldeals_categryModelArrayList, getActivity());
                                recyclerView1.setAdapter(recyclerAdapterCategory1);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress1.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sortby", str_sorted_by);
                System.out.println("----------- params : " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void get_filter_list_by_catagories(final ArrayList<String> jsonlist) {
        progress1.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/explore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress1.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                JSONObject jsonObjec = new JSONObject(response);
                                String status=jsonObjec.getString("status");
                                String data=jsonObjec.getString("data");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    if (jsonObject.has("categories")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("categories");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                CategoryModel categoryModel = new CategoryModel();
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    category_id = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("category")) {
                                                    categorys = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("background")) {
                                                    background = jsonobject.getString("background");
                                                }
                                                if (jsonobject.has("_18plusOnly")) {
                                                    _18plusOnly = jsonobject.getString("_18plusOnly");
                                                }
                                                if (jsonobject.has("icon")) {
                                                    icon = jsonobject.getString("icon");
                                                }
                                                dealsModel.setCategory_background(background);
                                                dealsModel.setTitle(categorys);
                                                dealsModel.setCategory_name(categorys);
                                                dealsModel.setCategory_icon(icon);
                                                dealsModel.setCategory_id(category_id);
                                                dealsModel.setCategory_type("category");
                                                dealsModel.setType("category");
                                                dealsModel.setDelivery("");
                                                dealsModel.setDescription("");
                                                dealsModel.setDiscount("");
                                                dealsModel.setImage(background);
                                                dealsModel.setTags("");
                                                dealsModel.setSeller_id("");
                                                dealsModel.setTiming("");
                                                dealsModel.setCurrency("");
                                                dealsModel.setPrice("");
                                                categoryModel.setBackground(background);
                                                categoryModel.setCategory(categorys);
                                                categoryModel.setIcon(icon);
                                                categoryModel.setId(category_id);
                                                categoryModel.set_18plusOnly(_18plusOnly);
                                                categoryModel.setType("category");
                                                categoryModelArrayList.add(categoryModel);
                                                all_background.add(background);
                                                alldeals_categryModelArrayList.add(dealsModel);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (jsonObject.has("deals")) {
                                        dealsModelArrayList = new ArrayList<>();
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("deals");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    id_deal = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("title")) {
                                                    title = jsonobject.getString("title");
                                                }
                                                if (jsonobject.has("image")) {
                                                    image = jsonobject.getString("image");
                                                }
                                                if (jsonobject.has("description")) {
                                                    description = jsonobject.getString("description");
                                                }
                                                if (jsonobject.has("timing")) {
                                                    timing = jsonobject.getString("timing");
                                                }
                                                if (jsonobject.has("delivery")) {
                                                    delivery = jsonobject.getString("delivery");
                                                }
                                                if (jsonobject.has("category")) {
                                                    category1 = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("tags")) {
                                                    tags = jsonobject.getString("tags");
                                                }
                                                if (jsonobject.has("price")) {
                                                    price = jsonobject.getString("price");
                                                }
                                                if (jsonobject.has("tkt_discounted_price")) {
                                                    tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                                }
                                                if (jsonobject.has("seller_id")) {
                                                    seller_id = jsonobject.getString("seller_id");
                                                }
                                                if (jsonobject.has("currency")) {
                                                    currency = jsonobject.getString("currency");
                                                }
                                                dealsModel.setId(id_deal);
                                                dealsModel.setCategory_id(category);
                                                dealsModel.setTitle(title);
                                                dealsModel.setType("deal");
                                                dealsModel.setDelivery(delivery);
                                                dealsModel.setDescription(description);
                                                dealsModel.setDiscount(tkt_discounted_price);
                                                dealsModel.setImage(image);
                                                dealsModel.setTags(tags);
                                                dealsModel.setSeller_id(seller_id);
                                                dealsModel.setTiming(timing);
                                                dealsModel.setCurrency(currency);
                                                if (price == null || price.equals(0)) {
                                                    dealsModel.setPrice("0");
                                                } else {
                                                    dealsModel.setPrice(price);
                                                }
                                                alldeals_categryModelArrayList.add(dealsModel);
                                                dealsModel.setType("deal");
                                                dealsModelArrayList.add(dealsModel);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (jsonObject.has("banner")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("banner");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                String imageid = jsonobject.getString("id");
                                                String caption = jsonobject.getString("caption");
                                                experience.setText(caption);
                                                image = jsonobject.getString("image");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException ee) {
                                ee.printStackTrace();
                            }
                            if (getActivity() != null) {
                                if (image != null && !image.isEmpty()) {
                                    Picasso.with(getActivity()).load(image).fit().into(img_country);
                                }
                            }
                            PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemViewCacheSize(20);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                            if (categoryModelArrayList != null && dealsModelArrayList != null) {

                                recyclerAdapterCategory_after_filter = new RecyclerAdapterCategory_after_filter(categoryModelArrayList, dealsModelArrayList, getActivity());
                                recyclerView.setAdapter(recyclerAdapterCategory_after_filter);

                            }
                            PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                            recyclerView1.setLayoutManager(layoutManager1);
                            recyclerView1.setNestedScrollingEnabled(false);
                            recyclerView1.setHasFixedSize(true);
                            recyclerView1.setItemViewCacheSize(20);
                            recyclerView1.setDrawingCacheEnabled(true);
                            recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (alldeals_categryModelArrayList != null) {
                                recyclerAdapterCategory1 = new RecyclerAdapterCategory1(alldeals_categryModelArrayList, getActivity());
                                recyclerView1.setAdapter(recyclerAdapterCategory1);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress1.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categories", String.valueOf(jsonlist));
                System.out.println("----------- params : " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void get_filter_list_by_location(final ArrayList<String> jsonlist) {
        progress1.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/explore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress1.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                JSONObject jsonObjec = new JSONObject(response);
                                String status=jsonObjec.getString("status");
                                String data=jsonObjec.getString("data");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    if (jsonObject.has("categories")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("categories");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                CategoryModel categoryModel = new CategoryModel();
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    category_id = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("category")) {
                                                    categorys = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("background")) {
                                                    background = jsonobject.getString("background");
                                                }
                                                if (jsonobject.has("_18plusOnly")) {
                                                    _18plusOnly = jsonobject.getString("_18plusOnly");
                                                }
                                                if (jsonobject.has("icon")) {
                                                    icon = jsonobject.getString("icon");
                                                }
                                                dealsModel.setCategory_background(background);
                                                dealsModel.setTitle(categorys);
                                                dealsModel.setCategory_name(categorys);
                                                dealsModel.setCategory_icon(icon);
                                                dealsModel.setCategory_id(category_id);
                                                dealsModel.setCategory_type("category");
                                                dealsModel.setType("category");
                                                dealsModel.setDelivery("");
                                                dealsModel.setDescription("");
                                                dealsModel.setDiscount("");
                                                dealsModel.setImage(background);
                                                dealsModel.setTags("");
                                                dealsModel.setSeller_id("");
                                                dealsModel.setTiming("");
                                                dealsModel.setCurrency("");
                                                dealsModel.setPrice("");
                                                categoryModel.setBackground(background);
                                                categoryModel.setCategory(categorys);
                                                categoryModel.setIcon(icon);
                                                categoryModel.setId(category_id);
                                                categoryModel.set_18plusOnly(_18plusOnly);
                                                categoryModel.setType("category");
                                                categoryModelArrayList.add(categoryModel);
                                                all_background.add(background);
                                                alldeals_categryModelArrayList.add(dealsModel);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (jsonObject.has("deals")) {
                                        dealsModelArrayList = new ArrayList<>();
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("deals");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    id_deal = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("title")) {
                                                    title = jsonobject.getString("title");
                                                }
                                                if (jsonobject.has("image")) {
                                                    image = jsonobject.getString("image");
                                                }
                                                if (jsonobject.has("description")) {
                                                    description = jsonobject.getString("description");
                                                }
                                                if (jsonobject.has("timing")) {
                                                    timing = jsonobject.getString("timing");
                                                }
                                                if (jsonobject.has("delivery")) {
                                                    delivery = jsonobject.getString("delivery");
                                                }
                                                if (jsonobject.has("category")) {
                                                    category1 = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("tags")) {
                                                    tags = jsonobject.getString("tags");
                                                }
                                                if (jsonobject.has("price")) {
                                                    price = jsonobject.getString("price");
                                                }
                                                if (jsonobject.has("tkt_discounted_price")) {
                                                    tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                                }
                                                if (jsonobject.has("seller_id")) {
                                                    seller_id = jsonobject.getString("seller_id");
                                                }
                                                if (jsonobject.has("currency")) {
                                                    currency = jsonobject.getString("currency");
                                                }
                                                dealsModel.setId(id_deal);
                                                dealsModel.setCategory_id(category);
                                                dealsModel.setTitle(title);
                                                dealsModel.setType("deal");
                                                dealsModel.setDelivery(delivery);
                                                dealsModel.setDescription(description);
                                                dealsModel.setDiscount(tkt_discounted_price);
                                                dealsModel.setImage(image);
                                                dealsModel.setTags(tags);
                                                dealsModel.setSeller_id(seller_id);
                                                dealsModel.setTiming(timing);
                                                dealsModel.setCurrency(currency);
                                                if (price == null || price.equals(0)) {
                                                    dealsModel.setPrice("0");
                                                } else {
                                                    dealsModel.setPrice(price);
                                                }
                                                alldeals_categryModelArrayList.add(dealsModel);
                                                dealsModel.setType("deal");
                                                dealsModelArrayList.add(dealsModel);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (jsonObject.has("banner")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("banner");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                String imageid = jsonobject.getString("id");
                                                String caption = jsonobject.getString("caption");
                                                experience.setText(caption);
                                                image = jsonobject.getString("image");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (getActivity() != null) {
                                if (image != null && !image.isEmpty()) {
                                    Picasso.with(getActivity()).load(image).fit().into(img_country);
                                }
                            }
                            PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemViewCacheSize(20);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (categoryModelArrayList != null && dealsModelArrayList != null) {
                                recyclerAdapterCategory_after_filter = new RecyclerAdapterCategory_after_filter(categoryModelArrayList, dealsModelArrayList, getActivity());
                                recyclerView.setAdapter(recyclerAdapterCategory_after_filter);

                            }
                            PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                            recyclerView1.setLayoutManager(layoutManager1);
                            recyclerView1.setNestedScrollingEnabled(false);
                            recyclerView1.setHasFixedSize(true);
                            recyclerView1.setItemViewCacheSize(20);
                            recyclerView1.setDrawingCacheEnabled(true);
                            recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (alldeals_categryModelArrayList != null) {
                                recyclerAdapterCategory1 = new RecyclerAdapterCategory1(alldeals_categryModelArrayList, getActivity());
                                recyclerView1.setAdapter(recyclerAdapterCategory1);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress1.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location", String.valueOf(jsonlist));
                System.out.println("----------- params : " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void get_filter_list_by_price(final String range_from, final String range_to) {
        progress1.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/explore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress1.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                JSONObject jsonObjec = new JSONObject(response);
                                String status=jsonObjec.getString("status");
                                String data=jsonObjec.getString("data");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    if (jsonObject.has("categories")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("categories");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                CategoryModel categoryModel = new CategoryModel();
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    category_id = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("category")) {
                                                    categorys = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("background")) {
                                                    background = jsonobject.getString("background");
                                                }
                                                if (jsonobject.has("_18plusOnly")) {
                                                    _18plusOnly = jsonobject.getString("_18plusOnly");
                                                }
                                                if (jsonobject.has("icon")) {
                                                    icon = jsonobject.getString("icon");
                                                }
                                                dealsModel.setCategory_background(background);
                                                dealsModel.setTitle(categorys);
                                                dealsModel.setCategory_name(categorys);
                                                dealsModel.setCategory_icon(icon);
                                                dealsModel.setCategory_id(category_id);
                                                dealsModel.setCategory_type("category");
                                                dealsModel.setType("category");
                                                dealsModel.setDelivery("");
                                                dealsModel.setDescription("");
                                                dealsModel.setDiscount("");
                                                dealsModel.setImage(background);
                                                dealsModel.setTags("");
                                                dealsModel.setSeller_id("");
                                                dealsModel.setTiming("");
                                                dealsModel.setCurrency("");
                                                dealsModel.setPrice("");
                                                categoryModel.setBackground(background);
                                                categoryModel.setCategory(categorys);
                                                categoryModel.setIcon(icon);
                                                categoryModel.setId(category_id);
                                                categoryModel.set_18plusOnly(_18plusOnly);
                                                categoryModel.setType("category");
                                                categoryModelArrayList.add(categoryModel);
                                                all_background.add(background);
                                                alldeals_categryModelArrayList.add(dealsModel);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (jsonObject.has("deals")) {
                                        dealsModelArrayList = new ArrayList<>();
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("deals");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    id_deal = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("title")) {
                                                    title = jsonobject.getString("title");
                                                }
                                                if (jsonobject.has("image")) {
                                                    image = jsonobject.getString("image");
                                                }
                                                if (jsonobject.has("description")) {
                                                    description = jsonobject.getString("description");
                                                }
                                                if (jsonobject.has("timing")) {
                                                    timing = jsonobject.getString("timing");
                                                }
                                                if (jsonobject.has("delivery")) {
                                                    delivery = jsonobject.getString("delivery");
                                                }
                                                if (jsonobject.has("category")) {
                                                    category1 = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("tags")) {
                                                    tags = jsonobject.getString("tags");
                                                }
                                                if (jsonobject.has("price")) {
                                                    price = jsonobject.getString("price");
                                                }
                                                if (jsonobject.has("tkt_discounted_price")) {
                                                    tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                                }
                                                if (jsonobject.has("seller_id")) {
                                                    seller_id = jsonobject.getString("seller_id");
                                                }
                                                if (jsonobject.has("currency")) {
                                                    currency = jsonobject.getString("currency");
                                                }
                                                dealsModel.setId(id_deal);
                                                dealsModel.setCategory_id(category);
                                                dealsModel.setTitle(title);
                                                dealsModel.setType("deal");
                                                dealsModel.setDelivery(delivery);
                                                dealsModel.setDescription(description);
                                                dealsModel.setDiscount(tkt_discounted_price);
                                                dealsModel.setImage(image);
                                                dealsModel.setTags(tags);
                                                dealsModel.setSeller_id(seller_id);
                                                dealsModel.setTiming(timing);
                                                dealsModel.setCurrency(currency);
                                                if (price == null || price.equals(0)) {
                                                    dealsModel.setPrice("0");
                                                } else {
                                                    dealsModel.setPrice(price);
                                                }
                                                alldeals_categryModelArrayList.add(dealsModel);
                                                dealsModel.setType("deal");
                                                dealsModelArrayList.add(dealsModel);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (jsonObject.has("banner")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("banner");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                String imageid = jsonobject.getString("id");
                                                String caption = jsonobject.getString("caption");
                                                experience.setText(caption);
                                                image = jsonobject.getString("image");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (getActivity() != null) {
                                if (image != null && !image.isEmpty()) {
                                    Picasso.with(getActivity()).load(image).fit().into(img_country);
                                }
                            }
                            PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemViewCacheSize(20);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (categoryModelArrayList != null && dealsModelArrayList != null) {
                                recyclerAdapterCategory_after_filter = new RecyclerAdapterCategory_after_filter(categoryModelArrayList, dealsModelArrayList, getActivity());
                                recyclerView.setAdapter(recyclerAdapterCategory_after_filter);

                            }
                            PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                            recyclerView1.setLayoutManager(layoutManager1);
                            recyclerView1.setNestedScrollingEnabled(false);
                            recyclerView1.setHasFixedSize(true);
                            recyclerView1.setItemViewCacheSize(20);
                            recyclerView1.setDrawingCacheEnabled(true);
                            recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (alldeals_categryModelArrayList != null) {
                                recyclerAdapterCategory1 = new RecyclerAdapterCategory1(alldeals_categryModelArrayList, getActivity());
                                recyclerView1.setAdapter(recyclerAdapterCategory1);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress1.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("price_from", range_from);
                params.put("price_to", range_to);
                System.out.println("----------- params : " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void get_filter_list_by_schedule(final String strt_date, final String end_dat) {
        progress1.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/services/explore.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress1.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                JSONObject jsonObjec = new JSONObject(response);
                                String status=jsonObjec.getString("status");
                                String data=jsonObjec.getString("data");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    if (jsonObject.has("categories")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("categories");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                CategoryModel categoryModel = new CategoryModel();
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    category_id = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("category")) {
                                                    categorys = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("background")) {
                                                    background = jsonobject.getString("background");
                                                }
                                                if (jsonobject.has("_18plusOnly")) {
                                                    _18plusOnly = jsonobject.getString("_18plusOnly");
                                                }
                                                if (jsonobject.has("icon")) {
                                                    icon = jsonobject.getString("icon");
                                                }
                                                dealsModel.setCategory_background(background);
                                                dealsModel.setTitle(categorys);
                                                dealsModel.setCategory_name(categorys);
                                                dealsModel.setCategory_icon(icon);
                                                dealsModel.setCategory_id(category_id);
                                                dealsModel.setCategory_type("category");
                                                dealsModel.setType("category");
                                                dealsModel.setDelivery("");
                                                dealsModel.setDescription("");
                                                dealsModel.setDiscount("");
                                                dealsModel.setImage(background);
                                                dealsModel.setTags("");
                                                dealsModel.setSeller_id("");
                                                dealsModel.setTiming("");
                                                dealsModel.setCurrency("");
                                                dealsModel.setPrice("");
                                                categoryModel.setBackground(background);
                                                categoryModel.setCategory(categorys);
                                                categoryModel.setIcon(icon);
                                                categoryModel.setId(category_id);
                                                categoryModel.set_18plusOnly(_18plusOnly);
                                                categoryModel.setType("category");
                                                categoryModelArrayList.add(categoryModel);
                                                all_background.add(background);
                                                alldeals_categryModelArrayList.add(dealsModel);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (jsonObject.has("deals")) {
                                        dealsModelArrayList = new ArrayList<>();
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("deals");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                DealsModel dealsModel = new DealsModel();

                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    id_deal = jsonobject.getString("id");
                                                }
                                                if (jsonobject.has("title")) {
                                                    title = jsonobject.getString("title");
                                                }
                                                if (jsonobject.has("image")) {
                                                    image = jsonobject.getString("image");
                                                }
                                                if (jsonobject.has("description")) {
                                                    description = jsonobject.getString("description");
                                                }
                                                if (jsonobject.has("timing")) {
                                                    timing = jsonobject.getString("timing");
                                                }
                                                if (jsonobject.has("delivery")) {
                                                    delivery = jsonobject.getString("delivery");
                                                }
                                                if (jsonobject.has("category")) {
                                                    category1 = jsonobject.getString("category");
                                                }
                                                if (jsonobject.has("tags")) {
                                                    tags = jsonobject.getString("tags");
                                                }
                                                if (jsonobject.has("price")) {
                                                    price = jsonobject.getString("price");
                                                }
                                                if (jsonobject.has("tkt_discounted_price")) {
                                                    tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                                }
                                                if (jsonobject.has("seller_id")) {
                                                    seller_id = jsonobject.getString("seller_id");
                                                }
                                                if (jsonobject.has("currency")) {
                                                    currency = jsonobject.getString("currency");
                                                }
                                                dealsModel.setId(id_deal);
                                                dealsModel.setCategory_id(category);
                                                dealsModel.setTitle(title);
                                                dealsModel.setType("deal");
                                                dealsModel.setDelivery(delivery);
                                                dealsModel.setDescription(description);
                                                dealsModel.setDiscount(tkt_discounted_price);
                                                dealsModel.setImage(image);
                                                dealsModel.setTags(tags);
                                                dealsModel.setSeller_id(seller_id);
                                                dealsModel.setTiming(timing);
                                                dealsModel.setCurrency(currency);
                                                if (price == null || price.equals(0)) {
                                                    dealsModel.setPrice("0");
                                                } else {
                                                    dealsModel.setPrice(price);
                                                }
                                                alldeals_categryModelArrayList.add(dealsModel);
                                                dealsModel.setType("deal");
                                                dealsModelArrayList.add(dealsModel);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (jsonObject.has("banner")) {
                                        try {
                                            JSONArray jsonarray = jsonObject.getJSONArray("banner");
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                String imageid = jsonobject.getString("id");
                                                String caption = jsonobject.getString("caption");
                                                experience.setText(caption);
                                                image = jsonobject.getString("image");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } catch (JSONException ee) {
                                    ee.printStackTrace();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (getActivity() != null) {
                                if (image != null && !image.isEmpty()) {
                                    Picasso.with(getActivity()).load(image).fit().into(img_country);
                                }
                            }
                            PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setNestedScrollingEnabled(false);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setItemViewCacheSize(20);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (categoryModelArrayList != null && dealsModelArrayList != null) {
                                recyclerAdapterCategory_after_filter = new RecyclerAdapterCategory_after_filter(categoryModelArrayList, dealsModelArrayList, getActivity());
                                recyclerView.setAdapter(recyclerAdapterCategory_after_filter);

                            }
                            PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                            recyclerView1.setLayoutManager(layoutManager1);
                            recyclerView1.setNestedScrollingEnabled(false);
                            recyclerView1.setHasFixedSize(true);
                            recyclerView1.setItemViewCacheSize(20);
                            recyclerView1.setDrawingCacheEnabled(true);
                            recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                            if (alldeals_categryModelArrayList != null) {
                                recyclerAdapterCategory1 = new RecyclerAdapterCategory1(alldeals_categryModelArrayList, getActivity());
                                recyclerView1.setAdapter(recyclerAdapterCategory1);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress1.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date_from", strt_date);
                params.put("date_to", end_dat);
                System.out.println("----------- params : " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }


    public void displaypopup_filter_popupwindow() {
        categry.performClick();
        try {
            filter_popupwindow = new PopupWindow(custompopup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                filter_popupwindow.setElevation(5.0f);
            }
            filter_popupwindow.setFocusable(true);
            filter_popupwindow.setAnimationStyle(R.style.AppTheme);
            filter_popupwindow.showAtLocation(coordinatorLayout, Gravity.CENTER, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button1:
                sort_by.setBackgroundColor(getResources().getColor(R.color.fiter2));
                categry.setBackgroundColor(getResources().getColor(R.color.white));
                location.setBackgroundColor(getResources().getColor(R.color.fiter1));
                prce.setBackgroundColor(getResources().getColor(R.color.fiter2));
                schedule.setBackgroundColor(getResources().getColor(R.color.fiter1));
                duration.setBackgroundColor(getResources().getColor(R.color.fiter2));
                sort_by.setTextColor(Color.WHITE);
                categry.setTextColor(Color.BLACK);
                location.setTextColor(Color.WHITE);
                prce.setTextColor(Color.WHITE);
                schedule.setTextColor(Color.WHITE);
                duration.setTextColor(Color.WHITE);
                calendar_view.setVisibility(View.GONE);
                range_layout.setVisibility(View.GONE);
                //get_catagories();
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                recyclerViewpopup.setLayoutManager(llm);
                collectionsAdapter1 = new CollectionsAdapter1(getActivity(), SpinList1);
                recyclerViewpopup.scheduleLayoutAnimation();
                recyclerViewpopup.setAdapter(collectionsAdapter1);
                catagories_list_main.setVisibility(View.VISIBLE);
                sort_by_layout.setVisibility(View.GONE);
                filter_by = "catagories";
                break;
            case R.id.button2:
                sort_by.setBackgroundColor(getResources().getColor(R.color.fiter2));
                location.setBackgroundColor(getResources().getColor(R.color.white));
                categry.setBackgroundColor(getResources().getColor(R.color.fiter1));
                prce.setBackgroundColor(getResources().getColor(R.color.fiter2));
                schedule.setBackgroundColor(getResources().getColor(R.color.fiter1));
                duration.setBackgroundColor(getResources().getColor(R.color.fiter2));
                sort_by.setTextColor(Color.WHITE);
                categry.setTextColor(Color.WHITE);
                location.setTextColor(Color.BLACK);
                prce.setTextColor(Color.WHITE);
                schedule.setTextColor(Color.WHITE);
                duration.setTextColor(Color.WHITE);
                calendar_view.setVisibility(View.GONE);
                range_layout.setVisibility(View.GONE);
                LinearLayoutManager llmm = new LinearLayoutManager(getActivity());
                recyclerViewpopup.setLayoutManager(llmm);
                collectionsAdapter1 = new CollectionsAdapter1(getActivity(), location_list);
                recyclerViewpopup.scheduleLayoutAnimation();
                recyclerViewpopup.setAdapter(collectionsAdapter1);
                catagories_list_main.setVisibility(View.VISIBLE);
                sort_by_layout.setVisibility(View.GONE);
                filter_by = "location";
                break;
            case R.id.button3:
                sort_by.setBackgroundColor(getResources().getColor(R.color.fiter1));
                categry.setBackgroundColor(getResources().getColor(R.color.fiter2));
                prce.setBackgroundColor(getResources().getColor(R.color.white));
                location.setBackgroundColor(getResources().getColor(R.color.fiter1));
                schedule.setBackgroundColor(getResources().getColor(R.color.fiter2));
                duration.setBackgroundColor(getResources().getColor(R.color.fiter2));
                sort_by.setTextColor(Color.WHITE);
                categry.setTextColor(Color.WHITE);
                location.setTextColor(Color.WHITE);
                prce.setTextColor(Color.BLACK);
                schedule.setTextColor(Color.WHITE);
                duration.setTextColor(Color.WHITE);
                calendar_view.setVisibility(View.GONE);
                range_layout.setVisibility(View.VISIBLE);
                catagories_list_main.setVisibility(View.GONE);
                sort_by_layout.setVisibility(View.GONE);
                filter_by = "price";
                break;
            case R.id.button4:
                prce.setBackgroundColor(Color.WHITE);
                sort_by.setBackgroundColor(getResources().getColor(R.color.fiter1));
                categry.setBackgroundColor(getResources().getColor(R.color.fiter2));
                location.setBackgroundColor(getResources().getColor(R.color.fiter1));
                prce.setBackgroundColor(getResources().getColor(R.color.fiter2));
                schedule.setBackgroundColor(getResources().getColor(R.color.white));
                duration.setBackgroundColor(getResources().getColor(R.color.fiter2));
                sort_by.setTextColor(Color.WHITE);
                categry.setTextColor(Color.WHITE);
                location.setTextColor(Color.WHITE);
                prce.setTextColor(Color.WHITE);
                schedule.setTextColor(Color.BLACK);
                duration.setTextColor(Color.WHITE);
                range_layout.setVisibility(View.GONE);
                calendar_view.setVisibility(View.VISIBLE);
                catagories_list_main.setVisibility(View.GONE);
                sort_by_layout.setVisibility(View.GONE);
                filter_by = "schedule";
                break;
            case R.id.button5:
                sort_by.setBackgroundColor(getResources().getColor(R.color.fiter1));
                categry.setBackgroundColor(getResources().getColor(R.color.fiter2));
                location.setBackgroundColor(getResources().getColor(R.color.fiter1));
                prce.setBackgroundColor(getResources().getColor(R.color.fiter2));
                schedule.setBackgroundColor(getResources().getColor(R.color.white));
                duration.setBackgroundColor(getResources().getColor(R.color.fiter2));
                sort_by.setTextColor(Color.WHITE);
                categry.setTextColor(Color.WHITE);
                location.setTextColor(Color.WHITE);
                prce.setTextColor(Color.WHITE);
                schedule.setTextColor(Color.BLACK);
                duration.setTextColor(Color.WHITE);
                calendar_view.setVisibility(View.GONE);
                range_layout.setVisibility(View.VISIBLE);
                catagories_list_main.setVisibility(View.GONE);
                sort_by_layout.setVisibility(View.GONE);
                break;
            case R.id.button6:
                sort_by.setBackgroundColor(getResources().getColor(R.color.white));
                categry.setBackgroundColor(getResources().getColor(R.color.fiter2));
                location.setBackgroundColor(getResources().getColor(R.color.fiter1));
                prce.setBackgroundColor(getResources().getColor(R.color.fiter2));
                schedule.setBackgroundColor(getResources().getColor(R.color.fiter1));
                duration.setBackgroundColor(getResources().getColor(R.color.white));
                sort_by.setTextColor(Color.BLACK);
                categry.setTextColor(Color.WHITE);
                location.setTextColor(Color.WHITE);
                prce.setTextColor(Color.WHITE);
                schedule.setTextColor(Color.WHITE);
                duration.setTextColor(Color.WHITE);
                calendar_view.setVisibility(View.GONE);
                range_layout.setVisibility(View.GONE);
                catagories_list_main.setVisibility(View.GONE);
                sort_by_layout.setVisibility(View.VISIBLE);
                LinearLayoutManager ls = new LinearLayoutManager(getActivity());
                recyclerview_sort_by.setLayoutManager(ls);
                collectionsAdapter2 = new CollectionsAdapter2(getActivity(), sort_by_list);
                recyclerview_sort_by.scheduleLayoutAnimation();
                recyclerview_sort_by.setAdapter(collectionsAdapter2);
                filter_by = "sort_by";
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void get_explore() {
        progress1.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL + "all-deals-categories-banners.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        progress1.setVisibility(View.GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("categories")) {
                                try {
                                    JSONArray jsonarray = jsonObject.getJSONArray("categories");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        CategoryModel categoryModel = new CategoryModel();
                                        DealsModel dealsModel = new DealsModel();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        if (jsonobject.has("id")) {
                                            category_id = jsonobject.getString("id");
                                        }
                                        if (jsonobject.has("category")) {
                                            categorys = jsonobject.getString("category");
                                        }
                                        if (jsonobject.has("background")) {
                                            background = jsonobject.getString("background");
                                        }
                                        if (jsonobject.has("_18plusOnly")) {
                                            _18plusOnly = jsonobject.getString("_18plusOnly");
                                        }
                                        if (jsonobject.has("icon")) {
                                            icon = jsonobject.getString("icon");
                                        }
                                        dealsModel.setCategory_background(background);
                                        dealsModel.setTitle(categorys);
                                        dealsModel.setCategory_name(categorys);
                                        dealsModel.setCategory_icon(icon);
                                        dealsModel.setCategory_id(category_id);
                                        dealsModel.setCategory_type("category");
                                        dealsModel.setType("category");
                                        dealsModel.setDelivery("");
                                        dealsModel.setDescription("");
                                        dealsModel.setDiscount("");
                                        dealsModel.setImage(background);
                                        dealsModel.setTags("");
                                        dealsModel.setSeller_id("");
                                        dealsModel.setTiming("");
                                        dealsModel.setCurrency("");
                                        dealsModel.setPrice("");
                                        categoryModel.setBackground(background);
                                        categoryModel.setCategory(categorys);
                                        categoryModel.setIcon(icon);
                                        categoryModel.setId(category_id);
                                        categoryModel.set_18plusOnly(_18plusOnly);
                                        categoryModel.setType("category");
                                        categoryModelArrayList.add(categoryModel);
                                        all_background.add(background);
                                        alldeals_categryModelArrayList.add(dealsModel);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (jsonObject.has("deals")) {
                                dealsModelArrayList = new ArrayList<>();
                                try {
                                    JSONArray jsonarray = jsonObject.getJSONArray("deals");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        DealsModel dealsModel = new DealsModel();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        if (jsonobject.has("id")) {
                                            id_deal = jsonobject.getString("id");
                                        }
                                        if (jsonobject.has("title")) {
                                            title = jsonobject.getString("title");
                                        }
                                        if (jsonobject.has("image")) {
                                            image = jsonobject.getString("image");
                                        }
                                        if (jsonobject.has("description")) {
                                            description = jsonobject.getString("description");
                                        }
                                        if (jsonobject.has("timing")) {
                                            timing = jsonobject.getString("timing");
                                        }
                                        if (jsonobject.has("delivery")) {
                                            delivery = jsonobject.getString("delivery");
                                        }
                                        if (jsonobject.has("category")) {
                                            category1 = jsonobject.getString("category");
                                        }
                                        if (jsonobject.has("tags")) {
                                            tags = jsonobject.getString("tags");
                                        }
                                        if (jsonobject.has("price")) {
                                            price = jsonobject.getString("price");
                                        }
                                        if (jsonobject.has("tkt_discounted_price")) {
                                            tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                        }
                                        if (jsonobject.has("seller_id")) {
                                            seller_id = jsonobject.getString("seller_id");
                                        }
                                        if (jsonobject.has("currency")) {
                                            currency = jsonobject.getString("currency");
                                        }
                                        dealsModel.setId(id_deal);
                                        dealsModel.setCategory_id(category);
                                        dealsModel.setTitle(title);
                                        dealsModel.setType("deal");
                                        dealsModel.setDelivery(delivery);
                                        dealsModel.setDescription(description);
                                        dealsModel.setDiscount(tkt_discounted_price);
                                        dealsModel.setImage(image);
                                        dealsModel.setTags(tags);
                                        dealsModel.setSeller_id(seller_id);
                                        dealsModel.setTiming(timing);
                                        dealsModel.setCurrency(currency);
                                        if (price == null || price.equals(0)) {
                                            dealsModel.setPrice("0");
                                        } else {
                                            dealsModel.setPrice(price);
                                        }
                                        alldeals_categryModelArrayList.add(dealsModel);
                                        dealsModel.setType("deal");
                                        dealsModelArrayList.add(dealsModel);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (jsonObject.has("banner")) {
                                try {
                                    JSONArray jsonarray = jsonObject.getJSONArray("banner");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        String imageid = jsonobject.getString("id");
                                        String caption = jsonobject.getString("caption");
                                        experience.setText(caption);
                                        image = jsonobject.getString("image");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (getActivity() != null) {
                            if (image != null && !image.isEmpty()) {
                                Picasso.with(getActivity()).load(image).fit().into(img_country);
                            }
                        }
                        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        recyclerView.setDrawingCacheEnabled(true);
                        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        if (categoryModelArrayList != null && dealsModelArrayList != null) {
                            recyclerAdapterCategory = new RecyclerAdapterCategory(categoryModelArrayList, dealsModelArrayList, getActivity());
                            recyclerView.setAdapter(recyclerAdapterCategory);

                        }
                        PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                        recyclerView1.setLayoutManager(layoutManager1);
                        recyclerView1.setNestedScrollingEnabled(false);
                        recyclerView1.setHasFixedSize(true);
                        recyclerView1.setItemViewCacheSize(20);
                        recyclerView1.setDrawingCacheEnabled(true);
                        recyclerView1.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        if (alldeals_categryModelArrayList != null) {
                            recyclerAdapterCategory1 = new RecyclerAdapterCategory1(alldeals_categryModelArrayList, getActivity());
                            recyclerView1.setAdapter(recyclerAdapterCategory1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress1.setVisibility(View.GONE);
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
        requestQueue.getCache().clear();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_toolbr_cart, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_cart));
        searchView.setVisibility(View.GONE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setActivated(false);
                appBarLayout.setExpanded(true, false);
                float heightDp = getResources().getDisplayMetrics().heightPixels / 3;
                CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                lp.height = (int) heightDp;
                appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, Math.round(heightDp / 3)));
                coordinatorLayout.setBackgroundResource(R.color.white);
                mySwipeRefreshLayout1.setVisibility(View.VISIBLE);
                mySwipeRefreshLayout.setVisibility(View.GONE);
                FrameLayoutActivity.tabbar.setVisibility(View.GONE);
                FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
                FrameLayoutActivity.img_toolbar_crcname.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                coordinatorLayout.setBackgroundResource(R.drawable.login_background);
                appBarLayout.setExpanded(true, false);
                mySwipeRefreshLayout1.setVisibility(View.GONE);
                mySwipeRefreshLayout.setVisibility(View.VISIBLE);
                appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(AppBarLayout.LayoutParams.WRAP_CONTENT, AppBarLayout.LayoutParams.WRAP_CONTENT));
                FrameLayoutActivity.tabbar.setVisibility(View.VISIBLE);
                FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
                FrameLayoutActivity.img_toolbar_crcname.setVisibility(View.VISIBLE);
                return false;
            }

        });
        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(R.string.search_hint));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                coordinatorLayout.setBackgroundResource(R.color.white);
                recyclerAdapterCategory1.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                coordinatorLayout.setBackgroundResource(R.color.white);
                img_country.setBackgroundResource(R.color.white);
                recyclerAdapterCategory1.filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            return true;
        }
        return false;
    }

    public void onResume() {
        super.onResume();
    }

}
