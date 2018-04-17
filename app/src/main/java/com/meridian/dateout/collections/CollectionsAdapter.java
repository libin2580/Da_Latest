package com.meridian.dateout.collections;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meridian.dateout.R;
import com.meridian.dateout.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user 1 on 19-11-2016.
 */

public class CollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<CategoryModel> arrCategoryModelList;
    private int mRowIndex = -1;

    private List<CategoryModel> itemsCopy = new ArrayList<>();

    public CollectionsAdapter(Context context, List<CategoryModel> arrCategoryModelList) {
        this.context = context;
        this.arrCategoryModelList = arrCategoryModelList;
        this.mRowIndex = mRowIndex;
        itemsCopy.addAll(arrCategoryModelList);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView iconname;
        FrameLayout frameLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_icon);
            iconname = (TextView) itemView.findViewById(R.id.icon_name);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.inflate_collections, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;
        if (context != null && !((Activity)context).isFinishing()) {
            Glide
                    .with(context)
                    .load(arrCategoryModelList.get(position).getBackground())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(holder1.imageView);
        }


        holder1.iconname.setText(arrCategoryModelList.get(position).getCategory());


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
            List<CategoryModel> categoryModelArrayList1 = new ArrayList<>();
            text = text.toLowerCase();
            for (CategoryModel item : itemsCopy) {
                if (item.getCategory().toLowerCase().contains(text) || item.getCategory().toLowerCase().contains(text)
                        ) {
                    categoryModelArrayList1.add(item);
                }
            }
            arrCategoryModelList.clear();
            arrCategoryModelList.addAll(categoryModelArrayList1);
        }
        notifyDataSetChanged();
    }

}
