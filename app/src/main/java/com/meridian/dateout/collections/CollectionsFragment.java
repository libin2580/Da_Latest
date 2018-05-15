package com.meridian.dateout.collections;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.CollectionsAdapter1;
import com.meridian.dateout.explore.CollectionsAdapter2;
import com.meridian.dateout.explore.PreCachingLayoutManager;
import com.meridian.dateout.explore.RecyclerAdapterCategory;
import com.meridian.dateout.explore.RecyclerAdapterCategory1;
import com.meridian.dateout.explore.RecyclerItemClickListener;
import com.meridian.dateout.explore.Spinner_model1;
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
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.meridian.dateout.Constants.URL;
import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.Constants.analytics;
import static com.meridian.dateout.login.FrameLayoutActivity.txt_cart_number;
import static com.meridian.dateout.nearme.NearMeFragment.linear;
import static com.meridian.dateout.nearme.NearMeFragment.relative;


public class CollectionsFragment extends Fragment {


    static RecyclerView recyclerView,recyclerView_search;
    int mRowIndex = -1;
    String id, category, background, icon, _18plusOnly;
    ArrayList<CategoryModel> categoryModelArrayList;
    ArrayList<CategoryModel> categoryModelArrayList1;
    ArrayList<DealsModel> dealsModelArrayList;
    ArrayList<DealsModel> alldeals_categryModelArrayList;
    String title,image,description,timing,delivery,id_deal,category1, currency,tags, category_id, categorys,seller_id, tkt_discounted_price, price;
    ArrayList<String> all_background;
    CollectionsAdapter collectionsAdapter;
    CollectionsAdapter3 collectionsAdapter3;
    LinearLayout coordinatorLayout,sort_by_layout;
    ProgressBar progress, progress1;
    String userid,android_id;
    List<Pair<String, String>> params;
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
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);
        progress1= (ProgressBar) view.findViewById(R.id.progress1);
        FrameLayoutActivity.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), Cart_details.class);
                startActivity(i);
            }
        });



        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_collections);
        recyclerView_search = (RecyclerView) view.findViewById(R.id.recycler_collections1);
        get_explore();
        android_id = Settings.Secure.getString(getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);
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
                add(new Pair<String, String>("guest_device_token","0"));


            }};
        }
        else {
            params = new ArrayList<Pair<String, String>>() {{
                add(new Pair<String, String>("guest_device_token",android_id));
                add(new Pair<String, String>("user_id","0"));
                ;

            }};
        }
        cart_number();
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



        return view;
    }

    private void cart_number() {
        Fuel.post(URL1+"cart-totals.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String status = jsonObj.getString("status");
                    System.out.println("cart-totals**********" + s);
                    if(Objects.equals(status, "true")){
                        String data = jsonObj.getString("data");
                        JSONObject jsonObj1 = new JSONObject(data);
                        String total_items = jsonObj1.getString("total_items");
                        txt_cart_number.setText(total_items);
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





    public static Fragment newInstance() {
        CollectionsFragment fragment = new CollectionsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
    private void get_explore() {
        progress1.setVisibility(View.VISIBLE);
        all_background = new ArrayList<>();
        alldeals_categryModelArrayList=new ArrayList<>();
        categoryModelArrayList1=new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL +"all-deals-categories-banners.php",
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
                                        categoryModelArrayList1.add(categoryModel);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        PreCachingLayoutManager layoutManager1 = new PreCachingLayoutManager(getActivity());
                        recyclerView_search.setLayoutManager(layoutManager1);
                        recyclerView_search.setNestedScrollingEnabled(false);
                        recyclerView_search.setHasFixedSize(true);
                        recyclerView_search.setItemViewCacheSize(20);
                        recyclerView_search.setDrawingCacheEnabled(true);
                        recyclerView_search.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        if (alldeals_categryModelArrayList != null) {
                            collectionsAdapter3 = new CollectionsAdapter3(alldeals_categryModelArrayList, getActivity());
                            recyclerView_search.setAdapter(collectionsAdapter3);
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
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_search.setVisibility(View.GONE);

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

                collectionsAdapter3.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                collectionsAdapter3.filter(newText);
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
