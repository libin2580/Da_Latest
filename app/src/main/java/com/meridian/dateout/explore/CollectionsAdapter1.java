package com.meridian.dateout.explore;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.meridian.dateout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by libin on 3/16/2018.
 */

public class CollectionsAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<Spinner_model1> SpinList1;
    public static ArrayList<String> jsonlist;

    public CollectionsAdapter1(Context context, List<Spinner_model1> SpinList1) {
        this.context = context;
        this.SpinList1 = SpinList1;


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        CheckedTextView ctv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ctv = (CheckedTextView)itemView.findViewById(R.id.checkedTextView1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.inflate_category, parent, false);
        CollectionsAdapter1.ItemViewHolder holder = new CollectionsAdapter1.ItemViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CollectionsAdapter1.ItemViewHolder holder1 = (CollectionsAdapter1.ItemViewHolder) holder;
        jsonlist=new ArrayList<>();

        holder1.ctv.setText(SpinList1.get(position).getCategory());
        holder1.ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder1.ctv.isChecked()){
                    holder1.ctv.setChecked(false);
                    holder1.ctv.setCheckMarkDrawable(R.drawable.ic_tick);
                    jsonlist.add(SpinList1.get(position).getId());
                    System.out.println("----------- jsonlist: " + jsonlist);
                }
                else{
                    holder1.ctv.setChecked(true);
                    holder1.ctv.setCheckMarkDrawable(R.drawable.ic_tick_unselect);
                    jsonlist.remove(SpinList1.get(position).getId());
                    System.out.println("----------- jsonlist: " + jsonlist);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return SpinList1.size();
    }



}