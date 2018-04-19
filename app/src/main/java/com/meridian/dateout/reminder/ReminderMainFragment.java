package com.meridian.dateout.reminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.analytics;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReminderMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressLint("SimpleDateFormat")
public class ReminderMainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout menu;

    Toolbar toolbar;
    TextView txt;
    public static ViewPager viewPager;
    TabLayout tabLayoutl;
    public  static ReminderMain_Adapter reminderMain_adapter;
    UpcomingAdapter upcomingAdapter;
    PastAdapter pastAdapter;
    ArrayList<ReminderModel>   array_up=new ArrayList<>();
    ArrayList<PastModel>   array_up1=new ArrayList<>();
    RecyclerView recycle_upcoming,recycle_past;
    String userid,curent_month;
    String event_date2,event_month2;
    String id,event_type,event_name,event_date,event_time,event_details;
    ProgressBar progress;
    TextView view_no_reminder,tvcal;
    View custompopup_view;
    PopupWindow reminder_popupwindow;
    LinearLayout inflate_layout,layoutupcoming,layoutpast;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    CompactCalendarView compactCalendarView;
    ImageView showPreviousMonthBut,showNextMonthBut;
    String evnt_name,evnt_date,evnt_time,evnt_details;
    private OnFragmentInteractionListener mListener;

    public ReminderMainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReminderMainFragment newInstance() {
        ReminderMainFragment fragment = new ReminderMainFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_reminder_main, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        analytics = FirebaseAnalytics.getInstance(getActivity());

        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        recycle_upcoming=(RecyclerView)v.findViewById(R.id.recyler_upcoming);
        recycle_past=(RecyclerView)v.findViewById(R.id.recyler_past);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),1);
        GridLayoutManager llmm = new GridLayoutManager(getActivity(),1);
        recycle_upcoming.setLayoutManager(llm);
        recycle_past.setLayoutManager(llmm);
        progress = v. findViewById(R.id.progress_bar);
        try {


            SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
            String  user_id = preferences.getString("user_id", null);

            if (user_id != null) {
                userid = user_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences1 = getActivity().getSharedPreferences("myfbid", MODE_PRIVATE);
            String  profile_id = preferences1.getString("user_idfb", null);
            if (profile_id != null) {
                userid = profile_id;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences2 = getActivity().getSharedPreferences("value_gmail", MODE_PRIVATE);
            String profileid_gmail = preferences2.getString("user_id", null);
            if (profileid_gmail != null) {
                userid = profileid_gmail;
                System.out.println("userid" + userid);
            }
            SharedPreferences preferences_user_id =getActivity().getSharedPreferences("user_idnew", MODE_PRIVATE);
            SharedPreferences.Editor editor =preferences_user_id.edit();
            editor.putString("new_userid",  userid);
            editor.commit();

        }
        catch (NullPointerException e)
        {

        }

        allreminder();
        inflate_layout=v.findViewById(R.id.layout);
        layoutupcoming=v.findViewById(R.id.layoutupcoming);
        layoutpast=v.findViewById(R.id.layoutpast);
        showPreviousMonthBut = v. findViewById(R.id.prev_button);
        showNextMonthBut = v. findViewById(R.id.next_button);
        final LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        custompopup_view = inflator.inflate(R.layout.popup_reminder, null);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat month_formate = new SimpleDateFormat("yyyy-MM");
        curent_month= month_formate.format(new Date());
        compactCalendarView = (CompactCalendarView)v.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.colorAccent));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDayClick(final Date date_day) {
                if (new Date().after(date_day)) {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Cannot set reminder to this date")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();

                                }
                            })
                            .show();

                    dialog.findViewById(R.id.confirm_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));
                }
                if(new Date().before(date_day)){

                    final SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.CUSTOM_IMAGE_TYPE);
                    dialog.setTitleText("Reminder")
                            .setCustomImage(R.drawable.dateout_icon)
                            .setConfirmText("View")
                            .setCancelText("Add")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    layoutupcoming.setVisibility(View.GONE);
                                    layoutpast.setVisibility(View.GONE);
                                    array_up.clear();
                                    array_up1.clear();
                                    progress.setVisibility(View.VISIBLE);
                                    NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
                                    boolean i = networkCheckingClass.ckeckinternet();
                                    if (i) {
                                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                                        String url = Constants.URL+"all-reminders.php?";
                                        StringRequest stringRequest1 = new StringRequest
                                                (Request.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        progress.setVisibility(View.GONE);
                                                        System.out.println("++++++++++++++RESPONSE+++++++++++++++ Upcominggg :" + response);

                                                        try {
                                                            System.out.println("Upcoming result : " + response);
                                                            JSONObject  jsonObject = new JSONObject(response);
                                                            String status = jsonObject.getString("status");
                                                            if (status.equalsIgnoreCase("true")) {
                                                                String data = jsonObject.getString("data");
                                                                JSONObject Object = new JSONObject(data);
                                                                String past_reminders = Object.getString("past_reminders");
                                                                JSONArray json = new JSONArray(past_reminders);
                                                                String upcoming_reminders = Object.getString("upcoming_reminders");
                                                                JSONArray jsonArray = new JSONArray(upcoming_reminders);
                                                                for (int j = 0; j < jsonArray.length(); j++) {
                                                                    JSONObject obj = jsonArray.getJSONObject(j);
                                                                    System.out.println("Upcoming result..... : " + obj);
                                                                    id = obj.getString("id");
                                                                    event_type = obj.getString("event_type");
                                                                    event_name = obj.getString("event_name");
                                                                    event_date = obj.getString("event_date");
                                                                    event_time = obj.getString("event_time");
                                                                    event_details = obj.getString("event_details");
                                                                    String date = event_date;
                                                                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                                                    String dat = formatter.format(date_day);
                                                                    if(Objects.equals(dat, date)){

                                                                        SimpleDateFormat from_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                                                        SimpleDateFormat to_date = new SimpleDateFormat("dd", Locale.US);


                                                                        try {
                                                                            event_date2 = to_date.format(from_date.parse(date));
                                                                            System.out.println("Upcoming#####................" + event_date2);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        SimpleDateFormat to_month = new SimpleDateFormat("MMMM", Locale.US);
                                                                        try {
                                                                            event_month2 = to_month.format(from_date.parse(date));
                                                                            System.out.println("Upcoming##$$$................" + event_month2);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        ReminderModel upcomingmodel = new ReminderModel();
                                                                        DateFormat input_time = new SimpleDateFormat("HH:mm:ss");
                                                                        Date date1 = null;
                                                                        try {
                                                                            date1 = input_time.parse(event_time);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        DateFormat time2 = new SimpleDateFormat("hh:mm a");
                                                                        String event_time2 = time2.format(date1);
                                                                        upcomingmodel.setId(id);
                                                                        upcomingmodel.setEvent_type(event_type);
                                                                        upcomingmodel.setEvent_name(event_name);
                                                                        upcomingmodel.setEvent_date(event_date2);
                                                                        upcomingmodel.setdate(date);
                                                                        upcomingmodel.setEvent_month(event_month2);
                                                                        upcomingmodel.setEvent_time(event_time2);
                                                                        upcomingmodel.setEvent_details(event_details);

                                                                        array_up.add(upcomingmodel);
                                                                        System.out.println("Upcoming................" + array_up.size());

                                                                    }


                                                                    upcomingAdapter = new UpcomingAdapter(array_up, getActivity());
                                                                    recycle_upcoming.scheduleLayoutAnimation();
                                                                    recycle_upcoming.setAdapter(upcomingAdapter);
                                                                    recycle_upcoming.setHasFixedSize(true);
                                                                    System.out.println("1");

                                                                    recycle_upcoming.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                                                @Override
                                                                                public void onItemClick(View view, int position) {
                                                                                    event_name=array_up.get(position).getEvent_name();
                                                                                    event_date=array_up.get(position).getdate();
                                                                                    event_time=array_up.get(position).getEvent_time();
                                                                                    event_details=array_up.get(position).getEvent_details();
                                                                                    displaypopup_reminder_popupwindow(event_name,event_date,event_time,event_details);


                                                                                }
                                                                            })
                                                                    );

                                                                }

                                                                for (int j = 0; j < json.length(); j++) {

                                                                    JSONObject obj = json.getJSONObject(j);
                                                                    System.out.println("Upcoming result..... : " + obj);
                                                                    id = obj.getString("id");
                                                                    event_type = obj.getString("event_type");
                                                                    event_name = obj.getString("event_name");
                                                                    event_date = obj.getString("event_date");
                                                                    event_time = obj.getString("event_time");
                                                                    event_details = obj.getString("event_details");
                                                                    String date = event_date;
                                                                    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                                                    String dat = formatter.format(date_day);
                                                                    if(Objects.equals(dat, date)){

                                                                        SimpleDateFormat from_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                                                        SimpleDateFormat to_date = new SimpleDateFormat("dd", Locale.US);


                                                                        try {
                                                                            event_date2 = to_date.format(from_date.parse(date));
                                                                            System.out.println("Upcoming#####................" + event_date2);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        SimpleDateFormat to_month = new SimpleDateFormat("MMMM", Locale.US);
                                                                        try {
                                                                            event_month2 = to_month.format(from_date.parse(date));
                                                                            System.out.println("Upcoming##$$$................" + event_month2);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        PastModel pastModel = new PastModel();
                                                                        DateFormat input_time = new SimpleDateFormat("HH:mm:ss");
                                                                        Date date1 = null;
                                                                        try {
                                                                            date1 = input_time.parse(event_time);
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        DateFormat time2 = new SimpleDateFormat("hh:mm a");
                                                                        String event_time2 = time2.format(date1);
                                                                        pastModel.setId(id);
                                                                        pastModel.setEvent_type(event_type);
                                                                        pastModel.setEvent_name(event_name);
                                                                        pastModel.setEvent_date(event_date2);
                                                                        pastModel.setdate(date);
                                                                        pastModel.setEvent_month(event_month2);
                                                                        pastModel.setEvent_time(event_time2);
                                                                        pastModel.setEvent_details(event_details);

                                                                        array_up1.add(pastModel);
                                                                        System.out.println("Upcoming................" + array_up1.size());

                                                                    }

                                                                    pastAdapter = new PastAdapter(array_up1, getActivity());
                                                                    System.out.println("1");
                                                                    recycle_past.scheduleLayoutAnimation();
                                                                    recycle_past.setAdapter(pastAdapter);
                                                                    recycle_past.setHasFixedSize(true);
                                                                    progress.setVisibility(View.GONE);
                                                                    recycle_past.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                                                @Override
                                                                                public void onItemClick(View view, int position) {
                                                                                    event_name=array_up1.get(position).getEvent_name();
                                                                                    event_date=array_up1.get(position).getdate();
                                                                                    event_time=array_up1.get(position).getEvent_time();
                                                                                    event_details=array_up1.get(position).getEvent_details();
                                                                                    displaypopup_reminder_popupwindow(event_name,event_date,event_time,event_details);


                                                                                }
                                                                            })
                                                                    );

                                                                }

                                                            }
                                                            else{
                                                                progress.setVisibility(View.GONE);
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }


                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {


                                                    }
                                                })
                                        {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("user_id", userid);
                                                return params;
                                            }

                                        };


                                        queue.add(stringRequest1);
                                        queue.getCache().clear();
                                    }
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("key", formatter.format(date_day));

                                    ReminderAddFragment_new fragment = new ReminderAddFragment_new();
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                                    transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
                                    transaction.replace(R.id.flFragmentPlaceHolder, fragment, "reminder").addToBackStack("s");
                                    transaction.commit();
                                }
                            })
                            .show();

                    dialog.findViewById(R.id.confirm_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));
                    dialog.findViewById(R.id.cancel_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));

                }
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                curent_month= month_formate.format(firstDayOfNewMonth);
                System.out.println("curent_month"+curent_month);
                tvcal.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                layoutupcoming.setVisibility(View.VISIBLE);
                layoutpast.setVisibility(View.VISIBLE);
                allreminder();
            }
        });
        compactCalendarView.drawSmallIndicatorForEvents(false);
        compactCalendarView.invalidate();

        tvcal = (TextView)v.findViewById(R.id.textViewcal);
        tvcal.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showPreviousMonth();
            }
        });

        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showNextMonth();
            }
        });

        txt= (TextView)v. findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        txt.setText("REMINDER");
        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
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

    private void allreminder() {

            array_up.clear();
            array_up1.clear();
            progress.setVisibility(View.VISIBLE);
            NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
            boolean i = networkCheckingClass.ckeckinternet();
            if (i) {

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = Constants.URL+"all-reminders.php?";
                StringRequest stringRequest1 = new StringRequest
                        (Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progress.setVisibility(View.GONE);

                                try {
                                    System.out.println("Upcoming result****" + response);
                                    JSONObject  jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equalsIgnoreCase("true")) {
                                        String data = jsonObject.getString("data");
                                        JSONObject Object = new JSONObject(data);
                                        String past_reminders = Object.getString("past_reminders");
                                        JSONArray json = new JSONArray(past_reminders);
                                        String upcoming_reminders = Object.getString("upcoming_reminders");
                                        JSONArray jsonArray = new JSONArray(upcoming_reminders);
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            JSONObject obj = jsonArray.getJSONObject(j);
                                            System.out.println("Upcoming result..... : " + obj);
                                            id = obj.getString("id");
                                            event_type = obj.getString("event_type");
                                            event_name = obj.getString("event_name");
                                            event_date = obj.getString("event_date");
                                            event_time = obj.getString("event_time");
                                            event_details = obj.getString("event_details");
                                            String date = event_date;
                                            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                                            try {
                                                Date dat = formatter.parse(event_date);
                                                compactCalendarView.addEvent(new CalendarDayEvent(dat.getTime(), Color.parseColor("#368aba")), true);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            event_date=event_date.substring(0,7);
                                            if(Objects.equals(curent_month, event_date)){

                                                SimpleDateFormat from_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                                SimpleDateFormat to_date = new SimpleDateFormat("dd", Locale.US);


                                                try {
                                                    event_date2 = to_date.format(from_date.parse(date));
                                                    System.out.println("Upcoming#####................" + event_date2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat to_month = new SimpleDateFormat("MMMM", Locale.US);
                                                try {
                                                    event_month2 = to_month.format(from_date.parse(date));
                                                    System.out.println("Upcoming##$$$................" + event_month2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                ReminderModel upcomingmodel = new ReminderModel();
                                                DateFormat input_time = new SimpleDateFormat("HH:mm:ss");
                                                Date date1 = null;
                                                try {
                                                    date1 = input_time.parse(event_time);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                DateFormat time2 = new SimpleDateFormat("hh:mm a");
                                                String event_time2 = time2.format(date1);
                                                upcomingmodel.setId(id);
                                                upcomingmodel.setEvent_type(event_type);
                                                upcomingmodel.setEvent_name(event_name);
                                                upcomingmodel.setEvent_date(event_date2);
                                                upcomingmodel.setdate(date);
                                                upcomingmodel.setEvent_month(event_month2);
                                                upcomingmodel.setEvent_time(event_time2);
                                                upcomingmodel.setEvent_details(event_details);

                                                array_up.add(upcomingmodel);
                                                System.out.println("Upcoming................" + array_up.size());

                                            }


                                            upcomingAdapter = new UpcomingAdapter(array_up, getActivity());
                                            recycle_upcoming.scheduleLayoutAnimation();
                                            recycle_upcoming.setAdapter(upcomingAdapter);
                                            recycle_upcoming.setHasFixedSize(true);
                                            System.out.println("1");

                                            recycle_upcoming.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(View view, int position) {
                                                            event_name=array_up.get(position).getEvent_name();
                                                            event_date=array_up.get(position).getdate();
                                                            event_time=array_up.get(position).getEvent_time();
                                                            event_details=array_up.get(position).getEvent_details();
                                                            displaypopup_reminder_popupwindow(event_name,event_date,event_time,event_details);


                                                        }
                                                    })
                                            );

                                        }

                                        for (int j = 0; j < json.length(); j++) {

                                            JSONObject obj = json.getJSONObject(j);
                                            System.out.println("Upcoming result..... : " + obj);
                                            id = obj.getString("id");
                                            event_type = obj.getString("event_type");
                                            event_name = obj.getString("event_name");
                                            event_date = obj.getString("event_date");
                                            event_time = obj.getString("event_time");
                                            event_details = obj.getString("event_details");
                                            String date = event_date;

                                            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date dat = formatter.parse(event_date);
                                                compactCalendarView.addEvent(new CalendarDayEvent(dat.getTime(), Color.parseColor("#C0C0C0")), true);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            event_date=event_date.substring(0,7);
                                            if(Objects.equals(curent_month, event_date)){

                                                SimpleDateFormat from_date = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                                SimpleDateFormat to_date = new SimpleDateFormat("dd", Locale.US);


                                                try {
                                                    event_date2 = to_date.format(from_date.parse(date));
                                                    System.out.println("Upcoming#####................" + event_date2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat to_month = new SimpleDateFormat("MMMM", Locale.US);
                                                try {
                                                    event_month2 = to_month.format(from_date.parse(date));
                                                    System.out.println("Upcoming##$$$................" + event_month2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                PastModel pastModel = new PastModel();
                                                DateFormat input_time = new SimpleDateFormat("HH:mm:ss");
                                                Date date1 = null;
                                                try {
                                                    date1 = input_time.parse(event_time);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                DateFormat time2 = new SimpleDateFormat("hh:mm a");
                                                String event_time2 = time2.format(date1);
                                                pastModel.setId(id);
                                                pastModel.setEvent_type(event_type);
                                                pastModel.setEvent_name(event_name);
                                                pastModel.setEvent_date(event_date2);
                                                pastModel.setdate(date);
                                                pastModel.setEvent_month(event_month2);
                                                pastModel.setEvent_time(event_time2);
                                                pastModel.setEvent_details(event_details);

                                                array_up1.add(pastModel);
                                                System.out.println("Upcoming................" + array_up1.size());

                                            }

                                            pastAdapter = new PastAdapter(array_up1, getActivity());
                                            System.out.println("1");
                                            recycle_past.scheduleLayoutAnimation();
                                            recycle_past.setAdapter(pastAdapter);
                                            recycle_past.setHasFixedSize(true);
                                            progress.setVisibility(View.GONE);
                                            recycle_past.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                                                @Override
                                                                public void onItemClick(View view, int position) {
                                                                    event_name=array_up1.get(position).getEvent_name();
                                                                    event_date=array_up1.get(position).getdate();
                                                                    event_time=array_up1.get(position).getEvent_time();
                                                                    event_details=array_up1.get(position).getEvent_details();
                                                                    displaypopup_reminder_popupwindow(event_name,event_date,event_time,event_details);


                                                                }
                                                            })
                                                    );

                                        }

                                    }
                                    else{
                                        progress.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {


                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", userid);
                        return params;
                    }

                };


                queue.add(stringRequest1);
                queue.getCache().clear();
            }

        }

    private void displaypopup_reminder_popupwindow(String event_name, String event_date, String event_time, String event_details) {
        try {

            ImageView closebutton = (ImageView) custompopup_view.findViewById(R.id.close_point_converter);
            TextView event_nam = (TextView) custompopup_view.findViewById(R.id.event_name);
            TextView event_dat = (TextView) custompopup_view.findViewById(R.id.txt_date);
            TextView event_tim = (TextView) custompopup_view.findViewById(R.id.txt_time);
            TextView event_detail= (TextView) custompopup_view.findViewById(R.id.event_details);
            event_nam.setText(event_name);
            event_dat.setText(event_date);
            event_tim.setText(event_time);
            event_detail.setText(event_details);
            closebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reminder_popupwindow.dismiss();
                }
            });
            reminder_popupwindow = new PopupWindow(custompopup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                reminder_popupwindow.setElevation(5.0f);
            }
            reminder_popupwindow.setFocusable(true);
            reminder_popupwindow.setAnimationStyle(R.style.AppTheme);
            reminder_popupwindow.showAtLocation(inflate_layout, Gravity.CENTER, 0, 0);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
