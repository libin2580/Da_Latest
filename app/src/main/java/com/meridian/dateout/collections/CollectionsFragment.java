package com.meridian.dateout.collections;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.CollectionsAdapter1;
import com.meridian.dateout.explore.CollectionsAdapter2;
import com.meridian.dateout.explore.RecyclerItemClickListener;
import com.meridian.dateout.explore.Spinner_model1;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.CategoryModel;
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


public class CollectionsFragment extends Fragment implements View.OnClickListener{
    ArrayList<CategoryModel> categoryModelArrayList;
    String id, id1, category, background, icon;
    static RecyclerView recyclerView;
    int mRowIndex = -1;
    CollectionsAdapter collectionsAdapter;
    Button sort_by, categry, location, prce, schedule, duration;
    CrystalRangeSeekbar rangeSeekbar;
    LinearLayout apply_btn,popup_close,catagories_list_main;
    TextView clear_txt,tvMin,tvMax;
    RecyclerView recyclerview_sort_by,recyclerViewpopup;
    CalendarPickerView calendar_view;
    View custompopup_view;
    PopupWindow filter_popupwindow;
    NetworkCheckingClass networkCheckingClass;
    LinearLayout coordinatorLayout,sort_by_layout;
    RelativeLayout range_layout;
    String filter_by,str_range_from,str_range_to;
    CollectionsAdapter1 collectionsAdapter1;
    CollectionsAdapter2 collectionsAdapter2;
    ProgressBar progress, progress1;
    ArrayList<Spinner_model1> SpinList1;
    ArrayList<Spinner_model1> location_list;
    ArrayList<Spinner_model1> sort_by_list;
    Spinner_model1 spinnerModel1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_collections, container, false);
        coordinatorLayout = (LinearLayout) view.findViewById(R.id.activity_category_list);
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        progress1= (ProgressBar) view.findViewById(R.id.progress1);
        FrameLayoutActivity.search_nearby.setVisibility(View.GONE);
        FrameLayoutActivity.my_location.setVisibility(View.GONE);
        FrameLayoutActivity.img_collections.setBackgroundResource(R.drawable.collections);
        FrameLayoutActivity.txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
        FrameLayoutActivity.img_account.setBackgroundResource(R.drawable.account_click);
        FrameLayoutActivity.txt_accnt_name.setTextColor(getResources().getColor(R.color.black));
        FrameLayoutActivity.img_chat.setBackgroundResource(R.drawable.email_blue);
        FrameLayoutActivity.txt_chat_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
        FrameLayoutActivity.img_explore.setBackgroundResource(R.drawable.explore_click);
        FrameLayoutActivity.txt_explorenam.setTextColor(getResources().getColor(R.color.black));
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        FrameLayoutActivity.tabbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.filter.setVisibility(View.GONE);
        FrameLayoutActivity.cart.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_toolbar_crcname.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_toolbar_crcname.setText("Collections");
        FrameLayoutActivity.toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaypopup_filter_popupwindow();
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
        categry.setVisibility(View.GONE);
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
        // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_collections);
        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_popupwindow.dismiss();
                if (filter_by.equalsIgnoreCase("catagories")) {
                    Toast.makeText(getApplicationContext(), "catagories", Toast.LENGTH_SHORT).show();
                    // get_filter_list_by_catagories(jsonlist);
                }
                if (filter_by.equalsIgnoreCase("location")) {
                    Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_SHORT).show();
                    //get_filter_list_by_location();
                }
                if (filter_by.equalsIgnoreCase("price")) {
                    str_range_from = tvMin.getText().toString();
                    str_range_to = tvMax.getText().toString();
                    Toast.makeText(getApplicationContext(), "price", Toast.LENGTH_SHORT).show();
                    //  get_filter_list_by_price(str_range_from, str_range_to);
                }
                if (filter_by.equalsIgnoreCase("schedule")) {
                    Date start_date = calendar_view.getSelectedDates().get(0);
                    Date end_date = calendar_view.getSelectedDates().get((calendar_view.getSelectedDates().size()) - 1);
                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String start_date1 = formatter.format(start_date);
                    String end_date1 = formatter.format(end_date);
                    Toast.makeText(getApplicationContext(), "schedule", Toast.LENGTH_SHORT).show();
                    // get_filter_list_by_schedule(start_date1, end_date1);
                }
                if (filter_by.equalsIgnoreCase("sort_by")) {

                    //get_filter_list_sort_by(str_sorted_by);
                }
            }
        });

        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        final boolean i = networkCheckingClass.ckeckinternet();
        if(i) {
            new GetContacts1().execute();
           // get_explore();
        }
        else {
            final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
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


    public static Fragment newInstance() {
        CollectionsFragment fragment = new CollectionsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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

        private class GetContacts1 extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress1.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                try {


                    String jsonStr = sh.makeServiceCall(Constants.URL+"categories.php");
                    System.out.println(jsonStr);
                    //  Log.e(TAG, "Response from url: " + jsonStr);

                    if (jsonStr != null) {

                        try {
                            JSONArray jsonarray = new JSONArray(jsonStr);
                            categoryModelArrayList = new ArrayList<>();


                            for (int i = 0; i < jsonarray.length(); i++)
                            {
                                CategoryModel categoryModel = new CategoryModel();

                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String id = jsonobject.getString("id");
                                String category = jsonobject.getString("category");
                                String _18plusOnly = jsonobject.getString("_18plusOnly");
                                background = jsonobject.getString("background");
                                icon = jsonobject.getString("icon");

                                categoryModel.setBackground(background);
                                categoryModel.setCategory(category);
                                categoryModel.setIcon(icon);
                                categoryModel.setId(id);
                                categoryModel.set_18plusOnly(_18plusOnly);
                                categoryModel.setType("category");
                                categoryModelArrayList.add(categoryModel);


                            }



                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    } else {


                    }
                }
                catch (Exception e)
                {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                progress1.setVisibility(View.GONE);
                try {


                    if (!categoryModelArrayList.isEmpty() && categoryModelArrayList != null) {
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(llm);
                        collectionsAdapter = new CollectionsAdapter(getActivity(), categoryModelArrayList);

                        recyclerView.scheduleLayoutAnimation();
                        recyclerView.setAdapter(collectionsAdapter);

                        recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, final int position) {

                                        System.out.println("catttttttttttttttttttt" + categoryModelArrayList.get(position).getCategory()+"##....");
                                        if(categoryModelArrayList.get(position).get_18plusOnly().equals("1"))
                                        {

                                            final SweetAlertDialog dialog = new SweetAlertDialog(getContext(),SweetAlertDialog.NORMAL_TYPE);
                                            dialog.setTitleText("Alert!")
                                                    .setContentText("Are you Above 18 years ?")

                                                    .setConfirmText("No")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            dialog.dismiss();

                                                        }
                                                    })
                                                    .setCancelText("Yes")
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismiss();
                                                            FragmentManager fragmentManager;
                                                            Fragment fragment;
                                                            Bundle  args=new Bundle();
                                                            android.support.v4.app.FragmentTransaction transaction=null;
                                                            int id = Integer.parseInt(categoryModelArrayList.get(position).getId());
                                                            fragmentManager = getActivity().getSupportFragmentManager();
                                                            transaction = fragmentManager.beginTransaction();
                                                            fragment = CategoryDealFragment.newInstance();
                                                            transaction.replace(R.id.flFragmentPlaceHolder,fragment,"cat_deal").addToBackStack("cat_deal");

                                                            args.putInt("category_id", id);
                                                            args.putString("names", categoryModelArrayList.get(position).getCategory());
                                                            System.out.println("catttttttttttttttttttt" + categoryModelArrayList.get(position).getCategory()+"##....");
                                                            fragment.setArguments(args);
                                                            transaction.commit();
                                                        }
                                                    })
                                                    .show();
                                            dialog.findViewById(R.id.cancel_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));
                                            dialog.findViewById(R.id.confirm_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));
                                        }
                                        else
                                        {
                                            FragmentManager fragmentManager;
                                            Fragment fragment;
                                            Bundle args=new Bundle();
                                            android.support.v4.app.FragmentTransaction transaction=null;
                                            int id = Integer.parseInt(categoryModelArrayList.get(position).getId());
                                            fragmentManager = getActivity().getSupportFragmentManager();
                                            transaction = fragmentManager.beginTransaction();
                                            fragment = CategoryDealFragment.newInstance();
                                            transaction.replace(R.id.flFragmentPlaceHolder,fragment,"cat_deal").addToBackStack("cat_deal");
                                            args.putInt("category_id", id);
                                            args.putString("names", categoryModelArrayList.get(position).getCategory());
                                            System.out.println("catttttttttttttttttttt" + categoryModelArrayList.get(position).getCategory()+"##....");
                                            fragment.setArguments(args);
                                            transaction.commit();
                                        }



                                    }

                                })
                        );


                    }
                }
                catch (Exception e)
                {

                }
            }

        }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_toolbr_cart, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_cart));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayoutActivity.tabbar.setVisibility(View.GONE);
                FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
                FrameLayoutActivity.img_toolbar_crcname.setVisibility(View.GONE);

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
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

                collectionsAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                collectionsAdapter.filter(newText);
                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {


            super.getActivity().onBackPressed();
            return true;
        }
        return false;
    }

}
