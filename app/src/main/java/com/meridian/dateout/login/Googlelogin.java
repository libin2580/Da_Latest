package com.meridian.dateout.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.meridian.dateout.R;

import static com.meridian.dateout.Constants.analytics;

/**
 * Created by libin on 1/3/2017.
 */

public class Googlelogin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //Signin button
    private SignInButton signInButton;

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;
    private static int TIME = 3000;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    public static String personName, email, str_personphoto;
    //TextViews


    private TextView textViewName;
    private TextView textViewEmail;
    private NetworkImageView profilePhoto;

    //Image Loader
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_layout);
        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

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


        //Setting onclick listener to signing button
        signInButton.setOnClickListener(this);
    }


    //This function will option signing intent
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                inn.putExtra("personPhoto",personPhoto);
                inn.putExtra("go","go");
                startActivity(inn);
                finish();
            }



        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin

            signIn();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
