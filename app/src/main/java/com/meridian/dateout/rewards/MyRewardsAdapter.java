package com.meridian.dateout.rewards;

/**
 * Created by Anvin on 10/7/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meridian.dateout.Constants;
import com.meridian.dateout.R;
import com.meridian.dateout.login.NetworkCheckingClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.rewards.MyRewardFragment.progress_bar_my;
import static com.meridian.dateout.rewards.MyRewardFragment.text_myrewrds;
import static com.meridian.dateout.rewards.RewardsMainFragment.userid;

/**
 * Created by Anvin on 8/25/2017.
 */

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {

    private PopupWindow mPopupWindow;
    ArrayList<RewardsModel> rewardsArraylist;
    Context context;
    private boolean[] checkboxStatus;//for prventing 'star' icon being checked in different rows


    public MyRewardsAdapter(ArrayList<RewardsModel> values, Context context) {
        this.rewardsArraylist = values;
        this.context = context;
        System.out.println("adapter size is : "+rewardsArraylist.size());
        checkboxStatus = new boolean[values.size()];//for prventing 'star' icon being checked in different rows
        for(int k=0;k<values.size();k++)
            checkboxStatus[k]=false;//for prventing 'star' icon being checked in different rows

    }




    @Override
    public int getItemCount() {
        return rewardsArraylist.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_row, viewGroup, false);

        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reward_image,reward_star,reward_golden_star;
        TextView reward_text,reward_points;
        LinearLayout button_view_now,reward_info;
        ViewHolder(View itemView) {
            super(itemView);
            reward_image=(ImageView)itemView.findViewById(R.id.reward_image);
            reward_star=(ImageView)itemView.findViewById(R.id.reward_type);
           // reward_golden_star=(ImageView)itemView.findViewById(R.id.reward_golden_star);
            reward_text=(TextView) itemView.findViewById(R.id.reward_text);
            reward_points= (TextView) itemView.findViewById(R.id.reward_points);
            button_view_now=(LinearLayout) itemView.findViewById(R.id.button_redeem);
       reward_info=(LinearLayout) itemView.findViewById(R.id.rewrd_info);

        }
    }
    @Override
    public void onBindViewHolder(final ViewHolder personViewHolder, final int i) {
        System.out.println("checkboxStatus["+i+"] : "+checkboxStatus[i]);
        SharedPreferences preferences2 = context.getSharedPreferences("user_idnew", MODE_PRIVATE);
        final String new_userid = preferences2.getString("new_userid", null);
        personViewHolder.setIsRecyclable(false);//for prventing 'star' icon being checked in different rows
        personViewHolder.reward_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RewardsMainFragment.pop_up_rewrd_info.setVisibility(View.VISIBLE);
                RewardsMainFragment.rewrd_des.setText(rewardsArraylist.get(i).getReward_des());
                RewardsMainFragment.rewrd_title.setText(rewardsArraylist.get(i).getRewrd_title());
                RewardsMainFragment.rewrd_points.setText(rewardsArraylist.get(i).getRedeem_points()+" Points");
                Picasso.with(context).load(rewardsArraylist.get(i).getReward_image()).into(RewardsMainFragment.rewrd_image);


            }
        });
        Picasso.with(context).load(rewardsArraylist.get(i).getRewrd_type_image()).into(personViewHolder.reward_star);

        System.out.println("reward getRedeem_points() "+rewardsArraylist.get(i).getRedeem_points());
        System.out.println("rnew__useridddd......."+new_userid);
        Picasso.with(context).load(rewardsArraylist.get(i).getReward_image()).into( personViewHolder.reward_image);

        personViewHolder.reward_text.setText(rewardsArraylist.get(i).getRewrd_title());
        personViewHolder.reward_points.setText(rewardsArraylist.get(i).getRedeem_points()+" Points");
        if(new_userid !=null)
        {
            text_myrewrds.setVisibility(View.GONE);


        }
        else
        {
            text_myrewrds.setVisibility(View.VISIBLE);

        }



        personViewHolder.button_view_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Toast.makeText(context,"view now",Toast.LENGTH_SHORT).show();


                final SweetAlertDialog dialog = new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE);
                dialog.setTitleText("Redeem")
                        .setContentText("Are you sure you want to redeem this?")
                        .setConfirmText("YES")
                        .setCancelText("NO")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                load(new_userid, rewardsArraylist.get(i).getId(), i);
                                dialog.dismiss();


                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                dialog.findViewById(R.id.confirm_button).setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));



            }
        });
    }

    public  void load(final String user_id, final String rewrd_id, final int i)
    {
progress_bar_my.setVisibility(View.VISIBLE);
        System.out.println("user_id..."+user_id);
        System.out.println("rewrd_id..."+rewrd_id);

        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(context);
        final boolean inter = networkCheckingClass.ckeckinternet();
        if(inter) {
            System.out.println("login....i" + inter);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL+"applyReward.php?",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("responseeeeemyrewrd" + response);
                            JSONObject jsonObj = null;
                            JSONObject jsonobject1=null;

                            try {
                                System.out.println("registration result : " + response);
                                jsonObj = new JSONObject(response);

                                String status = jsonObj.getString("status");


                                if (status.equalsIgnoreCase("success"))
                                {
                                    progress_bar_my.setVisibility(View.GONE);
                                    String data = jsonObj.getString("data");
                                    System.out.println("responseeeeeeeeeeeeeeeeee"+ data);
                                    String current_points=null,user_type="",user_type_image="";
                                    jsonobject1 = new JSONObject(data);
                                    if (jsonobject1.has("current_points")) {
                                       current_points= jsonobject1.getString("current_points");
                                            RewardsMainFragment.rewrd_current_points.setText(current_points+" Points");
                                    }
                                    if (jsonobject1.has("user_type")) {

                                        user_type=jsonobject1.getString("user_type");

                                            RewardsMainFragment.member_type.setText(user_type);

                                    }
                                    if (jsonobject1.has("user_type_image")) {
                                        user_type_image=jsonobject1.getString("user_type_image");

                                            if(!user_type.equalsIgnoreCase("normal")){
                                                try {
                                                    Glide
                                                            .with(context)
                                                            .load(user_type_image)
                                                            .centerCrop()
                                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                            .crossFade()
                                                            .into(RewardsMainFragment.type_image);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                    }

                                    if (jsonobject1.has("message")) {
                                        String message= jsonobject1.getString("message");
                                        System.out.println("responseeeeeeeeeeeeeeeeee"+ message+"arraylist sizeeee"+rewardsArraylist.size());
                                        final SweetAlertDialog dialog = new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE);

                                        dialog.setTitleText("Reward")
                                                .setContentText(message)
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        dialog.dismiss();



                                                        if( rewardsArraylist.size()==0)
                                                        {
                                                            text_myrewrds.setVisibility(View.VISIBLE);
                                                        }

//
                                                    }
                                                })
                                                .show();
                                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                    }






                                }
                                else
                                 {
                                    if (status.equalsIgnoreCase("failed")) {
                                        progress_bar_my.setVisibility(View.GONE);
                                        String message = jsonObj.getString("message");
                                        final SweetAlertDialog dialog = new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE);
                                        dialog.setTitleText("Reward")
                                                .setContentText(message)
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();


                                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                                    }

                                }


                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Refresh();//calling refresh
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();



                    params.put("user_id",user_id);
                    params.put("reward_id",rewrd_id);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
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
    public  void Refresh()
    {

        progress_bar_my.setVisibility(View.VISIBLE);
        NetworkCheckingClass networkCheckingClass = new NetworkCheckingClass(context);
        final boolean i = networkCheckingClass.ckeckinternet();
        if(i) {
            System.out.println("login....i" + i);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL+"allrewards.php?",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String user_type="",user_type_image="";
                            System.out.println("responseeeeelogin" + response);
                            JSONObject jsonObj = null;
                            JSONObject jsonobject1=null;
                            try {
                                System.out.println("registration result : " + response);
                                jsonObj = new JSONObject(response);

                                String status = jsonObj.getString("status");

                                if (status.equalsIgnoreCase("success")) {
                                    String data = jsonObj.getString("data");

                                    System.out.println("responseeeeeeeeeeeeeeeeee"+ data);
                                    if (jsonObj.has("data")) {
                                        jsonobject1 = new JSONObject(data);
                                        if (jsonobject1.has("current_points")) {
                                            String current_points = jsonobject1.getString("current_points");

                                                RewardsMainFragment.rewrd_current_points.setText(current_points +" Points");


                                        }
                                        if (jsonobject1.has("user_type")) {

                                            user_type=jsonobject1.getString("user_type");

                                            RewardsMainFragment.member_type.setText(user_type);

                                        }
                                        if (jsonobject1.has("user_type_image")) {
                                            user_type_image=jsonobject1.getString("user_type_image");

                                            if(!user_type.equalsIgnoreCase("normal")){
                                                try {
                                                    Glide
                                                            .with(context)
                                                            .load(user_type_image)
                                                            .centerCrop()
                                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                            .crossFade()
                                                            .into(RewardsMainFragment.type_image);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }else{
                                                try {
                                                    Glide
                                                            .with(context)
                                                            .load(R.drawable.medal)
                                                            .centerCrop()
                                                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                            .crossFade()
                                                            .into(RewardsMainFragment.type_image);
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        if (jsonobject1.has("all_rewards")) {
                                            ArrayList<RewardsModel>    all_rewardArraylist=new ArrayList<>();

                                            JSONArray jsonarray = jsonobject1.getJSONArray("all_rewards");
                                            System.out.println("responseeeeeeeeeeeeeeeeee22222"+jsonarray);


                                            for (int i = 0; i < jsonarray.length(); i++) {

                                                RewardsModel rm=new RewardsModel();
                                                String id="",title="",image="",discription="",date="",redeem_point="",rewardtype="";
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                if (jsonobject.has("id")) {
                                                    id = jsonobject.getString("id");
                                                    System.out.println("all_rewards"+id);
                                                }
                                                if (jsonobject.has("title")) {
                                                    title = jsonobject.getString("title");
                                                    System.out.println("all_rewards"+title);
                                                }
                                                if (jsonobject.has("image")) {
                                                    image = jsonobject.getString("image");


                                                    System.out.println("all_rewards"+image);

                                                }
                                                if (jsonobject.has("description")) {
                                                    discription = jsonobject.getString("description");
                                                    System.out.println("all_rewards"+discription);
                                                }
                                                if (jsonobject.has("date")) {
                                                    date = jsonobject.getString("date");
                                                }


                                                if (jsonobject.has("redeempoint")) {
                                                    redeem_point= jsonobject.getString("redeempoint");
                                                    System.out.println("redeem_point"+redeem_point);
                                                }
                                                if (jsonobject.has("rewardtype")) {
                                                    rewardtype= jsonobject.getString("rewardtype");
                                                }
                                                rm.setId(id);
                                                rm.setRewrd_title(title);
                                                rm.setReward_image(image);
                                                rm.setReward_des(discription);

                                                rm.setRewrd_date(date);
                                                rm.setRewrd_type(rewardtype);
                                                rm.setRedeem_points(redeem_point);



                                                all_rewardArraylist.add(rm);



                                            }


                                            SharedPreferences preferences = context.getSharedPreferences("rewards", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString("all_rewards",jsonarray.toString());
                                            editor.commit();

                                        }
                                        if (jsonobject1.has("my_rewards")) {
                                           ArrayList<RewardsModel> my_rewardArraylist=new ArrayList<>();


                                            if(jsonobject1.getString("my_rewards")!="null")
                                            {  text_myrewrds.setVisibility(View.GONE);
                                                String id="",title="",image="",discription="",date="",redeem_point="",rewardtype="";
                                                JSONArray jsonarray = jsonobject1.getJSONArray("my_rewards");
                                                System.out.println("my_rewards____arrayyyyy" + jsonarray);


                                                for (int i = 0; i < jsonarray.length(); i++) {

                                                    RewardsModel rm = new RewardsModel();
                                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                    if (jsonobject.has("id")) {
                                                        id = jsonobject.getString("id");
                                                        System.out.println("all_rewards"+id);
                                                    }
                                                    if (jsonobject.has("title")) {
                                                        title = jsonobject.getString("title");
                                                        System.out.println("my_rewards" + title);
                                                    }
                                                    if (jsonobject.has("image")) {
                                                        image = jsonobject.getString("image");
                                                        System.out.println("my_rewards" + image);
                                                    }
                                                    if (jsonobject.has("description")) {
                                                        discription = jsonobject.getString("description");
                                                        System.out.println("my_rewards" + discription);
                                                    }
                                                    if (jsonobject.has("date")) {
                                                        date = jsonobject.getString("date");
                                                    }
                                                    if (jsonobject.has("redeempoint")) {
                                                        redeem_point = jsonobject.getString("redeempoint");
                                                    }
                                                    if (jsonobject.has("rewardtype")) {
                                                        rewardtype = jsonobject.getString("rewardtype");
                                                    }

                                                    rm.setId(id);
                                                    rm.setRewrd_title(title);
                                                    rm.setReward_image(image);
                                                    rm.setReward_des(discription);
                                                    rm.setRewrd_date(date);

                                                    rm.setRewrd_type(rewardtype);
                                                    rm.setRedeem_points(redeem_point);
                                                    my_rewardArraylist.add(rm);




                                                }
                                                progress_bar_my.setVisibility(View.GONE);
                                                rewardsArraylist=my_rewardArraylist;

                                                notifyDataSetChanged();


                                                SharedPreferences preferences = context.getSharedPreferences("rewards", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("my_rewards", jsonarray.toString());
                                                editor.commit();




                                            }
                                            else {

                                                text_myrewrds.setVisibility(View.VISIBLE);
                                                progress_bar_my.setVisibility(View.GONE);
                                                System.out.println("no_rewards found.............");
                                                rewardsArraylist.clear();
                                                notifyDataSetChanged();
                                            }
                                        }





                                    }

                                }


                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id",userid);

                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);
        } else {

            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
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

}