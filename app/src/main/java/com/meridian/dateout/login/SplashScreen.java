package com.meridian.dateout.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.iid.FirebaseInstanceId;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.fcm.Config;
import com.meridian.dateout.intro.New_WelcomeActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashScreen extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "DALoozQumWJx9L5GT8DzdaDkE";
    private static final String TWITTER_SECRET = "nOzRECtRDsoVDHNqN6u9WjvLTvIHJ02fSAVRkxYmlbHFzzN8j6";
    FirebaseAnalytics mFirebaseAnalytics;
    private static int SPLASH_TIME_OUT = 3000;
    String userid;
    FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        PackageInfo packageInfo;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", refreshedToken);
        editor.apply();
        String key = null;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "SplashScreen", null );
        try {
            //getting application package name, as defined in manifest
            String packageName = "com.meridian.dateout";

            //Retriving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));


                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
                System.out.println("hashh...." +  key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }


        SharedPreferences preferencesuser_id = getSharedPreferences("MyPref", MODE_PRIVATE);
        String   user_id = preferencesuser_id.getString("user_id", null);

        if (user_id != null) {
            userid = user_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences_fb_id = getSharedPreferences("myfbid", MODE_PRIVATE);
        String profile_id = preferences_fb_id.getString("user_idfb", null);
        if (profile_id != null) {
            userid = profile_id;
            System.out.println("userid" + userid);
        }
        SharedPreferences preferences_gmail_id =getSharedPreferences("value_gmail", MODE_PRIVATE);
        String profileid_gmail = preferences_gmail_id.getString("user_id", null);
        if (profileid_gmail != null) {
            userid = profileid_gmail;
            System.out.println("userid" + userid);
        }


        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        Boolean log = sp.getBoolean("Islogin",false);
        Boolean first = sp.getBoolean("first",false);

        System.out.println("logged user first :" + log);
        System.out.println("opened app skip intro :" + first);
        if(log==true||first==true)
        {
            if(userid!=null)
            {
                Intent intent = new Intent(this,FrameLayoutActivity.class); //call your ViewPager class
                startActivity(intent);
                finish();

            }
            else
            {
                Intent intent = new Intent(this, LoginActivity.class); //call your ViewPager class
                startActivity(intent);
                finish();
            }
        }
        else
        {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, New_WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);


        }
      //  shareIt();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)

                        Log.e("aaaaaaaaaaaaaaa11", String.valueOf(pendingDynamicLinkData));

                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {

                            analytics=FirebaseAnalytics.getInstance(SplashScreen.this);
                            deepLink = pendingDynamicLinkData.getLink();
                            Intent i=new Intent(SplashScreen.this, CategoryDealDetail.class);
                            i.putExtra("deeplink",String.valueOf(deepLink));
                            startActivity(i);

                            Log.e("aaaaaaaaaaaaaaa12", String.valueOf(deepLink));

                            FirebaseAppInvite invites=FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                            if(invites!=null)
                            {
                                String invitationId=invites.getInvitationId();
                                Log.e("aaaaaaaaaaaaaaa13", String.valueOf(invitationId));

                                if(!TextUtils.isEmpty(invitationId))
                                {
                                    Log.e("aaaaaaaaaaaaaaa13", String.valueOf(invitationId));
                                }
                            }

                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
    }
    private void shareIt1() {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/"))
                .setDynamicLinkDomain("rkag5.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.meridian.dateout")
                                .setMinimumVersion(125)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.meridian.wildlyyours")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("orkut")
                                .setMedium("social")
                                .setCampaign("example-promo")
                                .build())
                .setItunesConnectAnalyticsParameters(
                        new DynamicLink.ItunesConnectAnalyticsParameters.Builder()
                                .setProviderToken("123456")
                                .setCampaignToken("example-promo")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildDynamicLink();  // Or buildShortDynamicLink()

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.e("bbbbbbbbbbbbb", String.valueOf(dynamicLinkUri));

        Intent in = new Intent();
        String msg = dynamicLinkUri.toString();
        in.setAction(Intent.ACTION_SEND);
        in.putExtra(Intent.EXTRA_TEXT, msg);
        in.setType("text/plain");
        startActivity(in);


    }













    private void shareIt() {
              /*  Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Date Out");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, " ");
                sharingIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out "+"'"+title+"'"+" Deal in DateOut website \n\n" +
                                "http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/"+deal_slug);
                sharingIntent.setType("text/plain");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));*/

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/null"))
                .setDynamicLinkDomain("rkag5.app.goo.gl")
                // Set parameters
                // ...
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.meridian.dateout").build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.meridian.wildlyyours").build())
                .buildShortDynamicLink()
                .addOnCompleteListener(SplashScreen.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Log.e("aaaaaaaaaaaaaaa14", String.valueOf(shortLink));
                            Log.e("aaaaaaaaaaaaaaaa15",String.valueOf(flowchartLink));

                            Intent in=new Intent();
                            String msg=flowchartLink.toString();
                            in.setAction(Intent.ACTION_SEND);
                            in.putExtra(Intent.EXTRA_TEXT,msg);
                            in.setType("text/plain");
                            startActivity(in);


                        } else
                        {
                            Log.e("aaaaaaaaaaaaaaa16","errorloading");
                        }
                    }
                });



               /* DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("http://www.dateout.co.php56-27.phx1-2.websitetestlink.com/view_deal/null"))
                        .setDynamicLinkDomain("rkag5.app.goo.gl")
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.meridian.dateout").build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder("com.meridian.wildlyyours").build())
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();

                Log.e("aaaaaaaaaaa", String.valueOf(dynamicLinkUri));*/

    }





    @Override
    protected void onResume() {
        super.onResume();
    }
}
