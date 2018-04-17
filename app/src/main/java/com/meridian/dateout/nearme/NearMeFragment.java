package com.meridian.dateout.nearme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.collections.HttpHandler;
import com.meridian.dateout.explore.cart.Cart_details;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.model.DealsModel;
import com.meridian.dateout.nearme.map.DetectConnection;
import com.meridian.dateout.nearme.map.GPSTracker1;
import com.meridian.dateout.nearme.map.GoogleMapModel;
import com.meridian.dateout.nearme.map.HttpConnection;
import com.meridian.dateout.nearme.map.PathJSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.meridian.dateout.login.FrameLayoutActivity.places;

/**
 * Created by Anvin on 10/4/2017.
 */

public class NearMeFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    String firstName, lastName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Polyline line;
    private BottomSheetBehavior mBottomSheetBehavior;
    public static float zoom = 10;
    public HashMap<String, Marker> hashMapMarker;//for storing marker with a key value(key value is 'username')
    public CoordinatorLayout main_layout;
    GPSTracker1 gps;
    double mylongitude, mylatitude;
    public static LatLng MY_LOCATION = new LatLng(0, 0);
    public static LatLng ORIGINAL_LOCATION = new LatLng(0, 0);

    public static GoogleMap mMap;
    // public static Circle circle;
    private TextView marker_name, marker_distance, marker_duration, marker_description, marker_price;
    private LinearLayout btn_navigate;
    public ProgressBar location_spec_progressbar;
    private static LatLng CLICKED_MARKER;
    private ArrayList<DealsModel> dealsModelArrayList;
    private ProgressBar main_progressbar;
    private ImageView deal_imageview;
    private String[] id_slug_array;
    private String[] description;
    private String[] image_array;
    private String[] title;
    private String[] price_array;
    private String[] currency_array;
    private String[] id_array;
    SupportMapFragment mapFragment;
    MapView mapView;
    //SupportPlaceAutocompleteFragment places;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView autoCompleteTextView;

    private OnFragmentInteractionListener mListener;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyAo27AsUvSaSFEQ5OZWGc4l2SczpZhwnL4";

    private static String place_ids[];
    String selected_placeid = "", str_id;
    Marker my_loc_marker;
    Marker new_marker;
    RelativeLayout linear_mylocation, direction;
    private boolean location_selected = false;
    private LatLng new_positon;
    private String selected_place = "";
    LinearLayout selected_location_layout, Popup_submit, popup_close, linr_txt;
    TextView selected_location_name;
    TextView Categoty_txt;
    ImageView selected_location_close, Re_center;
    EditText Range_frm, Range_to;
    View custompopup_view;
    PopupWindow filter_popupwindow;
    ArrayList<String> SpinList;
    ArrayList<Spinner_model> Spin_list = new ArrayList<>();
    RelativeLayout topLayout;
    int count;

    List<Marker> markers;
    int marker_found_flag;
    Spinner_model spinnerModel;
    SpinnerDialog doctor_spinner;
    ProgressBar progress;
    DealsModel dealsModel;

    public NearMeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NearMeFragment newInstance() {
        NearMeFragment fragment = new NearMeFragment();

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

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nearme, container, false);
        //    FrameLayoutActivity.toolbar.setVisibility(View.VISIBLE);
        final String user_id, str_fullname, str_email, photo, str_emails, str_names, str_fullname1, str_email1;
        FrameLayoutActivity.toolbar.setVisibility(View.VISIBLE);
        FrameLayoutActivity.img_toolbar_crcname.setText("NEAR ME");
        FrameLayoutActivity.img_top_faq.setVisibility(View.GONE);

        FrameLayoutActivity.search_nearby.setVisibility(View.VISIBLE);
        FrameLayoutActivity.my_location.setVisibility(View.GONE);
        FrameLayoutActivity.filter.setVisibility(View.VISIBLE);
        FrameLayoutActivity.cart.setVisibility(View.VISIBLE);
        Re_center = (ImageView) v.findViewById(R.id.recenter);

        markers = new ArrayList<>();
        FrameLayoutActivity.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaypopup_filter_popupwindow();

            }
        });
        FrameLayoutActivity.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), Cart_details.class);
                startActivity(i);
            }
        });

        selected_location_layout = (LinearLayout) v.findViewById(R.id.selected_location_layout);
        selected_location_name = (TextView) v.findViewById(R.id.selected_location_name);
        selected_location_close = (ImageView) v.findViewById(R.id.selected_location_close);
        final LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        custompopup_view = inflator.inflate(R.layout.popup_filter, null);
        topLayout = (RelativeLayout) v.findViewById(R.id.top_layout);
        direction = (RelativeLayout) v.findViewById(R.id.direction);

        try {

            popup_close = (LinearLayout) custompopup_view.findViewById(R.id.closebutton);
            Popup_submit = (LinearLayout) custompopup_view.findViewById(R.id.linr_submit);
            linr_txt = (LinearLayout) custompopup_view.findViewById(R.id.linr_txt);
            Range_to = (EditText) custompopup_view.findViewById(R.id.range_to);
            Range_frm = (EditText) custompopup_view.findViewById(R.id.range_frm);
            Categoty_txt = (TextView) custompopup_view.findViewById(R.id.cat_txt);
            progress = (ProgressBar) custompopup_view.findViewById(R.id.progress);
            Fuel.get(Constants.URL + "categories.php").responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                @Override
                public void success(Request request, Response response, String s) {
                    try {
                        JSONArray jsonarray = new JSONArray(s);
                        SpinList = new ArrayList<>();


                        for (int i = 0; i < jsonarray.length(); i++) {
                            spinnerModel = new Spinner_model();
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String id = jsonobject.getString("id");
                            String category = jsonobject.getString("category");
                            spinnerModel.setId(id);
                            Spin_list.add(spinnerModel);
                            SpinList.add(category);
                            doctor_spinner = new SpinnerDialog(getActivity(), SpinList, "Search Category");
                            doctor_spinner.bindOnSpinerListener(new OnSpinerItemClick() {
                                @Override
                                public void onClick(String item, int position) {
                                    Categoty_txt.setText(item);
                                    String gg = Spin_list.get(position).getId();

                                    str_id = gg;
                                }
                            });
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(Request request, Response response, FuelError fuelError) {

                }
            });


            linr_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doctor_spinner.showSpinerDialog();


                }
            });
            Categoty_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doctor_spinner.showSpinerDialog();


                }
            });
            Popup_submit.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {

                    String fr_range = Range_frm.getText().toString();
                    String to_range = Range_to.getText().toString();


                    if (Objects.equals(str_id, null)) {
                        Toast.makeText(getContext(), "Select Category", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (Range_frm.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Range from is empty", Toast.LENGTH_LONG).show();

                        return;
                    }
                    if (Range_to.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Range to is empty", Toast.LENGTH_LONG).show();
                        return;
                    }
                    int from = Integer.parseInt(fr_range);
                    int to = Integer.parseInt(to_range);
                    if (from > to) {
                        Toast.makeText(getContext(), "to price must be greater than from price", Toast.LENGTH_LONG).show();
                        return;
                    }
                    progress.setVisibility(View.VISIBLE);
                    Fuel.get(Constants.URL + "deals-by-filter.php?category=" + str_id + "&rate_from=" + fr_range + "&rate_to=" + to_range).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                        @Override
                        public void success(Request request, Response response, String s) {
                            progress.setVisibility(View.GONE);
                            filter_popupwindow.dismiss();
                            mMap.clear();
                            if (s.isEmpty()) {

                                Toast.makeText(getApplicationContext(), "No any near by deals please search a location", Toast.LENGTH_SHORT).show();
                            } else {

                                try {
                                    JSONArray jsonarray = new JSONArray(s);
                                    dealsModelArrayList = new ArrayList<>();
                                    dealsModelArrayList.clear();
                                    title = new String[jsonarray.length()];
                                    description = new String[jsonarray.length()];
                                    image_array = new String[jsonarray.length()];
                                    price_array = new String[jsonarray.length()];
                                    id_slug_array = new String[jsonarray.length()];
                                    currency_array = new String[jsonarray.length()];
                                    id_array = new String[jsonarray.length()];

                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        dealsModel = new DealsModel();

                                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                                        if (jsonobject.has("id")) {
                                            //id_deal = jsonobject.getString("id");
                                            dealsModel.setId(jsonobject.getString("id"));
                                        }

                                        if (jsonobject.has("title")) {
                                            //title = jsonobject.getString("title");
                                            dealsModel.setTitle(jsonobject.getString("title"));
                                        }
                                        if (jsonobject.has("image")) {
                                            //image = jsonobject.getString("image");
                                            dealsModel.setImage(jsonobject.getString("image"));
                                        }
                                        if (jsonobject.has("description")) {
                                            //description = jsonobject.getString("description");
                                            dealsModel.setDescription(jsonobject.getString("description"));
                                        }
                                        if (jsonobject.has("latitude")) {
                                            //latitude = jsonobject.getString("latitude");
                                            dealsModel.setLatitude(jsonobject.getString("latitude"));
                                        }
                                        if (jsonobject.has("longitude")) {
                                            //longitude  = jsonobject.getString("longitude");
                                            dealsModel.setLongitude(jsonobject.getString("longitude"));
                                        }
                                        if (jsonobject.has("timing")) {
                                            //timing = jsonobject.getString("timing");
                                            dealsModel.setTiming(jsonobject.getString("timing"));
                                        }
                                        if (jsonobject.has("delivery")) {
                                            //delivery = jsonobject.getString("delivery");
                                            dealsModel.setDelivery(jsonobject.getString("delivery"));
                                        }
                                        if (jsonobject.has("category")) {
                                            //category1 = jsonobject.getString("category");
                                            dealsModel.setCategory(jsonobject.getString("category"));
                                        }
                                        if (jsonobject.has("tags")) {
                                            //tags = jsonobject.getString("tags");
                                            dealsModel.setTags(jsonobject.getString("tags"));
                                        }
                                        if (jsonobject.has("price")) {
                                            //price = jsonobject.getString("price");
                                            if (jsonobject.getString("price") == null || jsonobject.getString("price").equals(0)) {
                                                dealsModel.setPrice("0");
                                            } else {
                                                dealsModel.setPrice(jsonobject.getString("price"));
                                            }

                                        }
                                        if (jsonobject.has("tkt_discounted_price")) {
                                            //tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                                            dealsModel.setDiscount(jsonobject.getString("tkt_discounted_price"));
                                        }
                                        if (jsonobject.has("seller_id")) {
                                            //seller_id = jsonobject.getString("seller_id");
                                            dealsModel.setSeller_id(jsonobject.getString("seller_id"));
                                        }
                                        if (jsonobject.has("currency")) {
                                            //currency = jsonobject.getString("currency");
                                            dealsModel.setCurrency(jsonobject.getString("currency"));
                                        }
                                        if (jsonobject.has("deal_slug")) {
                                            //currency = jsonobject.getString("currency");
                                            dealsModel.setDeal_slug(jsonobject.getString("deal_slug"));
                                        }


                                        // dealsModel.set
                                        dealsModel.setType("deal");

                                        id_array[i] = jsonobject.getString("id");
                                        id_slug_array[i] = jsonobject.getString("deal_slug");
                                        title[i] = jsonobject.getString("title");
                                        description[i] = jsonobject.getString("description");
                                        image_array[i] = jsonobject.getString("image");
                                        price_array[i] = jsonobject.getString("price");
                                        currency_array[i] = jsonobject.getString("currency");

                                        dealsModelArrayList.add(dealsModel);

                                        addMarkers();


                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        }

                        @Override
                        public void failure(Request request, Response response, FuelError fuelError) {

                        }
                    });

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter_popupwindow.dismiss();
            }
        });


        //start = (Button) v.findViewById(R.id.chat_start);
        /* places= (PlaceAutocompleteFragment)
                 getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);*/
        // places = (SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        location_selected = false;


        main_progressbar = (ProgressBar) v.findViewById(R.id.main_progressbar);
        hashMapMarker = new HashMap<>();
        SharedPreferences preferences_user = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id = preferences_user.getString("user_id", null);
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
        linear_mylocation = (RelativeLayout) v.findViewById(R.id.linear_mylocation);
        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));

        marker_name = (TextView) v.findViewById(R.id.marker_name);
        marker_distance = (TextView) v.findViewById(R.id.marker_distance);
        marker_duration = (TextView) v.findViewById(R.id.marker_duration);
        marker_description = (TextView) v.findViewById(R.id.marker_description);
        marker_price = (TextView) v.findViewById(R.id.marker_price);
        deal_imageview = (ImageView) v.findViewById(R.id.deal_imageview);
        btn_navigate = (LinearLayout) v.findViewById(R.id.btn_navigate);
        location_spec_progressbar = (ProgressBar) v.findViewById(R.id.location_spec_progressbar);
        View bottomSheet = v.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // change the state of the bottom sheet
        // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        // mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

