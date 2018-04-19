package com.meridian.dateout.coupon;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class CouponFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,id,event,loc,time,deal_date;
    String cpn_code,cpn_image,cpn_strt_date,cpn_exp_date,cpn_discount,cpn_discount_unit,cpn_desc,expire_in;
    RecyclerView recyclerView;
    LinearLayout menu;
    String category;
    TextView txt;
    Toolbar toolbar;
   static LinearLayout coupn_pop_up;
    ArrayList<CouponModel> notificationModelArrayList;
   CouponAdapter couponadapter;
   static TextView expire_date,start_date,coupn_code_pop,coupn_category;
    static Button but_push;
    private OnFragmentInteractionListener mListener;

    public CouponFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CouponFragment newInstance() {
       CouponFragment fragment = new CouponFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       // FrameLayoutActivity.img_toolbar_crcname.setText("Coupon");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_notlist);
        coupn_pop_up= (LinearLayout) view.findViewById(R.id.popup1);
        coupn_code_pop= (TextView) view.findViewById(R.id.coupn_code_pop);
        but_push= (Button) view.findViewById(R.id.but_push);
        txt= (TextView) view.findViewById(R.id.toolbar_txt);
        coupn_category= (TextView) view.findViewById(R.id.coupn_category);
        start_date= (TextView) view.findViewById(R.id.start_date);
        expire_date=(TextView) view.findViewById(R.id.expire_date);
        menu= (LinearLayout) view.findViewById(R.id.menu);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        txt.setText("Coupon");
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }


            }
        });
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        final boolean i = networkCheckingClass.ckeckinternet();
        if(i) {



            new GetContacts1().execute();

        }
        else {
            final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("Alert!")
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
    private class GetContacts1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constants.URL+"coupons.php");

            //  Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    notificationModelArrayList = new ArrayList<>();


                    for (int i = 0; i < jsonarray.length(); i++) {
                        CouponModel couponModel =new CouponModel();

                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        if(jsonobject.has("id")) {
                            id= jsonobject.getString("id");}
                        if(jsonobject.has("cpn_code")) {
                            cpn_code = jsonobject.getString("cpn_code");
                        }
                        if(jsonobject.has("cpn_image")) {
                            cpn_image = jsonobject.getString("cpn_image");
                        }
                        if(jsonobject.has("cpn_strt_date")) {
                            cpn_strt_date= jsonobject.getString("cpn_strt_date");
                        }
                        if(jsonobject.has("cpn_exp_date")) {
                            cpn_exp_date= jsonobject.getString("cpn_exp_date");
                        }
                        if(jsonobject.has("cpn_desc")) {
                            cpn_desc= jsonobject.getString("cpn_desc");
                        }
                        if(jsonobject.has("cpn_discount")) {
                            cpn_discount= jsonobject.getString("cpn_discount");
                        }
                        if(jsonobject.has("cpn_discount_unit")) {
                            cpn_discount_unit= jsonobject.getString("cpn_discount_unit");
                        }
                        if(jsonobject.has("expire_in")) {
                            expire_in= jsonobject.getString("expire_in");
                        }
                        if(jsonobject.has("category")) {
                           category= jsonobject.getString("category");
                        }
                         couponModel.setId(id);
                        couponModel.setCpn_code(cpn_code);
                        couponModel.setCpn_image(cpn_image);
                        couponModel.setCpn_strt_date(cpn_strt_date);
                        couponModel.setCpn_exp_date(cpn_exp_date);
                        couponModel.setCpn_desc(cpn_desc);
                        couponModel.setCpn_discount(cpn_discount);
                        couponModel.setCpn_discount_unit(cpn_discount_unit);
                        couponModel.setExpire_in(expire_in);
                        couponModel.setCategory(category);




                        notificationModelArrayList.add(couponModel);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //  Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(notificationModelArrayList!=null) {
            GridLayoutManager    lLayout = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(lLayout);
                couponadapter = new CouponAdapter(getActivity(), notificationModelArrayList);
                recyclerView.scheduleLayoutAnimation();
                recyclerView.setAdapter(couponadapter);

            }



        }

    }
}
