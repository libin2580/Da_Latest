package com.meridian.dateout.explore.category_booking_detailspage;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.explore.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

import static com.meridian.dateout.explore.category_booking_detailspage.Booking_DetailsActivity.somejson;

/**
 * Created by user 1 on 19-11-2016.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    Context context;
    private ArrayList<ItemModel> orderHistoryArraylist;
    ItemAdapter1 adapter;
    StringBuffer strBuf=new StringBuffer();
    StringBuffer strBuf1=new StringBuffer();
    StringBuffer strBuf2=new StringBuffer();
    final String[] test = new String[1];
    String  kk;


    public ItemAdapter(ArrayList<ItemModel> arrList, Context context) {
        this.context = context;
        this.orderHistoryArraylist = arrList;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemslist, viewGroup, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        holder.title.setText(orderHistoryArraylist.get(position).getname());
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> list_nw = new ArrayList<>();
        final ArrayList<String> list_nw1 = new ArrayList<>();
        try {
            String data =orderHistoryArraylist.get(position).gettype();
            final JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++){
                String aa = jsonArray.get(i).toString();
                System.out.println("-----------sort_by"+aa);
                list.add(aa);
                if(position==i){

                    strBuf.append("{\"\"label\"\":\"\"" + orderHistoryArraylist.get(position).getname() + "\"\",\"\"option\"\":\"\"" + jsonArray.get(0).toString() + "\"\"},");
                    System.out.println("-----------strBuf" + strBuf);
                    test[0] = String.valueOf(strBuf);
                    test[0] = test[0].substring(0, test[0].length() - 1);
                    test[0] = String.valueOf("[" + test[0] + "]");
                    somejson=   test[0];
                    System.out.println("-----------list_test" + test[0]);
                    strBuf1.append(jsonArray.get(0).toString()+",");
                    kk= String.valueOf(strBuf1);
                    kk = kk.substring(0, kk.length() - 1);
                    System.out.println("-----------kkkkkkkkkkkkkkkkkkkkkk" + kk);

                }

                holder.checkedTextView.setText(jsonArray.get(0).toString());
                holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("-----------position"+position);

                        if (holder.checkedTextView.isChecked()){
                            holder.checkedTextView.setChecked(false);
                            holder.recyclerView.setVisibility(View.VISIBLE);
                            adapter = new ItemAdapter1(list, context);
                            holder.recyclerView.scheduleLayoutAnimation();
                            holder.recyclerView.setAdapter(adapter);
                            holder.recyclerView.setHasFixedSize(true);
                        }
                        else{
                            holder.recyclerView.setVisibility(View.GONE);
                            holder.checkedTextView.setChecked(true);
                        }

                    }
                });
                holder.recyclerView.addOnItemTouchListener(
                                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int i) {
                                        try {
                                            holder.checkedTextView.setText(jsonArray.get(i).toString());
                                            holder.recyclerView.setVisibility(View.GONE);
                                            holder.checkedTextView.setChecked(true);
                                            System.out.println("-----------*******" + kk);
                                            String[] a = kk.split(",");
                                            list_nw.clear();
                                            list_nw.addAll(Arrays.asList(a));
                                            list_nw.set(position,jsonArray.get(i).toString());
                                            System.out.println("-----------kkkkkkkkkkkkkkkkkkkkkk" + list_nw);
                                            kk= String.valueOf(list_nw);
                                            kk = kk.replaceAll("[]]","");
                                            kk = kk.substring(1);
                                            kk = kk.replace(" ", "");
                                            for (int g=0;g<list_nw.size();g++){
                                                strBuf2.append("{\"\"label\"\":\"\"" + orderHistoryArraylist.get(g).getname() + "\"\"#\"\"option\"\":\"\"" + list_nw.get(g) + "\"\"},");
                                                test[0] = String.valueOf(strBuf2);
                                                test[0] = test[0].substring(0, test[0].length() - 1);
                                                System.out.println("-----------test[0]" + test[0]);
                                                String[] b = test[0].split(",");
                                                list_nw1.addAll(Arrays.asList(b));

                                            }
                                            somejson=list_nw1.get(list_nw1.size()-2)+","+list_nw1.get(list_nw1.size()-1);
                                            somejson=somejson.replace("#",",");
                                            somejson = String.valueOf("[" +somejson + "]");
                                            System.out.println("-----------list_test" +somejson);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                })
                        );

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return orderHistoryArraylist.size();
    }

    public  class ItemViewHolder extends RecyclerView.ViewHolder {
          TextView title;
         CheckedTextView checkedTextView;
         RecyclerView recyclerView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.txt);
            checkedTextView = (CheckedTextView)itemView. findViewById(R.id.spinner);
            recyclerView= (RecyclerView)itemView. findViewById(R.id.recy);
            GridLayoutManager llm = new GridLayoutManager(context,1);
            recyclerView.setLayoutManager(llm);

        }
    }

}