// set the peek height
        //mBottomSheetBehavior.setPeekHeight(80);

// set hideable or not
        mBottomSheetBehavior.setHideable(true);

// set callback for changes
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        try {
            MapsInitializer.initialize(this.getActivity());
            mapView = (MapView) v.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        } catch (InflateException e) {
            Log.e("", "Inflate exception");
        } catch (Exception e) {

        }

         /*mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);*/
        FrameLayoutActivity.search_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("inside fragment click");
                final View root = places.getView();

                root.post(new Runnable() {
                    @Override
                    public void run() {
                        root.findViewById(R.id.place_autocomplete_search_input)
                                .performClick();
                    }
                });

            }
        });
        selected_location_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_location_layout.setVisibility(View.GONE);
                selected_place = null;


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ORIGINAL_LOCATION,
                        zoom));
                //circle.setCenter(ORIGINAL_LOCATION);
                try {
                    new_marker.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new GetDeals().execute();

            }
        });
        FrameLayoutActivity.my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_location_layout.setVisibility(View.GONE);
                selected_place = null;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ORIGINAL_LOCATION, zoom));
                //circle.setCenter(ORIGINAL_LOCATION);
                try {
                    new_marker.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new GetDeals().execute();
            }
        });

        FrameLayoutActivity.places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                location_selected = true;

                new_positon = place.getLatLng();
                selected_place = place.getName().toString();
                System.out.println("selected_place : " + selected_place);
                gotoSelectedPlace();
                //new GetDeals().execute();


                //Toast.makeText(getApplicationContext(),place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                location_selected = false;
                Toast.makeText(getApplicationContext(), "No any near by deals please search a location", Toast.LENGTH_SHORT).show();

            }
        });

        new GetDeals().execute();

        linear_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ORIGINAL_LOCATION,
                        zoom));
                //circle.setCenter(ORIGINAL_LOCATION);
                try {
                    new_marker.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (DetectConnection
                .checkInternetConnection(getApplicationContext())) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                   /* Intent intent = getIntent();
                    finish();
                    startActivity(intent);*/
                } else {
                    showGPSDisabledAlertToUser();
                }
            } else {
                gps = new GPSTracker1(getActivity());

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    mylatitude = gps.getLatitude();
                    mylongitude = gps.getLongitude();


                    MY_LOCATION = new LatLng(mylatitude, mylongitude);
                    System.out.println("MY_LOCATION : " + MY_LOCATION);
                    ORIGINAL_LOCATION = MY_LOCATION;


                    // \n is for new line
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    // gps.showSettingsAlert();
                    showGPSDisabledAlertToUser();

                }
            }

        } else {
            Toast.makeText(getActivity(), "Oops Your Connection Seems Off..!", Toast.LENGTH_SHORT).show();
        }

        Re_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mylatitude, mylongitude)).zoom(17).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
        // transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        transaction.replace(R.id.flFragmentPlaceHolder, fragment, s).addToBackStack(s);
        transaction.commit();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Please enable GPS and set MODE to High Accuracy. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                getActivity().finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        //2. now setup to change color of the button


        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black));
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));
    }

    public void onMapReady(GoogleMap googleMap) {
        System.out.println("[[[[[[[[[[[[[[[ inside on map ready");
        mMap = googleMap;



        // change compass position

        try {
            assert mapFragment.getView() != null;
            final ViewGroup parent = (ViewGroup) mapFragment.getView().findViewWithTag("GoogleMapMyLocationButton").getParent();
            parent.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0, n = parent.getChildCount(); i < n; i++) {
                            View view = parent.getChildAt(i);
                            View compassButton = mapView.findViewWithTag("GoogleMapCompass");//this works for me
                            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_END);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                            rlp.addRule(RelativeLayout.ALIGN_PARENT_START,0);
                            rlp.topMargin = 50;
                            view.requestLayout();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.style_json));
            if (!success) {
                Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("", "Can't find style. Error: ", e);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MY_LOCATION,
                zoom));
        //addMarkers();
        mMap.setPadding(0,0,0,0);


        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                System.out.println(latLng);
            }
        });

      if(selected_place!=null) {
            try {
                System.out.println("place.getLatLng(); : " + new_positon);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new_positon,
                        zoom));
               // circle.setCenter(new_positon);

                //my_loc_marker.setPosition(new_positon);
                int height = 100;
                int width = 100;
                //BitmapDrawable bitmapdraw_home=(BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.gift);
                Bitmap b = FrameLayoutActivity.bitmapdraw_search.getBitmap();
                Bitmap searched_marker = Bitmap.createScaledBitmap(b, width, height, false);
                try {
                    new_marker.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new_marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(searched_marker)).position(new_positon));
                selected_location_name.setText(selected_place);
                selected_location_layout.setVisibility(View.VISIBLE);

                new GetDeals().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
       // }

      }
    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        System.out.println("marker.getTitle() : " + marker.getTitle());
        // System.out.println("PackagerHomePage.name : "+PackagerHomePage.name);
        if (marker.getTitle() != null){

            if (!marker.getTitle().equalsIgnoreCase("me")) {


                marker_name.setText(marker.getTitle().toString());
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                System.out.println("inside marker click");
                CLICKED_MARKER = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                System.out.println("CLICKED_MARKER : " + CLICKED_MARKER);


                marker_distance.setText("");
                marker_duration.setText("");
                marker_description.setText("");
                marker_price.setText("");


                String url = getMapsApiDirectionsUrl(CLICKED_MARKER);
                System.out.println("inside marker click -> url : " + url);
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);

                for (int k = 0; k < title.length; k++) {
                    if (title[k].equalsIgnoreCase(marker.getTitle())) {
                        System.out.println("image_array[k] : " + image_array[k]);
                        System.out.println("description[k] : " + description[k]);
                        System.out.println("Price : " + currency_array[k] + price_array[k]);

                    /*Glide.with(getActivity())
                            .load(image_array[k])
                            .into(deal_imageview);*/

                        marker_description.setText(description[k]);
                        marker_price.setText("Price : " + currency_array[k] + price_array[k]);

                        final String id_val = id_array[k];
                        btn_navigate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                    /*Uri gmmIntentUri = Uri.parse("google.navigation:q="+CLICKED_MARKER.latitude+","+CLICKED_MARKER.longitude);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);*/

                                Intent intnt = new Intent(getActivity(), CategoryDealDetail.class);
                                intnt.putExtra("deal_id", Integer.parseInt(id_val));
                                startActivity(intnt);

                            }
                        });

                    }
                }


            } else {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                try {
                    line.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
        return false;
    }
    private String getMapsApiDirectionsUrl(LatLng clicked_loc) {
        String waypoints = "waypoints=optimize:true|"
                + MY_LOCATION.latitude + "," + MY_LOCATION.longitude
                + "|"+clicked_loc.latitude + ","
                + clicked_loc.longitude;
        String sensor = "sensor=false";
        String origin="destination="+clicked_loc.latitude+","+clicked_loc.longitude;
        String destination="origin="+MY_LOCATION.latitude+","+MY_LOCATION.longitude;
        String mode="mode=driving";
        //String key="key=AIzaSyBb9KOCLuGoTq2-_2pclfulXNOOPPs5Dg4";//this is browser key not android api key
        // String params = waypoints + "&" + sensor+ "&" +origin+ "&" +destination+ "&" +mode+ "&" +key+"&alternatives=true";
        String params = sensor+ "&" +origin+ "&" +destination+ "&" +mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        System.out.println("url : "+url);
        return url;
    }

    private void addMarkers() {
        if (mMap != null) {
            System.out.println("^^^^^^^^^^^^^^^^^^^ insdie addMarkers() ^^^^^^^^^^^^^^^^^");
            //System.out.println("name : " + name);
           // System.out.println("status : " + status);
            //System.out.println("cordinate : " + cordinates.toString());

            int height = 100;
            int width = 100;
            //BitmapDrawable bitmapdraw_home=(BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.gift);
            Bitmap b=FrameLayoutActivity.bitmapdraw_home.getBitmap();
            Bitmap home_smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            //BitmapDrawable bitmapdraw_me=(BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.meeeee);
            Bitmap a=FrameLayoutActivity.bitmapdraw_me.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(a, width, height, false);

            //////////////////////////////////////////--- OUR LOCATION --START --////////////////////////////////////////////////////////
            if(hashMapMarker.containsKey("0")) //deleting marker from map to prevent displaying same marker at two different cordinates,,'0' denotes out location key in hashmap
            {
                Marker mkr=hashMapMarker.get("0");
                mkr.remove();
                hashMapMarker.remove("0");
            }
             my_loc_marker=mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).position(MY_LOCATION).title("Me")
            );//.title(PackagerHomePage.name)
            hashMapMarker.put("0",my_loc_marker); //adding our location marker to hashmap to check on next service call
            //////////////////////////////////////////--- OUR LOCATION --END --////////////////////////////////////////////////////////


            //////////////////////////////////////////--- DELIVERY BOY LOCATION --START --////////////////////////////////////////////////////////
            System.out.println("inside location_circle.setOnClickListener");
            float[] distance = new float[2];
             //count=0;
            markers.clear();
            for(DealsModel dm:dealsModelArrayList) {
                try {
                    LatLng cordinate = new LatLng(Double.parseDouble(dm.getLatitude()), Double.parseDouble(dm.getLongitude()));
                   /* Location.distanceBetween(cordinate.latitude, cordinate.longitude,
                            circle.getCenter().latitude, circle.getCenter().longitude, distance);*/
                   // if (distance[0] > circle.getRadius()) {
                    //Toast.makeText(getBaseContext(), "Outside, distance from center: " + distance[0] + " radius: " + circle.getRadius(), Toast.LENGTH_LONG).show();
                    //} else {

                        // Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + circle.getRadius() , Toast.LENGTH_LONG).show();
                       //count++;
                        System.out.println("[[[[[ dm.getTitle() : "+dm.getTitle());
                        //System.out.println("[[[[[ count : "+count);
                    if (hashMapMarker.containsKey(dm.getId()))//deleting marker from map to prevent displaying same marker at two different cordinates
                    {
                        Marker mkr = hashMapMarker.get(dm.getId());
                        mkr.remove();
                        hashMapMarker.remove(dm.getId());
                    }
                     //   }
                    Marker deal_location_marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(home_smallMarker)).position(cordinate)
                            .title(dm.getTitle()));
                    hashMapMarker.put(dm.getId(), deal_location_marker);//adding marker to hashmap to check on next service call
                    markers.add(deal_location_marker);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    main_progressbar.setVisibility(View.GONE);

                }
            }

            marker_found_flag=0;
            System.out.println("markers.size() : "+markers.size());
            if(markers.size()>0) {


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {//otherwise Toast shows two times since we are taking lat lng of screen cordinates


                LatLngBounds currentScreen = mMap.getProjection().getVisibleRegion().latLngBounds;
                for (Marker marker : markers) {
                    if (currentScreen.contains(marker.getPosition())) {
                        // marker inside visible region
                        marker_found_flag = 1;
                        break;
                    } else {
                        // marker outside visible region
                        marker_found_flag = 0;
                    }
                }
                try {
                        if (marker_found_flag == 1) {
                            Toast.makeText(getContext(), "You have deals near you", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "No any Deals near by please search a location", Toast.LENGTH_SHORT).show();
                        }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                        main_progressbar.setVisibility(View.GONE);


                    }
                }, 2500);

            }
            else {
                Toast.makeText(getContext(), "No any Deals near by please search a location", Toast.LENGTH_SHORT).show();
                main_progressbar.setVisibility(View.GONE);
            }





