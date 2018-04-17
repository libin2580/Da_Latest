package com.meridian.dateout.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user 1 on 19-11-2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<NotificationModel> arrCategoryModelList;
    private int mRowIndex = -1;

    private List<NotificationModel> itemsCopy = new ArrayList<>();

    public NotificationAdapter(Context context, ArrayList<NotificationModel> arrCategoryModelList) {
        this.context = context;
        this.arrCategoryModelList = arrCategoryModelList;
        this.mRowIndex = mRowIndex;
        itemsCopy.addAll(arrCategoryModelList);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        //   private LinearLayout linearLayout;

        TextView txt_event, txt_loc, txt_time, txt_dealtime;
        FrameLayout frameLayout;
        LinearLayout chatlist;

        public ItemViewHolder(View itemView) {
            super(itemView);

            txt_event = (TextView) itemView.findViewById(R.id.txt_notif_evnt);
            txt_loc = (TextView) itemView.findViewById(R.id.txt_notif_loc);
            txt_time = (TextView) itemView.findViewById(R.id.txt_notif_time);
            txt_dealtime = (TextView) itemView.findViewById(R.id.txt_notif_dealTim);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.inflate_notification, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;
        holder1.txt_event.setText(arrCategoryModelList.get(position).getNot_event());
        holder1.txt_loc.setText(arrCategoryModelList.get(position).getNot_loc());
        holder1.txt_time.setText(arrCategoryModelList.get(position).getNot_time());
       holder1.txt_dealtime.setText(arrCategoryModelList.get(position).getNot_dealtme());

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
            //  ArrayList<PhoneBookItem> result = new ArrayList<>();
            List<NotificationModel> categoryModelArrayList1 = new ArrayList<>();
            text = text.toLowerCase();
            arrCategoryModelList.clear();
            arrCategoryModelList.addAll(categoryModelArrayList1);
        }
        notifyDataSetChanged();
    }

}
