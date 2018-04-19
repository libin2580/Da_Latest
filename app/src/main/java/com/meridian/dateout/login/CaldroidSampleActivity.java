package com.meridian.dateout.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.meridian.dateout.R;
import com.meridian.dateout.explore.calendar.CaldroidSampleCustomFragment;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.model.ScheduleModel;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.meridian.dateout.Constants.analytics;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends AppCompatActivity {
    private boolean undo = false;
    CaldroidFragment caldroidFragment,child_discount_tkt_price;
    private CaldroidFragment dialogCaldroidFragment;
    String minimumdate,maximumdate;
    static String  mnth_remov_zero_min;
    static String  mnth_remov_zero_max;
    Calendar cal;
    LinearLayout linearLayout_schedule;
    public ArrayList<ScheduleModel> scheduleModelArrayList;

    public static ArrayList<ScheduleModel> DisableModelArrayList;

   int deal_id;
    String str_time,calendar_instruction;
    ArrayList<String> arrayListtime;
    ImageView back;
    TextView text_display_time,note_deal;
    ArrayList<Date> disabledDates_fromstart,disabledDates_fromend,disabledDates_fromjson;
    ArrayList<Date> disabledDates_addall;
    Date minDate,maxDate;
    String FORMATTED_MINDATE,FORMATTED_MAXDATE,deal_type;
    String calendar_show;
    LinearLayout gifts_calendar,layout_deal_note;
    TextView next;

    TextView toolbar_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        back= (ImageView) findViewById(R.id.img_crcdtlnam);
        next = (Button) findViewById(R.id.next);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        deal_id = getIntent().getIntExtra("deal_id", 0);
        deal_type= getIntent().getStringExtra("deal_type");
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat formatter1 = new SimpleDateFormat("MM");
        final SimpleDateFormat formatterday = new SimpleDateFormat("dd");
        final SimpleDateFormat formattermnth = new SimpleDateFormat("MM");
        final SimpleDateFormat formatteryear = new SimpleDateFormat("yyyy");
        linearLayout_schedule = (LinearLayout) findViewById(R.id.linear_sch);
        gifts_calendar= (LinearLayout) findViewById(R.id.gifts_calendar);
        layout_deal_note= (LinearLayout) findViewById(R.id.layout_deal_note);
        linearLayout_schedule = (LinearLayout) findViewById(R.id.linear_sch);
        text_display_time= (TextView) findViewById(R.id.txt_display_time);
        note_deal= (TextView) findViewById(R.id.note_deal);
        analytics = FirebaseAnalytics.getInstance(CaldroidSampleActivity.this);
        analytics.setCurrentScreen(CaldroidSampleActivity.this,CaldroidSampleActivity.this.getLocalClassName(), null /* class override */);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

   CategoryDealDetail.checkout.performClick();

//                Intent i=new Intent(getApplicationContext(),Booking_DetailsActivity.class);
//                startActivity(i);
            }
        });
        toolbar_name= (TextView) findViewById(R.id.toolbar_CRCNAM);
        if(getIntent().getStringExtra("calendar_instruction")!=null) {
            calendar_instruction=getIntent().getStringExtra("calendar_instruction");
        }
        if(getIntent().getStringExtra("calendar_show")!=null) {
            calendar_show = getIntent().getStringExtra("calendar_show");
            System.out.println("calendar showwww"+calendar_show);

            if(calendar_show.contentEquals("no")) {
                gifts_calendar.setVisibility(View.GONE);
                layout_deal_note.setVisibility(View.VISIBLE);

            }
            else {
                note_deal.setText(calendar_instruction);
                gifts_calendar.setVisibility(View.VISIBLE);
                layout_deal_note.setVisibility(View.GONE);

            }

        }
        if(deal_type!=null) {
            toolbar_name.setText("Availability of Deal Activity/Gift Delivery");
//            if (deal_type.contentEquals("gifts")) {
//                toolbar_name.setText("Availability of Gift Delivery");
//
//
//            } else if (deal_type.contentEquals("food_and_beverages")) {
//                toolbar_name.setText("Availability of Gift Delivery");
//
//            } else {
//                toolbar_name.setText("Availability of deal");
//
//            }
        }


        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
       // caldroidFragment = new CaldroidFragment();



        caldroidFragment = new CaldroidSampleCustomFragment();
        if (savedInstanceState != null)
        {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);

            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
            caldroidFragment.setArguments(args);
        }

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();


        cal = Calendar.getInstance();

        schedule();

        minDate = cal.getTime();
        minimumdate= String.valueOf(minDate);
        String removezero_min=formatter1.format(minDate);
        FORMATTED_MINDATE=formatter.format(minDate);
        if (removezero_min.startsWith("0")) {
            mnth_remov_zero_min = removezero_min.replaceFirst("0", "");
        } else {
            mnth_remov_zero_min = removezero_min;
        }
        System.out.println("minimumdate..........."+ minimumdate);
        System.out.println("remove zero...formatted..minimumdate..........."+ mnth_remov_zero_min);
        System.out.println("formatter....minimumdate..........."+ FORMATTED_MINDATE);

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 3); // Jump two months ahead
        cal.add(Calendar.DATE, -1);
        maxDate = cal.getTime();
        FORMATTED_MAXDATE=formatter.format(maxDate);


        maximumdate= String.valueOf(maxDate);
        System.out.println("maximumdate..........."+maximumdate);
        System.out.println("formatted..maximumdate..........."+formatter1.format(maxDate));
        System.out.println("formatter....maximumdate..........."+ FORMATTED_MAXDATE);
        String removezero_max=formatter1.format(maxDate);

        if (removezero_max.startsWith("0")) {
            mnth_remov_zero_max = removezero_max.replaceFirst("0", "");
        } else {
            mnth_remov_zero_max = removezero_max;
        }
        System.out.println("remove zero...formatted..maximumdate..........."+mnth_remov_zero_max);



        // Customize
        caldroidFragment.setMinDate(minDate);
        caldroidFragment.setMaxDate(maxDate);




        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                System.out.println("dateeeedddd..." + date);