//////////////////////////////////////////--- DELIVERY BOY LOCATION --END --////////////////////////////////////////////////////////
        }else {
            main_progressbar.setVisibility(View.GONE);

        }
    }




    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            location_spec_progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, GoogleMapModel> {

        @Override
        protected GoogleMapModel doInBackground(
                String... jsonData) {
            GoogleMapModel gmm=new GoogleMapModel();

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                gmm = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gmm;
        }

        @Override
        protected void onPostExecute(GoogleMapModel routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            List<HashMap<String, String>> path = null;

            try{
                // traversing through routes
                if (routes.getRoutes().size() != 0) {
                    System.out.println("routes.size() : " + routes.getRoutes().size());
                    for (int i = 0; i < routes.getRoutes().size(); i++) {
                        System.out.println("********************************" + (i + 1) + " Route************************************");
                        points = new ArrayList<LatLng>();
                        polyLineOptions = new PolylineOptions();
                        path = routes.getRoutes().get(i);


                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));

                            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~lat,lng : " + lat + "," + lng);

                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }
                /*polyLineOptions.addAll(points);
                polyLineOptions.width(5);
                polyLineOptions.color(getResources().getColor(R.color.path_color));
                System.out.println("polyLineOptions.getPoints() : "+polyLineOptions.getPoints());*/

                        try {
                            line.remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }//removing other polylines
                        line = mMap.addPolyline(new PolylineOptions()
                                .addAll(points)
                                .width(15)
                                .color(getResources().getColor(R.color.path_color2)));


                    }
                    System.out.println("!!!!!!!!!!!!!!!!!!! DISTANCE : " + routes.getDistance());
                    System.out.println("!!!!!!!!!!!!!!!!!!! DURATION : " + routes.getDuration());
                    marker_distance.setText(routes.getDistance() + " away from your current location.");
                    marker_duration.setText("You can reach there within " + routes.getDuration() + ".");
                    //mMap.addPolyline(polyLineOptions);
                }
                location_spec_progressbar.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("ReadTask Exception");

                location_spec_progressbar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"oops..try again",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class GetDeals extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            // progress.setVisibility(View.VISIBLE);
            main_progressbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(Constants.URL+"alldeals.php");
            System.out.println("jsonStr ....al deals" + jsonStr);
            //  Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    dealsModelArrayList = new ArrayList<>();
                    dealsModelArrayList.clear();
                    title=new String[jsonarray.length()];
                    description=new String[jsonarray.length()];
                    image_array=new String[jsonarray.length()];
                    price_array=new String[jsonarray.length()];
                    id_slug_array=new String[jsonarray.length()];
                    currency_array=new String[jsonarray.length()];
                    id_array=new String[jsonarray.length()];

                    for (int i = 0; i < jsonarray.length(); i++) {
                       dealsModel = new DealsModel();

                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        if (jsonobject.has("id")) {
                            //id_deal = jsonobject.getString("id");
                            dealsModel.setId(jsonobject.getString("id"));
                        }
                        if (jsonobject.has("title")) {
                            //title = jsonobject.getString("title");
                            dealsModel.setTitle(jsonobject.getString("title"));
                        }
                        if (jsonobject.has("image")) {
                            //image = jsonobject.getString("image");
                            dealsModel.setImage(jsonobject.getString("image"));
                        }
                        if (jsonobject.has("description")) {
                            //description = jsonobject.getString("description");
                            dealsModel.setDescription(jsonobject.getString("description"));
                        }
                        if (jsonobject.has("latitude")) {
                            //latitude = jsonobject.getString("latitude");
                            dealsModel.setLatitude(jsonobject.getString("latitude"));
                        }
                        if (jsonobject.has("longitude")) {
                            //longitude  = jsonobject.getString("longitude");
                            dealsModel.setLongitude(jsonobject.getString("longitude"));
                        }
                        if (jsonobject.has("timing")) {
                            //timing = jsonobject.getString("timing");
                            dealsModel.setTiming(jsonobject.getString("timing"));
                        }
                        if (jsonobject.has("delivery")) {
                            //delivery = jsonobject.getString("delivery");
                            dealsModel.setDelivery(jsonobject.getString("delivery"));
                        }
                        if (jsonobject.has("category")) {
                            //category1 = jsonobject.getString("category");
                            dealsModel.setCategory(jsonobject.getString("category"));
                        }
                        if (jsonobject.has("tags")) {
                            //tags = jsonobject.getString("tags");
                            dealsModel.setTags(jsonobject.getString("tags"));
                        }
                        if (jsonobject.has("price")) {
                            //price = jsonobject.getString("price");
                            if (jsonobject.getString("price") == null || jsonobject.getString("price").equals(0)) {
                                dealsModel.setPrice("0");
                            } else {
                                dealsModel.setPrice(jsonobject.getString("price"));
                            }

                        }
                        if (jsonobject.has("tkt_discounted_price")) {
                            //tkt_discounted_price = jsonobject.getString("tkt_discounted_price");
                            dealsModel.setDiscount(jsonobject.getString("tkt_discounted_price"));
                        }
                        if (jsonobject.has("seller_id")) {
                            //seller_id = jsonobject.getString("seller_id");
                            dealsModel.setSeller_id(jsonobject.getString("seller_id"));
                        }
                        if (jsonobject.has("currency")) {
                            //currency = jsonobject.getString("currency");
                            dealsModel.setCurrency(jsonobject.getString("currency"));
                        }
                        if (jsonobject.has("deal_slug")) {
                            //currency = jsonobject.getString("currency");
                            dealsModel.setDeal_slug(jsonobject.getString("deal_slug"));
                        }


                        // dealsModel.set
                        dealsModel.setType("deal");

                        id_array[i]=jsonobject.getString("id");
                        id_slug_array[i]=jsonobject.getString("deal_slug");
                        title[i]=jsonobject.getString("title");
                        description[i]=jsonobject.getString("description");
                        image_array[i]=jsonobject.getString("image");
                        price_array[i]=jsonobject.getString("price");
                        currency_array[i]=jsonobject.getString("currency");

                        dealsModelArrayList.add(dealsModel);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //  Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /*Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();*/

                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            //   progress.setVisibility(View.INVISIBLE);
//            recyclerAdapterCategory = new RecyclerAdapterCategory(categoryModelArrayList, dealsModelArrayList, Main2Activity.this);
//            recyclerView.scheduleLayoutAnimation();
//            recyclerView.setAdapter(recyclerAdapterCategory);
            System.out.println("dealsModelArrayList.size() : "+dealsModelArrayList.size());
            if(dealsModelArrayList.size()!=0){
                /*final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 2000);*/
                addMarkers();
            }
            else {
                //Toast.makeText(getActivity(),"No Deals Near you",Toast.LENGTH_SHORT).show();
                final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("")
                        .setContentText("No any near by deals please search a location")

                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                main_progressbar.setVisibility(View.GONE);
                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
            }


        }

    }


    @Override
    public void onPause() {

        mapView.onPause();
        super.onPause();
        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();

            // add markers from database to the map
        }
    }



    @Override
    public void onResume() {

        mapView.onResume();
        super.onResume();

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        System.out.println("[[[[[[[[[[[[[[[ inside onresume");
        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();

            // add markers from database to the map
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            System.out.println("sb.toString() : "+sb.toString());

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            place_ids=new String[predsJsonArray.length()];
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                place_ids[i]=predsJsonArray.getJSONObject(i).getString("place_id");
            }
        } catch (JSONException e) {

        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return  (String) resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }



     private class FetchSelectedPlaceCordinates extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler h = new HttpHandler();
            String s = h.makeServiceCall("https://maps.googleapis.com/maps/api/place/details/json?placeid="+selected_placeid+"&key="+API_KEY);
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObj = new JSONObject(s);
                JSONObject jObjectResult = (JSONObject) jsonObj.get("result");

                    System.out.println("%%%%%% lat" + jObjectResult.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    System.out.println("%%%%%% lng" + jObjectResult.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                    System.out.println("============================================================");


                LatLng new_positon=new LatLng(Double.parseDouble(jObjectResult.getJSONObject("geometry").getJSONObject("location").getString("lat")),
                        Double.parseDouble(jObjectResult.getJSONObject("geometry").getJSONObject("location").getString("lng")));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new_positon,
                        zoom));

                int height = 100;
                int width = 100;

                Bitmap b=FrameLayoutActivity.bitmapdraw_search.getBitmap();
                Bitmap searched_marker = Bitmap.createScaledBitmap(b, width, height, false);
                    try{
                        new_marker.remove();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                  new_marker=mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(searched_marker)).position(new_positon));

                new GetDeals().execute();

            }
            catch (JSONException e)
            {

                e.printStackTrace();
            }
        }
    }


    public void gotoSelectedPlace(){
        if(selected_place!=null) {
            try {
                System.out.println("place.getLatLng(); : " + new_positon);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new_positon,
                        zoom));

                int height = 100;
                int width = 100;

                Bitmap b = FrameLayoutActivity.bitmapdraw_search.getBitmap();
                Bitmap searched_marker = Bitmap.createScaledBitmap(b, width, height, false);
                try {
                    new_marker.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new_marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(searched_marker)).position(new_positon));
                selected_location_name.setText(selected_place);
                selected_location_layout.setVisibility(View.VISIBLE);

                new GetDeals().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
// }

        }
    }


    public void displaypopup_filter_popupwindow() {
        try {
            filter_popupwindow = new PopupWindow(custompopup_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (Build.VERSION.SDK_INT >= 21) {
                filter_popupwindow.setElevation(5.0f);
            }
            filter_popupwindow.setFocusable(true);
            filter_popupwindow.setAnimationStyle(R.style.AppTheme);
            filter_popupwindow.showAtLocation(topLayout, Gravity.CENTER, 0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
