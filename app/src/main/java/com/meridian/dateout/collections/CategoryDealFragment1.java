package com.meridian.dateout.collections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.RecyclerItemClickListener;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.CategoryDealModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryDealFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryDealFragment1 extends Fragment {
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
    String seller_id,deal_slug;
    String currency,price,tkt_discounted_price;
    ImageView back;
    ImageView img_top_cal, img_top_faq;
    RelativeLayout relativeLayout1;

LinearLayout menu;
    Toolbar toolbar;
    TextView txt;

    RecyclerAdapterCategoryDeal recyclerAdapterCategoryDeal;
    String REGISTER_URL = Constants.URL+"dealsbycategory.php?";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoryDealFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CategoryDealFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryDealFragment1 newInstance() {
        CategoryDealFragment1 fragment = new CategoryDealFragment1();
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
//        category_id = getActivity().getIntent().getIntExtra("category_id", 0);
//        String name = getActivity().getIntent().getStringExtra("names");
        System.out.println("categry_id" + category_id);
      //  relativeLayout1.setVisibility(View.VISIBLE);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

      //  FrameLayoutActivity.img_toolbar_crcname.setText(name);
        FrameLayoutActivity.tabbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        relativeLayout1= (RelativeLayout) v.findViewById(R.id.relativeLayout1);
        //FrameLayoutActivity.img_top_places.setVisibility(View.GONE);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_categorydeal);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        img_top_cal = (ImageView) v.findViewById(R.id.img_top_calendar);
        txt= (TextView) v.findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        txt.setText(name);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        img_top_faq = (ImageView) v.findViewById(R.id.img_top_faq);




        FrameLayoutActivity.img_explore.setBackgroundResource(R.drawable.explore_click);

        FrameLayoutActivity.txt_explorenam.setTextColor(getResources().getColor(R.color.black));
        FrameLayoutActivity.img_collections.setBackgroundResource(R.drawable.collections);
        FrameLayoutActivity. txt_collctnz_nam.setTextColor(getResources().getColor(R.color.txtcolor_icons));
        FrameLayoutActivity.img_account.setBackgroundResource(R.drawable.account);
        FrameLayoutActivity. txt_accnt_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));
        FrameLayoutActivity.img_chat.setBackgroundResource(R.drawable.email_blue);
        FrameLayoutActivity. txt_chat_name.setTextColor(getResources().getColor(R.color.txtcolor_icons));



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
                                    categoryDealModel.setId(id);
                                    categoryDealModel.setCategory(category);
                                    categoryDealModel.setTitle(title);
                                    categoryDealModel.setDelivery(delivery);
                                    categoryDealModel.setDescription(description);

                                    categoryDealModel.setImage(image);
                                    categoryDealModel.setTags(tags);
                                    categoryDealModel.setSeller_id(seller_id);
                                    categoryDealModel.setTiming(timing);
                                    categoryDealModel.setCurrency(currency);
                                    categoryDealModel.setDiscount(tkt_discounted_price);
                                    categoryDealModel.setPrice(price);
                                    categoryDealModel.setDeal_slug(deal_slug);

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

                                                        Intent i = new Intent(getActivity(), CategoryDealDetail.class);
                                                        i.putExtra("deal_id", clickd_id);
                                                        i.putExtra("deal_slug", deal_slug);
                                                        startActivity(i);


                                                    }
                                                })
                                        );


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
}
