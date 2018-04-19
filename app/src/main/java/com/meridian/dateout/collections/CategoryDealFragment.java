package com.meridian.dateout.collections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.CollectionsAdapter1;
import com.meridian.dateout.explore.CollectionsAdapter2;
import com.meridian.dateout.explore.RecyclerItemClickListener;
import com.meridian.dateout.explore.Spinner_model1;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.CategoryDealModel;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryDealFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryDealFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    ArrayList<CategoryDealModel> categoryDealModelArrayList;
    String id;
    int category_id;
    String title;
    String image;
    String description;
    String discount;
    String timing;
    String delivery;
    String category;
    String tags;
    String seller_id,deal_slug,comment_option;
    String currency,price,tkt_discounted_price;
    ImageView back;
    ImageView img_top_cal, img_top_faq;
    RelativeLayout relativeLayout1;
    TextView txt_name;



    RecyclerAdapterCategoryDeal recyclerAdapterCategoryDeal;
    String REGISTER_URL = Constants.URL+"dealsbycategory.php?";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout menu,Filter;
    Toolbar toolbar;
    TextView txt;
    ImageView filter1;
    private OnFragmentInteractionListener mListener;
    Button sort_by, categry, location, prce, schedule, duration;
    LinearLayout apply_btn,popup_close,catagories_list_main;
    TextView clear_txt,tvMin,tvMax;
    RecyclerView recyclerview_sort_by,recyclerViewpopup;
    CalendarPickerView calendar_view;
    View custompopup_view;
    PopupWindow filter_popupwindow;
    ProgressBar progress;
    CrystalRangeSeekbar rangeSeekbar;
    LinearLayout coordinatorLayout,sort_by_layout;
    String filter_by,str_range_from,str_range_to;
    RelativeLayout range_layout;
    ArrayList<Spinner_model1> SpinList1;
    ArrayList<Spinner_model1> location_list;
    ArrayList<Spinner_model1> sort_by_list;
    Spinner_model1 spinnerModel1;
    CollectionsAdapter1 collectionsAdapter1;
    CollectionsAdapter2 collectionsAdapter2;
    public CategoryDealFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryDealFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryDealFragment newInstance() {
        CategoryDealFragment fragment = new CategoryDealFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_category_deal, container, false);
        String name=  getArguments().getString("names");
        category_id=getArguments().getInt("category_id", 0);
        System.out.println("categry_id" + category_id);
        FrameLayoutActivity.tabbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        relativeLayout1= (RelativeLayout) v.findViewById(R.id.relativeLayout1);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_categorydeal);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        img_top_cal = (ImageView) v.findViewById(R.id.img_top_calendar);
        txt= (TextView) v.findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        Filter= (LinearLayout) v. findViewById(R.id.filter);
        filter1= (ImageView) v. findViewById(R.id.filter1);
        coordinatorLayout = (LinearLayout) v.findViewById(R.id.activity_category_list);
        filter1.setVisibility(View.VISIBLE);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        FrameLayoutActivity.img_collections.setBackgroundResource(R.drawable.collections);
        FrameLayoutActivity.txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
        FrameLayoutActivity.img_account.setBackgroundResource(R.drawable.account_click);
        FrameLayoutActivity.txt_accnt_name.setTextColor(getResources().getColor(R.color.black));
        FrameLayoutActivity.img_chat.setBackgroundResource(R.drawable.email_blue);
        FrameLayoutActivity.txt_chat_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
        FrameLayoutActivity.img_explore.setBackgroundResource(R.drawable.explore_click);
        FrameLayoutActivity.txt_explorenam.setTextColor(getResources().getColor(R.color.black));
        txt.setText(name);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

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
        get_filter();
        Filter.setOnClickListener(new View.OnClickListener() {
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
        img_top_faq = (ImageView) v.findViewById(R.id.img_top_faq);
        recycler_inflate();
        return v;

    }

    private void recycler_inflate() {


        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealactivity :" + response);


                            try {
                                JSONArray jsonarray = new JSONArray(response);
                                categoryDealModelArrayList = new ArrayList<>();


                                for (int i = 0; i < jsonarray.length(); i++) {
                                    CategoryDealModel categoryDealModel = new CategoryDealModel();

                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    if(jsonobject.has("id")) {
                                        id = jsonobject.getString("id");
                                    }
                                    if(jsonobject.has("title")) {
                                        title = jsonobject.getString("title");
                                    }
                                    if(jsonobject.has("image")) {
                                        image = jsonobject.getString("image");
                                    }
                                    if(jsonobject.has("description")) {
                                        description = jsonobject.getString("description");
                                    }
                                    if(jsonobject.has("timing")) {
                                        timing = jsonobject.getString("timing");
                                    }
                                    if(jsonobject.has("delivery")) {
                                        delivery = jsonobject.getString("delivery");
                                    }

                                    if(jsonobject.has("tags")) {
                                        tags = jsonobject.getString("tags");
                                    }  if(jsonobject.has("price")) {
                                        price = jsonobject.getString("price");
                                    }
                                    if(jsonobject.has("tkt_discounted_price")) {
                                        tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                    }
                                    if(jsonobject.has("seller_id")) {
                                        seller_id = jsonobject.getString("seller_id");
                                    }
                                    if(jsonobject.has("currency")) {
                                        currency = jsonobject.getString("currency");
                                    }
                                    if(jsonobject.has("deal_slug")) {
                                        deal_slug = jsonobject.getString("deal_slug");
                                    }
                                    if(jsonobject.has("comment_option")) {
                                        comment_option = jsonobject.getString("comment_option");
                                    }
                                    categoryDealModel.setId(id);
                                    categoryDealModel.setCategory(category);
                                    categoryDealModel.setTitle(title);
                                    categoryDealModel.setDelivery(delivery);
                                    categoryDealModel.setDescription(description);
                                    //   categoryDealModel.setDiscount(discount);
                                    categoryDealModel.setImage(image);
                                    categoryDealModel.setTags(tags);
                                    categoryDealModel.setSeller_id(seller_id);
                                    categoryDealModel.setTiming(timing);
                                    categoryDealModel.setCurrency(currency);
                                    categoryDealModel.setDiscount(tkt_discounted_price);
                                    categoryDealModel.setPrice(price);
                                    categoryDealModel.setDeal_slug(deal_slug);
                                    categoryDealModel.setcomment_option(comment_option);
                                    categoryDealModelArrayList.add(categoryDealModel);


                                }


                                recyclerAdapterCategoryDeal = new RecyclerAdapterCategoryDeal(categoryDealModelArrayList, getActivity());


                                recyclerView.scheduleLayoutAnimation();

                                recyclerView.setAdapter(recyclerAdapterCategoryDeal);

                                recyclerView.addOnItemTouchListener
                                        (
                                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(View view, int position) {
                                                        int clickd_id =
                                                                Integer.parseInt(categoryDealModelArrayList.get(position).getId());
                                                        String deal_slug = categoryDealModelArrayList.get(position).getDeal_slug();
                                                        // int clickd_id = position + 1;
                                                        Intent i = new Intent(getActivity(), CategoryDealDetail.class);
                                                        i.putExtra("deal_id", clickd_id);
                                                        i.putExtra("deal_slug", deal_slug);
                                                        startActivity(i);


                                                    }
                                                })
                                        );
////

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("category_id", String.valueOf(category_id));

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        } else {
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



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

}
