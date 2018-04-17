package com.meridian.dateout.coupon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meridian.dateout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user 1 on 19-11-2016.
 */

public class CouponAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<CouponModel> arrCategoryModelList;
    private int mRowIndex = -1;

    private List<CouponModel> itemsCopy = new ArrayList<>();

    public CouponAdapter(Context context, ArrayList<CouponModel> arrCategoryModelList) {
        this.context = context;
        this.arrCategoryModelList = arrCategoryModelList;
        this.mRowIndex = mRowIndex;
        itemsCopy.addAll(arrCategoryModelList);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {



        TextView  txt_offer,txt_validity,txt_time,txt_off_perc;
        LinearLayout coupn;
        ImageView coupon_imag;
        FrameLayout frameLayout;
        LinearLayout chatlist;

        public ItemViewHolder(View itemView) {
            super(itemView);

            coupon_imag= (ImageView) itemView.findViewById(R.id.img_coupon);
            txt_offer = (TextView) itemView.findViewById(R.id.textView_offer);
            txt_validity= (TextView) itemView.findViewById(R.id.textView_validity);
            txt_off_perc = (TextView) itemView.findViewById(R.id.textView_off_perc);
            coupn= (LinearLayout) itemView.findViewById(R.id.lay_coupon);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.coupon_row, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;
        holder1.txt_offer.setText(arrCategoryModelList.get(position).getCpn_desc());
        holder1.txt_validity.setText(arrCategoryModelList.get(position).getExpire_in());
        if(arrCategoryModelList.get(position).getCpn_discount_unit().contentEquals("cash"))
        {
            holder1.txt_off_perc.setText("$ "+arrCategoryModelList.get(position).getCpn_discount());
        }
        else {
            holder1.txt_off_perc.setText(arrCategoryModelList.get(position).getCpn_discount()+"%");
        }
        try{
        Glide.with(context)
                .load(arrCategoryModelList.get(position).getCpn_image())
                .centerCrop()

                .crossFade()
                .into( holder1.coupon_imag);
    }catch (Exception e){
        e.printStackTrace();
    }
    holder1.coupn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CouponFragment.coupn_pop_up.setVisibility(View.VISIBLE);
            CouponFragment.coupn_code_pop.setText(arrCategoryModelList.get(position).getCpn_code());
            CouponFragment.start_date.setText(arrCategoryModelList.get(position).getCpn_strt_date());
            CouponFragment.expire_date.setText(arrCategoryModelList.get(position).getCpn_exp_date());
            CouponFragment.coupn_category.setText(arrCategoryModelList.get(position).getCategory());
            CouponFragment.but_push.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CouponFragment.coupn_pop_up.setVisibility(View.GONE);
                }
            });

        }
    });


    }

    @Override
    public int getItemCount() {
        return arrCategoryModelList.size();
    }

    public void filter(String text) {

        if (text.isEmpty()) {
            {
                arrCategoryModelList.clear();
                arrCategoryModelList.addAll(itemsCopy);
            }
        } else {

            List<CouponModel> categoryModelArrayList1 = new ArrayList<>();
            text = text.toLowerCase();
            arrCategoryModelList.clear();
            arrCategoryModelList.addAll(categoryModelArrayList1);
        }
        notifyDataSetChanged();
    }

}
