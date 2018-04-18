package com.meridian.dateout.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Use the {@link Email_Submit_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Email_Submit_Fragment extends Fragment {
  static   EditText name,email,contact,comment;
    Button sent;
    String name1,email1,contact1,comment1;
    LinearLayout lin,back;
    boolean edittexterror=false;
    LinearLayout menu;
    Toolbar toolbar;
    TextView txt;
   static String user_id,str_fullname,str_email,userid,profile_id;
    static String res_name,res_email,res_phone,res_image,res_location;
    static ProgressBar progress;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Email_Submit_Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Email_Submit_Fragment newInstance() {
        Email_Submit_Fragment fragment1 = new Email_Submit_Fragment();

        return fragment1;
    }
    public static Email_Submit_Fragment newInstance(String param1, String param2) {
        Email_Submit_Fragment fragment = new Email_Submit_Fragment();
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
        View view = inflater.inflate(R.layout.email_submit, container, false);
        name=(EditText) view.findViewById(R.id.email_txt_name);
        email=(EditText) view.findViewById(R.id.email_txt_email);
        contact=(EditText) view.findViewById(R.id.email_txt_contact);
        comment=(EditText) view.findViewById(R.id.email_txt_comment);
        lin=(LinearLayout) view.findViewById(R.id.lin_btn);
        back = (LinearLayout)view. findViewById(R.id.img_crcdtlnam);
        progress=(ProgressBar) view.findViewById(R.id.progress_bar);


        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);


        txt= (TextView) view.findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) view.findViewById(R.id.menu);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        txt.setText("Email");
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

        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        str_fullname = preferences.getString("fullname", null);
        str_email = preferences.getString("email", null);

        SharedPreferences preferencesfb = getActivity().getSharedPreferences("myfb", MODE_PRIVATE);
        String str_emails = preferencesfb.getString("emails", null);
        String str_names = preferencesfb.getString("names", null);
        System.out.println("emails" + str_emails);
        System.out.println("names" + str_names);

        SharedPreferences preferences1 = getActivity().getSharedPreferences("value_google_user", MODE_PRIVATE);

        String str_fullname1 = preferences1.getString("name", null);
        String str_email1 = preferences1.getString("email", null);

        SharedPreferences preferencesuser_id = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id = preferencesuser_id.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences_fb_id = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
        profile_id = preferences_fb_id.getString("user_idfb", null);
        if (profile_id != null) {
            userid = profile_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences_gmail_id =getActivity().getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences_gmail_id.getString("user_id", null);
        if (profileid_gmail != null) {
            userid = profileid_gmail;
            System.out.println("userid" + userid);
        }
        new getDetails().execute();

        System.out.println("+++++++++++++++++"+name.getText().toString()+email.getText().toString());




        sent=(Button)view.findViewById(R.id.btn_sent);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("+++++====???????????");
                if (name.getText().toString().isEmpty()) {
                    name.setError("Field cannot be left blank.");
                    edittexterror = true;

                      final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                     alertDialog.setTitle("Warning");
                      alertDialog.setMessage("Please fill required fields");
                }
                else if(email.getText().toString().isEmpty())
                {
                    email.setError("Enter email");

                    edittexterror = true;
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    email.setError("Enter email") ;
                    //error_msg.setVisibility(View.VISIBLE);
                    edittexterror = true;
                }
                else if(contact.getText().toString().isEmpty())
                {
                    contact.setError("Enetr Phone number");

                    edittexterror = true;
                }
                else if (comment.getText().toString().isEmpty()) {
                    comment.setError("Field cannot be left blank.");
                    edittexterror = true;


                }
                else
                {
                    edittexterror = false;
                  //  sub();
                }

                if(edittexterror==false) {
                  //  submit();
                    sub();

                }
            }

            private void sub() {


                System.out.println("+++++====");
                email1 =  email.getText().toString().trim();
                name1 = name.getText().toString().trim();
                contact1 =  contact.getText().toString().trim();
                comment1 = comment.getText().toString().trim();

                NetworkCheckingClass networkCheckingClass=new NetworkCheckingClass(getActivity());
                boolean i= networkCheckingClass.ckeckinternet();
                if(i==true) {


                    String Schedule_url = Constants.URL+"email_section_submit_form.php?";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    System.out.println("+++++++++++++++++"+name1+email1+contact1+comment1);
                                    System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);
                                    JSONObject jobj = null;
                                    try {
                                        jobj=new JSONObject(response);
                                        String status=jobj.getString("status");


                                        if(status.equalsIgnoreCase("true")) {
                                            String msg=jobj.getString("message");
                                            System.out.println("++++++++++++"+status+".."+msg);

                                            Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                                        }


                                    }
                                    catch (JSONException e)
                                    {

                                        e.printStackTrace();
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

                            params.put("name", name1);
                            params.put("email",email1);
                            params.put("phone",contact1);
                            params.put("comment",comment1);

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
        });



        return view;
    }
    public static class getDetails extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            System.out.println("++++++++++++++++++++++++++++++++++++userid++++++++++++++"+userid);

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler h = new HttpHandler();
            String s = h.makeServiceCall(Constants.URL+"view-profile.php?user_id="+userid);
            System.out.println("++++++++++++++++++++++++++++++++++++VIEW++++++++++++++"+s);
            if(s!=null){
                try {
                    JSONObject jobj=new JSONObject(s);
                    String status=jobj.getString("status");
                    if(status.equalsIgnoreCase("true")){
                        JSONObject dataObj=jobj.getJSONObject("data");





                        if (dataObj.has("name")) {
                            res_name=dataObj.getString("name");

                        }

                        if(dataObj.has("email"))
                        {
                            res_email=dataObj.getString("email");

                        }
                        if(dataObj.has("phone")){
                            res_phone=dataObj.getString("phone");

                        }
                        if(dataObj.has("image"))
                        {
                            res_image=dataObj.getString("image");
                        }
                        if(dataObj.has("location"))
                        {
                            res_location=dataObj.getString("location");

                        }


                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);

            name.setText(res_name);
            email.setText(res_email);
            contact.setText(res_phone);


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
