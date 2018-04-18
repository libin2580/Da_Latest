package com.meridian.dateout.sidebar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.model.Sidebar_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;

public class PrivacyActivity extends Fragment {
LinearLayout back;
    ArrayList<Sidebar_Model> sidebarlist;
    String id,title1,description1;
    TextView title,description;
    Toolbar toolbar;
    public static PrivacyActivity newInstance() {
    PrivacyActivity fragment = new PrivacyActivity();
    return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     // FrameLayoutActivity.img_toolbar_crcname.setText("PRIVACY POLICY");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
       // title= (TextView) view.findViewById(R.id.title);
        back = (LinearLayout)view. findViewById(R.id.img_crcdtlnam);
      //  fb= (ImageView)view. findViewById(R.id.fb);
        description= (TextView) view.findViewById(R.id.description);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        toolbar = (Toolbar)view.findViewById(R.id.toolbar_tops);
        toolbar.setVisibility(View.VISIBLE);
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        privacy();
        return  view;
    }

    private  void privacy()
    {
        NetworkCheckingClass networkCheckingClass=new NetworkCheckingClass(getActivity());
        boolean i= networkCheckingClass.ckeckinternet();
        if(i==true) {


            String Schedule_url = Constants.URL+"pages.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);

                            if (response != null) {
                                try {
                                    JSONArray jsonarray = new JSONArray(response);

                                    sidebarlist = new ArrayList<>();


                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        Sidebar_Model sidebar_model = new Sidebar_Model();


                                        JSONObject jsonobject = jsonarray.getJSONObject(1);
                                        id = jsonobject.getString("id");
                                        title1 = jsonobject.getString("title");
                                        description1 = jsonobject.getString("description");
                                        System.out.println("description"+description1);

                                        sidebar_model.setId(id);
                                        sidebar_model.setTitle(title1);
                                        sidebar_model.setDescription(description1);


                                        sidebarlist.add(sidebar_model);

                                        description.setText(description1);
                                  //  title.setText(title1);

                                    }


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
                    http://meridian.net.in/demo/etsdc/response.php?fid=1&email=" + email + "&phone=" + phon + "&name=" + fulnam + "&occupation=" + occ + "&location=" + loc + "&password=" + pass

                    params.put("id", String.valueOf(2));


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


}
