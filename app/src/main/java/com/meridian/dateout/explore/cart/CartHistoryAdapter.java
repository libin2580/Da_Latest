package com.meridian.dateout.explore.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.login.FrameLayoutActivity;
import com.meridian.dateout.login.LoginActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kotlin.Pair;

import static android.content.Context.MODE_PRIVATE;
import static com.meridian.dateout.Constants.URL1;
import static com.meridian.dateout.explore.cart.Cart_details.progress_bar_explore;
import static com.meridian.dateout.explore.cart.Cart_details.totalprize_for_order;

/**
 * Created by user 1 on 19-11-2016.
 */

public class CartHistoryAdapter extends RecyclerView.Adapter<CartHistoryAdapter.ItemViewHolder> {
    Context context;
    private ArrayList<CartHistoryModel> orderHistoryArraylist;
    List<Pair<String, String>> params;
    String userid,str_comnt;

    public CartHistoryAdapter(Context context, ArrayList<CartHistoryModel> arrList) {
        this.context = context;
        this.orderHistoryArraylist = arrList;
        System.out.println("orderHistoryArraylist.size() : "+orderHistoryArraylist.size());
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_itemslist, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        holder.title.setText(orderHistoryArraylist.get(position).getTitle());
        Picasso.with(context).load(orderHistoryArraylist.get(position).getImage()).into(holder.imag_ordr);
        holder.booking_date.setText(orderHistoryArraylist.get(position).getBooking_date());
        holder.amount.setText("$"+orderHistoryArraylist.get(position).getamount());
        holder.cart_text.setText(orderHistoryArraylist.get(position).getQuantity());
        final int k = Integer.parseInt(orderHistoryArraylist.get(position).getamount());
        holder.delete_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("cart_item_id", orderHistoryArraylist.get(position).getcart_item_id()));

                }};

                final SweetAlertDialog dialog = new SweetAlertDialog(view.getRootView().getContext(),SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("Are you Sure ?")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                dialog.dismissWithAnimation();
                                delete(position,k,view);

                            }
                        })
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
                dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));

            }
        });
        holder.layout_chat_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CategoryDealDetail.class);
                i.putExtra("deal_id", Integer.parseInt(orderHistoryArraylist.get(position).getDeal_id()));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        holder.layout_wish_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("cart_item_id", orderHistoryArraylist.get(position).getcart_item_id()));

                }};
                SharedPreferences preferences = context.getSharedPreferences("MyPref", MODE_PRIVATE);
                String   user_id = preferences.getString("user_id", null);

                if (user_id != null) {
                    userid = user_id;
                    System.out.println("userid" + userid);
                }
                SharedPreferences preferences1 =context.getSharedPreferences("myfbid", MODE_PRIVATE);
                String  profile_id = preferences1.getString("user_idfb", null);
                if (profile_id != null) {
                    userid = profile_id;
                    System.out.println("userid" + userid);
                }
                SharedPreferences preferences2 = context.getSharedPreferences("value_gmail", MODE_PRIVATE);
                String profileid_gmail = preferences2.getString("user_id", null);
                if (profileid_gmail != null) {
                    userid = profileid_gmail;
                    System.out.println("userid" + userid);
                }


                if(user_id!=null){

                    final SweetAlertDialog dialog = new SweetAlertDialog(view.getRootView().getContext(),SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("Are you Sure ?")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismissWithAnimation();
                                    wish_list(position,view);

                                }
                            })
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                    dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                }else {
                    final SweetAlertDialog dialog1 = new SweetAlertDialog(view.getRootView().getContext(), SweetAlertDialog.WARNING_TYPE);
                    dialog1.setTitleText("Please login!")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog1.dismissWithAnimation();
                                    Intent i = new Intent(context, LoginActivity.class);
                                    // i.putExtra("tab_id",0);
                                    i.setFlags(i.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                    //finish();

                                }
                            })
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                }
                            })
                            .show();
                    dialog1.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                }
            }
        });

    }
    private void delete(final int position,final int k,final View view) {
        progress_bar_explore.setVisibility(View.VISIBLE);
        Fuel.post(URL1+"delete-cart-item.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                progress_bar_explore.setVisibility(View.GONE);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String status = jsonObj.getString("status");
                    if(Objects.equals(status, "true")){
                        removeAt(position);
                        totalprize_for_order-=k;
                        Cart_details.Total_txt.setText("$" + String.valueOf(totalprize_for_order));
                        if(orderHistoryArraylist.size()==0){
                            final SweetAlertDialog dialog = new SweetAlertDialog(view.getRootView().getContext(),SweetAlertDialog.WARNING_TYPE);
                            dialog.setTitleText("Cart is empty!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            dialog.dismissWithAnimation();
                                            Intent i  =new Intent(context, FrameLayoutActivity.class);
                                            i.putExtra("tab_id",0);
                                            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK );
                                            context.startActivity(i);
                                            //finish();

                                        }
                                    })
                                    .show();
                            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                        }
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
    private void wish_list(final int position,final View view) {
        progress_bar_explore.setVisibility(View.VISIBLE);
        Fuel.post(URL1+"moveto-wishlist.php",params).responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
            @Override
            public void success(com.github.kittinunf.fuel.core.Request request, com.github.kittinunf.fuel.core.Response response, String s) {
                progress_bar_explore.setVisibility(View.GONE);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String status = jsonObj.getString("status");
                    if(Objects.equals(status, "true")){
                        removeAt(position);
                        if(orderHistoryArraylist.size()==0){
                            final SweetAlertDialog dialog = new SweetAlertDialog(view.getRootView().getContext(),SweetAlertDialog.WARNING_TYPE);
                            dialog.setTitleText("Cart is empty!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            dialog.dismissWithAnimation();
                                            Intent i  =new Intent(context, FrameLayoutActivity.class);
                                            i.putExtra("tab_id",0);
                                            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK );
                                            context.startActivity(i);
                                            //finish();

                                        }
                                    })
                                    .show();
                            dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
                        }
                    }
                    else {
                        final SweetAlertDialog dialog = new SweetAlertDialog(view.getRootView().getContext(),SweetAlertDialog.WARNING_TYPE);
                        dialog.setTitleText("Deal already in Wishlist!")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        dialog.dismissWithAnimation();

                                    }
                                })
                                .show();
                        dialog.findViewById(R.id.confirm_button).setBackgroundColor(Color.parseColor("#368aba"));
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

    @Override
    public int getItemCount() {

        return orderHistoryArraylist.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public  TextView title,booking_date,amount;
        public LinearLayout cart_decre,cart_incre,delete_cart,layout_chat_list,layout_wish_list;
        public  TextView cart_text;
        ImageView imag_ordr;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.names_cart);
            booking_date=(TextView)itemView.findViewById(R.id.datess);
            amount=(TextView)itemView.findViewById(R.id.amount);
            imag_ordr= (ImageView) itemView.findViewById(R.id.imag_ordr);
            layout_chat_list= (LinearLayout) itemView.findViewById(R.id.layout_chat_list);
            layout_wish_list= (LinearLayout) itemView.findViewById(R.id.layout_wish);
            delete_cart= (LinearLayout) itemView.findViewById(R.id.delete_cart);
            cart_decre= (LinearLayout) itemView.findViewById(R.id.cart_decrement);
            cart_text= (TextView)itemView. findViewById(R.id.cart_numbers);
            cart_incre= (LinearLayout)itemView. findViewById(R.id.cart_incremnt);

        }
    }

    public void removeAt(int position) {
        orderHistoryArraylist.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orderHistoryArraylist.size());
    }

}
