package com.meridian.dateout.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.account.order.OrderMainFragment;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.Googlelogin;
import com.meridian.dateout.login.LoginActivity;
import com.meridian.dateout.login.Vis_FilePath;
import com.meridian.dateout.reminder.ReminderMainFragment;
import com.meridian.dateout.rewards.RewardsMainFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    String firstName ;
    String lastName ;
    String str_emails ;
    String str_names ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 1;
    private static final int PICK_FILE_REQUEST = 1;
    private final String MY_value = "value";
    String str_fullname, str_email, str_fullname1, str_email1, photo, str_pic;
    LinearLayout linear_change, linear_login;
    ImageView log_image;
    TextView login;
    CircleImageView img_profile;
    GoogleApiClient mGoogleApiClient;
    TextView email;
    String filenamepath;
    ImageView profile_change_icon;
    LinearLayout linear_wallet, linear_faq, linear_notif, linear_favrt,lin_update_profile,lin_order_history, lin_remind;
    LinearLayout linear_credit;
    String str_fullname_fb;
    String image;
    TextView name;
    String selectedFilePath,user_id;
    RelativeLayout linearLayout8;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //    ImageView img_profile;
    String res_name,res_email,res_phone,res_image,res_location;
    ProgressBar progress_bar;
    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();

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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())

                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

         name = (TextView) v.findViewById(R.id.txt_acountname);
        email = (TextView) v.findViewById(R.id.txt_acountemail);
        login = (TextView) v.findViewById(R.id.txt_login);
        log_image = (ImageView) v.findViewById(R.id.image_login);
        linear_login = (LinearLayout) v.findViewById(R.id.lin_login);
        linear_notif = (LinearLayout) v.findViewById(R.id.lin_notification);

        linear_change = (LinearLayout) v.findViewById(R.id.lin_change);
        linear_change = (LinearLayout) v.findViewById(R.id.lin_change);
        lin_update_profile=(LinearLayout)v.findViewById(R.id.lin_update_profile);
        linear_wallet = (LinearLayout) v.findViewById(R.id.lin_wallet);
        linear_faq = (LinearLayout) v.findViewById(R.id.lin_faq);
        linear_credit = (LinearLayout) v.findViewById(R.id.lin_credit);
        linear_favrt = (LinearLayout) v.findViewById(R.id.lin_fav);
        img_profile = (CircleImageView) v.findViewById(R.id.imageView5);
        profile_change_icon= (ImageView) v.findViewById(R.id.imageView_profile_upload_icon);
        lin_order_history=(LinearLayout)v.findViewById(R.id.lin_order_history);
        //  img_profile= (ImageView) v.findViewById(R.id.imageView5);
        log_image = (ImageView) v.findViewById(R.id.image_login);
        progress_bar=(ProgressBar)v.findViewById(R.id.progress_bar);
        linearLayout8= (RelativeLayout) v.findViewById(R.id.linearLayout8);
        lin_remind= (LinearLayout) v.findViewById(R.id.lin_remind);
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        FrameLayoutActivity.img_toolbar_crcname.setText("My Account");
        FrameLayoutActivity.toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.search_nearby.setVisibility(View.GONE);
        FrameLayoutActivity.my_location.setVisibility(View.GONE);
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);
        FrameLayoutActivity.filter.setVisibility(View.GONE);
        // FrameLayoutActivity.img_top_cal.setVisibility(View.INVISIBLE);
        FrameLayoutActivity.cart.setVisibility(View.VISIBLE);
        SharedPreferences preferences_user = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id=preferences_user.getString("user_id",null);

        FrameLayoutActivity.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), Cart_details.class);
                startActivity(i);
            }
        });
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        str_fullname = preferences.getString("fullname", null);
        str_email = preferences.getString("email", null);
        photo = preferences.getString("photo", null);

        email.setVisibility(View.VISIBLE);
        SharedPreferences preferencesfb = getActivity().getSharedPreferences("myfb", MODE_PRIVATE);
        str_emails = preferencesfb.getString("emails", null);
        str_names = preferencesfb.getString("names", null);
        System.out.println("emails" + str_emails);
        System.out.println("names" + str_names);
        Glide
                .with(getActivity())
                .load(photo)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(img_profile);

        System.out.println("fullname" + str_fullname);
        System.out.println("str_email" + str_email);

        SharedPreferences preferences1 = getActivity().getSharedPreferences("value_google_user", MODE_PRIVATE);

        str_fullname1 = preferences1.getString("name", null);
        str_email1 = preferences1.getString("email", null);
        String google_photo = preferences1.getString("pic", null);


        FacebookSdk.sdkInitialize(getActivity());
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {

            }
        };
        profileTracker.startTracking();
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            firstName = profile.getFirstName();
            lastName = profile.getLastName();
            String pic = profile.getProfilePictureUri(500, 500).toString();
            String upperString1 =   firstName.substring(0,1).toUpperCase() + firstName.substring(1);
            String upperString2 =   lastName.substring(0,1).toUpperCase() + lastName.substring(1);
            System.out.println("name" + upperString1 + upperString2 );
            name.setText(upperString1 + "" + upperString2);
            img_profile.setImageURI(Uri.parse(pic));
            System.out.println("piccccc" + firstName + " " + lastName);
            System.out.println("pic" + pic);
            Glide
                    .with(getActivity())
                    .load(photo)//pic
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(img_profile);
            /// Picasso.with(getActivity()).load(pic).into(img_profile);

            linearLayout8.setVisibility(View.GONE);
            if (firstName != null && lastName != null && str_emails != null) {
                String upperString3 =   firstName.substring(0,1).toUpperCase() + firstName.substring(1);
                String upperString4 =   lastName.substring(0,1).toUpperCase() + lastName.substring(1);
                profile_change_icon.setVisibility(View.GONE);
                email.setText(str_emails);
                login.setText("Logout");
                name.setText(upperString3 + " " + upperString4);
                log_image.setImageResource(R.drawable.log_out);



            } else {
                login.setText("Login");
                linearLayout8.setVisibility(View.GONE);
                log_image.setImageResource(R.drawable.log_in);


            }
        }
        else if ((str_fullname1 != null && str_email1 != null ))
        {
            profile_change_icon.setVisibility(View.GONE);
            String upperString3 =   str_fullname1.substring(0,1).toUpperCase() +str_fullname1.substring(1);

            name.setText("" + upperString3);
            email.setText("" + str_email1);
            login.setText("Logout");
            Glide
                    .with(getActivity())
                    .load(photo)//google_photo
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                    .crossFade()
                    .into(img_profile);

            log_image.setImageResource(R.drawable.log_out);

            linearLayout8.setVisibility(View.GONE);
        }
        else if ((str_fullname != null && str_email != null)) {
            String upperString3 =   str_fullname.substring(0,1).toUpperCase() +str_fullname.substring(1);
            name.setText("" + upperString3);
            email.setText("" + str_email);
            login.setText("Logout");
            log_image.setImageResource(R.drawable.log_out);


        }
        else if(user_id==null)
        {

            email.setVisibility(View.GONE);
            name.setText("Guest");
            login.setText("Login");
            log_image.setImageResource(R.drawable.log_in);
            linearLayout8.setVisibility(View.GONE);
        }
        else {
            email.setVisibility(View.VISIBLE);
            login.setText("Login");
            linearLayout8.setVisibility(View.GONE);
            log_image.setImageResource(R.drawable.log_in);
            profile_change_icon.setVisibility(View.INVISIBLE);


        }

        profile_change_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {

                    requestPermissions();
                }
                showFileChooser();


            }
        });
        linear_favrt.setOnClickListener(new View.
                OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName != null && lastName != null && str_emails != null) {
                    replacefragment(FavrtFragment.newInstance(), "fav");
                }
                else if((str_fullname1 != null && str_email1 != null ))
                {
                    replacefragment(FavrtFragment.newInstance(), "fav");
                }
                else if((str_fullname != null && str_email != null)) {
                    replacefragment(FavrtFragment.newInstance(), "fav");
                }
                else
                {
                    com.nispok.snackbar.Snackbar.with(getContext()) // context
                            .text("Please Login..")
                            .color(Color.parseColor("#4797c4"))// text to display
                            .show((Activity) getContext());

                }

            }
        });
        lin_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstName != null && lastName != null && str_emails != null) {
//                    Intent i=new Intent(getActivity(), ReminderActivity.class);
//                    getActivity().startActivity(i);
                 replacefragment(ReminderMainFragment.newInstance(), "reminder");
                }
                else if((str_fullname1 != null && str_email1 != null )){
                  //  Intent i=new Intent(getActivity(),  ReminderActivity.class);
              //  getActivity().startActivity(i);
                    replacefragment(ReminderMainFragment.newInstance(), "reminder");

                }
                else if((str_fullname != null && str_email != null)) {
                //    Intent i=new Intent(getActivity(),  ReminderActivity.class);
                //    getActivity().startActivity(i);
                    replacefragment(ReminderMainFragment.newInstance(), "reminder");
                }
                else
                {
                    com.nispok.snackbar.Snackbar.with(getContext()) // context
                            .text("Please Login..")
                            .color(Color.parseColor("#4797c4"))// text to display
                            .show((Activity) getContext());

                }
            }
        });

        linear_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firstName != null && lastName != null && str_emails != null)
                {
System.out.println("not.....1.......");
                    replacefragment(NotificationFragment.newInstance(), "not");
                }
                else if((str_fullname1 != null && str_email1 != null ))
                {
                    System.out.println("not.....2.......");
                    replacefragment(NotificationFragment.newInstance(), "not");
                }
                else if((str_fullname != null && str_email != null))
                {
                    System.out.println("not.....3.......");
                    replacefragment(NotificationFragment.newInstance(), "not");
                }
                else
                {
                    com.nispok.snackbar.Snackbar.with(getContext()) // context
                            .text("Please Login..")
                            .color(Color.parseColor("#4797c4"))// text to display
                            .show((Activity) getContext());
                }
            }
        });

        lin_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replacefragment(OrderMainFragment.newInstance(), "f");
            }
        });

        linear_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firstName != null && lastName != null && str_emails != null) {
                    replacefragment(WalletFragment.newInstance(), "wallet");
                }
                else if((str_fullname1 != null && str_email1 != null ))
                {
                    replacefragment(WalletFragment.newInstance(), "wallet");
                }
                else if((str_fullname != null && str_email != null)) {

                    replacefragment(WalletFragment.newInstance(), "wallet");
                }

                else
                {
                    com.nispok.snackbar.Snackbar.with(getContext()) // context
                            .text("Please Login..")
                            .color(Color.parseColor("#4797c4"))// text to display
                            .show((Activity) getContext());
                }

            }
        });
        linear_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //  FrameLayoutActivity.lay_rewrd_name.performClick();
                if(firstName != null && lastName != null && str_emails != null) {

                }
                else if((str_fullname1 != null && str_email1 != null ))
                {
                    replacefragment(RewardsMainFragment.newInstance(), "rewrdmembership");
                }
                else if((str_fullname != null && str_email != null)) {

                    replacefragment(RewardsMainFragment.newInstance(), "rewrdmembership");
                }
                else
                {
                    com.nispok.snackbar.Snackbar.with(getContext()) // context
                            .text("Please Login..")
                            .color(Color.parseColor("#4797c4"))// text to display
                            .show((Activity) getContext());
                }



            }
        });


        linearLayout8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replacefragment(ChangePassword.newInstance(), "change");
            }
        });

        lin_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replacefragment(UpdateProfile.newInstance(), "update_profile");
            }
        });

        linear_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("")
                        .setContentText("Do you want to logout?")

                        .setConfirmText("OK")

                        .setCancelText("CANCEL")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener(){
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismiss();
                            }
                        })


                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                                preferences.edit().clear().commit();
                                SharedPreferences preferences_fb = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
                                preferences_fb.edit().clear().commit();

                                SharedPreferences preferences2 = getActivity().getSharedPreferences("myfb", MODE_PRIVATE);
                                preferences2.edit().clear().commit();
                                LoginManager.getInstance().logOut();
                                SharedPreferences preferences_google_id = getActivity().getSharedPreferences("value_gmail", MODE_PRIVATE);
                                preferences_google_id.edit().clear().commit();
                                SharedPreferences  preferences_google = getActivity().getSharedPreferences("value_google_user", MODE_PRIVATE);
                                preferences_google.edit().clear().commit();
                                SharedPreferences preferences_rerwrd= getActivity().getSharedPreferences("user_idnew", MODE_PRIVATE);
                                preferences_rerwrd.edit().clear().commit();

                                //  FaceBukFragment.loginButton.setVisibility(View.INVISIBLE);
                                if (str_fullname1 != null && str_email1 != null|| Googlelogin.personName != null || Googlelogin.email != null) {
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                            new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(Status status) {
                                                    // [START_EXCLUDE]
                                                    // updateUI(false);
                                                    // [END_EXCLUDE]


                                                }
                                            });
                                }
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        })
                        .show();

                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));



            }
        });
        System.out.println("FrameLayoutActivity.to_notification (in account frag): "+FrameLayoutActivity.to_notification);
        if(FrameLayoutActivity.to_notification.equalsIgnoreCase("true")){
            linear_notif.performClick();
        }
        return v;

    }
    public void replacefragment(Fragment fragment, String s) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
       // transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.flFragmentPlaceHolder, fragment, s).addToBackStack("s");
        transaction.commit();
    }
    private String returnValueFromBundles(String profileImageUrl) {
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();


    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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

    public void logoutFromFacebook() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Vis_Upload.."), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {

                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = Vis_FilePath.getPath(getActivity(), selectedFileUri);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {


                    filenamepath = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);

                    uploadVideo1(selectedFilePath,filenamepath);

                    System.out.println("Selected File Path:" + selectedFilePath+".........."+filenamepath);

                    Bitmap bmp = BitmapFactory.decodeFile(selectedFilePath);
                    img_profile.setImageBitmap(bmp);


                } else {
                    Toast.makeText(getActivity(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                    filenamepath = "null";
                }
            }
        }
    }

    private void uploadVideo1(final String file, final String filename ) {


        class UploadVideo extends AsyncTask<Void, Void, String> {

            ArrayList<String> mArrayUri2;
            Random generator = new Random();
            int rand = generator.nextInt(500) + 1;
            String doc_id;
            String response="";



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                System.out.println("SERVER REPLIED.............image...postexecute:"+image+s);

            }

            @Override
            protected String doInBackground(Void... params) {
                String charset = "UTF-8";

                String requestURL = Constants.URL+"updateprofilepic.php";


                try {

                    Vis_Uploads multipart = new Vis_Uploads(requestURL, charset);

                    multipart.addHeaderField("User-Agent", "CodeJava");
                    multipart.addHeaderField("Test-Header", "Header-Value");

                    multipart.addFormField("user_id",user_id);
                    multipart.addFormField("profile_pic",filename);


                    multipart.addFilePart("profile_pic",file);

                    System.out.println("update_profileee............"+ user_id+"...."+filename+"......"+file);
                    response = multipart.finish();


                    System.out.println("SERVER REPLIED:"+response);

                } catch (IOException ex) {
                    System.err.println(ex);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                System.out.println("Serverresponse:"+response);

                JSONObject jsonObject=null;
                if(response!=null)
                {
                    try {
                        jsonObject = new JSONObject(response);
                        String status =  jsonObject.getString("status");
                        image = jsonObject.getString("image");

                        System.out.println("Serverresponse...image:"+image);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                return  image;

            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Toast.makeText(getActivity(), "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();


                } else {


                    Toast.makeText(getActivity(), "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
                    getActivity().finish();

                }
                return;
            }


        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class getDetails extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress_bar_my.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler h = new HttpHandler();
            String s = h.makeServiceCall(Constants.URL+"view-profile.php?user_id="+user_id);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s!=null){
                try {
                    JSONObject jobj=new JSONObject(s);
                    String status=jobj.getString("status");
                    if(status.equalsIgnoreCase("true")){
                        JSONObject dataObj=jobj.getJSONObject("data");
                        res_name=dataObj.getString("name");
                        res_email=dataObj.getString("email");
                        res_phone=dataObj.getString("phone");
                        res_image=dataObj.getString("image");
                        res_location=dataObj.getString("location");
                        String upperString3 =   res_name.substring(0,1).toUpperCase() + res_name.substring(1);
                        name.setText("" + upperString3);

                        name.setText("" + upperString3);

                        if(res_image!=null) {

                            Glide
                                    .with(getActivity())
                                    .load(res_image)
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                    .crossFade()
                                    .into(img_profile);

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor edt=preferences.edit();
                            edt.putString("photo",res_image);
                            edt.putString("fullname",res_name);
                            edt.putString("username",res_name);
                            edt.putString("phone",res_phone);
                            edt.putString("location",res_location);
                            edt.commit();



                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();


                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new getDetails().execute();
    }
}