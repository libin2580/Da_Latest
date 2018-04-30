package com.meridian.dateout.explore;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meridian.dateout.R;
import com.meridian.dateout.collections.CategoryDealFragment;
import com.meridian.dateout.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HorizontalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<CategoryModel> categoryModelArraylst1 = new ArrayList<>();
    private int mRowIndex = -1;

    public HorizontalRVAdapter(Context context) {

        this.context = context;

    }

    public void setData(List<CategoryModel> categoryModelArraylstt) {
        if (categoryModelArraylst1 != categoryModelArraylstt) {
            categoryModelArraylst1 = categoryModelArraylstt;
            notifyDataSetChanged();
        }
    }


    public void setRowIndex(int index) {
        mRowIndex = index;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView iconname;
        FrameLayout frameLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_icon);
            iconname = (TextView) itemView.findViewById(R.id.icon_name);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_horizontal);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.inflate_category_horizontalsnw, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, final int position) {

        final ItemViewHolder holder = (ItemViewHolder) rawHolder;
        System.out.println("get_18plusOnly" + categoryModelArraylst1.get(position).get_18plusOnly());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int id = Integer.parseInt(categoryModelArraylst1.get(position).getId());
                final int _18plusOnly = Integer.parseInt(categoryModelArraylst1.get(position).get_18plusOnly());
                System.out.println("intimate_id" + id);
                if(_18plusOnly==1){
                    final SweetAlertDialog dialog = new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE);
                    dialog.setTitleText("")
                            .setContentText("Are you Above 18 years ?")

                            .setConfirmText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    dialog.dismiss();

                                }
                            })
                            .setCancelText("Yes")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    FragmentTransaction trans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

                                    Fragment fragment = CategoryDealFragment.newInstance();
                                    Bundle args = new Bundle();
                                    args.putInt("category_id", id);

                                    args.putString("names", categoryModelArraylst1.get(position).getCategory());
                                    fragment.setArguments(args);
                                    trans.replace(R.id.flFragmentPlaceHolder, fragment, "cat_deal1").addToBackStack("cat_deal1");
                                    trans.commit();
                                }
                            })
                            .show();
                    dialog.findViewById(R.id.cancel_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));
                    dialog.findViewById(R.id.confirm_button).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#368aba")));

                }
                else {
                    FragmentTransaction trans = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

                    Fragment fragment = CategoryDealFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putInt("category_id", id);

                    args.putString("names", categoryModelArraylst1.get(position).getCategory());
                    fragment.setArguments(args);
                    trans.replace(R.id.flFragmentPlaceHolder, fragment, "cat_deal1").addToBackStack("cat_deal1");
                    trans.commit();

                }


            }
        });

        try{
        Glide
                .with(context)
                .load(categoryModelArraylst1.get(position).getBackground())
                .centerCrop()

                .crossFade()
                .into(holder.imageView);
    }catch (Exception e){
            e.printStackTrace();
        }

        holder.iconname.setText(categoryModelArraylst1.get(position).getCategory());

        System.out.println("horizontal" + categoryModelArraylst1.get(position));


    }

    @Override
    public int getItemCount() {
        return categoryModelArraylst1.size();
    }

}