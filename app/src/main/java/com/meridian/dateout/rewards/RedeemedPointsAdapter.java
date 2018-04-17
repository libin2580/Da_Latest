package com.meridian.dateout.rewards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meridian.dateout.R;

import java.util.ArrayList;

/**
 * Created by Anvin on 11/1/2017.
 */

public class RedeemedPointsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private ArrayList<String> namesArraylist;
    private ArrayList<String> pointsArraylist;
    private ArrayList<String> dateArraylist;


    public RedeemedPointsAdapter(Context context, ArrayList<String>  namess,ArrayList<String> pointss,ArrayList<String>  dateArraylist) {
        this.context = context;
        this.namesArraylist =  namess;
        this.pointsArraylist=pointss;
        this.dateArraylist=dateArraylist;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView txt_name,txt_points,date;
        View view_line;
        public ItemViewHolder(View itemView) {
            super(itemView);
            txt_name=(TextView)itemView.findViewById(R.id.txt_name);
            txt_points=(TextView)itemView.findViewById(R.id.txt_points);
            date=(TextView)itemView.findViewById(R.id.txt_date);
            view_line=(View)itemView.findViewById(R.id.view_line);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.earned_points_row, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;
        holder1.txt_name.setText(namesArraylist.get(position));
        holder1.txt_points.setText(pointsArraylist.get(position));
        holder1.date.setText(dateArraylist.get(position));
        if(position==namesArraylist.size()-1)
        {
            holder1.view_line.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return namesArraylist.size();
    }


}