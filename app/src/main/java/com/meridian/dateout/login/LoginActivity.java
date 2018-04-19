package com.meridian.dateout.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.fcm.Config;
import com.meridian.dateout.model.FacebookModel;
import com.meridian.dateout.model.Login_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.analytics;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button login, bsignup;
    TextView Reg, txtnew, gst;
    EditText edtusrnam, edtpass;
    String usernam, pass, named;
    ProgressDialog pd;
    TextView fb, twtr, gpls, lnkdlin, frgt;
    WebView wv;
    ArrayList<Login_Model> loginModelArrayList = new ArrayList<>();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String eml, status;
    String  fullname, photo, phone, log_status, username;
    Login_Model login_model;
    String REGISTER_URL = Constants.URL+"login.php?";
    ProgressBar progress;
    String token;
    boolean doubleBackToExitPressedOnce = false;

    /*google login components --start*/
    private SignInButton signInButton;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private static int TIME = 3000;
    private int RC_SIGN_IN = 100;
    public static String personName, email, str_personphoto;
    private TextView textViewName;
    private TextView textViewEmail;
    private NetworkImageView profilePhoto;
    private ImageLoader imageLoader;
    /*google login components --end*/
    LinearLayout redister_lay;



    /*facebook login components --start*/
    ArrayList<FacebookModel> facebookModelArrayList;
    String user_id;
    public static LoginButton loginButton;
    ProfileTracker profileTracker;
    CallbackManager callbackManager;
    View progressDialog;

    /*facebook login components --end*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        edtusrnam = (EditText) findViewById(R.id.edit_usernm);
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        analytics = FirebaseAnalytics.getInstance(this);
        analytics.setCurrentScreen(this,"LoginActivity", null /* class override */);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        token  =pref.getString("regId", null);
        System.out.println("*******token"+token);
        edtpass = (EditText) findViewById(R.id.edt_pass);
        fb = (TextView) findViewById(R.id.txt_idfb);
        gst = (TextView) findViewById(R.id.guest);
        frgt = (TextView) findViewById(R.id.frgt);
        // twtr= (TextView) findViewById(R.id.txt_idtwtr);
        gpls = (TextView) findViewById(R.id.txt_id_googl);
        SharedPreferences prefs =getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        boolean first = Boolean.parseBoolean("true");
        prefs.edit().putBoolean("first", first).commit();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String chk = preferences.getString("email", null);
        SharedPreferences preferences1 = getApplicationContext().getSharedPreferences("test", MODE_PRIVATE);
        String chk1 = preferences.getString("email", null);
        //  if(chk!=null)
//        { Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(i);
//            finish();
//        }



        redister_lay=(LinearLayout)findViewById(R.id.redister_lay);


        /*google login components --start*/
        //Initializing Views
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);

        profilePhoto = (NetworkImageView) findViewById(R.id.profileImage);

        //Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("816167652294-rtnak331cn8jeklehpt4o1nnotqhr5vt.apps.googleusercontent.com")
                .build();

        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        /*google login components --end*/

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);






        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();



        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {



            }
        };




