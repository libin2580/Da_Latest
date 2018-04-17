package com.meridian.dateout.rewards;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.PointModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PointHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PointHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView txt;
    LinearLayout menu;
    Toolbar toolbar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressBar progress_bar;
    String user_id,userid,profile_id,deal_name,deal_image,points_earned,purchase_amount,date,deal_currency;
    ArrayList<PointModel> list;
    //RecyclerView recycler_point_history;
    //PointAdapter pointAdapter;
    private OnFragmentInteractionListener mListener;
TextView txt_points_in_hand_date,txt_points_in_hand_points,txt_points_earned_date,txt_points_earned_points,txt_points_redeemed_date,txt_points_redeemed_points;
    ImageView img_points_earned_down,img_points_earned_up,img_points_redeemed_down,img_points_redeemed_up;
    LinearLayout linear_points_earned_expanded,linear_points_redeemed_expanded,linear_points_earned_arrow,linear_points_redeemed_arrow;
    RecyclerView recycler_points_earned,recycler_points_redeemed;
    EarnedPointsAdapter epa;
    RedeemedPointsAdapter rpa;
    String points_earned_arrow_flag="down",points_redeemed_arrow_flag="down";
    TextView txt_empty_earned,txt_empty_redeemed;
    public PointHistory()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment PointHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static PointHistory newInstance() {
        PointHistory fragment = new PointHistory();

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
        final View v= inflater.inflate(R.layout.fragment_point_history, container, false);
        progress_bar=(ProgressBar)v.findViewById(R.id.progress_bar);
        txt= (TextView) v.findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        txt.setText("POINTS HISTORY");
        toolbar = (Toolbar)v.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
     //   txt_points_in_hand_date=(TextView)v.findViewById(R.id.txt_points_in_hand_date);
        txt_points_in_hand_points=(TextView)v.findViewById(R.id.txt_points_in_hand_points);
       // txt_points_earned_date=(TextView)v.findViewById(R.id.txt_points_earned_date);
        txt_points_earned_points=(TextView)v.findViewById(R.id.txt_points_earned_points);
      //  txt_points_redeemed_date=(TextView)v.findViewById(R.id.txt_points_redeemed_date);
        txt_points_redeemed_points=(TextView)v.findViewById(R.id.txt_points_redeemed_points);

        img_points_earned_down=(ImageView) v.findViewById(R.id.img_points_earned_down);
        img_points_earned_up=(ImageView)v.findViewById(R.id.img_points_earned_up);
        img_points_redeemed_down=(ImageView)v.findViewById(R.id.img_points_redeemed_down);
        img_points_redeemed_up=(ImageView)v.findViewById(R.id.img_points_redeemed_up);

        linear_points_earned_expanded=(LinearLayout) v.findViewById(R.id.linear_points_earned_expanded);
        linear_points_redeemed_expanded=(LinearLayout) v.findViewById(R.id.linear_points_redeemed_expanded);
        linear_points_earned_arrow=(LinearLayout)v.findViewById(R.id.linear_points_earned_arrow);
        linear_points_redeemed_arrow=(LinearLayout)v.findViewById(R.id.linear_points_redeemed_arrow);

        recycler_points_earned=(RecyclerView) v.findViewById(R.id.recycler_points_earned);
        recycler_points_redeemed=(RecyclerView)v.findViewById(R.id.recycler_points_redeemed);
        LinearLayoutManager llm1 = new LinearLayoutManager(getActivity());
        recycler_points_earned.setLayoutManager(llm1);
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        recycler_points_redeemed.setLayoutManager(llm2);


        txt_empty_earned=(TextView)v.findViewById(R.id.txt_empty_earned);
        txt_empty_redeemed=(TextView)v.findViewById(R.id.txt_empty_redeemed);


        linear_points_earned_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_points_redeemed_down.setVisibility(View.VISIBLE);
                linear_points_redeemed_expanded.setVisibility(View.GONE);
                img_points_redeemed_up.setVisibility(View.GONE);
                points_redeemed_arrow_flag="down";

                if(points_earned_arrow_flag.equalsIgnoreCase("down")){
                    img_points_earned_down.setVisibility(View.GONE);
                    linear_points_earned_expanded.setVisibility(View.VISIBLE);
                    img_points_earned_up.setVisibility(View.VISIBLE);
                    points_earned_arrow_flag="up";
                }else{
                    img_points_earned_down.setVisibility(View.VISIBLE);
                    linear_points_earned_expanded.setVisibility(View.GONE);
                    img_points_earned_up.setVisibility(View.GONE);
                    points_earned_arrow_flag="down";
                }
            }
        });
        linear_points_redeemed_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_points_earned_down.setVisibility(View.VISIBLE);
                linear_points_earned_expanded.setVisibility(View.GONE);
                img_points_earned_up.setVisibility(View.GONE);
                points_earned_arrow_flag="down";

                if(points_redeemed_arrow_flag.equalsIgnoreCase("down")){
                    img_points_redeemed_down.setVisibility(View.GONE);
                    linear_points_redeemed_expanded.setVisibility(View.VISIBLE);
                    img_points_redeemed_up.setVisibility(View.VISIBLE);
                    points_redeemed_arrow_flag="up";
                }
                else
                    {
                    img_points_redeemed_down.setVisibility(View.VISIBLE);
                    linear_points_redeemed_expanded.setVisibility(View.GONE);
                    img_points_redeemed_up.setVisibility(View.GONE);
                    points_redeemed_arrow_flag="down";
                }
            }
        });


        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
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
        /*recycler_point_history= (RecyclerView) v.findViewById(R.id.recycler_point_history);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        recycler_point_history.setLayoutManager(llm);*/
        load();






        return  v;
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
    public  void load()
    {
        progress_bar.setVisibility(View.VISIBLE);
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        final boolean i = networkCheckingClass.ckeckinternet();
        if(i) {
            System.out.println("login....i" + i);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL+"point_reward_details.php?",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String points_in_hand="",points_in_hand_date="",points_earned="",points_redeemed="";
                            ArrayList<String>points_earned_name=new ArrayList<>();
                            ArrayList<String>points_earned_points=new ArrayList<>();
                            ArrayList<String>points_earned_ids=new ArrayList<>();
                            ArrayList<String>points_earned_date=new ArrayList<>();
                            ArrayList<String>points_redeemed_name=new ArrayList<>();
                            ArrayList<String>points_redeemed_points=new ArrayList<>();
                            ArrayList<String>points_redeemed_ids=new ArrayList<>();
                            ArrayList<String>points_redeemed_date=new ArrayList<>();

                            System.out.println("responseeeeelogin" + response);

                            try{
                                JSONObject jsonobj1=new JSONObject(response);
                                String status=jsonobj1.getString("status");
                                if(status.equalsIgnoreCase("success")){
                                    JSONObject dataObj=jsonobj1.getJSONObject("data");
                                    points_in_hand=dataObj.getString("point_in_hand");


                                    //points earned section --start
                                    JSONObject jsonobj2=dataObj.getJSONObject("points_earned");
                                    points_earned=jsonobj2.getString("total_points_earned");
                                    JSONArray jsonarray1=jsonobj2.getJSONArray("earned_deals");
                                    if(jsonarray1.length()>0){
                                        for(int i=0;i<jsonarray1.length();i++){
                                            JSONObject arrayObj=jsonarray1.getJSONObject(i);
                                            points_earned_ids.add(arrayObj.getString("deal_id"));
                                            points_earned_name.add(arrayObj.getString("deal_name"));
                                            points_earned_points.add(arrayObj.getString("point"));
                                            points_earned_date.add(arrayObj.getString("date"));
                                        }
                                    }
                                    //points earned section --end



                                    //points redeemed section --start
                                    JSONObject jsonobj3=dataObj.getJSONObject("points_redeemed");
                                    System.out.println("jsonobj3 : "+jsonobj3);
                                    points_redeemed=jsonobj3.getString("total_points_redeemed");
                                    JSONArray jsonarray2=jsonobj3.getJSONArray("rewards");
                                    if(jsonarray2.length()>0){
                                        for(int i=0;i<jsonarray2.length();i++){

                                            JSONObject arrayObj=jsonarray2.getJSONObject(i);
                                            points_redeemed_ids.add(arrayObj.getString("reward_id"));
                                            points_redeemed_name.add(arrayObj.getString("reward_name"));
                                            points_redeemed_points.add(arrayObj.getString("point"));
                                            points_redeemed_date.add(arrayObj.getString("date"));
                                        }
                                    }


                                }
                                else{


                                }
                                txt_points_in_hand_points.setText(points_in_hand);

                                txt_points_earned_points.setText(points_earned);

                                txt_points_redeemed_points.setText(points_redeemed);

                                System.out.println("points_earned_name.size() : "+points_earned_name.size());
                                if(points_earned_name.size()==0)
                                    txt_empty_earned.setVisibility(View.VISIBLE);
                                else
                                    txt_empty_earned.setVisibility(View.GONE);

                                epa=new EarnedPointsAdapter(getActivity(),points_earned_name,points_earned_points,points_earned_date);
                                recycler_points_earned.setAdapter(epa);



                                if(points_redeemed_name.size()==0)
                                    txt_empty_redeemed.setVisibility(View.VISIBLE);
                                else
                                    txt_empty_redeemed.setVisibility(View.GONE);


                                rpa=new RedeemedPointsAdapter(getActivity(),points_redeemed_name,points_redeemed_points,points_redeemed_date);
                                recycler_points_redeemed.setAdapter(rpa);
                                progress_bar.setVisibility(View.GONE);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                progress_bar.setVisibility(View.GONE);
                            }



                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                                                            progress_bar.setVisibility(View.GONE);

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
        }
        else
            {

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
}
