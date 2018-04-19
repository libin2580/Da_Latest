package com.meridian.dateout.rewards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.meridian.dateout.account.RecyclerViewAdapter;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.LoginActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.CreditsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.analytics;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RewardsMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RewardsMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String points;
    LinearLayout menu;
    Toolbar toolbar;
    String user_id,profile_id,rewards_text;
    TextView credits_shown,txt;

   ArrayList<CreditsModel> creditsModelArrayList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    private RewardsModel rm;
    private ArrayList<RewardsModel> all_rewardArraylist;
    private ArrayList<RewardsModel> my_rewardArraylist;
    View customView;
    private OnFragmentInteractionListener mListener;
    static int tabno;
    ViewPager viewPager;
    ImageView close_reward;
    public static  ImageView  rewrd_image;
    int pos_flag;
    LinearLayout lay_rewrd_details;
   public  static LinearLayout    pop_up_rewrd_info;
    LinearLayout linear_my_rewards,linear_all_rewards;
    TextView txt_my_rewards,txt_all_rewards;
    public static TextView rewrd_des,rewrd_points,rewrd_title,rewrd_current_points,member_type;
    public static ImageView type_image;
  public static String userid;
           String redeem_point,rewardtype,date;
    String title,image,discription,start_date,end_date,current_points,id;
    public RewardsMainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RewardsMainFragment newInstance() {
        RewardsMainFragment fragment = new RewardsMainFragment();

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
        View view = inflater.inflate(R.layout.datesout_credits, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        lay_rewrd_details= (LinearLayout) view.findViewById(R.id.lay_rewrd_details);
        LayoutInflater inflater2 = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater2.inflate(R.layout.point_converter, null);
     //   FrameLayoutActivity.toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.search_nearby.setVisibility(View.GONE);
        FrameLayoutActivity.my_location.setVisibility(View.GONE);
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        pop_up_rewrd_info= (LinearLayout) view.findViewById(R.id.pop_up_rewrd_info);
        FrameLayoutActivity.img_toolbar_crcname.setText("REWARDS");
       // credits_shown= (TextView) view.findViewById(R.id.credits_shown);
        rewrd_current_points= (TextView) view.findViewById(R.id.rewrd_current_points);
        type_image=(ImageView)view.findViewById(R.id.type_image);
        member_type=(TextView)view.findViewById(R.id.textView4);
        rewrd_des =(TextView) view.findViewById(R.id.rewrd_des);
        rewrd_title =(TextView) view.findViewById(R.id.rewrd_title);
        rewrd_points= (TextView) view.findViewById(R.id.rewrd_points);
        rewrd_image = (ImageView) view.findViewById(R.id.rewrd_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);
        close_reward= (ImageView) view.findViewById(R.id.close_reward);
        txt= (TextView) view.findViewById(R.id.toolbar_txt);
        txt.setText("REWARDS");
        menu= (LinearLayout) view. findViewById(R.id.menu);
        close_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up_rewrd_info.setVisibility(View.GONE);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        analytics = FirebaseAnalytics.getInstance(getActivity());

        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        linear_my_rewards=(LinearLayout)view.findViewById(R.id.linear_my_rewards);
        linear_all_rewards=(LinearLayout)view.findViewById(R.id.linear_all_rewards);
        txt_my_rewards=(TextView)view.findViewById(R.id.txt_my_rewards);
        txt_all_rewards=(TextView)view.findViewById(R.id.txt_all_rewards);

        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        // tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(2);
        new LoadViewPager().execute();
        viewPager.setCurrentItem(0);

        try {
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
            SharedPreferences preferences_user_id =getActivity().getSharedPreferences("user_idnew", MODE_PRIVATE);
            SharedPreferences.Editor editor =preferences_user_id.edit();
            editor.putString("new_userid",  userid);
          editor.commit();

        }
            catch (NullPointerException e)
        {

        }
        System.out.println("useridmypreflogin" + userid);
        lay_rewrd_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user_id!=null && user_id!="") {
                    replacefragment(MembershipFragment.newInstance(), "membership");
                }else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("LOGIN").
                            setContentText("Please login to avail this feature")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    dialog.dismiss();
                                    Intent i=new Intent(getActivity(),LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .setCancelText("CANCEL")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                }
            }
        });


        linear_my_rewards.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                if(user_id!=null && user_id!="") {

                    pos_flag = 1;

                    viewPager.setCurrentItem(1);

                    linear_my_rewards.setBackground(getResources().getDrawable(R.drawable.myreward));
                    linear_all_rewards.setBackground(getResources().getDrawable(R.drawable.allrewardsinactive));
                    txt_my_rewards.setTextColor(Color.parseColor("#ffffff"));
                    txt_all_rewards.setTextColor(Color.parseColor("#000000"));
                    MyRewardFragment af = new MyRewardFragment();
                }else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("LOGIN").
                            setContentText("Please login to avail this feature")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    dialog.dismiss();
                                    Intent i=new Intent(getActivity(),LoginActivity.class);
                                    startActivity(i);
                                }
                            })
                            .setCancelText("CANCEL")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                }

            }
        });
        linear_all_rewards.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                pos_flag=0;
                viewPager.setCurrentItem(0);

                linear_my_rewards.setBackground(getResources().getDrawable(R.drawable.myrewardinactive));
                linear_all_rewards.setBackground(getResources().getDrawable(R.drawable.allrewards));

                txt_my_rewards.setTextColor(Color.parseColor("#000000"));
                txt_all_rewards.setTextColor(Color.parseColor("#ffffff"));

                AllRewardsFragment vf=new AllRewardsFragment();

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        linear_all_rewards.performClick();
                        break;
                    case  1:
                        if(user_id!=null && user_id!="") {

                            linear_my_rewards.performClick();
                        }else {
                            linear_all_rewards.performClick();
                            final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                            dialog.setTitleText("LOGIN").
                                    setContentText("Please login to avail this feature")

                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {

                                            dialog.dismiss();
                                            Intent i=new Intent(getActivity(),LoginActivity.class);
                                            startActivity(i);
                                        }
                                    })
                                    .setCancelText("CANCEL")
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                        }

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class LoadViewPager extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    tabno = position;
                    switch (position){
                        case 0:
                            AllRewardsFragment fdv=new AllRewardsFragment();

                            return  fdv;
                        case 1:
                            MyRewardFragment fpv=new MyRewardFragment();
                            return fpv;
                        default: AllRewardsFragment tabf1 = new AllRewardsFragment();
                            return tabf1;
                    }
                }

                @Override
                public CharSequence getPageTitle(int position)
                {
                    if(position==0) {
                        return "All Rewards";

                    }
                    if(position==1) {
                        return "My Rewards";
                    }
                    else {
                        return  null;
                    }
                }
                @Override
                public int getCount() {
                    return 2;
                }
            });
        }
    }



    public  void load() {
        try{
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

                                if (status.equalsIgnoreCase("success")) {
                                    String data = jsonObj.getString("data");

                                    System.out.println("responseeeeeeeeeeeeeeeeee" + data);
                                    if (jsonObj.has("data")) {
                                        jsonobject1 = new JSONObject(data);
                                        if (jsonobject1.has("current_points")) {
                                            current_points = jsonobject1.getString("current_points");
                                            rewrd_current_points.setText(current_points + " Points");

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
                                                    rm.setId(id);
                                                    rm.setRewrd_title(title);
                                                    rm.setReward_image(image);
                                                    rm.setReward_des(discription);

                                                    rm.setRewrd_date(date);
                                                    rm.setRewrd_type(rewardtype);
                                                    rm.setRedeem_points(redeem_point);


                                                    all_rewardArraylist.add(rm);


                                                    //System.out.println("responseeeeeee@@@"+list.get(0).getImage());


                                                }
                                            }

                                            SharedPreferences preferences = getActivity().getSharedPreferences("rewards", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("all_rewards", jsonarray.toString());
                                            editor.commit();

                                        }
                                        if (jsonobject1.has("my_rewards")) {
                                            my_rewardArraylist = new ArrayList<>();


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

                                                        rm.setId(id);
                                                        rm.setRewrd_title(title);
                                                        rm.setReward_image(image);
                                                        rm.setReward_des(discription);
                                                        rm.setRewrd_date(date);

                                                        rm.setRewrd_type(rewardtype);
                                                        rm.setRedeem_points(redeem_point);
                                                        my_rewardArraylist.add(rm);


                                                        //System.out.println("responseeeeeee@@@"+list.get(0).getImage());


                                                    }
                                                }


                                                SharedPreferences preferences = getActivity().getSharedPreferences("rewards", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("my_rewards", jsonarray.toString());
                                                editor.commit();


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

                    if(user_id!=null && user_id!="") {
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
    }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void schedule() {

        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {


            String Schedule_url = Constants.URL+"user_credits.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);
                            if (response == null||response.isEmpty()||response.contentEquals("")) {
                                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                                dialog.setTitleText("Alert!")
                                        .setContentText("No credits")

                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                dialog.dismiss();
                                                getActivity().onBackPressed();
                                            }
                                        })
                                        .show();
                                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                        }
                        else {
                                try {


                                    JSONObject obj = new JSONObject(response);
                                    {

                                        creditsModelArrayList = new ArrayList<>();

                                        if (obj.has("credit") && !obj.getString("credit").equals("null")) {
                                            try {
                                                JSONArray jsonarray = obj.getJSONArray("credit");


                                                for (int i = 0; i < jsonarray.length(); i++) {


                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);


                                                    if (jsonobject.has("id")) {
                                                        String id = jsonobject.getString("id");

                                                    }
                                                    if (jsonobject.has("user_id")) {
                                                        String user_id = jsonobject.getString("user_id");

                                                    }
                                                    if (jsonobject.has("points")) {
                                                        points = jsonobject.getString("points");
                                                        credits_shown.setText(points);

                                                    }


                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        if (obj.has("history") && !obj.getString("history").equals("null")) {
                                            try {

                                                JSONArray jsonarray = obj.getJSONArray("history");

                                                for (int i = 0; i < jsonarray.length(); i++) {

                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                                                    CreditsModel creditsModel = new CreditsModel();
                                                    if (jsonobject.has("bk_id")) {
                                                        String bk_id = jsonobject.getString("bk_id");

                                                        creditsModel.setBk_id(bk_id);

                                                    }
                                                    if (jsonobject.has("title")) {
                                                        String title = jsonobject.getString("title");
                                                        creditsModel.setTitle(title);

                                                    }
                                                    if (jsonobject.has("bk_date")) {
                                                        String bk_date = jsonobject.getString("bk_date");
                                                        creditsModel.setBk_date(bk_date);

                                                    }
                                                    if (jsonobject.has("country")) {
                                                        String country = jsonobject.getString("country");
                                                        creditsModel.setTotal_price(country);

                                                    }
                                                    if (jsonobject.has("total_price")) {
                                                        String total_price = jsonobject.getString("total_price");
                                                        creditsModel.setTotal_price(total_price);

                                                    }

                                                    creditsModelArrayList.add(creditsModel);


                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    mAdapter = new RecyclerViewAdapter(getActivity(), creditsModelArrayList);


                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);

                                    recyclerView.setAdapter(mAdapter);


                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                }

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
                    params.put("user_id",userid);

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
    public void replacefragment(Fragment fragment, String s) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.flFragmentPlaceHolder, fragment, s).addToBackStack("s");
        transaction.commit();
    }
}
