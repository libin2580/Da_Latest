package com.meridian.dateout.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.MainActivity;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.LoginActivity;
import com.meridian.dateout.login.NetworkCheckingClass;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.analytics;

public class ChangePassword extends Fragment {
    EditText oldpassword, newpassword, edt_confirmpass;
    String oldpass, newpass, userid, confrmpass;
    Button save;
    ImageView clos;
    LinearLayout menu;
    Toolbar toolbar;
    boolean s, error = false;
    ProgressBar progress;
    TextView txt;
    String REGISTER_URL = Constants.URL+"changepassword.php?";
    public static ChangePassword newInstance() {
        ChangePassword fragment = new ChangePassword();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  FrameLayoutActivity.img_toolbar_crcname.setText("CHANGE PASSWORD");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_change_password, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), "AccountFragment", null /* class override */);

        progress = (ProgressBar)  view.findViewById(R.id.progress_bar);
        oldpassword = (EditText)  view.findViewById(R.id.edt_oldpassword);
        newpassword = (EditText)  view.findViewById(R.id.edt_newpassword);
        edt_confirmpass = (EditText) view. findViewById(R.id.edt_cnfrmpassword);
        save = (Button) view. findViewById(R.id.but_chngpaswrd);
        clos = (ImageView)  view.findViewById(R.id.close);
        menu= (LinearLayout) view. findViewById(R.id.menu);
        txt= (TextView) view. findViewById(R.id.toolbar_txt);
        toolbar = (Toolbar)  view.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        txt.setText("CHANGE PASSWORD");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                //startActivity(i);
                Intent i=new Intent(getActivity(), FrameLayoutActivity.class);
                startActivity(i);
                getActivity().finish();

            }
        });
       /* clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                startActivity(i);
            }
        });
*/
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        userid = preferences.getString("user_id", null);
        System.out.println("userid" + userid);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpass = oldpassword.getText().toString();
                newpass = newpassword.getText().toString();
                confrmpass = edt_confirmpass.getText().toString();
                s = checkPassWordAndConfirmPassword(newpass, confrmpass);
                if (oldpassword.getText().toString().isEmpty() || newpassword.getText().toString().isEmpty() || edt_confirmpass.getText().toString().isEmpty()) {

                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Empty Fields")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                  /*  final AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                    alertDialog.setTitle("");
                    // alertDialog.setIcon(R.drawable.warning_blue);
                    alertDialog.setMessage("Empty Fields");

                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });
                    alertDialog.show();
                    Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);*/
                    error = true;
                }
                if (s == false) {
                    edt_confirmpass.setError("Incorrect password");
                    error = true;
                } else {
                    error = false;
                }

                if (error == false) {
                    NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
                    boolean i = networkCheckingClass.ckeckinternet();
                    if (i) {

                        progress.setVisibility(ProgressBar.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {
                                        //   progress.setVisibility(ProgressBar.GONE);

                                        // JSONObject jsonObj = null;
                                        //  jsonObj = new JSONObject(response);
                                        System.out.println("responseeeee" + response);
                                        // statusd = jsonObj.getString("status");
                                        //  System.out.println("statussssscours"+statusd);
                                        // progress.setVisibility(ProgressBar.GONE);
                                        // pd.dismiss();
                                        if (response.contentEquals("\"success\"")) {
                                            final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                                            dialog.setTitleText("Password Reset")
                                                    .setContentText("Successfully Changed Password")

                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            progress.setVisibility(ProgressBar.GONE);
                                                            Intent is = new Intent(getActivity(), LoginActivity.class);
                                                            startActivity(is);
                                                            SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefN", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = preferences.edit();
//                                                editor.putString(" firstname", firstname);
//                                                editor.putString(" email", email);
//                                                editor.putString(" phon", phon);
//                                                editor.putString(" lastname",lastname);
//                                                editor.putString(" username", username);
                                                            getActivity().finish();
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                                          /*  AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                                            alertDialog.setTitle("Alert");
                                            alertDialog.setMessage(response);
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {


                                                            progress.setVisibility(ProgressBar.GONE);
                                                            Intent is = new Intent(getApplicationContext(), LoginActivity.class);
                                                            startActivity(is);
                                                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefN", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = preferences.edit();
//                                                editor.putString(" firstname", firstname);
//                                                editor.putString(" email", email);
//                                                editor.putString(" phon", phon);
//                                                editor.putString(" lastname",lastname);
//                                                editor.putString(" username", username);
                                                            finish();
                                                            dialog.dismiss();


                                                        }
                                                    });
                                            alertDialog.show();
*/
                                        } else {
                                            progress.setVisibility(ProgressBar.GONE);

                                            Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                                        dialog.setTitleText("Alert!")
                                                .setContentText(error.toString())

                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                             /*           AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setMessage(error.toString());
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
//                                        but_regcrc1.setBackgroundResource(R.color.butnbakcolr);
//                                        but_regcrc1.setTextColor(getResources().getColor(R.color.White));

                                                    }
                                                });
                                        alertDialog.show();
//*/
//                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                http:
//meridian.net.in/demo/etsdc/response.php?fid=1&email=" + email + "&phone=" + phon + "&name=" + fulnam + "&occupation=" + occ + "&location=" + loc + "&password=" + pass
                                params.put("c_password", oldpass);
                                params.put("new_password", newpass);
                                params.put("user_id", userid);

                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        int socketTimeout = 30000;//30 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(policy);
                        requestQueue.add(stringRequest);
                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("");
                        alertDialog.setMessage("Oops Your Connection Seems Off..");
                        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();


                            }
                        });
                        alertDialog.show();

                    }

                }
            }
        });

return  view;
    }

    public boolean checkPassWordAndConfirmPassword(String password, String confirmPassword) {
        boolean pstatus = false;
        if (confirmPassword != null && password != null) {
            if (password.equals(confirmPassword)) {
                pstatus = true;
            }
        }
        return pstatus;
    }


    public interface OnFragmentInteractionListener {
    }
}
