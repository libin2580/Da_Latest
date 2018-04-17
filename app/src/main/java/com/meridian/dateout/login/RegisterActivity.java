package com.meridian.dateout.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.fcm.Config;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {
    TextView AlReg;
    EditText edtemail, edtfulnam, edtcntct, edtpass, edtuser, edtcnfrmpass;
    String email, pass, statusd, username;
    Button butsignup;
    boolean edittexterror = false;
    String selectedFilePath,token;
    ImageView profile_img_upload,close;
    private static final int PICK_FILE_REQUEST = 2;
    private static final int CAMERA_REQUEST = 1;
    String REGISTER_URL = Constants.URL+"registration.php?";
    ImageView back;
    ImageView imageView;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    //    ProgressDialog pd;
    ProgressBar progress;
    static String filename = "null", filenamepath = "null";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        imageView = (ImageView) findViewById(R.id.profile_image);
        edtemail = (EditText) findViewById(R.id.edt_email);
        edtpass = (EditText) findViewById(R.id.edt_pass);
        edtcnfrmpass = (EditText) findViewById(R.id.edt_cnfrmpass);
        close= (ImageView) findViewById(R.id.close);
        //  edtfulnam = (EditText) findViewById(R.id.edt_fulname);
        // edtphon= (EditText) findViewById(R.id.edt_phone);
        //  edtcntct = (EditText) findViewById(R.id.edt_cntct);

      //  final Typeface myFont1 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Regular.ttf");

        String p= getResources().getString(R.string.By_clicking_on_create_you_agree_to_our);
        System.out.println("textttt"+p);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }


        }
        TextView textView = (TextView)findViewById(R.id.textView_clickterms);



        textView.setText(Html.fromHtml(p +" "+
                "<a href='id.web.freelancer.example.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));

        textView.setClickable(true);
       // textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this,TermsOfUse.class);
                startActivity(i);
            }
        });
        textView.setLinkTextColor(Color.BLACK);
        edtuser = (EditText) findViewById(R.id.edt_username);
        //  edtloc = (EditText) findViewById(R.id.edt_locatn);
        butsignup = (Button) findViewById(R.id.butsignup);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);

        token  =pref.getString("regId", null);
        System.out.println("tokennnnn"+token);

        progress = (ProgressBar) findViewById(R.id.progress_bar);
        profile_img_upload = (ImageView) findViewById(R.id.profile_upload_icon);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
                finish();

            }
        });
        profile_img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_pic();

          /*      if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {

                    requestPermissions();
                }*/
             /*   if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {

                    requestPermissions();
                }
                showFileChooser();*/


            }
        });
        progress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                progress.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        /*final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tops);
        setSupportActionBar(toolbar);*/
        /*back = (ImageView) findViewById(R.id.img_crcdtlnam);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  onBackPressed();
                Intent in=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
            }
        });*/
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeueLTStd-Lt.otf");
//        edtfulnam.setTypeface(tf, Typeface.BOLD);
//        edtphon.setTypeface(tf, Typeface.BOLD);
//        edtfulnam.setTypeface(tf, Typeface.BOLD);
//        edtphon.setTypeface(tf, Typeface.BOLD);
//        edtoccp.setTypeface(tf, Typeface.BOLD);
//        edtpass.setTypeface(tf, Typeface.BOLD);
        //  Typeface ttf = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeueLTStd-Roman.otf");
