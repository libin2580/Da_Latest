package com.meridian.dateout.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.analytics;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    String firstName,lastName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button start;
   String user_id,str_fullname,str_email,photo,str_emails,str_names,str_fullname1,str_email1 ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Toolbar toolbar;
    LinearLayout back;

    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        FrameLayoutActivity.search_nearby.setVisibility(View.GONE);
        FrameLayoutActivity.my_location.setVisibility(View.GONE);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        FrameLayoutActivity.img_toolbar_crcname.setText("EMAIL");




        toolbar = (Toolbar)v.findViewById(R.id.toolbar_tops);

        back = (LinearLayout)v. findViewById(R.id.img_crcdtlnam);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        start = (Button) v.findViewById(R.id.chat_start);

        SharedPreferences preferences_user = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id=preferences_user.getString("user_id",null);
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        str_fullname = preferences.getString("fullname", null);
        str_email = preferences.getString("email", null);
        photo = preferences.getString("photo", null);

        SharedPreferences preferencesfb = getActivity().getSharedPreferences("myfb", MODE_PRIVATE);
        str_emails = preferencesfb.getString("emails", null);
        str_names = preferencesfb.getString("names", null);


        SharedPreferences preferences1 = getActivity().getSharedPreferences("value_google_user", MODE_PRIVATE);

        str_fullname1 = preferences1.getString("name", null);
        str_email1 = preferences1.getString("email", null);
        String google_photo = preferences1.getString("pic", null);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


                }
                if (firstName != null && lastName != null && str_emails != null) {

                    replacefragment(Email_Submit_Fragment.newInstance(), "chat_list");
                }
               else if((str_fullname != null && str_email != null)) {

                    replacefragment(Email_Submit_Fragment.newInstance(), "chat_list");
                }
                else if ((str_fullname1 != null && str_email1 != null )) {

                    replacefragment(Email_Submit_Fragment.newInstance(), "chat_list");

                }
                else
                {
                    replacefragment(Email_Submit_Fragment.newInstance(), "chat_list");
                }

            }
        });
        return v;
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

    public void replacefragment(Fragment fragment, String s) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.flFragmentPlaceHolder, fragment, s).addToBackStack(s);
        transaction.commit();
    }
}
