package com.meridian.dateout.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.model.FacebookModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link FaceBukFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceBukFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<FacebookModel> facebookModelArrayList;
    String user_id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
  public static LoginButton loginButton;
    ProfileTracker profileTracker;
    CallbackManager callbackManager;
    View progressDialog;


    public FaceBukFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaceBukFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaceBukFragment newInstance(String param1, String param2) {
        FaceBukFragment fragment = new FaceBukFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        loginButton.setVisibility(View.GONE);
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {


                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("Main", response.toString());
                                        //displayMessage(profile);
                                        setProfileToView(object);


                                    }

                                    private void setProfileToView(JSONObject jsonObject) {
                                        try {
                                            String email = jsonObject.getString("email");
                                            String name = jsonObject.getString("name");
                                            String id = jsonObject.getString("id");

                                            //   String photo= jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");

                                            //  textView.setText(jsonObject.getString("email"));
                                            System.out.println("Email" + jsonObject.getString("email"));

                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("myfb", getActivity().MODE_PRIVATE).edit();
                                            editor.putString("emails", email);
                                            editor.putString("names", name);

                                            editor.commit();
//                                            recycler_inflate(id, name, email);
                                            Intent inn = new Intent(getActivity(),FrameLayoutActivity.class);

                                            inn.putExtra("name_fb",name);
                                            inn.putExtra("email_fb", email);
                                            inn.putExtra("profileid_fb",id);
                                            inn.putExtra("personPhoto_fb","sss");
                                            inn.putExtra("fb","fb");
                                            startActivity(inn);
                                            getActivity().finish();


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });



        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {



            }
        };


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fac_layout, container, false);
        progressDialog=view.findViewById(R.id.progress_bar);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       //profileTracker.stopTracking();
    }

    public void recycler_inflate(final String profile_idfinal, final String names, final String email) {
        facebookModelArrayList = new ArrayList<>();
        progressDialog.setVisibility(View.VISIBLE);
        String REGISTER_URL = Constants.URL+"registration.php?";
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   dealdetail :" + response);


                            try {
                                facebookModelArrayList = new ArrayList<>();

                                JSONObject jsonobject = new JSONObject(response);


                                FacebookModel facebookModel = new FacebookModel();
                                user_id = jsonobject.getString("user_id");
                                String fullname = jsonobject.getString("fullname");
                                String username = jsonobject.getString("username");
                                String photo = jsonobject.getString("photo");
                                String email = jsonobject.getString("email");
                                String phone = jsonobject.getString("phone");
                                String log_status = jsonobject.getString("log_status");
                                String facebook_id = jsonobject.getString("facebook_id");
                                facebookModel.setEmail(email);
                                facebookModel.setFullname(fullname);
                                facebookModel.setFacebook_id(facebook_id);
                                facebookModel.setLog_status(log_status);
                                facebookModel.setUser_id(user_id);
                                facebookModel.setUsername(username);
                                facebookModel.setPhoto(photo);
                                facebookModel.setPhone(phone);
                                facebookModelArrayList.add(facebookModel);

                                System.out.println("user_id" + user_id + fullname);


                                if (getActivity() != null)

                                {

                                    SharedPreferences preferences = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    if (user_id != null) {
//
                                        editor.putString("user_idfb", user_id);
                                    }


                                    editor.commit();
                                    progressDialog.setVisibility(View.GONE);
                                    Intent intent = new Intent(getActivity(),FrameLayoutActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();

                                }


////

                            } catch (JSONException e) {
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


                    System.out.println("name" + names + "profil" + profile_idfinal + "firstname" + email);

                    params.put("name", "nil");
                    params.put("username", names);
                    params.put("email", email);
                    params.put("password", "nil");
                    params.put("profile_pic", "fhgfg.png");
                    params.put("fb_id", profile_idfinal);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            requestQueue.add(stringRequest);
        } else {

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

    }
}
