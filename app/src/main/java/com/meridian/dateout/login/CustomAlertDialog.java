package com.meridian.dateout.login;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.meridian.dateout.Constants.analytics;

/**
 * Created by user 1 on 11-07-2016.
 */
public class CustomAlertDialog extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText editText;
    ProgressBar progress;
    String status;
    String REGISTER_URL = Constants.URL+"forgotpassword.php?";

    public CustomAlertDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customalert);
        //analytics.setCurrentScreen(CustomAlertDialog.this, con.getLocalClassName(), null /* class override */);

        editText = (EditText) findViewById(R.id.alert_email);
        progress = (ProgressBar) findViewById(R.id.progress_bars);
        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        yes = (Button) findViewById(R.id.btn_ok);
        no = (Button) findViewById(R.id.btn_cancel);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                final String eml = editText.getText().toString();
                if (eml.matches("")) {
                    Toast.makeText(c, "Empty Field", Toast.LENGTH_SHORT).show();

                } else {
                    NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(c);
                    boolean i = networkCheckingClass.ckeckinternet();
                    progress.setVisibility(View.VISIBLE);


                    if (i == true) {

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("statusforgot" + response + "xxxxx");

                                        if (response.contentEquals("\"success\"")) {

                                            Toast.makeText(c, "New password has been sent to Your Email", Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.INVISIBLE);
                                            dismiss();

                                        } else {

                                            Toast.makeText(c, "Please Enter Valid Email id", Toast.LENGTH_SHORT).show();
                                            progress.setVisibility(View.INVISIBLE);
                                        }


                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("email", eml);
                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(c);
                        requestQueue.add(stringRequest);
                    } else {

                        final SweetAlertDialog dialog = new SweetAlertDialog(c,SweetAlertDialog.NORMAL_TYPE);
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

                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }

    }
}