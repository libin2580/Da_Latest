package com.meridian.dateout.explore.cart;

import android.content.Context;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Pair;

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
            public void onClick(View view) {
                params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("cart_item_id", orderHistoryArraylist.get(position).getcart_item_id()));

                }};
                delete(position,k);

            }
        });
        holder.layout_chat_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CategoryDealDetail.class);
                i.putExtra("deal_id", Integer.parseInt(orderHistoryArraylist.get(position).getDeal_id()));
                context.startActivity(i);
            }
        });

        holder.layout_wish_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params = new ArrayList<Pair<String, String>>() {{
                    add(new Pair<String, String>("cart_item_id", orderHistoryArraylist.get(position).getcart_item_id()));

                }};
                wish_list(position);
            }
        });

    }

    private void delete(final int position,final int k) {
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
    private void wish_list(final int position) {
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
                    }
                    else {
                        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
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