//        AlReg= (TextView) findViewById(R.id.txtsignin);
//        AlReg.setTypeface(ttf, Typeface.BOLD);
//        AlReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // reg();
//                Intent is = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(is);
//                finish();
//
//            }
//        });
        butsignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
                boolean i = networkCheckingClass.ckeckinternet();
                if (i)
                {
                    //Toast.makeText(getActivity(),
                    // "You have Internet Connection", Toast.LENGTH_LONG)
                    if (selectedFilePath == null) {
                        reg();
                        // Toast.makeText(getApplicationContext(), "Select Image", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reg();
                        uploadVideo();
                    }

                }
                else
                {
                    final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("No Internet")

                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
               /*     final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("Alert");

                    alertDialog.setMessage("No Internet");


                    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });
                    alertDialog.show();*/


                }


            }
        });
    }

    private void select_pic() {



            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Add Photo!");
            final CharSequence[] options1 = {"Take Photo", "Choose from Gallery", "Cancel"};
            //  final CharSequence[] options2 = { "Take Photo", "Choose from Gallery","Delete Photo" };

            builder.setItems(options1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options1[item].equals("Take Photo")) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                    } else if (options1[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, PICK_FILE_REQUEST);

                    } else if (options1[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();


    }


    private void reg() {
        email = edtemail.getText().toString();
        pass = edtpass.getText().toString();
        username = edtuser.getText().toString();

        edittexterror = false;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString().trim()).matches()) {
            edtemail.setError("Invalid Email");
            edittexterror = true;
        } /*else if (edtemail.getText().toString().isEmpty()) {
            edtemail.setError("Enter Email Id");
            edittexterror = true;
        } */else {

        }

        if (edtemail.getText().toString().isEmpty()) {
            edtemail.setError("Enter Email Id");
            edittexterror = true;
        }


        if (edtpass.getText().toString().isEmpty()) {
            edtpass.setError("Enter password");


        }
        else if (!isValidPassword(edtpass.getText().toString())) {
            edtpass.setError("Password should be minimum 6 characters");
            edittexterror = true;
        } /*else if (edtpass.getText().toString().isEmpty()) {
            edtpass.setError("Enter password");
            edittexterror = true;
        }*/ else if (!edtcnfrmpass.getText().toString().matches(edtpass.getText().toString())) {

            edtpass.setError("");
            edittexterror = true;

        } else {


        }


        if(edtuser.getText().toString().isEmpty())
        {
            edtuser.setError("Enter UserName");
            edittexterror=true;
        }
        if(edtcnfrmpass.getText().toString().isEmpty())
        {
            edtcnfrmpass.setError("Password confirmation is required");
            edittexterror=true;
        }
        if (email.trim().matches("") || pass.matches("") ||  username.matches("") )
        {

            final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.NORMAL_TYPE);
            dialog.setTitleText("")
                    .setContentText("Empty Fields")

                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


          /*  final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Alert");
            //  alertDialog .setIcon(R.drawable.warning_blue);
            alertDialog.setMessage("Empty Fields");

            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();


                }
            });
            alertDialog.show();
            Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);*/
            //  nbutton.setTextColor(getResources().getColor(R.color.Orange));

        }
        else if (filenamepath != null)
        {
            filename = filenamepath;


        } else
        {
            filename = "null";

        }


        if (edittexterror == false) {
            edtemail.setError(null);
//            edtpass.setError(null);

            // edtcntct.setError(null);
            //   edtloc.setError(null);
            //  edtfulnam.setError(null);
            NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
            boolean i = networkCheckingClass.ckeckinternet();
            if (i) {

                progress.setVisibility(ProgressBar.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                progress.setVisibility(ProgressBar.GONE);

                                // JSONObject jsonObj = null;
                                //  jsonObj = new JSONObject(response);
                                System.out.println("responseeeee" + response);
                                // statusd = jsonObj.getString("status");
                                System.out.println("statussssscours" + statusd);
                                progress.setVisibility(ProgressBar.GONE);



                                JSONObject jsonObject = null;
                                try {
                                    System.out.println("registration result : " + response);
                                    jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");

                                    if (status.equalsIgnoreCase("success"))
                                    {
                                        JSONObject jsonObjdata = jsonObject.getJSONObject("data");
                                     String   user_id = jsonObjdata.getString("user_id");
                                        String    fullname =jsonObjdata.getString("fullname");
                                        username = jsonObjdata.getString("username");
                                        String     photo = jsonObjdata.getString("photo");
                                        email = jsonObjdata.getString("email");
                                        String   phone = jsonObjdata.getString("phone");
                                        String log_status =jsonObjdata.getString("log_status");
                                      //  String message = jsonObjdata.getString("message");

                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();

                                        editor.putString("user_id", user_id);
                                        editor.putString("fullname", username);
                                        editor.putString("email", email);
                                        editor.putString("username", username);
                                        editor.putString("photo", photo);
                                        editor.commit();
                                        final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                                    dialog.setTitleText(status)
                                            .setContentText("You have successfully registered")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                        Intent is = new Intent(getApplicationContext(), FrameLayoutActivity.class);
                                        startActivity(is);
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                                        boolean Islogin = Boolean.parseBoolean("true");
                                        prefs.edit().putBoolean("Islogin", Islogin).commit();
                                        //   pd.dismiss();
                                        finish();

                                                }
                                            })
                                            .show();
                                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));



                                    }
                                    else
                                    {

                                        final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.NORMAL_TYPE);
                                    dialog.setTitleText("Already Registered")
                                            //  dialog.setContentText(response)

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


                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                // pd.dismiss();

