package com.meridian.dateout.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
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

import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.analytics;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavrtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavrtFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FavrtAdapter favrtAdapter;
    String id;
    String title;
    String image;
    String description;
    String discount;
    String timing;
    String delivery;
    String category;
    String tags;
    String seller_id;
    String deal_id;
    String deal_slug;
    ArrayList<String> arrayListtime, arrayListimage;
    String adult_age_range, adult_tkt_price, child_age_range, child_tkt_price, adult_discount_tkt_price, child_discount_tkt_price;
    String user_id;
    String REGISTER_URL = Constants.URL+"user_wishlist.php?";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String userid, profile_id,id_deal,category1,price,tkt_discounted_price,currency;
    RecyclerView recyclerView;
    ArrayList<CategoryDealModel> arrylistwsh;
    TextView empty_text,txt;
  LinearLayout menu;
    Toolbar toolbar;
String status;
    private OnFragmentInteractionListener mListener;

    public FavrtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FavrtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavrtFragment newInstance() {
        FavrtFragment fragment = new FavrtFragment();
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
        FrameLayoutActivity.img_toolbar_crcname.setText("Favourites");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favrt, container, false);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName() , null /* class override */);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_favrt);
        empty_text= (TextView) view.findViewById(R.id.empty_text);
        txt= (TextView) view.findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) view. findViewById(R.id.menu);
        txt.setText("My Wishes");
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        deal_id = getArguments().getString("id");
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id = preferences.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences1 = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
        profile_id = preferences1.getString("user_idfb", null);
        if (profile_id != null) {
            userid = profile_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences2 = getActivity().getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences2.getString("user_id", null);
        if (profileid_gmail != null) {
            userid = profileid_gmail;
            System.out.println("userid" + userid);
        }
        System.out.println("useridmypreflogin" + userid);
        System.out.println("userrrr" + user_id);


        FrameLayoutActivity.img_toolbar_crcname.setText("My Wishes");



        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        recycler_inflate();

        return view;
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
        System.out.println("attachhhh" + user_id);
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
        System.out.println("detattachhhh" + user_id);
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void recycler_inflate() {


        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealdetail :" + response);
                         if(response != null && !response.isEmpty() && !response.equals("null"))
                         {

                             try {

                                 JSONObject jsonObject=null;

                                 jsonObject=new JSONObject(response);
                                 if(jsonObject.has("status"))
                                 {
                                    JSONArray jsonArray=jsonObject.getJSONArray("status");
                                     for(int i=0;i<jsonArray.length();i++)
                                     {
                                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                         if(jsonObject1.has("status"))
                                         { status= jsonObject1.getString("status");

                                             System.out.println("statussss1........." + status);
                                             if (status.contentEquals("success") && !userid.equals(null))
                                             {


                                                 if(jsonObject.has("data"))
                                                 {
                                                     JSONArray jsonarray =null;
                                                     arrylistwsh = new ArrayList<>();

                                                     jsonarray=jsonObject.getJSONArray("data");
                                                     for (int j = 0; j < jsonarray.length(); j++)
                                                     {
                                                         CategoryDealModel categoryDealModel = new CategoryDealModel();


                                                         JSONObject jsonobject = jsonarray.getJSONObject(j);
                                                         if (jsonobject.has("id"))
                                                         {
                                                             id_deal = jsonobject.getString("id");
                                                         }
                                                         if (jsonobject.has("title"))
                                                         {
                                                             title = jsonobject.getString("title");
                                                         }
                                                         if (jsonobject.has("image"))
                                                         {
                                                             image = jsonobject.getString("image");
                                                         }
                                                         if (jsonobject.has("description"))
                                                         {
                                                             description = jsonobject.getString("description");
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
                                                             category1 = jsonobject.getString("category");
                                                         }
                                                         if (jsonobject.has("tags"))
                                                         {
                                                             tags = jsonobject.getString("tags");
                                                         }
                                                         if (jsonobject.has("price"))
                                                         {
                                                             price = jsonobject.getString("price");
                                                         }
                                                         if (jsonobject.has("tkt_discounted_price"))
                                                         {
                                                             tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                                         }
                                                         if (jsonobject.has("seller_id"))
                                                         {
                                                             seller_id = jsonobject.getString("seller_id");
                                                         }
                                                         if (jsonobject.has("currency"))
                                                         {
                                                             currency = jsonobject.getString("currency");
                                                         }
                                                         if (jsonobject.has("deal_slug"))
                                                         {
                                                            deal_slug = jsonobject.getString("deal_slug");
                                                         }


                                                         System.out.println("imagess" + image);
                                                         categoryDealModel.setTitle(title);
                                                         categoryDealModel.setCurrency(currency);
                                                         categoryDealModel.setPrice(price);
                                                         categoryDealModel.setDeal_slug(deal_slug);
                                                         categoryDealModel.setId(id_deal);
                                                         categoryDealModel.setDescription(description);
                                                         categoryDealModel.setDiscount(discount);
                                                         categoryDealModel.setDelivery(delivery);
                                                         categoryDealModel.setTiming(timing);
                                                         categoryDealModel.setSeller_id(seller_id);
                                                         categoryDealModel.setImage(image);
                                                         arrylistwsh.add(categoryDealModel);

                                                     }

                                                     favrtAdapter = new FavrtAdapter(arrylistwsh, getActivity());
                                                     recyclerView.setAdapter(favrtAdapter);
                                                     recyclerView.addOnItemTouchListener
                                                             (
                                                                     new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                                         @Override
                                                                         public void onItemClick(View view, int position) {
//
                                                                             int s = Integer.parseInt(arrylistwsh.get(position).getId());
                                                                             String deal_slug = arrylistwsh.get(position).getDeal_slug();
                                                                             System.out.println("idddddddddd..........." + s);
                                                                             Intent i = new Intent(getActivity(), CategoryDealDetail.class);
                                                                             i.putExtra("deal_id", s);
                                                                             i.putExtra("deal_slug", deal_slug);
                                                                             startActivity(i);


                                                                         }
                                                                     })
                                                             );
                                                 }


                                             }
                                             else {
                                                 System.out.println("statussss........." + status);
                                                 recyclerView.setVisibility(View.GONE);
                                                 empty_text.setVisibility(View.VISIBLE);
                                                 empty_text.setText("Your wish list is empty");

                                             }
                                         }

                                     }


                                 }

         }
                             catch (JSONException e)
                             {
                                 e.printStackTrace();
                             }
                         }
                         else {
                             final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                             dialog.setTitleText("Wish List")
                                     .setContentText("Your wishlist is empty")

                                     .setConfirmText("OK")
                                     .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                         @Override
                                         public void onClick(SweetAlertDialog sDialog) {

                                             dialog.dismiss();
                                             Intent i = new Intent(getActivity(),FrameLayoutActivity.class);
                                             startActivity(i);


                                         }
                                     })
                                     .show();
                             dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

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
                    http:
                    params.put("user_id", String.valueOf(userid));

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

}
