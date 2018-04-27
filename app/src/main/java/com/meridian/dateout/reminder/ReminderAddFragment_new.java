/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.meridian.dateout.reminder;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ObjectOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;


public class ReminderAddFragment_new extends android.support.v4.app.Fragment implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener
{

    public static int EXTRA_REMINDER_ID =0                    ;


    private EditText mTitleText;
    AutoCompleteTextView title_type;
    private TextView mDateText_remind,mTimeText_remind,mDateText_event,mTimeText_event,txt_DateText_remind,txt_TimeText_event,txt_DateText_event,txt_TimeText_remind;
    String formatted_date,formatted_date_remind;
    public Calendar mCalendar,mCalendar11,calender;
    public  Calendar mCalendar1,mCalendar2,mCalendar3,mCalendar4,mCalendar5;
    public Calendar calendar1,calendar2,calendar3,calendar4,calendar5;
    private int mYear, mMonth, mHour, mMinute, mDay,mYear_event,mMonth_event,mMinute_event,mDay_event,mHour_event;
    private long mRepeatTime;
    private String str_mTitle,str_Title_type,str_details;
    private String mTime,mTime_event,remind_time;
    private String mDate,mDate_event,remind_date;
    String userid;
    private String mActive;
    Button alarm_save;
    ProgressBar progress;
    String time_zone;
    String date1;
    int offsetFromUtc;
    String time_date_ust;
    View customView;
    private PopupWindow mPopupWindow;
    CheckBox check_one,check_two,check_three,check_four,check_five;
    LinearLayout btn_sub;
    boolean error=false;
    EditText reminder_details;
    TimePickerDialog tpd_event,tpd_remind;
    DatePickerDialog dpd_event,dpd_remind;
    RelativeLayout rel_time_remind, rel_date_remind,rel_date_event,rel_time_event;
    RadioButton r1,r2,r3;
    Calendar call;
    ImageView close_point_converter;
    String time_zone1,time_zone_id;
    ArrayList<Calendar>array;
    boolean in=true;
    int inn;
    int flag_date_visible=0;
    int flag_time_visible=0;
    int flag_check_one=0;
    int flag_check_two=0;
    int flag_check_three=0;
    int flag_check_four=0;
    int flag_check_five=0;
    String currentDatee,dayDifference,current_date;
    int dateDifference;
    Float difference1;
    ArrayList<String> event_type=new ArrayList<>();
    int hours=0;
    PassingModel pm;
    ArrayList<PassingModel>passingArrayList;

