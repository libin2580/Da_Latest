package com.meridian.dateout.account.order;

/**
 * Created by SIDDEEQ DESIGNER on 3/16/2018.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;

import static com.meridian.dateout.Constants.analytics;


public class OrderMainFragment  extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout menu;
    Toolbar toolbar;
    TextView txt;
    public static ViewPager viewPager;
    TabLayout tabLayoutl;
    public  static OrderMain_Adapter orderMain_adapter;

    private OnFragmentInteractionListener mListener;

    public OrderMainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderMainFragment newInstance() {
        OrderMainFragment fragment = new OrderMainFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        View v=inflater.inflate(R.layout.activity_order_main, container, false);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        tabLayoutl=v.findViewById(R.id.remin_tab_layout);
        viewPager=v.findViewById(R.id.remin_view_pager);
        txt= (TextView)v. findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        txt.setText("Orders");
        orderMain_adapter=new OrderMain_Adapter(getChildFragmentManager());
        orderMain_adapter.addFragment(Upcominghistory.newInstance(),"Upcoming");
        orderMain_adapter.addFragment(Pasthistory.newInstance(),"History");
        viewPager.setAdapter(orderMain_adapter);

        tabLayoutl.setupWithViewPager(viewPager);
        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        return v;
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