//                Toast.makeText(getApplicationContext(), formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
//                caldroidFragment.setBackgroundDrawableForDate(R.drawable.round, date);


                //    caldroidFragment.setBackgroundDrawableForDate(getResources().get(R.color.black), date);


                if (scheduleModelArrayList != null && !scheduleModelArrayList.isEmpty()) {


                    for (ScheduleModel scheduleModel : scheduleModelArrayList) {

                        String[] parts = scheduleModel.getDate().split("-");

                        String year = parts[0]; // 004
                        String mnth = parts[1];
                        String day = parts[2];

                        String calendar_getday = String.valueOf(formatterday.format(date));
                        String calendar_getmonth = String.valueOf(formattermnth.format(date));
                        String calendar_getyear = String.valueOf(formatteryear.format(date));
                        System.out.println("calendar_day......" + calendar_getday + ".....jsonday...." + day);
                        System.out.println("calendar_month......" + calendar_getmonth + ".....jsonmonth...." +mnth);
                        System.out.println("calendar_year......" + calendar_getyear + ".....jsonyear...." + year);


                        if (calendar_getday.equals(day) && calendar_getmonth.equals(mnth) && calendar_getyear.equals(year)) {
                            System.out.println("dateeee..." + scheduleModel.getDate());
                          //  booking_date = scheduleModel.getDate();

                          //  linearLayout_schedule.removeAllViews();
                            String k = null;
                            for (int i = 0; i < scheduleModel.getTime().size(); i++)
                            {

                                k=scheduleModel.getTime().get(i);

                                    k = k+ "," ;
                               // }
                               // else {

                               // }
                                System.out.println("scheduled all timeeee........."+scheduleModel.getTime());
                                System.out.println("individual time"+scheduleModel.getTime().get(i));

                                System.out.println("appended time...."+ k);
                            }
                            String str = scheduleModel.getTime().toString().replaceAll("\\[", "").replaceAll("\\]","");
                            text_display_time.setText("" +str);

                       // if()) {
                              // text_display_time.setText("" + k.replaceAll("["," ")
                          // }


                        } else {


                        }


                    }
                } else {

                }








            }

            @Override
            public void onChangeMonth(int month, int year) {

                String text = "month: " + month + " year: " + year;
                String mnth= String.valueOf(month);
//                Toast.makeText(getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
                if(mnth.equals(mnth_remov_zero_max)) {

                    caldroidFragment.getRightArrowButton().setVisibility(View.INVISIBLE);
                    caldroidFragment.getLeftArrowButton().setVisibility(View.VISIBLE);

                }
                else if(mnth.equals(mnth_remov_zero_min))
                { caldroidFragment.getLeftArrowButton().setVisibility(View.INVISIBLE);
                    caldroidFragment.getRightArrowButton().setVisibility(View.VISIBLE);
                }
                else if(!mnth.equals(mnth_remov_zero_max)&&!mnth.equals(mnth_remov_zero_min))
                {
                    caldroidFragment.getLeftArrowButton().setVisibility(View.VISIBLE);
                    caldroidFragment.getRightArrowButton().setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onLongClickDate(Date date, View view) {
//                Toast.makeText(getApplicationContext(),
//                        "Long click " + formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
//                    Toast.makeText(getApplicationContext(),
//                            "Caldroid view is created", Toast.LENGTH_SHORT)
//                            .show();
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);
        // Setup Caldroid


       // final TextView textView = (TextView) findViewById(R.id.textview);






    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (dialogCaldroidFragment != null) {
            dialogCaldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }


    public void schedule() {

        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
        boolean i = networkCheckingClass.ckeckinternet();
        if (i == true) {


            String Schedule_url = Constants.URL+"dealschedule.php?";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Schedule_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //  tv.setText("Response is: "+ response);

                            System.out.println("++++++++++++++RESPONSE+++++++++++++++   schedule detail :" + response);


                            try {


                                JSONObject obj = new JSONObject(response);
                                {
                                    scheduleModelArrayList=new ArrayList<ScheduleModel>();
                                    DisableModelArrayList= new ArrayList<ScheduleModel>();

                                    if (obj.has("Enabled Dates")&&!obj.getString("Enabled Dates").equals("null"))
                                    {
                                        try {
                                        JSONArray jsonarray = obj.getJSONArray("Enabled Dates");



                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                ScheduleModel scheduleModel = new ScheduleModel();
                                                arrayListtime = new ArrayList<>();


                                                String date = jsonobject.getString("date");
                                                System.out.println("date...enabled......+" + date);
                                                String day = jsonobject.getString("day");
                                                String month = jsonobject.getString("month");
                                                String year = jsonobject.getString("year");
                                                System.out.println("month>>>>>>>>>>>"+month);
                                                if (jsonobject.has("dayscount"))
                                                {
                                                    String dayscount = jsonobject.getString("dayscount");
                                                    scheduleModel.setDayscount(dayscount);
                                                }
                                                JSONArray time = jsonobject.getJSONArray("time");
                                                for (int j = 0; j < time.length(); j++)
                                                {

                                                    scheduleModel.setStr_time(time.getString(j));
                                                    arrayListtime.add(time.getString(j));
                                                }
                                                scheduleModel.setDate(date);
                                                scheduleModel.setDay(day);
                                                scheduleModel.setMonth(month);
                                                scheduleModel.setYear(year);
                                                scheduleModel.setTime(arrayListtime);

                                                scheduleModelArrayList.add(scheduleModel);
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {   caldroidFragment.refreshView();
                                        ArrayList<Date> disabledDates_fromstart1=new ArrayList<>();
                                        disabledDates_fromstart1 = getDates(FORMATTED_MINDATE, FORMATTED_MAXDATE);
                                        caldroidFragment.setDisableDates(disabledDates_fromstart1);
                                    }
                                    if(obj.has("Disabled Dates")&&!obj.getString("Disabled Dates").equals("null"))
                                    {
                                        try
                                        {

                                                JSONArray jsonarray = obj.getJSONArray("Disabled Dates");

                                                for (int i = 0; i < jsonarray.length(); i++) {
                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                    ScheduleModel scheduleModel = new ScheduleModel();
                                                    arrayListtime = new ArrayList<>();
                                                    String date = jsonobject.getString("date");
                                                    System.out.println("date...disabled........+" + date);
                                                    String day = jsonobject.getString("day");
                                                    String month = jsonobject.getString("month");
                                                    String year = jsonobject.getString("" + "year");
                                                    if (jsonobject.has("dayscount"))
                                                    {   String dayscount = jsonobject.getString("dayscount");
                                                        scheduleModel.setDayscount(dayscount);
                                                    }
                                                    scheduleModel.setDate(date);

                                                    DisableModelArrayList.add(scheduleModel);
                                                }

                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
//                                    disable_enablearraylist.addAll(scheduleModelArrayList);
//                                    disable_enablearraylist.addAll(DisableModelArrayList);
                                }


                                if (scheduleModelArrayList != null && !scheduleModelArrayList.isEmpty())
                                {



                                    System.out.println("schedulemodel...getdate....." + scheduleModelArrayList.get(0).getDate()+"FORMATTED_MINDATE...."+FORMATTED_MINDATE);
                                    System.out.println("schedulemodel...getdatelast....." + scheduleModelArrayList.get(scheduleModelArrayList.size()- 1).getDate()+"FORMATTED_MAXDATE....."+FORMATTED_MAXDATE);

                                    String dateconvert_part1=formatDate(scheduleModelArrayList.get(0).getDate(),"yyyy-MM-dd","EE MMM dd HH:mm:ss z yyyy");
                                    DateFormat formatter = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                    Date date = formatter.parse(dateconvert_part1);
                                    String dateconvert_part2=formatDate(scheduleModelArrayList.get(scheduleModelArrayList.size()- 1).getDate(),"yyyy-MM-dd","EE MMM dd HH:mm:ss z yyyy");
                                    DateFormat formatter2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                    Date date2 = formatter2.parse(dateconvert_part2);


                                    System.out.println("dateconvertd.................." + date);
                                    System.out.println("dateconvertd1.................." +date2);

                                    disabledDates_addall = new ArrayList<Date>();
                                    disabledDates_fromstart = new ArrayList<Date>();
                                    disabledDates_fromend = new ArrayList<Date>();
                                    disabledDates_fromjson = new ArrayList<Date>();

                                    System.out.println("schedulemodel...getdate....." + scheduleModelArrayList.get(0).getDate() + "FORMATTED_MINDATE...." + FORMATTED_MINDATE);

                                    String[] partsfrst = scheduleModelArrayList.get(0).getDate().split("-");

                                    String year0 = partsfrst[0]; // 004
                                    String mnth0 = partsfrst[1];
                                    String day0 = partsfrst[2];
                                    int s= Integer.parseInt(day0)-1;

                                    String p=year0+"-"+mnth0+"-"+s;

                                    String[] partsland = scheduleModelArrayList.get(scheduleModelArrayList.size() - 1).getDate().split("-");

                                    String year1 = partsland[0]; // 004
                                    String mnth1 = partsland[1];
                                    String day1 = partsland[2];

                                    int s1= Integer.parseInt(day1)+1;

                                    String p1=year1+"-"+mnth1+"-"+s1;
                                    System.out.println("schedulemodel...getdatelast2222....." + p+"...."+scheduleModelArrayList.get(scheduleModelArrayList.size() - 1).getDate() + "FORMATTED_MAXDATE....." + FORMATTED_MAXDATE);
                                    disabledDates_fromstart = getDates(FORMATTED_MINDATE,p);

                                    disabledDates_fromend = getDates(p1, FORMATTED_MAXDATE);
                                    System.out.println("disabledDates_fromstart.........." + disabledDates_fromstart);
                                    System.out.println("disabledDates_fromend.........." + disabledDates_fromend);
                                    disabledDates_addall.addAll(disabledDates_fromstart);
                                    disabledDates_addall.addAll(disabledDates_fromend);
                                    if(DisableModelArrayList!=null&&!DisableModelArrayList.isEmpty())
                                    {
                                        for (ScheduleModel scheduleModel : DisableModelArrayList)
                                        {
                                            String dateconvert_part3 = formatDate(scheduleModel.getDate(), "yyyy-MM-dd", "EE MMM dd HH:mm:ss z yyyy");
                                            DateFormat formatter3 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                            Date dates = formatter3.parse(dateconvert_part3);
                                            System.out.println("date____disabled in json.................." + dates);

                                            disabledDates_fromjson.add(dates);
                                            caldroidFragment.setTextColorForDate(R.color.caldroid_darker_gray, dates);
                                        }
                                        if(disabledDates_fromjson!=null)
                                        {
                                            disabledDates_addall.addAll(disabledDates_fromjson);
                                        }
                                    }
//                                    for (ScheduleModel scheduleModel : scheduleModelArrayList) {


                                    caldroidFragment.refreshView();
                                    System.out.println("datessss.....newww"+date);
                                    caldroidFragment.setSelectedDates(date,date2);

                                    if(disabledDates_addall!=null)
                                    {
                                        caldroidFragment.setDisableDates(disabledDates_addall);
                                    }


                                }



                            }
                            catch (JSONException e)
                            {

                                e.printStackTrace();
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("deal_id", String.valueOf(deal_id));
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(CaldroidSampleActivity.this,SweetAlertDialog.NORMAL_TYPE);
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





    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException
    {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }
    public static ArrayList<Date> getDates(String dateString1, String dateString2)
    {
        ArrayList<Date> dates = new ArrayList<Date>();
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1 .parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}