package com.meridian.dateout.nearme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.collections.CategoryDealFragment;
import com.meridian.dateout.collections.CollectionsFragment;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.model.DealsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ansal on 30-Apr-18.
 */

public class CollectionsAdapter4 extends RecyclerView.Adapter<CollectionsAdapter4.ViewHolder> {


    final Context context;

    static List<DealsModel> dealsModelArrayList = new ArrayList<>();
    List<DealsModel> itemsCopy = new ArrayList<>();

    public CollectionsAdapter4(List<DealsModel> dealsModelArrayList1, Context context) {

        this.context = context;
        dealsModelArrayList = dealsModelArrayList1;
        if (dealsModelArrayList != null)
            dealsModelArrayList = new ArrayList<>(dealsModelArrayList);
        else dealsModelArrayList = new ArrayList<>();

        itemsCopy.addAll(dealsModelArrayList);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView txt_titlergt,txt_title_category,txt_deal,txt_category;

        ViewHolder(View itemView)
        {
            super(itemView);

            System.out.println("filetrrrrrr");
            txt_titlergt = (TextView) itemView.findViewById(R.id.txt_title_filter_deal);
            txt_title_category = (TextView) itemView.findViewById(R.id.txt_title_filter_category);
            txt_deal = (TextView) itemView.findViewById(R.id.txt_deal);
            txt_category = (TextView) itemView.findViewById(R.id.txt_category);


        }

    }


    @Override
    public int getItemCount() {
        System.out.println("dataaa sizeeee"+dealsModelArrayList.size());
        return dealsModelArrayList.size();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_search, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        System.out.println("Dataaaaa" + i + dealsModelArrayList.size());
        System.out.println("<<<<<<<<<<<<<>>>>>>>>>>>>>>" + i + dealsModelArrayList.get(i).getTitle());

        viewHolder.txt_titlergt.setText("" + dealsModelArrayList.get(i).getTitle());




    }
    public void filter(String text)
    {

        System.out.println("Dataaaaa>>>>>>>>contentt"+ text);
        if (text.isEmpty()) {
            {
                dealsModelArrayList.clear();
                dealsModelArrayList.addAll(itemsCopy);

            }
        } else {


            List<DealsModel> deal_details1 = new ArrayList<>();

            for (DealsModel item : itemsCopy) {
                if (item.getTags().toLowerCase().contains(text)||item.getTags().toUpperCase().contains(text)||item.getTitle().toUpperCase().contains(text)|| item.getTitle().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)
                        ) {

                    deal_details1.add(item);



                }
                else{
                    if(  deal_details1.isEmpty()) {

                        System.out.println("Dataaaaa>>>>>>>>emptyyyyyy"+ text);
                    }else {
                        System.out.println("Dataaaaa>>>>>>>>notemptyyyyy"+ text);
                    }
                }
            }

            dealsModelArrayList.clear();
            dealsModelArrayList.addAll(deal_details1);
        }
        notifyDataSetChanged();
    }



}

