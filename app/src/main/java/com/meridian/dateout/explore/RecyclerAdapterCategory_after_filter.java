package com.meridian.dateout.explore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meridian.dateout.R;
import com.meridian.dateout.explore.category_booking_detailspage.CategoryDealDetail;
import com.meridian.dateout.model.CategoryModel;
import com.meridian.dateout.model.DealsModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by user1 on 14-10-2015.
 */
public class RecyclerAdapterCategory_after_filter extends RecyclerView.Adapter<RecyclerAdapterCategory_after_filter.ViewHolder> {
    List<CategoryModel> categoryModelArrayList = new ArrayList<>();
    final Context context;
    ArrayList<String> all_background;
    static List<DealsModel> dealsModelArrayList = new ArrayList<>();
    List<DealsModel> itemsCopy = new ArrayList<>();
    private HorizontalRVAdapter horizontalAdapter;
    public RecyclerAdapterCategory_after_filter(List<CategoryModel> categoryModelArraylst1, List<DealsModel> dealsModelArrayList1, Context context) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArraylst1;
        dealsModelArrayList = dealsModelArrayList1;
        if (dealsModelArrayList != null)
            dealsModelArrayList = new ArrayList<>(dealsModelArrayList);
        else dealsModelArrayList = new ArrayList<>();
        if (categoryModelArraylst1 != null)
            categoryModelArrayList = new ArrayList<>(categoryModelArraylst1);
        else categoryModelArrayList = new ArrayList<>();
        itemsCopy.addAll(dealsModelArrayList);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_category, img_category_one, img_categoryback;
        TextView txt_titleleftback, txt_discountleftback,txt_titleleft, txt_titlergt, txt_descriptionleft;
        LinearLayout layblnk;
        RecyclerView recyclerView;
        LinearLayout linearLayout1, linearLayout3;
        RelativeLayout relativeLayout1;
        TextView price_left, discnt_left, price_right, disnt_rgt, txt_priceback;
        ViewHolder(View itemView)
        {
            super(itemView);
            img_category = (ImageView) itemView.findViewById(R.id.image_favrt);
            img_categoryback = (ImageView) itemView.findViewById(R.id.image_favrtback);
            img_category_one = (ImageView) itemView.findViewById(R.id.image_category_vertical1);
            relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            linearLayout1 = (LinearLayout) itemView.findViewById(R.id.linearlayout1);
            linearLayout3 = (LinearLayout) itemView.findViewById(R.id.linearlayout3);
            txt_titleleft = (TextView) itemView.findViewById(R.id.txt_titlefavrt);
            txt_titleleftback = (TextView) itemView.findViewById(R.id.txt_titlefavrtback);
            txt_titlergt = (TextView) itemView.findViewById(R.id.txt_titlergt);
            price_left = (TextView) itemView.findViewById(R.id.txt_pricefavrt);
            discnt_left = (TextView) itemView.findViewById(R.id.txt_pricediscnt);
            price_right = (TextView) itemView.findViewById(R.id.txt_actualtrgt);
            disnt_rgt = (TextView) itemView.findViewById(R.id.txt_discrgt);
            txt_priceback = (TextView) itemView.findViewById(R.id.txt_pricefavrtback);
            txt_discountleftback = (TextView) itemView.findViewById(R.id.txt_pricediscntback);
            txt_descriptionleft = (TextView) itemView.findViewById(R.id.txt_descriptionfavrt);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_horizontal);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }
    @Override
    public int getItemCount() {

        return dealsModelArrayList.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_categorys_after_filter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        System.out.println("iiiiii" + i + categoryModelArrayList.size());
        viewHolder.linearLayout3.setVisibility(View.GONE);
        horizontalAdapter = new HorizontalRVAdapter(context);

            if(dealsModelArrayList.get(i).getDiscount().isEmpty()||dealsModelArrayList.get(i).getDiscount().equals(0)||dealsModelArrayList.get(i).getDiscount().equalsIgnoreCase("")) {
                    viewHolder.discnt_left.setText(dealsModelArrayList.get(i).getCurrency()+" "+ dealsModelArrayList.get(i).getPrice());
                    viewHolder.price_left.setVisibility(View.GONE);
            }
            else {
                final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
                viewHolder.price_left.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getPrice(), TextView.BufferType.SPANNABLE);
                Spannable spannable1 = (Spannable) viewHolder.price_left.getText();
                spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, viewHolder.price_left.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.discnt_left.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getDiscount());
            }
            viewHolder.txt_titleleft.setText(dealsModelArrayList.get(i).getTitle());
            if (dealsModelArrayList.get(i).getImage() != null && !dealsModelArrayList.get(i).getImage().isEmpty()) {
                try {
                    Glide
                            .with(context)
                            .load(dealsModelArrayList.get(i).getImage())
                            .centerCrop()

                            .crossFade()
                            .into(viewHolder.img_category);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                try{

                Glide
                        .with(context)
                        .load(R.drawable.login_background)
                        .centerCrop()

                        .crossFade()
                        .into(viewHolder.img_category);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        if (i ==(dealsModelArrayList.size()-1))
        {  viewHolder.linearLayout3.setVisibility(View.VISIBLE);
            int k = categoryModelArrayList.size();
            List<CategoryModel> newList = new ArrayList<>(categoryModelArrayList);
            horizontalAdapter.setData(newList);
            viewHolder.recyclerView.setAdapter(horizontalAdapter);
            horizontalAdapter.setRowIndex(7);
            System.out.println("xxdetailxiddd" + i + 7);
        }

        viewHolder.linearLayout1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    int ids = Integer.parseInt(dealsModelArrayList.get(i).getId());
                    String deal_slug = dealsModelArrayList.get(i).getDeal_slug();
                    System.out.println("xxdetailxiddd" + ids);
                    Intent i = new Intent(context, CategoryDealDetail.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra("deal_slug", deal_slug);
                    i.putExtra("deal_id", ids);
                    context.startActivity(i);
                }
                catch (Exception e)
                {

                }

            }
        });

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public void filter(String text)
    {

        if (text.isEmpty()) {
            {
                dealsModelArrayList.clear();
                dealsModelArrayList.addAll(itemsCopy);
            }
        } else {
            List<DealsModel> deal_details1 = new ArrayList<>();
            text = text.toLowerCase();
            for (DealsModel item : itemsCopy) {
                if (item.getTags().toLowerCase().contains(text)||item.getTags().toUpperCase().contains(text)||item.getTitle().toLowerCase().contains(text) || item.getTitle().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)
                        || item.getDescription().toUpperCase().contains(text)) {
                    deal_details1.add(item);
                }
            }
            dealsModelArrayList.clear();
            dealsModelArrayList.addAll(deal_details1);
        }
        notifyDataSetChanged();
    }



}
