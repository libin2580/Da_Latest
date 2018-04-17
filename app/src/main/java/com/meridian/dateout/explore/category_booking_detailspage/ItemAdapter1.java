package com.meridian.dateout.explore.category_booking_detailspage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meridian.dateout.R;

import java.util.ArrayList;

/**
 * Created by Ansal on 26-Mar-18.
 */


public class ItemAdapter1 extends RecyclerView.Adapter<ItemAdapter1.ItemViewHolder> {
    Context context;
    private ArrayList<String> orderHistoryArraylist;

    public ItemAdapter1(ArrayList<String> arrList, Context context) {
        this.context = context;
        this.orderHistoryArraylist = arrList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item1, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        holder.title.setText(orderHistoryArraylist.get(position));

    }

    @Override
    public int getItemCount() {
        return orderHistoryArraylist.size();
    }

    public  class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public ItemViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.txt);


        }
    }

}
