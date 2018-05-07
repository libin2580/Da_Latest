package com.meridian.dateout.account.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by user 1 on 19-11-2016.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ItemViewHolder> {
    Context context;
    private ArrayList<OrderHistoryModel> orderHistoryArraylist;

    String newtime;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryModel> arrList) {
        this.context = context;
        this.orderHistoryArraylist = arrList;
        System.out.println("orderHistoryArraylist.size() : "+orderHistoryArraylist.size());
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title,booking_date,booking_time;
        ImageView imag_ordr;
        public ItemViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            booking_date=(TextView)itemView.findViewById(R.id.booking_date);
            booking_time=(TextView)itemView.findViewById(R.id.booking_time);
            imag_ordr= (ImageView) itemView.findViewById(R.id.imag_ordr);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_history_row, viewGroup, false);
        ItemViewHolder pvh = new ItemViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.title.setText(orderHistoryArraylist.get(position).getTitle());
        Picasso.with(context).load(orderHistoryArraylist.get(position).getImage()).into(holder.imag_ordr);
        String string = orderHistoryArraylist.get(position).getBooking_time();
        holder.booking_date.setText(orderHistoryArraylist.get(position).getBooking_date());


        String[] parts = string.split(" ");

        String part1 = parts[0]; // 004
        String part2 = parts[1];


       /* Calendar cs = Calendar.getInstance(TimeZone.getTimeZone(part2));
        SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
        final String formattedDate = df.format(cs.getTime());*/

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        StringTokenizer tk = new StringTokenizer(string);
        String date = tk.nextToken();
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("HH:mm a");
        Date dt;
        try {
            dt = sdf.parse(time);
            System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here

            newtime= sdfs.format(dt);
            Log.e("aaaaaaaaaaaaaaa", sdfs.format(dt));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        holder.booking_time.setText(newtime);


    }
    @Override
    public int getItemCount() {
        return orderHistoryArraylist.size();
    }


}
