package com.meridian.dateout.explore.promo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.explore.address.Adddetails;
import com.meridian.dateout.explore.address.AddressModel;
import com.meridian.dateout.explore.address.EdittestActivity;

import java.util.List;

import kotlin.Pair;


/**
 * Created by libin on 3/17/2018.
 */

public class PromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<PromoModel> promolist;

    View custompopup_view;
    PopupWindow address_popupwindow;
    LinearLayout coordinatorLayout;
    int selectedPosition = -1;// no selection by default
    public static String selected_id = "-1";
    List<Pair<String, String>> params;
   EditText addr_name, addr_phone, addr_city, addr_area, addrs_flat, addr_state, addr_pin, addr_email;
    CheckBox addr_work_edit, addr_home_edit;
    LinearLayout save_data;
String ad_type;

    public PromoAdapter(Context context, List<PromoModel> promolist) {
        this.context = context;
        this.promolist = promolist;


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, off, validity, city, state, pin, work_home, adress_title, edit, email,phone;
        CheckBox address_check;
        Button code;
        public ItemViewHolder(View itemView) {
            super(itemView);

            code = (Button)itemView.findViewById(R.id.code);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            off = (TextView) itemView.findViewById(R.id.off);
            validity = (TextView) itemView.findViewById(R.id.validity);
            address_check = (CheckBox) itemView.findViewById(R.id.address_work);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.promo_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;



       // int s = position + 1;
//        holder1.adress_title.setText("Address " + s);
        holder1.item_name.setText(promolist.get(position).getCoupon_name());
        holder1.off.setText(promolist.get(position).getOffer());
        holder1.validity.setText("valid_till"+"  "+promolist.get(position).getCpn_valid_till());
        holder1.code.setText(promolist.get(position).getCoupon_code());

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                /*AdddetailsDelivery.address_id=addressModels.get(position).getId();*/
                notifyDataSetChanged();
            }
        });
        holder1.address_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1.itemView.performClick();
                Promo.promo_id1=promolist.get(position).getCoupon_id();
            }
        });
        if (selectedPosition == position) {
            holder1.address_check.setChecked(true);

        } else {
            holder1.address_check.setChecked(false);

        }
        if (holder1.address_check.isChecked()){
            Promo.promo_id1=promolist.get(position).getCoupon_id();
            Promo.coupn_value=promolist.get(position).getCoupon_code();
        }

    }


    @Override
    public int getItemCount() {
        return promolist.size();
    }



}