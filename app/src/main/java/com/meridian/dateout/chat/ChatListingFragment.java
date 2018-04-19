package com.meridian.dateout.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.ChatListModel;

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
 * Use the {@link ChatListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView txt;
    String id,category_name;
LinearLayout menu;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<ChatListModel> chatListModelArrayList;
    ChatlistAdapter chatlistAdapter;
    private OnFragmentInteractionListener mListener;

    public ChatListingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatListingFragment newInstance() {
        ChatListingFragment fragment = new ChatListingFragment();

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
        View view = inflater.inflate(R.layout.fragment_chat_listing, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_chatlist);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        txt= (TextView) view.findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) view.findViewById(R.id.menu);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        txt.setText("Email");
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        chat();
        return view;
    }

    private  void chat()
    {
        NetworkCheckingClass networkCheckingClass=new NetworkCheckingClass(getActivity());
        boolean i= networkCheckingClass.ckeckinternet();
        if(i==true) {


            String Schedule_url = Constants.URL+"email_category.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);

                            if (response != null) {
                                try {
                                    JSONArray jsonarray = new JSONArray(response);


                                    chatListModelArrayList=new ArrayList<>();


                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        ChatListModel chat_model = new ChatListModel();


                                        JSONObject jsonobject = jsonarray.getJSONObject(i);

                                        id = jsonobject.getString("id");
                                        category_name = jsonobject.getString("category_name");

                                        System.out.println("description" + category_name);

                                        chat_model.setId(id);
                                        chat_model.setCategory_name(category_name);



                                        chatListModelArrayList.add(chat_model);


                                    }
                                    if(chatListModelArrayList!=null) {
                                        chatlistAdapter = new ChatlistAdapter(getActivity(), chatListModelArrayList);

                                        recyclerView.scheduleLayoutAnimation();
                                        recyclerView.setAdapter(chatlistAdapter);
                                    }
                                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {


                                            try {
                                                sendEmail();
                                            }
                                            catch(Exception e)
                                            {

                                            }


                                        }

                                        protected void sendEmail() {
                                            Log.i("Send email", "");
                                            String[] TO = {"khong_esther@hotmail.com"};
                                            String[] CC = {""};
                                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                                            emailIntent.setData(Uri.parse("mailto:"));
                                            emailIntent.setType("text/plain");
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                                            emailIntent.putExtra(Intent.EXTRA_CC, CC);
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test Email");
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Date Out");

                                            try {
                                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                                //   finish();
                                                //Log.i("Finished sending email...", "");
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }));



                                } catch (JSONException e) {
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
                    params.put("id", String.valueOf(1));

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
