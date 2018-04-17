package com.meridian.dateout.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meridian.dateout.R;
import com.meridian.dateout.model.CategoryDealModel;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by user1 on 14-10-2015.
 */
public class FavrtAdapter extends RecyclerView.Adapter<FavrtAdapter.ViewHolder> {


    List<CategoryDealModel> categoryDealModelArrayList = new ArrayList<>();
    final Context context;
    ArrayList<String> all_background;
    List<CategoryDealModel> itemsCopy = new ArrayList<>();

    public FavrtAdapter(List<CategoryDealModel> categoryDealModelArrayList, Context context) {


        this.context = context;
        this.categoryDealModelArrayList = categoryDealModelArrayList;
        itemsCopy.addAll(categoryDealModelArrayList);


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_category, wsh;
        TextView txt_title, txt_description, txt_timimg, txt_discount;

        ViewHolder(View itemView) {
            super(itemView);
            img_category = (ImageView) itemView.findViewById(R.id.image_favrt);

            txt_title = (TextView) itemView.findViewById(R.id.txt_titlefavrt);
            txt_description = (TextView) itemView.findViewById(R.id.txt_descriptionfavrt);
            txt_discount = (TextView) itemView.findViewById(R.id.txt_pricefavrtback);
            wsh = (ImageView) itemView.findViewById(R.id.img_wishfavrt);


        }

    }


    @Override
    public int getItemCount() {

        return categoryDealModelArrayList.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_favrt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        System.out.println("categrydeal" + categoryDealModelArrayList.get(i).getDescription() + categoryDealModelArrayList.get(i).getTitle() + categoryDealModelArrayList.get(i).getDiscount());
        if (categoryDealModelArrayList.get(i).getImage() != null && !categoryDealModelArrayList.get(i).getImage().isEmpty()) {
            Glide
                    .with(context)
                    .load(categoryDealModelArrayList.get(i).getImage())
                    .centerCrop()

                    .crossFade()
                    .into(holder.img_category);

        }
        if (categoryDealModelArrayList.get(i).getTitle() != null) {
            holder.txt_title.setText(categoryDealModelArrayList.get(i).getTitle());
        }

        if (categoryDealModelArrayList.get(i).getDiscount() != null) {
            holder.txt_discount.setText("$ " + categoryDealModelArrayList.get(i).getDiscount());
        }

        holder.wsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.wsh.setBackgroundResource(R.drawable.whish_list);
                SharedPreferences preferences = context.getSharedPreferences("wishk", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("wish_select", String.valueOf(0));
            }
        });

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