//
//                                if (response.contentEquals("\"success\"")) {
//                                    //  alertDialog.setMessage("You have successfully registered");
//                                    final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.SUCCESS_TYPE);
//                                    dialog.setTitleText("Success")
//                                            .setContentText("You have successfully registered")
//                                            .setConfirmText("OK")
//                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                                @Override
//                                                public void onClick(SweetAlertDialog sDialog) {
//                                                    if (response.contentEquals("\"success\"")) {
//
//                                                        progress.setVisibility(ProgressBar.GONE);
//                                                        Intent is = new Intent(getApplicationContext(), LoginActivity.class);
//                                                        startActivity(is);
//                                                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefN", MODE_PRIVATE);
//                                                        SharedPreferences.Editor editor = preferences.edit();
//                                                        editor.putString("email", email);
//                                                        editor.putString("phone", null);
//                                                        editor.putString("name",  username);
//                                                        editor.putString("username", username);
//                                                        editor.putString("location",null);
//
//                                                        editor.putString("password", pass);
//
//
//
//
//
//                                                        finish();
//                                                    }
//                                                    dialog.dismiss();
//
//
//
//                                                }
//                                            })
//                                            .show();
//                                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
//
//                                }
//                                else {
//                                    final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.NORMAL_TYPE);
//                                    dialog.setTitleText("Already Registerd")
//                                            //  dialog.setContentText(response)
//
//                                            .setConfirmText("OK")
//                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                                @Override
//                                                public void onClick(SweetAlertDialog sDialog) {
//                                                    dialog.dismiss();
//
//
//                                                }
//                                            })
//                                            .show();
//                                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
//                                }


                  /*              AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                                alertDialog.setTitle("Alert");
                                if (response.contentEquals("\"success\"")) {
                                    alertDialog.setMessage("You have successfully registered");
                                } else {
                                    alertDialog.setMessage(response);
                                }
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (response.contentEquals("\"success\"")) {

                                                    progress.setVisibility(ProgressBar.GONE);
                                                    Intent is = new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(is);
                                                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPrefN", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("email", email);
                                                    editor.putString("phone", null);
                                                    editor.putString("name",  username);
                                                    editor.putString("username", username);
                                                    editor.putString("location",null);

                                                    editor.putString("password", pass);

                                                    finish();
                                                }
                                                dialog.dismiss();


                                            }
                                        });
                                alertDialog.show();*/

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                            android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(CourseRegistrationActivity.this).create();
//                            alertDialog.setTitle("Alert");
//                            alertDialog.setMessage("Please Login to Register For this Course");
//                            alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
////                                        but_regcrc1.setBackgroundResource(R.color.butnbakcolr);
////                                        but_regcrc1.setTextColor(getResources().getColor(R.color.White));
//
//                                        }
//                                    });
//                            alertDialog.show();

                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

//meridian.net.in/demo/etsdc/response.php?fid=1&email=" + email + "&phone=" + phon + "&name=" + fulnam + "&occupation=" + occ + "&location=" + loc + "&password=" + pass


                        params.put("email", email);
                        params.put("phone", "");
                        params.put("name", username);
                        params.put("username", username);
                        params.put("location","");
                        params.put("password", pass);
                        params.put("profile_pic", filename);
                        params.put("device_token",token);
                        params.put("device_type","android");
                        return params;
                    }

                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                int socketTimeout = 30000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);
            } else {


                final SweetAlertDialog dialog = new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Alert!")
                        .setContentText("Oops Your Connection Seems Off..s")

                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));



              /*  final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Oops Your Connection Seems Off..");

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                    }
                });
                alertDialog.show();*/

            }


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.home) {


            //  super.onBackPressed();
            Intent in=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(in);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 6;
    }
//    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK &&
//                event.getAction() == KeyEvent.ACTION_UP) {
//            revalidateEditText();
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }
//
//    public void revalidateEditText(){
//        // Dismiss your origial error dialog
//        setError(null);
//        // figure out which EditText it is, you already have this code
//        // call your validator like in the Q
//        validate(editText); // or whatever your equivalent is

    private void showFileChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("image/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Vis_Upload.."), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();

                // image_name_tv.setText(filePath);

                selectedFilePath = Vis_FilePath.getPath(getApplicationContext(), selectedFileUri);
                Bitmap bmp = BitmapFactory.decodeFile(selectedFilePath);
                System.out.println("Selected File Path:" + selectedFilePath);

                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    //  textViewResponse.setText(selectedFilePath);

                    filenamepath = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);


                    ///   Toast.makeText(getApplicationContext(),selectedFilePath,Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                    filenamepath = "null";
                }


                imageView.setImageBitmap(bmp);

            }
            else if(requestCode==CAMERA_REQUEST)
            {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
               // Drawable drawable=new BitmapDrawable(photo);
                imageView.setImageBitmap(photo);
                //backGroundImageLinearLayout.setBackgroundDrawable(drawable);

            }
        }
       /* if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

        }*/
    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

          //  ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
             //   uploading = ProgressDialog.show(RegisterActivity.this, "Registering", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            //    uploading.dismiss();

                // textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                // textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                Vis_Upload u = new Vis_Upload();
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<md>>>>>>>>>>>>>>>>>>>>>>>>>" + selectedFilePath);
                String msg = u.uploadVideo(selectedFilePath);
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<mdeeeeeeeeeeeeeeeeeee>>>>>>>>>>>>>>>>>>>>>>>>>" + msg);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(RegisterActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE);
        // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
                    finish();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(in);
        // super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}