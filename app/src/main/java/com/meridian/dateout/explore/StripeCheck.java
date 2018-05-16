package com.meridian.dateout.explore;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.Constants.analytics;
import static com.meridian.dateout.explore.address.Adddetails.address_id1;

public class StripeCheck extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String PUBLISHABLE_KEY = "pk_test_loGbDLQBWWNK6OZyes300duF";
    public static final String SECRET_KEY = "sk_test_YHBkCPBz2DeYC3fSmuWIjHZH";
    EditText number,cvc;
    Spinner spinner_mnth,spinner_year;
    Button save;
    String comment;
    double check_price;
    double check_total;
    String price,total,stoken;

    ProgressBar progressBar;
    ImageView back;
    private static final int REQUEST_CODE = 1;
    String userid;
    int quantity;
    int flag_cvc=0,flag_mnth=0,flag_number=0,flag_year=0;
    private PopupWindow mPopupWindow;
    TextView txt_amount,txt_transaction,txt_pop_descriptn,txt_pop_time,txt_booking_code;
    Button ok_pop;
    static String spinmnth="mm",spinyear="yyyy";
    String android_id;
    List<Pair<String, String>> params;
    String address,phone,email,name,date,time,booking_id,coupon_code;
    View customView;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    RelativeLayout top_layout;
    String statusd,txn_id = null;
    ImageView Home;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.payment_checkout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater2.inflate(R.layout.pop_up_stripe, null);
        top_layout = (RelativeLayout) findViewById(R.id.top_layout);
        txt_amount = (TextView) customView.findViewById(R.id.txt_pop_Amnt);
        txt_transaction = (TextView) customView.findViewById(R.id.txt_pop_transactn);
        txt_pop_descriptn = (TextView) customView.findViewById(R.id.txt_pop_descrptn);
        txt_pop_time = (TextView) customView.findViewById(R.id.txt_pop_date_time);
        txt_booking_code = (TextView) customView.findViewById(R.id.txt_pop_booking_id);
        ok_pop = (Button) customView.findViewById(R.id.but_ok_transactn);
        SharedPreferences shared_pref = getSharedPreferences("shr_comment", MODE_PRIVATE);
        comment=  shared_pref.getString("comment",null);
        Home= (ImageView) findViewById(R.id.home);
        back = (ImageView) findViewById(R.id.img_crcdtlnam);
        Home.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i  =new Intent(StripeCheck.this, FrameLayoutActivity.class);
            startActivity(i);
        }
        });
        analytics = FirebaseAnalytics.getInstance(StripeCheck.this);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        SharedPreferences preferences_coupon_code =getApplicationContext().getSharedPreferences("coupon_code", MODE_PRIVATE);
        coupon_code=preferences_coupon_code.getString("coupon_code",null);
            back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
            });
        android_id = Settings.Secure.getString(StripeCheck.this.getContentResolver(),Settings.Secure.ANDROID_ID);
        try {

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            String  user_id = preferences.getString("user_id", null);

            if (user_id != null) {
                userid = user_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("myfbid", MODE_PRIVATE);
            String  profile_id = preferences1.getString("user_idfb", null);
            if (profile_id != null) {
                userid = profile_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("value_gmail", MODE_PRIVATE);
            String profileid_gmail = preferences2.getString("user_id", null);
            if (profileid_gmail != null) {
                userid = profileid_gmail;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences_user_id =getApplicationContext().getSharedPreferences("user_idnew", MODE_PRIVATE);
            SharedPreferences.Editor editor =preferences_user_id.edit();
            editor.putString("new_userid",  userid);
            editor.apply();

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }


        number=  (EditText) findViewById(R.id.number);
        spinner_mnth=(Spinner) findViewById(R.id.expMonth);
        spinner_year=(Spinner) findViewById(R.id.expYear);
        cvc= (EditText) findViewById(R.id.cvc);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar_check);
        save= (Button) findViewById(R.id.save);
        address=CheckOutActivity.address_add;
        email=CheckOutActivity.addemail;
        phone=CheckOutActivity.addphone;
        name=CheckOutActivity.addfulname;
        String[] monthValues = getResources().getStringArray(R.array.month_array);
        String[] yearValues = getResources().getStringArray(R.array.year_array);
        spinner_mnth.setSelection(monthValues.length - 1);
        spinner_year.setSelection(yearValues.length-1);

        spinner_mnth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                spinmnth=  spinner_mnth.getSelectedItem().toString();
                System.out.println("spin...mnth"+ spinmnth);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                spinmnth="mm";

            }

        });



        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                spinyear= spinner_year.getSelectedItem().toString();
                System.out.println("spin..year"+spinyear);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                spinyear="yyyy";
                System.out.println("spin..year"+spinyear);

            }

        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Stripe stripe = null;
                Card card = null;

                if(spinmnth.contentEquals("mm")||spinyear.contentEquals("yyyy"))
                {


                    final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("Empty fields!")
                            .setContentText("Please select valid Year and month")

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
                else {
                    System.out.println("card..deatilss"+number.getText().toString()+Integer.parseInt(spinmnth)+Integer.parseInt(spinyear)+ cvc.getText().toString());
                    card = new Card(number.getText().toString(), Integer.parseInt(spinmnth), Integer.parseInt(spinyear), cvc.getText().toString());
                    if (!card.validateCVC() && isValidcvc(cvc.getText().toString()) == false) {
                        flag_cvc = 0;


                        final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("")
                                .setContentText("Enter Valid CVC")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismiss();
                                        progressBar.setVisibility(ProgressBar.GONE);
                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


                    } else {
                        flag_cvc = 1;
                    }
                    if (!card.validateExpMonth()) {
                        flag_mnth = 0;


                        final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("")
                                .setContentText("Enter Valid mnth")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismiss();
                                        progressBar.setVisibility(ProgressBar.GONE);
                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


                    } else {
                        flag_mnth = 1;
                    }
                    if (!card.validateExpYear()) {
                        flag_year = 0;


                        final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("")
                                .setContentText("Enter Valid year")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismiss();
                                        progressBar.setVisibility(ProgressBar.GONE);
                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                    } else {
                        flag_year = 1;
                    }

                    if (!card.validateNumber()) {
                    flag_number=0;

                        final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this, SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("")
                                .setContentText("Enter Valid number")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        progressBar.setVisibility(ProgressBar.GONE);
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                    } else {
                        flag_number = 1;
                    }

                    card.validateNumber();
                    card.validateCVC();

                    try {
                        stripe = new Stripe(PUBLISHABLE_KEY);
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }
                    if (flag_cvc== 1&&flag_mnth==1&&flag_year==1&flag_number==1) {

                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        stripe.createToken(
                                card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {

                                        stoken = token.getId();
                                        System.out.println("toast.........." + token.getId());

                                        token();
                                    }

                                    public void onError(Exception error) {

                                        progressBar.setVisibility(View.GONE);
                                        System.out.println("toast..........error" + error.toString());
                                        final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this,SweetAlertDialog.NORMAL_TYPE);
                                        dialog.setTitleText("")
                                                .setContentText("Transaction failed Please try again")

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
                        );
                    }


                }

            }
        });

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    public  void token()
    {

        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i) {

            if(userid!=null)
            {

                params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("stripe_api", SECRET_KEY));
                    add(new Pair<String, String>("stripe_token", stoken));
                    add(new Pair<String, String>("user_id", userid));
                    add(new Pair<String, String>("guest_device_token","0"));
                    add(new Pair<String, String>("address_id",address_id1));
                    add(new Pair<String, String>("coupon_code",coupon_code));


                }};
            }
            else {
                params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("stripe_api", SECRET_KEY));
                    add(new Pair<String, String>("stripe_token", stoken));
                    add(new Pair<String, String>("user_id","0"));
                    add(new Pair<String, String>("guest_device_token",android_id));
                    add(new Pair<String, String>("address_id",address_id1));
                    add(new Pair<String, String>("coupon_code",coupon_code));


                }};

            }
            System.out.println("s**********1111111111111111" + params);

            Fuel.post(URL1+"cart-checkout_test.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {

                    System.out.println("s**********" + s);
                    if(s != null && !s.isEmpty() && !response.equals("null")) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        System.out.println("responseeeee" + response);

                        try {
                            JSONObject jsonObj = new JSONObject(s);
                            statusd = jsonObj.getString("status");
                            if(statusd.equalsIgnoreCase("false")){
                                String message=jsonObj.getString("message");
                                final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this,SweetAlertDialog.NORMAL_TYPE);
                                dialog.setTitleText("Failed!")
                                        .setContentText(message)

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

            if(statusd.equalsIgnoreCase("failed")){
                String message=jsonObj.getString("message");

                final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this,SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Failed!")
                        .setContentText(message)

                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                }else {
                if(jsonObj.has("txn_id")) {
                    txn_id = jsonObj.getString("txn_id");
                }
                if(jsonObj.has("amount")) {

                    total = jsonObj.getString("amount");
                }
                if(jsonObj.has("date")) {

                    date = jsonObj.getString("date");
                }
                if(jsonObj.has("time")) {

                    time = jsonObj.getString("time");
                }
                if(jsonObj.has("booking_code")) {

                    booking_id = jsonObj.getString("booking_code");
                }
                System.out.println("transaction json......" +txn_id);
                final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this,SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Success")
                        .setContentText("Payment Done successfully")

                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                displayPopup();
                                System.out.println("transaction pop uppp......" +txn_id);
                                txt_amount.setText(":"+total);
                                txt_transaction.setText(":"+txn_id);
                                txt_pop_descriptn.setText(":"+time);
                                txt_pop_time.setText(":"+date);
                                txt_booking_code.setText(":"+booking_id);
                                ok_pop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        mPopupWindow.dismiss();
                                        progressBar.setVisibility(ProgressBar.GONE);
                                        Intent is = new Intent(getApplicationContext(),FrameLayoutActivity.class);
                                        is.putExtra("tab_id",0);
                                        startActivity(is);
                                        finishAffinity();

                                    }
                                });

                                dialog.dismiss();


                            }
                        })
                        .show();
                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }



                    }


                }

                @Override
                public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

                }
            });

        }
        else
        {
            final SweetAlertDialog dialog = new SweetAlertDialog(StripeCheck.this,SweetAlertDialog.NORMAL_TYPE);
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

    private boolean isValidcvc(String cvc)
    {
        return cvc != null && cvc.length() < 4;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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


            mPopupWindow.showAtLocation(top_layout, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();


                } else {



                    Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
                    finish();

                }
                return;
            }

        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                TextView tv = (TextView) findViewById(R.id.otp);
                tv.setText(message);
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(StripeCheck.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE);
        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

}

