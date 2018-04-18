package com.meridian.dateout.explore.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.meridian.dateout.R;
import com.meridian.dateout.login.NetworkCheckingClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.Constants.analytics;
import static com.meridian.dateout.explore.address.Adddetails.place_order;

/**
 * Created by libin on 3/26/2018.
 */

public class EdittestActivity extends AppCompatActivity {

    String adname, adphone, adcity, adarea, adflatads, adstate, adpin, ad_type,id;
    private String android_id;
    List<Pair<String, String>> params;
    EditText addr_name, addr_phone, addr_city, addr_area, addrs_flat, addr_state, addr_pin, addr_email;
    CheckBox addr_work_edit, addr_home_edit;
    LinearLayout coordinatorLayout;
    LinearLayout save_data;
    String name,phone,city,street,flat_no,state,pin,type;
    String vv,ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address_edit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        analytics.setCurrentScreen(this, this.getLocalClassName(), null /* class override */);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        if(name!=null){
            vv="update_address.php";
            phone = getIntent().getStringExtra("phone");
            city = getIntent().getStringExtra("city");
            street = getIntent().getStringExtra("street");
            flat_no= getIntent().getStringExtra("flat_no");
            state= getIntent().getStringExtra("state");
            pin= getIntent().getStringExtra("pin");
            type= getIntent().getStringExtra("type");
            ss="id";
        }
        else {
            vv="add_address.php";
            ss="user_id";
        }


        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinatorLayout);
        addr_name = (EditText) findViewById(R.id.address_name);
        addr_email = (EditText) findViewById(R.id.address_email);
        addr_phone = (EditText) findViewById(R.id.address_phone);
        addr_city = (EditText) findViewById(R.id.address_city);
        addr_area = (EditText) findViewById(R.id.address_area);
        addrs_flat = (EditText) findViewById(R.id.address_flatnumber);
        addr_state = (EditText) findViewById(R.id.addressr_state);
        addr_pin = (EditText) findViewById(R.id.address_pin);
        addr_work_edit = (CheckBox) findViewById(R.id.address_work_edit);
        addr_home_edit = (CheckBox) findViewById(R.id.address_home_edit);

        addr_name.setText(name);
        addr_phone.setText(phone);
        addr_city.setText(city);
        addr_area.setText(street);
        addrs_flat.setText(flat_no);
        addr_state.setText(state);
        addr_pin.setText(pin);

        save_data = (LinearLayout) findViewById(R.id.saves);


        addr_work_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    addr_work_edit.setChecked(false);
                    //   addr_home.setChecked(false);
                    addr_work_edit.setButtonDrawable(R.drawable.blue_tick);
                    addr_home_edit.setButtonDrawable(R.drawable.gray_tick);
                }else {
                    // ad_type="home";

                    addr_work_edit.setChecked(true);
                    addr_work_edit.setButtonDrawable(R.drawable.gray_tick);
                    addr_home_edit.setButtonDrawable(R.drawable.blue_tick);
                }


            }
        });
        addr_home_edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    addr_home_edit.setChecked(false);
                    // addr_work.setChecked(false);
                    //   ad_type="home";
                    addr_home_edit.setButtonDrawable(R.drawable.blue_tick);
                    addr_work_edit.setButtonDrawable(R.drawable.gray_tick);
                }
                else {
                    // ad_type="work";

                    addr_home_edit.setChecked(true);
                    addr_work_edit.setButtonDrawable(R.drawable.blue_tick);
                    addr_home_edit.setButtonDrawable(R.drawable.gray_tick);

                }
            }
        });
        LinearLayout close = (LinearLayout) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.dismiss();
                finish();
            }
        });


        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(EdittestActivity.this);

                boolean i = networkCheckingClass.ckeckinternet();
                if (i) {
                    if (addr_name.getText().toString() == null) {
                        addr_name.setError("Enter Name");
                    } else if (addr_phone.getText().toString() == null) {
                        addr_phone.setError("Enter Phone");
                    } else if (addr_city.getText().toString().length() == 0) {
                        addr_city.setError("Enter City");
                    } else if (addr_area.getText().toString().length() == 0) {
                        addr_area.setError("Enter Area");
                    } else if (addrs_flat.getText().toString().length() == 0) {
                        addrs_flat.setError("Enter Flat Number");
                    } else if (addr_state.getText().toString().length() == 0) {
                        addr_state.setError("Enter State");
                    } else if (addr_pin.getText().toString().length() == 0) {
                        addr_pin.setError("Enter Pin");
                    } else {
                        params = new ArrayList<Pair<String, String>>() {{
                            add(new Pair<String, String>(ss,id));
                            add(new Pair<String, String>("name", addr_name.getText().toString()));
                            add(new Pair<String, String>("phone", addr_phone.getText().toString()));
                            add(new Pair<String, String>("city",addr_city.getText().toString()));
                            add(new Pair<String, String>("street", addr_area.getText().toString()));
                            add(new Pair<String, String>("building", addrs_flat.getText().toString()));
                            add(new Pair<String, String>("state", addr_state.getText().toString()));
                            add(new Pair<String, String>("pin", addr_pin.getText().toString()));

                            add(new Pair<String, String>("type", "home"));
                            System.out.println("_________params_____________" + params);

                        }};
                        Fuel.post(URL1+vv,params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                            @Override
                            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {

                                try {

                                    JSONObject jsonObj = new JSONObject(s);
                                    String status = jsonObj.getString("status");
                                    System.out.println("_________status_____________" + status);
                                    final String data = jsonObj.getString("message");
                                    System.out.println("___________data___________" + data);
                                    if(status.equalsIgnoreCase("true")){
                                        Intent i = new Intent(EdittestActivity.this, Adddetails.class);


                                        startActivity(i);
                                        finish();

                                    }else {
                                    }




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void failure(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, FuelError fuelError) {

                            }
                        });

                    }

                } else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(EdittestActivity.this, SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Oops Your Connection Seems Off..s")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

            }
        });


    }
}