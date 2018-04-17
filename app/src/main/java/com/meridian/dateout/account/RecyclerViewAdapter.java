package com.meridian.dateout.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.model.CreditsModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<CreditsModel> creditsModelArrayList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre, issue, country,date,creditpoints;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
          date = (TextView) view.findViewById(R.id.date);
            creditpoints = (TextView) view.findViewById(R.id.creditpoints);
            country = (TextView) view.findViewById(R.id.country);
            issue = (TextView) view.findViewById(R.id.issue1);
        }
    }


    public RecyclerViewAdapter(Context context,ArrayList<CreditsModel> creditsModelArrayList) {
        this.creditsModelArrayList = creditsModelArrayList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_items, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CreditsModel creditsModel= creditsModelArrayList.get(position);
        holder.title.setText(creditsModel.getTitle());
        holder.date.setText(creditsModel.getBk_date());
        holder.creditpoints.setText(creditsModel.getTotal_price());
       holder.country.setText(creditsModel.getCountry());

        if (position % 2 == 0) {
            holder.creditpoints.setBackgroundResource(R.drawable.round);


        }


    }

    @Override
    public int getItemCount() {
        return creditsModelArrayList.size();
    }
}