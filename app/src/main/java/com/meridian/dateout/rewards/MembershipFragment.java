package com.meridian.dateout.rewards;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.LoginActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.MembershipModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.meridian.dateout.Constants.analytics;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MembershipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MembershipFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String user_id;
    String userid;
    TextView txt;
    LinearLayout menu;
    Toolbar toolbar;
    String profile_id;
    String title;
    String image,discription;
    String point_stage,points_away;
    PieChart pieChart ;
    ArrayList<PieEntry> entries ;
  List<String> data;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    String title_reward,points_reward;
    ArrayList<String> sample1;
    String[] mParties;
    List<String>graph_data;
    String tit;
    String point_next,status_next;
    LinearLayout lin_chart1,lin_chart2;
    List<String> sample;
    TextView txt_register_date,available_points, txt_current_stage,txt_my_rewards;
    String register_year;
    String points_earned;
    String points_redeemd;
    String balance_points;
    String current_stage,register_date,current_points,rewards_earned,current_status;
    String status;
    public static ImageView img_gold,img_silver,img_platinum,img_diamond;
    ArrayList<MembershipModel>list;
    List<String>img;
    LinearLayout point_history;
    MembershipAdapter recycler_membership_adapter;
    private PopupWindow mPopupWindow;
    String current_stage_code,next_stage_code,next_stage;
    RecyclerView recycler_membership;
    View customView;
    private OnFragmentInteractionListener mListener;
    LinearLayout linear_point_converter;
    ImageView close_point_converter;
    EditText popup_edt_amount;
    TextView txt_converted_point;
    String points_away_from;
    ProgressBar progress_bar;
    LinearLayout lin_reward;
    TextView currentstage_text,nextstage_text,nextstage_color,current_stage_color;
    View customView1;
    LinearLayout linear_convert_button;
    ProgressBar popup_progressbar;
    TextView current_text_txt,current_point_txt,next_text_txt,next_point_txt;
    public MembershipFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MembershipFragment newInstance() {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_membership, container, false);
        point_history= (LinearLayout) v.findViewById(R.id.point_history);
        progress_bar=(ProgressBar)v.findViewById(R.id.progress_bar);
        txt= (TextView) v.findViewById(R.id.toolbar_txt);
        lin_reward= (LinearLayout) v.findViewById(R.id.lin_reward);
        data=new ArrayList<>();
        img_silver= (ImageView) v.findViewById(R.id.img_silver);
        img_gold= (ImageView) v.findViewById(R.id.img_gold);
        img_diamond= (ImageView) v.findViewById(R.id.img_diamond);
        img_platinum= (ImageView) v.findViewById(R.id.img_platinum);
        LayoutInflater inflater3 = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView1 = inflater3.inflate(R.layout.graph, null);
        close_point_converter=(ImageView)customView1.findViewById(R.id.close_point_converter);
        nextstage_text= (TextView) customView1.findViewById(R.id.nextstage_text);
        nextstage_color=(TextView)customView1.findViewById(R.id.color_next_stage);
        currentstage_text= (TextView) customView1.findViewById(R.id.curretstage_text);
        current_stage_color=(TextView)customView1.findViewById(R.id.color_currnt_stage);
        popup_progressbar=(ProgressBar)customView1.findViewById(R.id.popup_progressbar);
        current_text_txt= (TextView) customView1.findViewById(R.id.current_text_txt);
        current_point_txt= (TextView) customView1.findViewById(R.id.current_point_txt);
        next_text_txt= (TextView) customView1.findViewById(R.id.next_text_txt);
        next_point_txt= (TextView) customView1.findViewById(R.id.next_point_txt);
        close_point_converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        analytics = FirebaseAnalytics.getInstance(getActivity());

        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        menu= (LinearLayout) v. findViewById(R.id.menu);
        txt.setText("MEMBERSHIP INFO");
        toolbar = (Toolbar)v.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        linear_point_converter=(LinearLayout)v.findViewById(R.id.linear_point_converter);

        LayoutInflater inflater2 = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater2.inflate(R.layout.point_converter, null);
        close_point_converter=(ImageView)customView.findViewById(R.id.close_point_converter);
        popup_edt_amount =(EditText)customView.findViewById(R.id.popup_edt_point);
        txt_converted_point =(TextView)customView.findViewById(R.id.txt_converted_amount);
        linear_convert_button=(LinearLayout)customView.findViewById(R.id.linear_convert_button);
        popup_progressbar=(ProgressBar)customView.findViewById(R.id.popup_progressbar);
        close_point_converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        linear_convert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popup_edt_amount.getText().toString().length()==0){
                    Toast.makeText(getActivity(),"Enter your amount",Toast.LENGTH_SHORT).show();
                }
                else{

                    getAmount(popup_edt_amount.getText().toString());
                }
            }
        });

        linear_point_converter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayPopup();
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
        //SharedPreferences preferences3 = getActivity().getSharedPreferences("value_gmail", MODE_PRIVATE);
        if(userid!=null) {
            load();
        }
        else {
            progress_bar.setVisibility(View.GONE);
        }
        txt_register_date= (TextView) v.findViewById(R.id.date_membership);
        available_points= (TextView) v.findViewById(R.id.available_points);
        txt_current_stage= (TextView) v.findViewById(R.id.txt_current_stage);
        //txt_my_rewards= (TextView) v.findViewById(R.id.my_rewards);
       // img_first= (ImageView) v.findViewById(R.id.img_first);

        recycler_membership= (RecyclerView) v.findViewById(R.id.recycler_membership);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        recycler_membership.setLayoutManager(llm);
        point_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(userid!=null) {

                    replacefragment(PointHistory.newInstance(), "point");

                }
                else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Please login")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    dialog.dismiss();
                                    Intent i = new Intent(getActivity(),LoginActivity.class);
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
        lin_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userid!=null)
                {

                    displayPopup1();

                }
                else
                {

                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Please login")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {

                                    dialog.dismiss();
                                    Intent i = new Intent(getActivity(),LoginActivity.class);
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
        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
            {
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
    public void replacefragment(Fragment fragment, String s) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.flFragmentPlaceHolder, fragment, s).addToBackStack("s");
        transaction.commit();
    }

