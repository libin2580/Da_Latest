package com.meridian.dateout.reminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meridian.dateout.R;

import java.util.ArrayList;

/**
 * Created by user1 on 29-Nov-17.
 */

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {
    ArrayList<ReminderModel>arrayList=new ArrayList<>();
    Context context;
    public UpcomingAdapter(ArrayList<ReminderModel> array_up, Context context) {
        this.arrayList=array_up;
        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list, parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txt_date.setText(arrayList.get(position).getEvent_date());
        holder.txt_event.setText(arrayList.get(position).getEvent_name());
        holder.txt_time.setText(arrayList.get(position).getEvent_time());
        holder.txt_month.setText(arrayList.get(position).getEvent_month());

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date,txt_month,txt_event,txt_time;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            txt_month=(TextView)itemView.findViewById(R.id.txt_month);
            txt_event=(TextView)itemView.findViewById(R.id.txt_event);
            txt_time=(TextView) itemView.findViewById(R.id.txt_time);
        }
    }
}
