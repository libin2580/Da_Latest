package com.meridian.dateout.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.explore.ExploreFragment;
import com.meridian.dateout.explore.RecyclerItemClickListener;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.analytics;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,id,event,loc,time,deal_date,deal_id;
    RecyclerView recyclerView;
LinearLayout menu;
    TextView txt;
    String not=null;
    Toolbar toolbar;
    ArrayList<NotificationModel> notificationModelArrayList;
    NotificationAdapter notificationAdapter;
String user_id;
    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            not=getArguments().getString("not",null);
        }
        FrameLayoutActivity.img_toolbar_crcname.setText("Notification");

    }
    String    not_flag,rew_flag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("not...6.......");
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_notlist);
        FrameLayoutActivity.to_notification="false";
        System.out.println("FrameLayoutActivity.to_notification (in notifi frag): "+FrameLayoutActivity.to_notification);
        txt= (TextView) view.findViewById(R.id.toolbar_txt);
       final SharedPreferences preferences = getActivity().getSharedPreferences("NotPref", MODE_PRIVATE);
       not_flag = preferences.getString("not_flag", null);

        menu= (LinearLayout) view. findViewById(R.id.menu);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        txt.setText("Notification");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences preferencesuser_id = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                String   user_id = preferencesuser_id.getString("new_userid", null);*/
                SharedPreferences preferencesuser_id = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                user_id = preferencesuser_id.getString("user_id", null);
                System.out.println("<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>> "+user_id);

                if (user_id != null) {

if(not_flag!=null) {
    Intent i = new Intent(getActivity(), FrameLayoutActivity.class);
    i.putExtra("tab_id", 3);

    startActivity(i);
    getActivity().finish();
    preferences.edit().clear().apply();

}else {
    Intent i=new Intent(getActivity(), FrameLayoutActivity.class);
    i.putExtra("tab_id",0);

    startActivity(i);
    getActivity().finish();
}
                }
                else {
                    Intent i=new Intent(getActivity(), FrameLayoutActivity.class);
                    i.putExtra("tab_id",0);

                    startActivity(i);
                    getActivity().finish();
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


            String jsonStr = sh.makeServiceCall(Constants.URL+"notification.php");

           Log.e("notification", "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    notificationModelArrayList = new ArrayList<>();


                    for (int i = 0; i < jsonarray.length(); i++) {
               NotificationModel notificationModel=new NotificationModel();

                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        if(jsonobject.has("id")) {
                           id= jsonobject.getString("id");}
                        if(jsonobject.has("event_name")) {
                            event = jsonobject.getString("event_name");
                        }
                        if(jsonobject.has("location")) {
                            loc = jsonobject.getString("location");
                        }
                        if(jsonobject.has("time")) {
                            time= jsonobject.getString("time");
                        }
                        if(jsonobject.has("deal_date")) {
                            deal_date= jsonobject.getString("deal_date");
                        }

                        if(jsonobject.has("deal_id")) {
                            deal_id= jsonobject.getString("deal_id");
                        }
                        notificationModel.setDeal_id(deal_id);
                        notificationModel.setNot_event(event);
                        notificationModel.setNot_loc(loc);
                        notificationModel.setNot_time(time);
                        notificationModel.setNot_dealtme(deal_date);
                        notificationModelArrayList.add(notificationModel);
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
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(llm);
                notificationAdapter = new NotificationAdapter(getActivity(), notificationModelArrayList);
                recyclerView.scheduleLayoutAnimation();
                recyclerView.setAdapter(notificationAdapter);

                recyclerView.addOnItemTouchListener
                        (
                                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Log.e("notification", "clickkk " +notificationModelArrayList.get(position).getDeal_id());
                                        if(notificationModelArrayList.get(position).getDeal_id().contentEquals("nil")) {

                                        }
                                        else {
                                            Intent i = new Intent(getActivity(), CategoryDealDetail.class);

                                            i.putExtra("deal_id", Integer.parseInt( notificationModelArrayList.get(position).getDeal_id()));
                                            startActivity(i);
                                        }


                                    }
                                })
                        );


            }


        }

    }
}
