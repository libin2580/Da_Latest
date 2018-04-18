package com.meridian.dateout.rewards;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.NetworkCheckingClass;

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
 * Use the {@link AllRewardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRewardsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView all_reward_recyclerview;
 public static ProgressBar progress_bar_all;
    private GridLayoutManager lLayout;
    private RewardsModel rm;
    private ArrayList<RewardsModel> all_rewardArraylist;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String userid,redeem_point,rewardtype,date;
String title,image,discription,start_date,end_date,current_points,id,user_type,user_type_image,rewrd_type_image;
    private OnFragmentInteractionListener mListener;

    public AllRewardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRewardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRewardsFragment newInstance(String param1, String param2) {
        AllRewardsFragment fragment = new AllRewardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        View view = inflater.inflate(R.layout.fragment_all_rewards, container, false);
        System.out.println("INSIDE ALL REWARD FRAGMENT");
        setRetainInstance(true);
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);


        lLayout = new GridLayoutManager(getActivity(), 2);
        all_reward_recyclerview=(RecyclerView)view.findViewById(R.id.all_reward_recyclerview);
        progress_bar_all =(ProgressBar)view.findViewById(R.id.progress_bar);
        all_reward_recyclerview.setHasFixedSize(true);
        all_reward_recyclerview.setLayoutManager(lLayout);

        try {


            SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
          String  user_id = preferences.getString("user_id", null);

            if (user_id != null) {
                userid = user_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences1 = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
          String  profile_id = preferences1.getString("user_idfb", null);
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
            SharedPreferences preferences_user_id =getActivity().getSharedPreferences("user_idnew", MODE_PRIVATE);
            SharedPreferences.Editor editor =preferences_user_id.edit();
            editor.putString("new_userid",  userid);
            editor.commit();

        }
        catch (NullPointerException e)
        {

        }


load();


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


    public  void load()
    {
        try {

            progress_bar_all.setVisibility(View.VISIBLE);
            NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
            final boolean i = networkCheckingClass.ckeckinternet();
            if (i) {
                System.out.println("login....i" + i);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL + "allrewards.php?",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("responseeeeelogin" + response);
                                JSONObject jsonObj = null;
                                JSONObject jsonobject1 = null;
                                try {
                                    System.out.println("registration result : " + response);
                                    jsonObj = new JSONObject(response);

                                    String status = jsonObj.getString("status");

                                    if (status.equalsIgnoreCase("success") || status.equalsIgnoreCase("failed")) {

                                        String data = jsonObj.getString("data");

                                        System.out.println("responseeeeeeeeeeeeeeeeee" + data);
                                        if (jsonObj.has("data")) {
                                            jsonobject1 = new JSONObject(data);
                                            if (jsonobject1.has("current_points")) {
                                                current_points = jsonobject1.getString("current_points");
                                                if (userid != null) {
                                                    RewardsMainFragment.rewrd_current_points.setText(current_points + " Points");
                                                }

                                            }
                                            if (jsonobject1.has("user_type")) {
                                                user_type=jsonobject1.getString("user_type");
                                                if (userid != null) {
                                                    RewardsMainFragment.member_type.setText(user_type);
                                                }
                                            }
                                            if (jsonobject1.has("user_type_image")) {
                                                user_type_image=jsonobject1.getString("user_type_image");
                                                if (userid != null) {
                                                    if(!user_type.equalsIgnoreCase("normal")){
                                                        try {
                                                            Glide
                                                                    .with(getActivity())
                                                                    .load(user_type_image)
                                                                    .centerCrop()
                                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                                    .crossFade()
                                                                    .into(RewardsMainFragment.type_image);
                                                        }
                                                        catch (Exception e)
                                                        {

                                                        }
                                                    }

                                                }


                                            }

                                            if (jsonobject1.has("all_rewards")) {
                                                all_rewardArraylist = new ArrayList<>();

                                                JSONArray jsonarray = jsonobject1.getJSONArray("all_rewards");
                                                System.out.println("responseeeeeeeeeeeeeeeeee22222" + jsonarray);

                                                if (jsonarray.length() > 0) {
                                                    for (int i = 0; i < jsonarray.length(); i++) {

                                                        rm = new RewardsModel();
                                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                        if (jsonobject.has("id")) {
                                                            id = jsonobject.getString("id");
                                                            System.out.println("all_rewards" + id);
                                                        }
                                                        if (jsonobject.has("title")) {
                                                            title = jsonobject.getString("title");
                                                            System.out.println("all_rewards" + title);
                                                        }
                                                        if (jsonobject.has("image")) {
                                                            image = jsonobject.getString("image");
                                                            System.out.println("all_rewards" + image);
                                                        }
                                                        if (jsonobject.has("description")) {
                                                            discription = jsonobject.getString("description");
                                                            System.out.println("all_rewards" + discription);
                                                        }
                                                        if (jsonobject.has("date")) {
                                                            date = jsonobject.getString("date");
                                                        }
                                                        if (jsonobject.has("redeempoint")) {
                                                            redeem_point = jsonobject.getString("redeempoint");
                                                            System.out.println("redeem_point" + redeem_point);
                                                        }
                                                        if (jsonobject.has("rewardtype")) {
                                                            rewardtype = jsonobject.getString("rewardtype");
                                                        }
                                                        if (jsonobject.has("rewrds_type_image")) {
                                                          rewrd_type_image = jsonobject.getString("rewrds_type_image");
                                                        }
                                                        rm.setId(id);
                                                        rm.setRewrd_title(title);
                                                        rm.setReward_image(image);
                                                        rm.setReward_des(discription);
                                                        rm.setRewrd_date(date);
                                                        rm.setRewrd_type(rewardtype);
                                                        rm.setRewrd_type_image(rewrd_type_image);
                                                        rm.setRedeem_points(redeem_point);
                                                        all_rewardArraylist.add(rm);

                                                        progress_bar_all.setVisibility(View.GONE);

                                                        //System.out.println("responseeeeeee@@@"+list.get(0).getImage());


                                                    }
                                                }

                                                RewardsAdapter rad = new RewardsAdapter(all_rewardArraylist, getActivity());
                                                all_reward_recyclerview.setAdapter(rad);


                                            }
                                            if (jsonobject1.has("my_rewards")) {
                                                ArrayList<RewardsModel> my_rewardArraylist = new ArrayList<>();


                                                if (jsonobject1.getString("my_rewards") != "null") {

                                                    JSONArray jsonarray = jsonobject1.getJSONArray("my_rewards");
                                                    System.out.println("my_rewards____arrayyyyy" + jsonarray);

                                                    if (jsonarray.length() > 0) {
                                                        for (int i = 0; i < jsonarray.length(); i++) {

                                                            rm = new RewardsModel();
                                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                            if (jsonobject.has("id")) {
                                                                id = jsonobject.getString("id");
                                                                System.out.println("all_rewards" + id);
                                                            }
                                                            if (jsonobject.has("title")) {
                                                                title = jsonobject.getString("title");
                                                                System.out.println("my_rewards" + title);
                                                            }
                                                            if (jsonobject.has("image")) {
                                                                image = jsonobject.getString("image");


                                                                System.out.println("my_rewards" + image);


                                                            }
                                                            if (jsonobject.has("description")) {
                                                                discription = jsonobject.getString("description");
                                                                System.out.println("my_rewards" + discription);
                                                            }
                                                            if (jsonobject.has("date")) {
                                                                date = jsonobject.getString("date");
                                                            }
                                                            if (jsonobject.has("redeempoint")) {
                                                                redeem_point = jsonobject.getString("redeempoint");
                                                            }
                                                            if (jsonobject.has("rewardtype")) {
                                                                rewardtype = jsonobject.getString("rewardtype");
                                                            }
                                                            if (jsonobject.has("rewrds_type_image")) {
                                                                rewrd_type_image = jsonobject.getString("rewrds_type_image");
                                                            }

                                                            rm.setId(id);
                                                            rm.setRewrd_title(title);
                                                            rm.setReward_image(image);
                                                            rm.setReward_des(discription);
                                                            rm.setRewrd_date(date);
                                                            rm.setRewrd_type(rewardtype);
                                                            rm.setRewrd_type(rewardtype);
                                                            rm.setRewrd_type_image(rewrd_type_image);
                                                            rm.setRedeem_points(redeem_point);
                                                            my_rewardArraylist.add(rm);
                                                            //System.out.println("responseeeeeee@@@"+list.get(0).getImage());
                                                        }

                                                    }



                                                } else {
                                                    System.out.println("no_rewards found.............");
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
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




                        if(userid!=null && userid!="") {
                            params.put("user_id", userid);
                        }else {
                            params.put("user_id", "null");
                        }

                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
