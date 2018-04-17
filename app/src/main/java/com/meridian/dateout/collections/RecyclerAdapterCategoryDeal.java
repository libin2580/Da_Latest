package com.meridian.dateout.collections;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meridian.dateout.R;
import com.meridian.dateout.model.CategoryDealModel;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by user1 on 14-10-2015.
 */
public class RecyclerAdapterCategoryDeal extends RecyclerView.Adapter<RecyclerAdapterCategoryDeal.ViewHolder> {


    List<CategoryDealModel> categoryDealModelArrayList = new ArrayList<>();
    final Context context;
    ArrayList<String> all_background;
    List<CategoryDealModel> itemsCopy = new ArrayList<>();

    public RecyclerAdapterCategoryDeal(List<CategoryDealModel> categoryDealModelArrayList, Context context) {


        this.context = context;
        this.categoryDealModelArrayList = categoryDealModelArrayList;
        itemsCopy.addAll(categoryDealModelArrayList);


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_category;
        TextView txt_title,  txt_discount,txt_price;

        ViewHolder(View itemView) {
            super(itemView);
            img_category = (ImageView) itemView.findViewById(R.id.image_category_deal);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_price = (TextView) itemView.findViewById(R.id.txt_pricedeal);
            txt_discount = (TextView) itemView.findViewById(R.id.txt_discount);


        }

    }


    @Override
    public int getItemCount() {

        return categoryDealModelArrayList.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_category_deal, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        Glide
                .with(context)
                .load(categoryDealModelArrayList.get(i).getImage())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .into(holder.img_category);

        holder.txt_title.setText(categoryDealModelArrayList.get(i).getTitle());
        if(categoryDealModelArrayList.get(i).getDiscount().isEmpty()||categoryDealModelArrayList.get(i).getDiscount().equals(0)||categoryDealModelArrayList.get(i).getDiscount().equals("")) {
            holder.txt_discount.setText(categoryDealModelArrayList.get(i).getCurrency() + " " + categoryDealModelArrayList.get(i).getPrice());
            holder.txt_price.setVisibility(View.GONE);

        }
        else {
            final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
            holder.txt_price.setText(categoryDealModelArrayList.get(i).getCurrency() + " " + categoryDealModelArrayList.get(i).getPrice(), TextView.BufferType.SPANNABLE);
            Spannable spannable1 = (Spannable) holder.txt_price.getText();
            spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, holder.txt_price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txt_discount.setText(categoryDealModelArrayList.get(i).getCurrency() + " " + categoryDealModelArrayList.get(i).getDiscount());
            System.out.println("categrydeal" + categoryDealModelArrayList.get(i).getDescription() + categoryDealModelArrayList.get(i).getTitle() + categoryDealModelArrayList.get(i).getDiscount() + categoryDealModelArrayList.get(i).getTiming());
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void filter(String text) {

        if (text.isEmpty()) {
            {
                categoryDealModelArrayList.clear();
                categoryDealModelArrayList.addAll(itemsCopy);
            }
        } else {
            //  ArrayList<PhoneBookItem> result = new ArrayList<>();
            List<CategoryDealModel> deal_details1 = new ArrayList<>();
            text = text.toLowerCase();
            for (CategoryDealModel item : itemsCopy) {
                if (item.getTitle().toLowerCase().contains(text) || item.getTitle().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)
                        || item.getDescription().toUpperCase().contains(text)) {
                    deal_details1.add(item);
                }
            }
            categoryDealModelArrayList.clear();
            categoryDealModelArrayList.addAll(deal_details1);
        }
        notifyDataSetChanged();
    }


}
