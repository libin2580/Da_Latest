package com.meridian.dateout.account.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.account.NotificationFragment;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.explore.RecyclerItemClickListener;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,id,event,loc,time,deal_date;
    RecyclerView recyclerView;
    String not=null;
    Toolbar toolbar;
    private ArrayList<OrderHistoryModel>OrderHistoryArraylist;
    private OrderHistoryModel ohm;
    ProgressBar progressbar;

    private OrderHistoryAdapter ohad;
    View customView;
    private PopupWindow mPopupWindow;
    ImageView close_order_history;
    private OnFragmentInteractionListener mListener;
    ImageView popup_imageview;
    String userid;
    TextView txt_empty;
    TextView  popup_txt_ordr_status,popup_txt_title,popup_txt_amount,popup_txt_booking_time,popup_txt_payment_status,popup_txt_points_earned,popup_txt_booking_date;
    WebView popup_txt_description;
    public OrderHistory() {

    }

    // TODO: Rename and change types and number of parameters
    public static OrderHistory newInstance() {
        OrderHistory fragment = new OrderHistory();

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
        FrameLayoutActivity.img_toolbar_crcname.setText("Order History");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        analytics = FirebaseAnalytics.getInstance(getActivity());

        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview1);
        txt_empty=(TextView)view.findViewById(R.id.txt_empty);
        try {


            SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        String    user_id = preferences.getString("user_id", null);

            if (user_id != null) {
                userid = user_id;
                System.out.println("userid" + userid);
            }

            SharedPreferences preferences1 = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
         String   profile_id = preferences1.getString("user_idfb", null);
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

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.scheduleLayoutAnimation();
        progressbar=(ProgressBar)view.findViewById(R.id.progressbar);
        LayoutInflater inflater2 = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater2.inflate(R.layout.order_history_popup, null);
        close_order_history=(ImageView)customView.findViewById(R.id.close_point_converter);
        popup_imageview=(ImageView)customView.findViewById(R.id.popup_imageview);
        popup_txt_title=(TextView) customView.findViewById(R.id.popup_txt_title);
        popup_txt_description=(WebView)customView.findViewById(R.id.popup_txt_description);
        popup_txt_amount=(TextView)customView.findViewById(R.id.popup_txt_amount);
        popup_txt_booking_time=(TextView)customView.findViewById(R.id.popup_txt_booking_time);
        popup_txt_payment_status=(TextView)customView.findViewById(R.id.popup_txt_payment_status);
        popup_txt_points_earned=(TextView)customView.findViewById(R.id.popup_txt_points_earned);
        popup_txt_ordr_status=(TextView)customView.findViewById(R.id. popup_txt_ordr_status);

        popup_txt_booking_date=(TextView)customView.findViewById(R.id.popup_txt_booking_date);
        close_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });








        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        final boolean i = networkCheckingClass.ckeckinternet();
        if(i) {

            new getOrderHistory().execute();

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
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        System.out.println("URI in kkkkkkkkkkkkkkkkkk ADAPTER : " + OrderHistoryArraylist.get(position).getTitle());
                        Glide.with(getActivity())
                                .load(OrderHistoryArraylist.get(position).getImage())
                                .into(popup_imageview);
                        popup_txt_title.setText(OrderHistoryArraylist.get(position).getTitle());
                        String text = "<html><body style='text-align:justify'> %s </body></Html>";
                        popup_txt_description.setBackgroundColor(Color.TRANSPARENT);
                        popup_txt_description.loadData(String.format(text,OrderHistoryArraylist.get(position).getDescription()), "text/html", "utf-8");
                        WebSettings webSettings = popup_txt_description.getSettings();
                        webSettings.setDefaultFontSize(15);
                        popup_txt_amount.setText("amount : "+OrderHistoryArraylist.get(position).getAmount());
                        popup_txt_booking_date.setText("booking date : "+OrderHistoryArraylist.get(position).getBooking_date());
                        popup_txt_booking_time.setText("booking time : "+OrderHistoryArraylist.get(position).getBooking_time());
                        popup_txt_payment_status.setText("payment status : "+OrderHistoryArraylist.get(position).getPayment_status());
                        popup_txt_points_earned.setText("points earned : "+OrderHistoryArraylist.get(position).getPoints_earned());
                        popup_txt_ordr_status.setText("Order Status : "+OrderHistoryArraylist.get(position).getOrder_status());



                        displayPopup();


                    }
                })
        );








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
    private class getOrderHistory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressbar.setVisibility(View.VISIBLE);
            txt_empty.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(Constants.URL+"orderHistory.php?user_id="+1);
           // String jsonStr = sh.makeServiceCall(Constants.URL+"orderHistory.php?user_id="+userid );

            if (jsonStr != null)
            {

                try
                {
                    OrderHistoryArraylist = new ArrayList<>();
                    JSONObject jsonObj=new JSONObject(jsonStr);
                    String status=jsonObj.getString("status");
                    if(status.equalsIgnoreCase("success"))
                    {
                        JSONArray dataArray=jsonObj.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++)
                        {
                            JSONObject obj=dataArray.getJSONObject(i);
                            ohm=new OrderHistoryModel();
                            ohm.setTitle(obj.getString("title"));
                            ohm.setImage(obj.getString("image"));
                            ohm.setDescription(obj.getString("description"));
                            ohm.setBooking_date(obj.getString("booking_date"));
                            ohm.setOrder_status(obj.getString("order_status"));
                            ohm.setAdult_total_price(obj.getString("adult_total_price"));
                            ohm.setAdult_number(obj.getString("adult_number"));
                            ohm.setChild_number(obj.getString("child_number"));
                            ohm.setChild_total_price(obj.getString("child_total_price"));
                            ohm.setQuantity(obj.getString("quantity"));
                            ohm.setBooking_time(obj.getString("booking_time"));
                            ohm.setPayment_status(obj.getString("payment_status"));
                            ohm.setAmount(obj.getString("amount"));
                            ohm.setPoints_earned(obj.getString("points_earned"));
                            ohm.setStripe_token(obj.getString("stripe_token"));
                            ohm.setTxn_id(obj.getString("txn_id"));
                            ohm.setCust_name(obj.getString("cust_name"));
                            ohm.setCust_email(obj.getString("cust_email"));
                            ohm.setCust_phone(obj.getString("cust_phone"));
                            ohm.setCust_address(obj.getString("cust_address"));
                            OrderHistoryArraylist.add(ohm);

                        }
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txt_empty.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"Your order history is empty Please Purchase", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(OrderHistoryArraylist.size()>0){
                ohad=new OrderHistoryAdapter(getActivity(),OrderHistoryArraylist);
                recyclerView.setAdapter(ohad);
            }
            progressbar.setVisibility(View.GONE);
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
}