/*facebook login components --end*/



        wv = new WebView(this);

        frgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomAlertDialog cc = new CustomAlertDialog(LoginActivity.this);
                cc.show();


            }
        });


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loginButton.performClick();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>()
                        {
                            @Override
                            public void onSuccess(LoginResult loginResult)
                            {
                                // handleFacebookAccessToken(loginResult.getAccessToken());
                                getUserDetails();
                            }

                            @Override
                            public void onCancel()
                            {
                                Toast.makeText(LoginActivity.this,"cancel", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(FacebookException exception)

                            {
                                Toast.makeText(LoginActivity.this,"cancel", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

        gpls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();




            }
        });

        login = (Button) findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                log();

            }
        });
        Reg = (TextView) findViewById(R.id.txtregister);

        //   Reg.setLinkTextColor(Color.parseColor("#05c5cf"));


        redister_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setupWindowAnimations();
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        SpannableString content = new SpannableString("Here");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        Reg.setText(content);

        gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   setupWindowAnimations();


                Intent i = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                startActivity(i);
                finish();



            }
        });
    }
    protected void getUserDetails( ) {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try
                {
                    if(json != null)
                    {
                        loginButton.setReadPermissions(Arrays.asList("id","name","link","email","picture","gender"));
                        String email = json.getString("email");
                        String name = json.getString("name");
                        String id = json.getString("id");

                        System.out.println("Email" + json.getString("email"));

                        SharedPreferences.Editor editor = getSharedPreferences("myfb", MODE_PRIVATE).edit();
                        editor.putString("emails", email);
                        editor.putString("names", name);

                        editor.commit();

                        URL imageURL= null;
                        try {
                            imageURL = new URL("https://graph.facebook.com/"+id+"/picture?type=large");

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        Intent inn = new Intent(LoginActivity.this,FrameLayoutActivity.class);
                        inn.putExtra("name_fb",name);
                        inn.putExtra("email_fb", email);
                        inn.putExtra("profileid_fb",id);
                        inn.putExtra("personPhoto_fb",imageURL.toString());
                        inn.putExtra("fb","fb");
                        startActivity(inn);
                        finish();


                    }
                }

                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void log() {
        progress.setVisibility(View.VISIBLE);
        usernam = edtusrnam.getText().toString();
        pass = edtpass.getText().toString();
        if (usernam.matches("") || pass.matches("")) {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("Alert!")
                    .setContentText("Empty Fields")

                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            progress.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

        } else {



            NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
            final boolean i = networkCheckingClass.ckeckinternet();
            if (i) {
                System.out.println("login....i" + i);


                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("responseeeeelogin" + response);
                                JSONObject jsonObj = null;
                                loginModelArrayList = new ArrayList<Login_Model>();
                                try {
                                    // String user_id,fullname,username,photo,,,log_status;
                                    login_model = new Login_Model();

                                    if (response.contentEquals("\"failed\""))

                                    {
                                        progress.setVisibility(View.GONE);
                                        final SweetAlertDialog dialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.NORMAL_TYPE);

                                        dialog.setTitleText("")
                                                .setContentText("Invalid Username or Password")

                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


                                    } else {
                                        progress.setVisibility(View.GONE);

                                        jsonObj = new JSONObject(response);
                                        user_id = jsonObj.getString("user_id");
                                        fullname = jsonObj.getString("fullname");
                                        username = jsonObj.getString("username");
                                        photo = jsonObj.getString("photo");
                                        email = jsonObj.getString("email");
                                        phone = jsonObj.getString("phone");
                                        log_status = jsonObj.getString("log_status");
                                        // photo = jsonObj.getString("photo");


                                        login_model.setUser_id(user_id);
                                        login_model.setFullname(fullname);
                                        login_model.setUsername(username);
                                        login_model.setPhoto(photo);
                                        login_model.setPhone(phone);
                                        login_model.setEmail(email);
                                        login_model.setLog_status(log_status);

                                        loginModelArrayList.add(login_model);
                                        System.out.println("result" + response);

                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
//
                                        editor.putString("user_id", user_id);
                                        editor.putString("fullname", usernam);
                                        editor.putString("email", email);
                                        editor.putString("username", username);
                                        editor.putString("photo", photo);
                                        editor.commit();



                                        SharedPreferences prefs =getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                                        boolean Islogin = Boolean.parseBoolean("true");
                                        prefs.edit().putBoolean("Islogin", Islogin).commit();
                                        Intent is = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                                        startActivity(is);

                                        //   pd.dismiss();
                                        finish();

//                                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("username", usernam);
                        params.put("password", pass);
                        params.put("device_token",token);
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            } else {

                final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE);
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







    }



    @Override
    public void onBackPressed() {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
// dialog.dismiss();
                ;
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            com.nispok.snackbar.Snackbar.with(LoginActivity.this) // context
                    .text("Press again to EXIT")  // text to display
                    .color(Color.parseColor("#4797c4"))// text to display

                    .show(LoginActivity.this);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                    //MusicService.mp.stop();
                }
            }, 2000);

    }

        @Override
        protected void attachBaseContext(Context newBase) {
            super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        }


    /*google login components --start*/
    private void signIn() {
        //Creating an intent
        try{
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
              }
                });
    }catch (Exception e){
            e.printStackTrace();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            //Displaying name and email
            textViewName.setText(acct.getDisplayName());
            textViewEmail.setText(acct.getEmail());

            //Initializing image loader
            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                    .getImageLoader();


            personName = acct.getGivenName();
            final Uri personPhoto = acct.getPhotoUrl();
            final String profileid = acct.getId();
            email = acct.getEmail();
            if (email != null || personPhoto != null || profileid != null || personName != null) {

                System.out.println("acnt_sign pic.... ....  " + personPhoto);
                System.out.println("acnt_sign name .... ....  " + personName);
                System.out.println("acnt_sign email .... ....  " + email);
                System.out.println("acnt_sign id .... ....  " + profileid);


                //  recycler_inflate(personName,email,profileid);
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("value_google_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("name", personName);
                editor.putString("email", email);
                editor.putString("user_id", profileid);

                if (personPhoto != null) {
                    str_personphoto = personPhoto.toString();
                    editor.putString("pic", str_personphoto);

                } else {
                    str_personphoto = null;
                    editor.putString("pic", str_personphoto);
                }

                editor.commit();



                Intent inn = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                                inn.putExtra("name", personName);
                                inn.putExtra("email", email);
                                inn.putExtra("profileid", profileid);
                                inn.putExtra("personPhoto",personPhoto.toString());
                                inn.putExtra("go","go");
                startActivity(inn);






            }


        } else {

        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