public  void load()
{
    progress_bar.setVisibility(View.VISIBLE);
    NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
    final boolean i = networkCheckingClass.ckeckinternet();
    if(i) {
        System.out.println("login....i" + i);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL+"rewards.php?",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                                    if (jsonobject1.has("plans")) {


                                        JSONArray jsonarray = jsonobject1.getJSONArray("plans");
                                        System.out.println("responseeeeeeeeeeeeeeeeee22222" + jsonarray);

                                        list = new ArrayList<>();
                                        img = new ArrayList<>();
                                        for (int i = 0; i < jsonarray.length(); i++) {

                                            MembershipModel mm = new MembershipModel();
                                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                                            if (jsonobject.has("title")) {
                                                title = jsonobject.getString("title");
                                                System.out.println("responseeeeeeeeeeeeeeeeee22" + title);
                                            }
                                            if (jsonobject.has("image")) {
                                                image = jsonobject.getString("image");



                                                System.out.println("responseeeeeeeeeeeeeeeeee22" + image);


                                            }
                                            if (jsonobject.has("description")) {
                                                discription = jsonobject.getString("description");
                                                System.out.println("responseeeeeeeeeeeeeeeeee22444" + discription);
                                            }
                                            if (jsonobject.has("point_stage")) {
                                                point_stage = jsonobject.getString("point_stage");
                                            }

                                            if (jsonobject.has("status")) {
                                                status = jsonobject.getString("status");
                                            }
                                            if(title.contentEquals("Silver"))
                                            {
                                                Picasso.with(getActivity()).load(image).into(img_silver);

                                            }
                                            else   if(title.contentEquals("Gold"))
                                            {
                                                Picasso.with(getActivity()).load(image).into(img_gold);
                                            }
                                            else   if(title.contentEquals("Platinum"))
                                            {
                                                Picasso.with(getActivity()).load(image).into(img_platinum);

                                            }
                                            else   if(title.contentEquals("Diamond"))
                                            {
                                                Picasso.with(getActivity()).load(image).into(img_diamond);

                                            }


                                            mm.setTitle(title);
                                            mm.setImage(image);
                                            mm.setDiscription(discription);
                                            mm.setPoint_stage(status);
                                            mm.setPoints_away(points_away);

                                            list.add(mm);


                                            //System.out.println("responseeeeeee@@@"+list.get(0).getImage());


                                        }


                                        // img.add(image);
                                        //  System.out.println("responseeeeeeeeeeeeeeeeee2222222222"+img);


                                    }

                                    recycler_membership_adapter = new MembershipAdapter(getActivity(), list);
                                    System.out.println("responseeeeeeeeeeeeeeeeeedataaa in listt" + list.get(1).getDiscription());
                                    recycler_membership.setAdapter(recycler_membership_adapter);
                                    if (jsonobject1.has("regiter_date")) {
                                        register_date = jsonobject1.getString("regiter_date");
                                        System.out.println("responseeeeeeeeeeeeeeeeee22333" + register_date);
                                        txt_register_date.setText(register_date);
                                    }
                                    if (jsonobject1.has("regiter_year")) {
                                        register_year = jsonobject1.getString("regiter_year");
                                    }
                                    if (jsonobject1.has("current_points")) {
                                        current_points = jsonobject1.getString("current_points");
                                        System.out.println("current_pointsss.......>>>>>" + current_points);
                                        if(userid!=null) {

                                            available_points.setText(current_points);

                                        }
                                        else {
                                            available_points.setText("");
                                        }

                                    }
                                    if (jsonobject1.has("rewards_earned")) {
                                        rewards_earned = jsonobject1.getString("rewards_earned");
                                        System.out.println("rewards_earned.......>>>>>" + rewards_earned);
                                        //   txt_my_rewards.setText(rewards_earned);
                                    }

                                    if (jsonobject1.has("points_redeemed")) {
                                        points_redeemd = jsonobject1.getString("points_redeemed");
                                    }
                                    if (jsonobject1.has("current_stage")) {
                                        current_stage = jsonobject1.getString("current_stage");
                                    }
                                    if (jsonobject1.has("points_away_from")) {
                                        points_away_from = jsonobject1.getString("points_away_from");
                                    }
                                    if (jsonobject1.has("status")) {
                                        current_status = jsonobject1.getString("status");
                                        txt_current_stage.setText(current_status);
                                    }
                                    if (jsonobject1.has("current_stage")) {
                                        current_stage = jsonobject1.getString("current_stage");
                                        currentstage_text.setText(current_stage);

                                    }
                                    if (jsonobject1.has("next_stage")) {
                                        next_stage = jsonobject1.getString("next_stage");
                                        nextstage_text.setText(next_stage);

                                      //  nextstage_color.setBackgroundColor(getResources().getColor( R.color.silver));

                                    }
                                    if (jsonobject1.has("current_stage_code")) {
                                        try {
                                            current_stage_code = jsonobject1.getString("current_stage_code");
                                            current_stage_color.setBackgroundColor(Color.parseColor(current_stage_code));
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    }
                                    if (jsonobject1.has("next_stage_code")) {
                                        try {
                                            next_stage_code = jsonobject1.getString("next_stage_code");
                                            nextstage_color.setBackgroundColor(Color.parseColor(next_stage_code));
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    }
                                }
                                progress_bar.setVisibility(View.GONE);

                            }


                            pieChart = (PieChart) customView1.findViewById(R.id.chart2);




                            pieChart.setMaxAngle(180f);
                            pieChart.setRotationAngle(180f);
                            pieChart.setRotationEnabled(false);
                            pieChart.setTransparentCircleColor(R.color.gray_circle);
                            pieChart.setTransparentCircleAlpha(0);
                            pieChart.setHoleColor(Color.WHITE);
                            pieChart.setEntryLabelColor(R.color.black);
                            pieChart.setCenterText(current_points);
                            pieChart.setCenterTextColor(Color.BLACK);
                            pieChart.setCenterTextSize(40f);
                            pieChart.setMinOffset(5);

                            pieChart.getDescription().setEnabled(false);
                            pieChart.getLegend().setEnabled(false);

                            int offset = (int)(300 * 1.0); /* percent to move */

                            LinearLayout.LayoutParams rlParams =
                                    (LinearLayout.LayoutParams) pieChart.getLayoutParams();
                            rlParams.setMargins(0, 0, 0, -offset);
                            pieChart.setLayoutParams(rlParams);



                            data.add(current_points);
                            data.add(points_away_from);
                            System.out.println("##@@" + current_points + ".." + points_away_from + ".." + data);
                            mParties = new String[]{current_stage, "to reach "+ next_stage};
                            System.out.println("##########<<<<<<<<<<<<<" + mParties.length + "..");

                            entries = new ArrayList<>();

                            ArrayList<Integer> s=new ArrayList<>();
                            s.add(R.color.colorPrimary);
                            s.add(R.color.colorPrimary);
                            pieDataSet = new PieDataSet(entries,"");
                            pieData = new PieData(pieDataSet);
                            pieData .setValueTextColor(Color.TRANSPARENT);
                            pieChart.highlightValues(null);
                            pieChart.setEntryLabelColor(Color.TRANSPARENT);

                            pieChart.setEntryLabelTextSize(12f);
                            pieChart.setData(pieData);
                            pieChart.animateX(3000);
                            setData(data.size(),12);
                            current_text_txt.setText(current_stage);
                            current_point_txt.setText(current_points);
                            next_text_txt.setText(next_stage);
                            next_point_txt.setText(points_away_from);
                         if(current_stage.contentEquals("Normal"))
                        {
                        pieDataSet.setColors((new int[] { R.color.normal, R.color.silver}), getActivity());



                                                                    }
                                            if(current_stage.contentEquals("Silver"))
                                            {
                                            pieDataSet.setColors((new int[] { R.color.silver,  R.color.gold}), getActivity());
                                            //    current_stage_color.setBackgroundColor(getResources().getColor( R.color.silver));
                                            //    nextstage_color.setBackgroundColor(getResources().getColor(  R.color.gold));

                                            }
                                            if(current_stage.contentEquals("Gold"))
{
pieDataSet.setColors((new int[] { R.color.gold,  R.color.platinum}), getActivity());


}
if(current_stage.contentEquals("Platinum"))
{
pieDataSet.setColors((new int[] { R.color.platinum,  R.color.diamond}), getActivity());

}



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    private void setData(int count, float range) {

                        float mult = range;



                        for (int i = 0; i < count; i++) {

                            String value = data.get(i);

                            entries.add(new PieEntry((float) (Integer.parseInt(value)),
                                    mParties[i % mParties.length], ""/*getResources().getDrawable(R.drawable.percent_2)*/));

                            System.out.println("##Entrties" + entries);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getAmount(final String point){
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i) {
            linear_convert_button.setVisibility(View.GONE);
            popup_progressbar.setVisibility(ProgressBar.VISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL+"point_converter.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            linear_convert_button.setVisibility(View.VISIBLE);
                            popup_progressbar.setVisibility(ProgressBar.GONE);
                            System.out.println("responseeeee" + response);
                            JSONObject jsonObject = null;
                            try {
                                System.out.println("registration result : " + response);
                                jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("success"))
                                {
                                    txt_converted_point.setVisibility(View.VISIBLE);
                                    txt_converted_point.setText(jsonObject.getString("points")+" points");

                                }
                                else
                                {
                                    txt_converted_point.setVisibility(View.GONE);
                                    linear_convert_button.setVisibility(View.VISIBLE);
                                    popup_progressbar.setVisibility(ProgressBar.GONE);
                                }

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                                txt_converted_point.setVisibility(View.GONE);

                                linear_convert_button.setVisibility(View.VISIBLE);
                                popup_progressbar.setVisibility(ProgressBar.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            linear_convert_button.setVisibility(View.VISIBLE);
                            popup_progressbar.setVisibility(ProgressBar.GONE);
                            txt_converted_point.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("amount", point);
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
                    .setContentText("Oops Your Connection Seems Off..s")

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
    public void displayPopup_rewrd_info() {

        try {
            System.out.println("inside display popup");
            mPopupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);


            mPopupWindow.showAtLocation(FrameLayoutActivity.activity_frame_layout, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayPopup1() {

        try {
            System.out.println("inside display popup");
            mPopupWindow = new PopupWindow(customView1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                mPopupWindow.setElevation(5.0f);
            }
            pieChart.animateY(3000);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setAnimationStyle(R.style.popupAnimation);


            mPopupWindow.showAtLocation(FrameLayoutActivity.activity_frame_layout, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
