package com.meridian.dateout.account;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.meridian.dateout.login.Vis_FilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.meridian.dateout.Constants.analytics;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView imageView_profile_upload_icon;
    CircleImageView profile_image;
    private EditText edt_name,edt_email,edt_phone,edt_location;
    private Button but_update;
    String selectedFilePath=null;
TextView txt;
    Toolbar toolbar;
    private static final int REQUEST_CODE = 1;
    private static final int PICK_FILE_REQUEST = 2;
    private static final int CAMERA_REQUEST = 1;
    String filenamepath;
    String image;
    ProgressBar progress_bar;
LinearLayout menu;
    String str_userid,str_fullname,str_username,str_email,str_photo,str_phone,str_location;
    public UpdateProfile() {
        // Required empty public constructor
    }
    public static UpdateProfile newInstance() {
        UpdateProfile fragment = new UpdateProfile();

        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfile newInstance(String param1, String param2) {
        UpdateProfile fragment = new UpdateProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        //FrameLayoutActivity.img_toolbar_crcname.setText("UPDATE PROFILE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_update_profile, container, false);
        FrameLayoutActivity.toolbar.setVisibility(View.GONE);
        profile_image=(CircleImageView) v.findViewById(R.id.profile_image_view);
        imageView_profile_upload_icon=(ImageView)v.findViewById(R.id.imageView_profile_upload_icon);
        edt_name=(EditText)v.findViewById(R.id.edt_name);
        edt_email=(EditText)v.findViewById(R.id.edt_email);
        but_update=(Button)v.findViewById(R.id.but_update);
        edt_phone=(EditText)v.findViewById(R.id.edt_phone);
        edt_location=(EditText)v.findViewById(R.id.edt_location);
        txt= (TextView) v. findViewById(R.id.toolbar_txt);
        menu= (LinearLayout) v. findViewById(R.id.menu);
        toolbar = (Toolbar)  v.findViewById(R.id.toolbar_tops1);
        toolbar.setVisibility(View.VISIBLE);
        analytics = FirebaseAnalytics.getInstance(getActivity());
        analytics.setCurrentScreen(getActivity(), getActivity().getLocalClassName(), null /* class override */);

        txt.setText("UPDATE PROFILE");
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
                    getActivity().getSupportFragmentManager().popBackStack();
                }

            }
        });
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        str_userid=preferences.getString("user_id",null);
        str_fullname=preferences.getString("fullname",null);
        str_username=preferences.getString("username",null);
        str_email=preferences.getString("email",null);
        str_photo=preferences.getString("photo",null);
        str_phone=preferences.getString("phone",null);
        str_location=preferences.getString("location",null);

        System.out.println("str_userid : "+str_userid);
        System.out.println("str_fullname : "+str_fullname);
        System.out.println("str_username : "+str_username);
        System.out.println("str_email : "+str_email);
        System.out.println("str_photo : "+str_photo);
        System.out.println("str_phone : "+str_phone);
        System.out.println("str_location : "+str_location);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }


        }
        progress_bar=(ProgressBar)v.findViewById(R.id.progress_bar);

        edt_name.setText(str_fullname);
        edt_email.setText(str_email);
        edt_phone.setText(str_phone);
        edt_location.setText(str_location);
        if(selectedFilePath==null || selectedFilePath.equals("")) {
            Glide
                    .with(getActivity())
                    .load(str_photo)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(profile_image);
        }




        imageView_profile_upload_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Add Photo!");
                final CharSequence[] options1 = {"Take Photo", "Choose from Gallery", "Cancel"};
                builder.setItems(options1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options1[item].equals("Take Photo")) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        } else if (options1[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, PICK_FILE_REQUEST);

                        } else if (options1[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        but_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(getApplicationContext());
                boolean i = networkCheckingClass.ckeckinternet();
                if (i) {
                    String regexStr ="^[+]?[0-9]{10,13}$";

                    if (edt_name.getText().toString().length() == 0) {
                        edt_name.setError("Enter Name");
                    } else if (edt_email.getText().toString().length() == 0) {
                        edt_email.setError("Enter E-mail");
                    } else if (edt_phone.getText().toString().length() == 0) {
                        edt_phone.setError("Enter phone");
                    }
                    else if (!edt_phone.getText().toString().matches(regexStr)) {
                        edt_phone.setError("invalid phone number");

                    }

                    else if(edt_location.getText().toString().length()==0){
                        edt_location.setText("Enter location");
                    }
                        else {
                        str_fullname=edt_name.getText().toString();
                        str_email=edt_email.getText().toString();
                        str_phone=edt_phone.getText().toString();
                        str_location=edt_location.getText().toString();

                        if (selectedFilePath == null || selectedFilePath.length() == 0) {

                            System.out.println("[[[[[[[[[[[[[[[ inside selectedFilePath==null");
                            System.out.println("[[[[[[[[[[[[[[[ selectedfilepath : "+selectedFilePath);

                             new SendToServer().execute();

                        }
                        else {
                            System.out.println("[[[[[[[[[[[[[[[ inside selectedfilepath not equal null");


                                    filenamepath = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
                            uploadVideo1(selectedFilePath,filenamepath);

                        }
                    }
                }else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
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
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE);

    }
    private void showFileChooser() {
        Intent intent = new Intent();

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


                profile_image.setImageBitmap(bmp);

            }
            else if(requestCode==CAMERA_REQUEST)
            {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                // Drawable drawable=new BitmapDrawable(photo);
                profile_image.setImageBitmap(photo);
                //backGroundImageLinearLayout.setBackgroundDrawable(drawable);

            }
        }
       /* if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

        }*/
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
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
                progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Picasso.with(UploadVideo.this).load(image).into(img_profile);
                System.out.println("SERVER REPLIED.............image...postexecute:"+image+s);
                // textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());

                new SendToServer().execute();

            }

            @Override
            protected String doInBackground(Void... params) {
                String charset = "UTF-8";

                String requestURL = Constants.URL+"updateprofilepic.php";


                try {

                    Vis_Uploads multipart = new Vis_Uploads(requestURL, charset);

                    multipart.addHeaderField("User-Agent", "CodeJava");
                    multipart.addHeaderField("Test-Header", "Header-Value");

                    multipart.addFormField("user_id",str_userid);
                    multipart.addFormField("profile_pic",filename);


                    multipart.addFilePart("profile_pic",file);

                    System.out.println("update_profileee............"+ str_userid+"...."+filename+"......"+file);
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
    class SendToServer extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress_bar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(Constants.URL+"update-profile.php");

                JSONObject postDataParams=new JSONObject();
                postDataParams.put("user_id", str_userid);
                //postDataParams.put("email", str_email);
                postDataParams.put("phone", str_phone);
                postDataParams.put("name", str_fullname);
                postDataParams.put("location", str_location);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("[[[[[[[[[[[[[[[ update result : "+result);
try {
    JSONObject obj = new JSONObject(result);
    if(obj.getString("status").equalsIgnoreCase("true")){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor edt=preferences.edit();
        edt.putString("fullname",str_fullname);
        edt.putString("username",str_fullname);
        edt.putString("phone",str_phone);
        edt.putString("location",str_location);
        edt.commit();



        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("Profile")

                .setContentText("Updated Successfully")

                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        dialog.dismiss();
                        FrameLayoutActivity.img_account.performClick();
                    }
                })

                .show();
        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));


    }
    else {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText("Sorry")
                .setContentText("Please Try Again")

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

}catch (Exception e){
    e.printStackTrace();
}


            progress_bar.setVisibility(ProgressBar.GONE);
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

}