    private static String[]stockArr;
    AutoCompleteTextView actv;
    private RadioGroup radioGroup;
    private OnFragmentInteractionListener mListener;
    //int iteration_flag=0;
     SweetAlertDialog serverdialog;
    SweetAlertDialog checkBoxdialog;
    Float p,p1;
    DateFormat readFormat ;
    DateFormat writeFormat;
    String combined_dt_time;
    Date date_now=null;
    Date entered_date=null;
    Calendar checkBoxCalenderEntered,checkBoxCalenderNow;
    public ReminderAddFragment_new() {
        // Required empty public constructor
    }
    LinearLayout menu;
    TextView txt;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment NewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderAddFragment_new newInstance() {
        ReminderAddFragment_new fragment = new ReminderAddFragment_new();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=    inflater.inflate(R.layout.addreminder_new, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);

        txt= (TextView)v. findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        txt.setText("Add Reminder");
        menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                //startActivity(i);
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        String strtext=getArguments().getString("key");
        System.out.println("_______^^^^^^^^^^^^^^strtext" +strtext);
        readFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm");
        writeFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss aa yyyy");

        serverdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        checkBoxdialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
        alarm_save= (Button) v.findViewById(R.id.alarm_save);

        rel_time_event =v.findViewById(R.id.time_EVENT);
        rel_date_event =v.findViewById(R.id.date_EVENT);
        mTitleText = (EditText)v. findViewById(R.id.reminder_title);
//      title_type= (EditText)v. findViewById(R.id.reminder_type);
        mDateText_remind = (TextView)v. findViewById(R.id.set_date_REMIND);
        reminder_details=v.findViewById(R.id.reminder_details);
        mDateText_event = (TextView)v. findViewById(R.id.set_date);
        mTimeText_event = (TextView)v. findViewById(R.id.set_time);
        txt_DateText_event = (TextView)v. findViewById(R.id.text_date);
        txt_TimeText_event = (TextView)v. findViewById(R.id.text_time);
        progress = (ProgressBar)v. findViewById(R.id.progress_bar);
        check_one=(CheckBox)v.findViewById(R.id.check_one);
        check_two=(CheckBox)v.findViewById(R.id.check_two);
        check_three=(CheckBox)v.findViewById(R.id.check_three);
        check_four=(CheckBox)v.findViewById(R.id.check_four);
        check_five=(CheckBox)v.findViewById(R.id.check_five);
        btn_sub=(LinearLayout)v.findViewById(R.id.linear_sub_button);
        title_type= (AutoCompleteTextView) v.findViewById(R.id.reminder_type);
        mTitleText.setText("");
        title_type.setText("");

        passingArrayList=new ArrayList<>();//for passing the different checked dates to service

        ReminderDatabase rd = new ReminderDatabase(getActivity());
        String[] table_evnt_types=rd.getEventTypes();

        if(table_evnt_types.length>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.new_auto_inflate, R.id.txt_auto_view,table_evnt_types);
            title_type.setAdapter(adapter);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("flag", MODE_PRIVATE);
        int flag_time_visible = prefs.getInt("flag_time_visible", 0);
        int flag_date_visible = prefs.getInt("flag_date_visible", 0);
        System.out.println("_______^^^^^^^^^^^^^^flag_time_visible" + flag_time_visible);
        System.out.println("_______^^^^^^^^^^^^^^flag_date_visible" +flag_date_visible);
        System.out.println("_______^^^^^^^^^^^^^^flag_1" + flag_time_visible);
        System.out.println("_______^^^^^^^^^^^^^^flag_2" +flag_date_visible);
        System.out.println("_______^^^^^^^^^^^^^^flag_3" + flag_time_visible);

        mDateText_event.setText(strtext);
        txt_DateText_event.setText(strtext);
        formatted_date=strtext;
        if(flag_date_visible==1)
        {
            String date = prefs.getString("date",null);
            System.out.println("_______^^^^^^^^^^^^^^date_visible" +date);

            mDateText_event.setVisibility(View.VISIBLE);
            txt_DateText_event.setVisibility(View.GONE);
            mDateText_event.setText(strtext);
        }
        else
        {

            mDateText_event.setVisibility(View.GONE);
            txt_DateText_event.setVisibility(View.VISIBLE);

        }



        if(flag_time_visible==1)
        {
            String time = prefs.getString("time",null);
            System.out.println("_______^^^^^^^^^^^^^^time_visible" +time);
            mTimeText_event.setVisibility(View.VISIBLE);
            txt_TimeText_event.setVisibility(View.GONE);
            mTimeText_event.setText(time);
        }
        else {
            mTimeText_event.setVisibility(View.GONE);
            txt_TimeText_event.setVisibility(View.VISIBLE);
        }
        if(flag_check_one==1)
        {
            check_one.setChecked(true);
        }
        if(flag_check_two==1)
        {
            check_two.setChecked(true);
        }
        if(flag_check_three==1)
        {
            check_three.setChecked(true);
        }
        if(flag_check_four==1)
        {
            check_four.setChecked(true);
        }
        if(flag_check_five==1)
        {
            check_five.setChecked(true);
        }


        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }
        });
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

        rel_date_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now1 = Calendar.getInstance();
                FragmentManager fm = getActivity().getFragmentManager();
                dpd_event = DatePickerDialog.newInstance(
                        ReminderAddFragment_new.this,
                        now1.get(Calendar.YEAR),
                        now1.get(Calendar.MONTH),
                        now1.get(Calendar.DAY_OF_MONTH)
                );
                dpd_event.show(fm, "Datepickerdialog");
            }
        });
        rel_time_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now2 = Calendar.getInstance();
                FragmentManager fm = getActivity().getFragmentManager();
                tpd_event = TimePickerDialog.newInstance(
                        ReminderAddFragment_new.this,
                        now2.get(Calendar.HOUR_OF_DAY),
                        now2.get(Calendar.MINUTE),
                        false
                );
                tpd_event.setThemeDark(false);
                tpd_event.show(fm, "Timepickerdialog");
            }
        });

        mActive = "true";


        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);
        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;


        TimeZone tz = TimeZone.getDefault();

        time_zone1=tz.getDisplayName(false, TimeZone.SHORT);
        time_zone_id=tz.getID();
        System.out.println("_______^^^^^^^^^^^^^^TimeeeeeZone "+tz.getDisplayName(false, TimeZone.SHORT));
        System.out.println("_______^^^^^^^^^^^^^^TimeeeeeZone>>>IDD "+tz.getID());

        mDate_event = formatted_date;
        mCalendar1 = Calendar.getInstance();
        mHour_event = mCalendar1.get(Calendar.HOUR_OF_DAY);
        mMinute_event = mCalendar1.get(Calendar.MINUTE);
        mTime_event = mHour_event + ":" + mMinute_event;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(mDate_event);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            mMonth_event = (cal.get(Calendar.MONTH)+1);
            mDay_event = (cal.get(Calendar.DATE));
            mYear_event = (cal.get(Calendar.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }

        calender =  Calendar.getInstance();
        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar3 = Calendar.getInstance();
        calendar4 = Calendar.getInstance();
        calendar5 = Calendar.getInstance();

        System.out.println("_______^^^^^^^^^^^^^^Current EventDATEEEE" +mDate_event);
        System.out.println("_______^^^^^^^^^^^^^^Current EventTIMEEEE" +mTime_event);
        System.out.println("_______^^^^^^^^^^^^^^Current TIMEEEE" +calender.getTime());

        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        current_date= dateFormat.format(calender.getTime());
        System.out.println("_______^^^^^^^^^^^^^^Current TIMEEEE parsed" +current_date);

        check_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txt_DateText_event.getVisibility()==View.VISIBLE&&txt_TimeText_event.getVisibility()==View.VISIBLE)
                {
                    check_one.setChecked(false);

                    checkBoxdialog.setTitleText("Reminder")
                            .setContentText("Please Select Date and Time")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    checkBoxdialog.dismiss();

                                }
                            })
                            .show();
                }else
                {
                    combined_dt_time=mDateText_event.getText().toString()+" "+mHour_event+":"+mMinute_event;
                    date_now=new Date();
                    System.out.println("####### date_now : "+date_now);
                    System.out.println("####### combined_dt_time : "+combined_dt_time);
                    try {
                        entered_date = readFormat.parse(combined_dt_time);
                        System.out.println("####### entered_date : "+entered_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    checkBoxCalenderEntered=Calendar.getInstance();
                    checkBoxCalenderEntered.setTime(entered_date);
                    checkBoxCalenderEntered.add(Calendar.DAY_OF_MONTH,-30);
                    System.out.println("####### minused new date : "+checkBoxCalenderEntered.getTime());

                    checkBoxCalenderNow=Calendar.getInstance();
                    checkBoxCalenderNow.setTime(date_now);
                    System.out.println("####### checkBoxCalenderNow.getTime() : "+checkBoxCalenderNow.getTime());


                    if(entered_date.after(date_now)){

                        if(checkBoxCalenderEntered.getTime().before(checkBoxCalenderNow.getTime()))
                        {
                            check_one.setChecked(false);
                            checkBoxdialog.setTitleText("Reminder")
                                    .setContentText("cannot set reminder to past date & time")

                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            checkBoxdialog.dismiss();

                                        }
                                    })
                                    .show();

                            flag_check_one=0;
                        }else {
                            flag_check_one=1;
                        }
                    }else
                    {
                        check_one.setChecked(false);
                        checkBoxdialog.setTitleText("Reminder")
                                .setContentText("cannot set reminder to past date & time")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        checkBoxdialog.dismiss();

                                    }
                                })
                                .show();

                        flag_check_one=0;
                    }
                }




            }
        });
        check_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_DateText_event.getVisibility()==View.VISIBLE&&txt_TimeText_event.getVisibility()==View.VISIBLE)
                {
                    check_two.setChecked(false);
                    checkBoxdialog.setTitleText("Reminder")
                            .setContentText("Please Select Date and Time")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    checkBoxdialog.dismiss();
                                }
                            })
                            .show();



                }else
                {
                    combined_dt_time=mDateText_event.getText().toString()+" "+mHour_event+":"+mMinute_event;
                    date_now=new Date();
                    System.out.println("####### date_now : "+date_now);
                    System.out.println("####### combined_dt_time : "+combined_dt_time);
                    try {
                        entered_date = readFormat.parse(combined_dt_time);
                        System.out.println("####### entered_date : "+entered_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    checkBoxCalenderEntered=Calendar.getInstance();
                    checkBoxCalenderEntered.setTime(entered_date);
                    checkBoxCalenderEntered.add(Calendar.DAY_OF_MONTH,-15);
                    System.out.println("####### minused new date : "+checkBoxCalenderEntered.getTime());

                    checkBoxCalenderNow=Calendar.getInstance();
                    checkBoxCalenderNow.setTime(date_now);
                    System.out.println("####### checkBoxCalenderNow.getTime() : "+checkBoxCalenderNow.getTime());


                    if(entered_date.after(date_now)){

                        if(checkBoxCalenderEntered.getTime().before(checkBoxCalenderNow.getTime()))
                        {
                            check_two.setChecked(false);
                            checkBoxdialog.setTitleText("Reminder")
                                    .setContentText("cannot set reminder to past date & time")

                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            checkBoxdialog.dismiss();

                                        }
                                    })
                                    .show();

                            flag_check_two=0;
                        }else {
                            flag_check_two=1;
                        }
                    }else
                    {
                        check_two.setChecked(false);
                        checkBoxdialog.setTitleText("Reminder")
                                .setContentText("cannot set reminder to past date & time")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        checkBoxdialog.dismiss();

                                    }
                                })
                                .show();

                        flag_check_two=0;
                    }
                }


            }
        });
        check_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_DateText_event.getVisibility()==View.VISIBLE&&txt_TimeText_event.getVisibility()==View.VISIBLE)
                {
                    check_three.setChecked(false);
                    checkBoxdialog.setTitleText("Reminder")
                            .setContentText("Please Select Date and Time")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    checkBoxdialog.dismiss();

                                }
                            })
                            .show();



                }else
                {
                    combined_dt_time=mDateText_event.getText().toString()+" "+mHour_event+":"+mMinute_event;
                    date_now=new Date();
                    System.out.println("####### date_now : "+date_now);
                    System.out.println("####### combined_dt_time : "+combined_dt_time);
                    try {
                        entered_date = readFormat.parse(combined_dt_time);
                        System.out.println("####### entered_date : "+entered_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    checkBoxCalenderEntered=Calendar.getInstance();
                    checkBoxCalenderEntered.setTime(entered_date);
                    checkBoxCalenderEntered.add(Calendar.DAY_OF_MONTH,-7);
                    System.out.println("####### minused new date : "+checkBoxCalenderEntered.getTime());

                    checkBoxCalenderNow=Calendar.getInstance();
                    checkBoxCalenderNow.setTime(date_now);
                    System.out.println("####### checkBoxCalenderNow.getTime() : "+checkBoxCalenderNow.getTime());


                    if(entered_date.after(date_now)){

                        if(checkBoxCalenderEntered.getTime().before(checkBoxCalenderNow.getTime()))
                        {
                            check_three.setChecked(false);
                            checkBoxdialog.setTitleText("Reminder")
                                    .setContentText("cannot set reminder to past date & time")

                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            checkBoxdialog.dismiss();

                                        }
                                    })
                                    .show();

                            flag_check_three=0;
                        }else {
                            flag_check_three=1;
                        }
                    }else
                    {
                        check_three.setChecked(false);
                        checkBoxdialog.setTitleText("Reminder")
                                .setContentText("cannot set reminder to past date & time")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        checkBoxdialog.dismiss();

                                    }
                                })
                                .show();

                        flag_check_three=0;
                    }
                }





            }
        });

        check_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_DateText_event.getVisibility()==View.VISIBLE&&txt_TimeText_event.getVisibility()==View.VISIBLE)
                {
                    check_four.setChecked(false);
                    checkBoxdialog.setTitleText("Reminder")
                            .setContentText("Please Select Date and Time")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    checkBoxdialog.dismiss();

                                }
                            })
                            .show();



                }else
                {
                    combined_dt_time=mDateText_event.getText().toString()+" "+mHour_event+":"+mMinute_event;
                    date_now=new Date();
                    System.out.println("####### date_now : "+date_now);
                    System.out.println("####### combined_dt_time : "+combined_dt_time);
                    try {
                        entered_date = readFormat.parse(combined_dt_time);
                        System.out.println("####### entered_date : "+entered_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    checkBoxCalenderEntered=Calendar.getInstance();
                    checkBoxCalenderEntered.setTime(entered_date);
                    checkBoxCalenderEntered.add(Calendar.DAY_OF_MONTH,-1);
                    System.out.println("####### minused new date : "+checkBoxCalenderEntered.getTime());

                    checkBoxCalenderNow=Calendar.getInstance();
                    checkBoxCalenderNow.setTime(date_now);
                    System.out.println("####### checkBoxCalenderNow.getTime() : "+checkBoxCalenderNow.getTime());


                    if(entered_date.after(date_now)){

                        if(checkBoxCalenderEntered.getTime().before(checkBoxCalenderNow.getTime()))
                        {
                            check_four.setChecked(false);
                            checkBoxdialog.setTitleText("Reminder")
                                    .setContentText("cannot set reminder to past date & time")

                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            checkBoxdialog.dismiss();

                                        }
                                    })
                                    .show();

                            flag_check_four=0;
                        }else {
                            flag_check_four=1;
                        }
                    }else
                    {
                        check_four.setChecked(false);
                        checkBoxdialog.setTitleText("Reminder")
                                .setContentText("cannot set reminder to past date & time")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        checkBoxdialog.dismiss();

                                    }
                                })
                                .show();

                        flag_check_four=0;
                    }
                }



            }
        });

        check_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_DateText_event.getVisibility()==View.VISIBLE&&txt_TimeText_event.getVisibility()==View.VISIBLE)
                {
                    System.out.println("_______^^^^^^^^^^^^^^000" );
                    check_five.setChecked(false);
                    checkBoxdialog.setTitleText("Reminder")
                            .setContentText("Please Select Date and Time")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    checkBoxdialog.dismiss();

                                }
                            })
                            .show();



                }
                else
                {
                    combined_dt_time=mDateText_event.getText().toString()+" "+mHour_event+":"+mMinute_event;
                    date_now=new Date();
                    System.out.println("####### date_now : "+date_now);
                    System.out.println("####### combined_dt_time : "+combined_dt_time);
                    try {
                        entered_date = readFormat.parse(combined_dt_time);
                        System.out.println("####### entered_date : "+entered_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    checkBoxCalenderEntered=Calendar.getInstance();
                    checkBoxCalenderEntered.setTime(entered_date);
                    checkBoxCalenderEntered.add(Calendar.HOUR_OF_DAY,-10);
                    System.out.println("####### minused new date : "+checkBoxCalenderEntered.getTime());

                    checkBoxCalenderNow=Calendar.getInstance();
                    checkBoxCalenderNow.setTime(date_now);
                    System.out.println("####### checkBoxCalenderNow.getTime() : "+checkBoxCalenderNow.getTime());


                    if(entered_date.after(date_now)){

                        if(checkBoxCalenderEntered.getTime().before(checkBoxCalenderNow.getTime()))
                        {
                            check_five.setChecked(false);
                            checkBoxdialog.setTitleText("Reminder")
                                    .setContentText("cannot set reminder to past date & time")

                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            checkBoxdialog.dismiss();

                                        }
                                    })
                                    .show();

                            flag_check_five=0;
                        }else {
                            flag_check_five=1;
                        }
                    }else
                    {
                        check_five.setChecked(false);
                        checkBoxdialog.setTitleText("Reminder")
                                .setContentText("cannot set reminder to past date & time")

                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        checkBoxdialog.dismiss();

                                    }
                                })
                                .show();

                        flag_check_five=0;
                    }
                }






            }
        });

        alarm_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getActivity());
                boolean i = networkCheckingClass.ckeckinternet();
                if (i == true) {
                passingArrayList.clear();
                mTitleText.setText(str_mTitle);
                title_type.setText(str_Title_type);
                reminder_details.setText(str_details);
                System.out.println("_______^^^^^^^^^^^^^^SAVE...time_event....." + mTimeText_event.getText().toString());
                System.out.println("_______^^^^^^^^^^^^^^SAVE...date_event....." + mDateText_event.getText().toString());

                int input_filled_flag=0;

                if (mTitleText.getText().toString().length() == 0) {
                    mTitleText.setError("Reminder  Title  cannot be blank!");
                    error = true;
                } else if (title_type.getText().toString().length() == 0) {
                    title_type.setError("Reminder Type cannot be blank!");
                    error = true;
                } else if (txt_TimeText_event.getVisibility() == View.VISIBLE) {
                    txt_TimeText_event.setError("Please select Event Time");
                    error = true;
                }  else {
                    input_filled_flag=1;
                    error = false;
                }

                int atleast_one_checked_flag=0;
                if(check_one.isChecked())
                    atleast_one_checked_flag=1;
                if(check_two.isChecked())
                    atleast_one_checked_flag=1;
                if(check_three.isChecked())
                    atleast_one_checked_flag=1;
                if(check_four.isChecked())
                    atleast_one_checked_flag=1;
                if(check_five.isChecked())
                    atleast_one_checked_flag=1;


                if(input_filled_flag==1) {
                    if (atleast_one_checked_flag == 1) {
                        error = false;
                    } else {
                        error = true;
                        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
                        dialog.setTitleText("Remind Time")
                                .setContentText("Select Remind Time")

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



                    if (error == false) {
                    mTitleText.setError(null);
                    title_type.setError(null);
                    txt_TimeText_event.setError(null);
                    txt_DateText_event.setError(null);

                    array = new ArrayList<>();


                    if (check_one.isChecked()) {

                        System.out.println("### mMonth_event : " + mMonth_event);
                        System.out.println("### mYear_event : " + mYear_event);
                        System.out.println("### mDay_event : " + mDay_event);
                        System.out.println("### mHour_event : " + mHour_event);
                        System.out.println("### mMinute_event : " + mMinute_event);

                        calendar1.set(Calendar.MONTH, mMonth_event - 1);
                        calendar1.set(Calendar.YEAR, mYear_event);
                        calendar1.set(Calendar.DAY_OF_MONTH, mDay_event);
                        calendar1.set(Calendar.HOUR_OF_DAY, mHour_event);
                        calendar1.set(Calendar.MINUTE, mMinute_event);
                        calendar1.set(Calendar.SECOND, 0);

                        System.out.println("****************************** prev calendar1.getTime(); : " + calendar1.getTime());
                        calendar1.add(Calendar.DAY_OF_MONTH,-30);

                        System.out.println("****************************** calendar1.getTime(); : " + calendar1.getTime());
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^ INSIDE saveReminder() ^^^^^^^^^^^");
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^ mCalendar1.MONTH : " + calendar1.get(Calendar.MONTH));
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^ mCalendar1.YEAR : " + calendar1.get(Calendar.YEAR));
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^ mCalendar1.DAY_OF_MONTH : " + calendar1.get(Calendar.DAY_OF_MONTH));
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^ mCalendar1.HOUR_OF_DAY : " + calendar1.get(Calendar.HOUR_OF_DAY));
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^ mCalendar1.MINUTE : " + calendar1.get(Calendar.MINUTE));
                        System.out.println("_______1.MONTH^^^^^^^^^^^^^^  mCalendar1.getTimeInMillis(): " + calendar1.getTimeInMillis());
                        array.add(calendar1);
                        System.out.println("________________________________________________1____________________________________________");

                        String passing_date = calendar1.get(Calendar.DAY_OF_MONTH) + "-" + (calendar1.get(Calendar.MONTH) + 1) + "-" + calendar1.get(Calendar.YEAR);
                        String passing_time = calendar1.get(Calendar.HOUR_OF_DAY) + ":" + calendar1.get(Calendar.MINUTE);
                        System.out.println("#### passing_date : " + passing_date);
                        System.out.println("#### passing_time : " + passing_time);
                        saveToSqlite(calendar1, passing_date, passing_time);

                        pm = new PassingModel();
                        pm.setPassing_date(passing_date);
                        pm.setPassing_time(passing_time);
                        passingArrayList.add(pm);


                    }
                    if (check_two.isChecked()) {

                        System.out.println("### mMonth_event : " + mMonth_event);
                        System.out.println("### mYear_event : " + mYear_event);
                        System.out.println("### mDay_event : " + mDay_event);
                        System.out.println("### mHour_event : " + mHour_event);
                        System.out.println("### mMinute_event : " + mMinute_event);

                        calendar2.set(Calendar.MONTH, mMonth_event - 1);
                        calendar2.set(Calendar.YEAR, mYear_event);
                        calendar2.set(Calendar.DAY_OF_MONTH, mDay_event);
                        calendar2.set(Calendar.HOUR_OF_DAY, mHour_event);
                        calendar2.set(Calendar.MINUTE, mMinute_event);
                        calendar2.set(Calendar.SECOND, 0);

                        calendar2.add(Calendar.DAY_OF_MONTH, -15);
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^ INSIDE saveReminder() ^^^^^^^^^^^");
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^ mCalendar2.MONTH : " + calendar2.get(Calendar.MONTH));
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^ mCalendar2.YEAR : " + calendar2.get(Calendar.YEAR));
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^ mCalendar2.DAY_OF_MONTH : " + calendar2.get(Calendar.DAY_OF_MONTH));
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^ mCalendar2.HOUR_OF_DAY : " + calendar2.get(Calendar.HOUR_OF_DAY));
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^ mCalendar2.MINUTE : " + calendar2.get(Calendar.MINUTE));
                        System.out.println("_______2.15DAYS^^^^^^^^^^^^^^  mCalendar2.getTimeInMillis(): " + calendar2.getTimeInMillis());
                        array.add(calendar2);
                        System.out.println("________________________________________________2____________________________________________");

                        String passing_date = calendar2.get(Calendar.DAY_OF_MONTH) + "-" + (calendar2.get(Calendar.MONTH) + 1) + "-" + calendar2.get(Calendar.YEAR);
                        String passing_time = calendar2.get(Calendar.HOUR_OF_DAY) + ":" + calendar2.get(Calendar.MINUTE);
                        System.out.println("#### passing_date : " + passing_date);
                        System.out.println("#### passing_time : " + passing_time);
                        saveToSqlite(calendar2, passing_date, passing_time);

                        pm = new PassingModel();
                        pm.setPassing_date(passing_date);
                        pm.setPassing_time(passing_time);
                        passingArrayList.add(pm);

                    }
                    if (check_three.isChecked()) {
                        System.out.println("### mMonth_event : " + mMonth_event);
                        System.out.println("### mYear_event : " + mYear_event);
                        System.out.println("### mDay_event : " + mDay_event);
                        System.out.println("### mHour_event : " + mHour_event);
                        System.out.println("### mMinute_event : " + mMinute_event);

                        calendar3.set(Calendar.MONTH, mMonth_event - 1);
                        calendar3.set(Calendar.YEAR, mYear_event);
                        calendar3.set(Calendar.DAY_OF_MONTH, mDay_event);
                        calendar3.set(Calendar.HOUR_OF_DAY, mHour_event);
                        calendar3.set(Calendar.MINUTE, mMinute_event);
                        calendar3.set(Calendar.SECOND, 0);
                        calendar3.add(Calendar.DAY_OF_MONTH, -7);

                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^ INSIDE saveReminder() ^^^^^^^^^^^");
                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^ mCalendar3.MONTH : " + calendar3.get(Calendar.MONTH));
                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^ mCalendar3.YEAR : " + calendar3.get(Calendar.YEAR));
                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^ mCalendar3.DAY_OF_MONTH : " + calendar3.get(Calendar.DAY_OF_MONTH));
                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^ mCalendar3.HOUR_OF_DAY : " + calendar3.get(Calendar.HOUR_OF_DAY));
                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^ mCalendar3.MINUTE : " + calendar3.get(Calendar.MINUTE));
                        System.out.println("_______3.1WEEK^^^^^^^^^^^^^^  mCalendar3.getTimeInMillis(): " + calendar3.getTimeInMillis());
                        array.add(calendar3);
                        System.out.println("________________________________________________3____________________________________________");

                        String passing_date = calendar3.get(Calendar.DAY_OF_MONTH) + "-" + (calendar3.get(Calendar.MONTH) + 1) + "-" + calendar3.get(Calendar.YEAR);
                        String passing_time = calendar3.get(Calendar.HOUR_OF_DAY) + ":" + calendar3.get(Calendar.MINUTE);
                        System.out.println("#### passing_date : " + passing_date);
                        System.out.println("#### passing_time : " + passing_time);
                        saveToSqlite(calendar3, passing_date, passing_time);

                        pm = new PassingModel();
                        pm.setPassing_date(passing_date);
                        pm.setPassing_time(passing_time);
                        passingArrayList.add(pm);
                    }
                    if (check_four.isChecked()) {
                        System.out.println("### mMonth_event : " + mMonth_event);
                        System.out.println("### mYear_event : " + mYear_event);
                        System.out.println("### mDay_event : " + mDay_event);
                        System.out.println("### mHour_event : " + mHour_event);
                        System.out.println("### mMinute_event : " + mMinute_event);

                        calendar4.set(Calendar.MONTH, mMonth_event - 1);
                        calendar4.set(Calendar.YEAR, mYear_event);
                        calendar4.set(Calendar.DAY_OF_MONTH, mDay_event);
                        calendar4.set(Calendar.HOUR_OF_DAY, mHour_event);
                        calendar4.set(Calendar.MINUTE, mMinute_event);
                        calendar4.set(Calendar.SECOND, 0);
                        calendar4.add(Calendar.DAY_OF_MONTH, -1);
                        //System.out.println("iddddd...."+ID);

                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^ INSIDE saveReminder() ^^^^^^^^^^^");
                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^ mCalendar4.MONTH : " + calendar4.get(Calendar.MONTH));
                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^ mCalendar4.YEAR : " + calendar4.get(Calendar.YEAR));
                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^ mCalendar4.DAY_OF_MONTH : " + calendar4.get(Calendar.DAY_OF_MONTH));
                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^ mCalendar4.HOUR_OF_DAY : " + calendar4.get(Calendar.HOUR_OF_DAY));
                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^ mCalendar4.MINUTE : " + calendar4.get(Calendar.MINUTE));
                        System.out.println("_______4.1DAY^^^^^^^^^^^^^^  mCalendar4.getTimeInMillis(): " + calendar4.getTimeInMillis());
                        array.add(calendar4);
                        System.out.println("________________________________________________4____________________________________________");

                        String passing_date = calendar4.get(Calendar.DAY_OF_MONTH) + "-" + (calendar4.get(Calendar.MONTH) + 1) + "-" + calendar4.get(Calendar.YEAR);
                        String passing_time = calendar4.get(Calendar.HOUR_OF_DAY) + ":" + calendar4.get(Calendar.MINUTE);
                        System.out.println("#### passing_date : " + passing_date);
                        System.out.println("#### passing_time : " + passing_time);
                        saveToSqlite(calendar4, passing_date, passing_time);

                        pm = new PassingModel();
                        pm.setPassing_date(passing_date);
                        pm.setPassing_time(passing_time);
                        passingArrayList.add(pm);

                    }
                    if (check_five.isChecked()) {

                        System.out.println("### mMonth_event : " + mMonth_event);
                        System.out.println("### mYear_event : " + mYear_event);
                        System.out.println("### mDay_event : " + mDay_event);
                        System.out.println("### mHour_event : " + mHour_event);
                        System.out.println("### mMinute_event : " + mMinute_event);

                        in = false;
                        calendar5.set(Calendar.MONTH, mMonth_event - 1);
                        calendar5.set(Calendar.YEAR, mYear_event);
                        calendar5.set(Calendar.DAY_OF_MONTH, mDay_event);
                        calendar5.set(Calendar.HOUR_OF_DAY, mHour_event);
                        calendar5.set(Calendar.MINUTE, mMinute_event);
                        calendar5.set(Calendar.SECOND, 0);

                        calendar5.add(Calendar.HOUR_OF_DAY, -10);

                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^ INSIDE saveReminder() ^^^^^^^^^^^");
                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^ mCalendar5.MONTH : " + calendar5.get(Calendar.MONTH));
                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^ mCalendar5.YEAR : " + calendar5.get(Calendar.YEAR));
                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^ mCalendar5.DAY_OF_MONTH : " + calendar5.get(Calendar.DAY_OF_MONTH));
                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^ mCalendar5.HOUR_OF_DAY : " + calendar5.get(Calendar.HOUR_OF_DAY));
                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^ mCalendar5.MINUTE : " + calendar5.get(Calendar.MINUTE));
                        System.out.println("_______5.10HRS^^^^^^^^^^^^^^  mCalendar5.getTimeInMillis(): " + calendar5.getTimeInMillis());
                        array.add(calendar5);
                        System.out.println("________________________________________________5____________________________________________");

                        String passing_date = calendar5.get(Calendar.DAY_OF_MONTH) + "-" + (calendar5.get(Calendar.MONTH) + 1) + "-" + calendar5.get(Calendar.YEAR);
                        String passing_time = calendar5.get(Calendar.HOUR_OF_DAY) + ":" + calendar5.get(Calendar.MINUTE);
                        System.out.println("#### passing_date : " + passing_date);
                        System.out.println("#### passing_time : " + passing_time);
                        saveToSqlite(calendar5, passing_date, passing_time);

                        pm = new PassingModel();
                        pm.setPassing_date(passing_date);
                        pm.setPassing_time(passing_time);
                        passingArrayList.add(pm);

                    }

                    System.out.println("________________________________________________ARRAY SIZEE____________________________________________" + array.size());



                    if(passingArrayList.size()>0){
                        //saveReminder();
                        //iteration_flag=0;
                        for(PassingModel pm:passingArrayList){

                            sendtoserver(pm.getPassing_date(),pm.getPassing_time());
                        }

                    }


                }


            }else {

                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE);
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
        });
        reminder_details.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str_details = s.toString().trim();
                reminder_details.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        title_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str_Title_type = s.toString().trim();
                title_type.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                str_mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        return  v;
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        check_one.setChecked(false);
        check_two.setChecked(false);
        check_three.setChecked(false);
        check_four.setChecked(false);
        check_five.setChecked(false);
        flag_check_one=0;
        flag_check_two=0;
        flag_check_three = 0;
        flag_check_four=0;
        if(view.getTag().equals("Timepickerdialog"))
        {

            mHour_event = hourOfDay;
            System.out.println("mHour_event inside onTimeSet: "+mHour_event);
            mMinute_event = minute;
            if (minute < 10) {
                mTime_event = hourOfDay + ":" + "0" + minute;
            } else {
                mTime_event = hourOfDay + ":" + minute;
            }
            txt_TimeText_event.setVisibility(View.GONE);
            mTimeText_event.setVisibility(View.VISIBLE);


            if(hourOfDay>12) {
                mTimeText_event.setText(String.valueOf(hourOfDay-12)+ ":"+(String.valueOf(minute)+" pm"));

            } else if(hourOfDay==12) {
                mTimeText_event.setText("12"+ ":"+(String.valueOf(minute)+" pm"));

            } else if(hourOfDay<12) {
                if(hourOfDay!=0) {
                    mTimeText_event.setText(String.valueOf(hourOfDay) + ":" + (String.valueOf(minute) + " am"));

                } else {
                    mTimeText_event.setText("12" + ":" + (String.valueOf(minute) + " am"));

                }
            }


            SimpleDateFormat from_date = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");



            try
            {
                String event_date2 = formatter1.format(from_date.parse(mTime_event));
                System.out.println("___________formatted TIMEEE yyyy-MM-dd'T'HH:mm:ss'Z'................" + event_date2);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

        }
        else {

            mHour = hourOfDay;
            mMinute = minute;
            if (minute < 10) {
                mTime = hourOfDay + ":" + "0" + minute;
            } else {
                mTime = hourOfDay + ":" + minute;
            }
            txt_TimeText_remind.setVisibility(View.GONE);
            mTimeText_remind.setVisibility(View.VISIBLE);

            if(hourOfDay>12) {
                //   mTimeText_remind.setText(String.valueOf(hourOfDay-12)+ ":"+(String.valueOf(minute)+" pm"));

            } else if(hourOfDay==12) {
                //     mTimeText_remind.setText("12"+ ":"+(String.valueOf(minute)+" pm"));

            } else if(hourOfDay<12) {
                if(hourOfDay!=0) {
                    //         mTimeText_remind.setText(String.valueOf(hourOfDay) + ":" + (String.valueOf(minute) + " am"));

                } else {
                    //          mTimeText_remind.setText("12" + ":" + (String.valueOf(minute) + " am"));

                }
            }

        }
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("flag", MODE_PRIVATE).edit();
        editor.putString("time", mTime_event);
        editor.putInt("flag_time_visible", 1);
        editor.apply();
        flag_time_visible=1;

        Date date1 = null;
        Long date_nw = null,date_nw1=null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm" );


        String   current_time = simpleDateFormat.format(calender.getTime());

        String sp=mTime_event.replaceAll(":",".");
         p= Float.valueOf(sp);
        String sp1=current_time.replaceAll(":",".");
         p1= Float.valueOf(sp1);
        difference1= p- p1;
        System.out.println("_______diffreenceeee"+p);
        System.out.println("_______diffreenceeee__hourssss"+p1);


    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        check_one.setChecked(false);
        check_two.setChecked(false);
        check_three.setChecked(false);
        check_four.setChecked(false);
        check_five.setChecked(false);
        flag_check_one=0;
        flag_check_two=0;
        flag_check_three = 0;
        flag_check_four=0;
        System.out.println("_______tag-date"+view.getTag());
        System.out.println("_______tag-date_yearrr"+year);
        System.out.println("_______tag-date_mDate_event"+mDate_event);
        if(view.getTag().equals("Datepickerdialog"))
        { monthOfYear ++;

            mDay_event = dayOfMonth;
            mMonth_event = monthOfYear;
            mYear_event = year;
            mDate_event = dayOfMonth + "/" + monthOfYear + "/" + year;
            txt_DateText_event.setVisibility(View.GONE);
            mDateText_event.setVisibility(View.VISIBLE);
            mDateText_event.setText(mDate_event);

            System.out.println("_______Dataaaeeeeeee"+mDate_event);





        }
        else
        {
            monthOfYear ++;
            mDay = dayOfMonth;
            mMonth = monthOfYear;
            mYear = year;
            mDate = dayOfMonth + "/" + monthOfYear + "/" + year;

            System.out.println("_______Dataaaeeeeeee___"+mDate);
        }
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("flag", MODE_PRIVATE).edit();
        editor.putString("date",  mDate_event);
        editor.putInt("flag_date_visible", 1);
        editor.apply();
        flag_date_visible=1;
        dateDifference = (int) getDateDiff(new SimpleDateFormat("dd/MM/yyyy"),current_date,mDate_event);
        System.out.println("_______1.mDate_event " + mDate_event);
        System.out.println("_______1.mDate " +current_date);
        System.out.println("_______1.dateDifference: " + dateDifference);


    }






    public void saveToSqlite(Calendar calVal,String passed_rem_date,String passed_rem_time)
    {
        Date dated = null,dated1 = null;
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/mm/yyyy", Locale.US);
        try {
            dated= dateFormat.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1=new SimpleDateFormat("dd-mm-yyyy", Locale.US);
        formatted_date_remind=dateFormat1.format(dated);
        System.out.println("_______formatted_date_remind___"+formatted_date);
        try {
            dated1= dateFormat.parse(mDate_event);
        } catch (ParseException e) {
            e.printStackTrace();
        }



            SimpleDateFormat new_date_format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String str_new_datee=calVal.get(Calendar.YEAR)+"-"+(calVal.get(Calendar.MONTH)+1)+"-"+calVal.get(Calendar.DAY_OF_MONTH)+" "+calVal.get(Calendar.HOUR_OF_DAY)+":"+calVal.get(Calendar.MINUTE);
            System.out.println("_______^^^^^^^^^^^^^^^^new_date_string1 : "+str_new_datee);
            Date new_date1=null;
            try {
                new_date1=new_date_format1.parse(str_new_datee);
                System.out.println("_______^^^^^^^^^^^^^^new_date(Date obj)1 : "+new_date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            formatted_date = dateFormat1.format(dated1);
            ReminderDatabase rb = new ReminderDatabase(getActivity());
            int ID = rb.addReminder(new Reminder(str_mTitle, str_Title_type, str_details, mDate_event, mTime_event, passed_rem_date, passed_rem_time, mActive));
            Calendar call1 = Calendar.getInstance();

            call1.setTime(new_date1);
            mCalendar1 = Calendar.getInstance();
            mCalendar1.set(Calendar.MONTH, call1.get(Calendar.MONTH));
            mCalendar1.set(Calendar.YEAR, call1.get(Calendar.YEAR));
            mCalendar1.set(Calendar.DAY_OF_MONTH, call1.get(Calendar.DAY_OF_MONTH));
            mCalendar1.set(Calendar.HOUR_OF_DAY, call1.get(Calendar.HOUR_OF_DAY));
            mCalendar1.set(Calendar.MINUTE, call1.get(Calendar.MINUTE));
            mCalendar1.set(Calendar.SECOND, 0);


            System.out.println("_______ReminderDatabase IDDDDDDDDDDDDDDDDDDDDDDDD...." + ID);
            System.out.println("_______Q1^^^^^^^^^^^^^^ INSIDE saveReminder() ^^^^^^^^^^^");
            System.out.println("_______Q1^^^^^^^^^^^^^^ mCalendar1.MONTH : " + call1.get(Calendar.MONTH));
            System.out.println("_______Q1^^^^^^^^^^^^^^ mCalendar1.YEAR : " + call1.get(Calendar.YEAR));
            System.out.println("_______Q1^^^^^^^^^^^^^^ mCalendar1.DAY_OF_MONTH : " + call1.get(Calendar.DAY_OF_MONTH));
            System.out.println("_______Q1^^^^^^^^^^^^^^ mCalendar1.HOUR_OF_DAY : " + call1.get(Calendar.HOUR_OF_DAY));
            System.out.println("_______Q1^^^^^^^^^^^^^^ mCalendar1.MINUTE : " + call1.get(Calendar.MINUTE));
            System.out.println("_______Q1^^^^^^^^^^^^^^  mCalendar1.getTimeInMillis(): " + mCalendar1.getTimeInMillis());


            new AlarmReceiver().setAlarm(getActivity(), mCalendar1, ID);

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
    public void sendtoserver(final String passed_date, final String passed_time)
    {
        //iteration_flag++;
        event_type.add(str_Title_type);
        stockArr  = new String[event_type.size()];
        stockArr= event_type.toArray(stockArr);
        //  spinner.setItems(stockArr);
        System.out.println("departmentname.stockArr............."+stockArr);

        progress.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.POST, Constants.URL + "add_reminder.php?",
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        System.out.println("response dataa"+response);

                        JSONObject json;

                        ObjectOutput out = null;
                        try {
                            json = new JSONObject(response);
                            if(json.getString("status").contentEquals("true"))
                            {
                                String message=json.getString("message");
                                if(message.contentEquals("Reminder added"))
                                {


                                    serverdialog.setTitleText("Reminder")
                                            .setContentText(message)

                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {

                                                    progress.setVisibility(View.GONE);
                                                    mTitleText.setText("");
                                                    title_type.setText("");
                                                    mTitleText.setHint("Event Title");
                                                    title_type.setHint("Event Type");
                                                    SharedPreferences preferences_flag= getActivity().getSharedPreferences("flag", MODE_PRIVATE);
                                                    preferences_flag.edit().clear().commit();
                                                    if(check_one.isChecked())
                                                    {
                                                        check_one.setChecked(false);
                                                    }
                                                    if(check_two.isChecked())
                                                    {
                                                        check_two.setChecked(false);
                                                    }
                                                    if(check_three.isChecked())
                                                    {
                                                        check_three.setChecked(false);
                                                    }
                                                    if(check_four.isChecked())
                                                    {
                                                        check_four.setChecked(false);
                                                    }
                                                    if(check_five.isChecked())
                                                    {
                                                        check_five.setChecked(false);
                                                    }
                                                    txt_DateText_event.setVisibility(View.VISIBLE);
                                                    // txt_DateText_remind.setVisibility(View.VISIBLE);
                                                    txt_TimeText_event.setVisibility(View.VISIBLE);
                                                    // txt_TimeText_remind.setVisibility(View.VISIBLE);
                                                    //  mDateText_remind.setVisibility(View.GONE);
                                                    //  mTimeText_remind.setVisibility(View.GONE);
                                                    mDateText_event.setVisibility(View.GONE);
                                                    mTimeText_event.setVisibility(View.GONE);
                                                    reminder_details.setText("");
                                                    progress.setVisibility(View.GONE);
                                                    serverdialog.dismiss();
                                                    if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                                                        getActivity().getSupportFragmentManager().popBackStack();
                                                    }
                                                }
                                            });



                                    serverdialog.show();
                                    serverdialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

                                        //}



                                }else
                                {
                                    Toast.makeText(getActivity(),message,
                                            Toast.LENGTH_SHORT).show();


                                }
                            }

                            //ReminderMainFragment.viewPager.setCurrentItem(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                        // Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_SHORT).show();
                        Log.d("", response);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

                // hide the progress dialog

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",userid);
                params.put("event_name",str_mTitle);
                params.put("event_type",str_Title_type);
                params.put("event_details",str_details);
                params.put("event_date",formatted_date);
                params.put("event_time",mTime_event);
                params.put("reminder_date",passed_date );//String.valueOf(formatted_date_remind)
                params.put("reminder_time",passed_time);//mTime
                params.put("time_zone",time_zone1);
                params.put("timezone_id",time_zone_id);
                System.out.println("datessss1"+params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
        requestQueue.add(req);
        // Adding request to request queue
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
