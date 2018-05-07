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
public class RecyclerAdapterCategory extends RecyclerView.Adapter<RecyclerAdapterCategory.ViewHolder> {


    List<CategoryModel> categoryModelArrayList = new ArrayList<>();
    final Context context;
    ArrayList<String> all_background;
    static List<DealsModel> dealsModelArrayList = new ArrayList<>();
    List<DealsModel> itemsCopy = new ArrayList<>();
    private HorizontalRVAdapter horizontalAdapter;

    public RecyclerAdapterCategory(List<CategoryModel> categoryModelArraylst1, List<DealsModel> dealsModelArrayList1, Context context) {


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
        TextView txt_titleleftback, txt_discountleftback, txt_descriptionleftback, txt_titleleft, txt_titlergt, txt_descriptionleft, txt_descriptionrgt, txt_discountleft, txt_discountrgt, txt_timingleft, txt_timingrgt;
        LinearLayout layblnk;
        RecyclerView recyclerView;
        LinearLayout linearLayout1, linearLayout2, linearLayout3;
        RelativeLayout relativeLayout1, relativeLayout2;
        TextView price_left, discnt_left, price_right, disnt_rgt, txt_priceback;

        ViewHolder(View itemView)
        {
            super(itemView);
            img_category = (ImageView) itemView.findViewById(R.id.image_favrt);
            img_categoryback = (ImageView) itemView.findViewById(R.id.image_favrtback);
            img_category_one = (ImageView) itemView.findViewById(R.id.image_category_vertical1);
            relativeLayout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            relativeLayout2 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout2);
            linearLayout1 = (LinearLayout) itemView.findViewById(R.id.linearlayout1);
            linearLayout2 = (LinearLayout) itemView.findViewById(R.id.linearlayout2);
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
            layblnk = (LinearLayout) itemView.findViewById(R.id.lay_blck);
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_categorys1, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        System.out.println("iiiiii" + i + categoryModelArrayList.size());


        horizontalAdapter = new HorizontalRVAdapter(context);


        if (i % 2 == 0) {

            viewHolder.relativeLayout1.setVisibility(View.VISIBLE);
            viewHolder.relativeLayout2.setVisibility(View.GONE);
            viewHolder.recyclerView.setVisibility(View.GONE);
            viewHolder.linearLayout1.setVisibility(View.VISIBLE);
            viewHolder.linearLayout2.setVisibility(View.GONE);
            viewHolder.linearLayout3.setVisibility(View.GONE);
            System.out.println("i" + i);
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


        }
        else {
            System.out.println("i" + dealsModelArrayList.get(i).getDiscount() + "pic" + dealsModelArrayList.get(i).getImage());
            viewHolder.relativeLayout1.setVisibility(View.GONE);
            viewHolder.relativeLayout2.setVisibility(View.VISIBLE);
            viewHolder.recyclerView.setVisibility(View.GONE);
            viewHolder.linearLayout1.setVisibility(View.GONE);
            viewHolder.linearLayout2.setVisibility(View.VISIBLE);


            viewHolder.linearLayout3.setVisibility(View.GONE);
            if(dealsModelArrayList.get(i).getDiscount().isEmpty()||dealsModelArrayList.get(i).getDiscount().equals(0)||dealsModelArrayList.get(i).getDiscount().equals("")) {
                {

                    viewHolder.disnt_rgt.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getPrice());
                    viewHolder.price_right.setVisibility(View.GONE);


                }


            }
            else {
                final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
                viewHolder.price_right.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getPrice(), TextView.BufferType.SPANNABLE);
                Spannable spannable1 = (Spannable) viewHolder.price_right.getText();
                spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, viewHolder.price_right.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.disnt_rgt.setText(dealsModelArrayList.get(i).getCurrency()+" "+ dealsModelArrayList.get(i).getDiscount());
            }
            viewHolder.txt_titlergt.setText(dealsModelArrayList.get(i).getTitle());
            if (dealsModelArrayList.get(i).getImage() != null && !dealsModelArrayList.get(i).getImage().isEmpty())
            {
                try {
                    Glide
                            .with(context)
                            .load(dealsModelArrayList.get(i).getImage())
                            .centerCrop()

                            .crossFade()
                            .into(viewHolder.img_category_one);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            else
            {
                try{
                Glide.with(context)
                        .load(R.drawable.
                                login_background)
                        .centerCrop()

                        .crossFade()
                        .into(viewHolder.img_category);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        if (i == 2)
        {
            viewHolder.layblnk.setVisibility(View.VISIBLE);
            final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
            if(dealsModelArrayList.get(i).getDiscount().isEmpty()||dealsModelArrayList.get(i).getDiscount().equals(0)||dealsModelArrayList.get(i).getDiscount().equals("")) {
                viewHolder.txt_discountleftback.setText(dealsModelArrayList.get(i).getCurrency()+" "+ dealsModelArrayList.get(i).getPrice());
                viewHolder.txt_priceback.setVisibility(View.GONE);

            }
            else {
                viewHolder.txt_priceback.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getPrice(), TextView.BufferType.SPANNABLE);
                Spannable spannable1 = (Spannable) viewHolder.txt_priceback.getText();
                spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, viewHolder.txt_priceback.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_discountleftback.setText(dealsModelArrayList.get(i).getCurrency()+" "+ dealsModelArrayList.get(i).getDiscount());
            }
            viewHolder.txt_titleleftback.setText(dealsModelArrayList.get(i).getTitle());


          if(dealsModelArrayList.get(i).getImage() != null && !dealsModelArrayList.get(i).getImage().isEmpty())
          {
              try{
              Glide
                  .with(context)
                  .load(dealsModelArrayList.get(i).getImage())
                  .centerCrop()

                  .crossFade()
                  .into(viewHolder.img_categoryback);
              }catch (Exception e){
                  e.printStackTrace();
              }

            } else {
              try{
              Glide.with(context)
                      .load(R.drawable.login_background)
                      .centerCrop()

                      .crossFade()
                      .into(viewHolder.img_categoryback);
              }catch (Exception e){
                  e.printStackTrace();
              }

            }

            List<CategoryModel> newList = new ArrayList<>(categoryModelArrayList.subList(0,(categoryModelArrayList.size()/2+1)));

            horizontalAdapter.setData(newList);

            viewHolder.recyclerView.setAdapter(horizontalAdapter);

            horizontalAdapter.setRowIndex(2);
            viewHolder.linearLayout3.setVisibility(View.VISIBLE);
            viewHolder.linearLayout1.setVisibility(View.GONE);
            viewHolder.linearLayout2.setVisibility(View.GONE);
            viewHolder.recyclerView.setVisibility(View.VISIBLE);
            viewHolder.relativeLayout1.setVisibility(View.GONE);
            viewHolder.relativeLayout2.setVisibility(View.GONE);


        }
        if (i ==7) {
            int k = categoryModelArrayList.size();
            viewHolder.layblnk.setVisibility(View.VISIBLE);
            final StrikethroughSpan STRIKE_THROUGH_SPAN1 = new StrikethroughSpan();
            if(dealsModelArrayList.get(i).getDiscount().isEmpty()||dealsModelArrayList.get(i).getDiscount().equals(0)||dealsModelArrayList.get(i).getDiscount().equals("")) {
                viewHolder.txt_discountleftback.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getPrice());
                viewHolder.txt_priceback.setVisibility(View.GONE);

            }
            else {
                viewHolder.txt_priceback.setText(dealsModelArrayList.get(i).getCurrency()+" "+dealsModelArrayList.get(i).getPrice(), TextView.BufferType.SPANNABLE);
                Spannable spannable1 = (Spannable) viewHolder.txt_priceback.getText();
                spannable1.setSpan(STRIKE_THROUGH_SPAN1, 0, viewHolder.txt_priceback.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                viewHolder.txt_discountleftback.setText(dealsModelArrayList.get(i).getCurrency()+" "+ dealsModelArrayList.get(i).getDiscount());
            }

            viewHolder.txt_titleleftback.setText(dealsModelArrayList.get(i).getTitle());


            if (dealsModelArrayList.get(i).getImage() != null && !dealsModelArrayList.get(i).getImage().isEmpty())
            {
                try{
                Glide.with(context)
                        .load(dealsModelArrayList.get(i).getImage())
                        .centerCrop()

                        .crossFade()
                        .into(viewHolder.img_categoryback);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            else
            {
                try{
                Glide.with(context)
                        .load(R.drawable.login_background)
                        .centerCrop()

                        .crossFade()
                        .into(viewHolder.img_categoryback);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            List<CategoryModel> newList = new ArrayList<>(categoryModelArrayList.subList((categoryModelArrayList.size()/2+1), k));
            horizontalAdapter.setData(newList);
            viewHolder.recyclerView.setAdapter(horizontalAdapter);
            horizontalAdapter.setRowIndex(7);
            System.out.println("xxdetailxiddd" + i + 7);
            viewHolder.linearLayout3.setVisibility(View.VISIBLE);
            viewHolder.linearLayout1.setVisibility(View.GONE);
            viewHolder.linearLayout2.setVisibility(View.GONE);
            viewHolder.recyclerView.setVisibility(View.VISIBLE);
            viewHolder.relativeLayout1.setVisibility(View.GONE);
            viewHolder.relativeLayout2.setVisibility(View.GONE);


        }
        viewHolder.layblnk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int ids = Integer.parseInt(dealsModelArrayList.get(i).getId());
                String deal_slug = dealsModelArrayList.get(i).getDeal_slug();
                System.out.println("xxdetailxiddd" + ids);
                Intent i = new Intent(context, CategoryDealDetail.class);
                i.putExtra("deal_slug", deal_slug);
                i.putExtra("deal_id", ids);
                context.startActivity(i);

            }
        });
        viewHolder.linearLayout1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    int ids = Integer.parseInt(dealsModelArrayList.get(i).getId());
                    String deal_slug = dealsModelArrayList.get(i).getDeal_slug();

                    System.out.println("xxdetailxiddd" + ids);
                    Intent i = new Intent(context, CategoryDealDetail.class);
                    i.putExtra("deal_slug", deal_slug);
                    i.putExtra("deal_id", ids);
                    context.startActivity(i);
                }
                catch (Exception e)
                {

                }
            }
        });
        viewHolder.linearLayout2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                try {
                    int ids = Integer.parseInt(dealsModelArrayList.get(i).getId());
                    String deal_slug = dealsModelArrayList.get(i).getDeal_slug();
                    System.out.println("xxdetailxiddd" + ids);

                    Intent i = new Intent(context, CategoryDealDetail.class);
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
