package com.meridian.dateout.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.model.ChatListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user 1 on 19-11-2016.
 */

public class ChatlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<ChatListModel> arrCategoryModelList;
    private int mRowIndex = -1;

    private List<ChatListModel> itemsCopy = new ArrayList<>();

    public ChatlistAdapter(Context context, ArrayList<ChatListModel> arrCategoryModelList) {
        this.context = context;
        this.arrCategoryModelList = arrCategoryModelList;
        this.mRowIndex = mRowIndex;
        itemsCopy.addAll(arrCategoryModelList);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        //   private LinearLayout linearLayout;

        TextView chatquestn, chat_des;
        FrameLayout frameLayout;
        LinearLayout chatlist;

        public ItemViewHolder(View itemView) {
            super(itemView);

            chatquestn = (TextView) itemView.findViewById(R.id.txt_ques);
            chat_des = (TextView) itemView.findViewById(R.id.txt_des);
            chatlist = (LinearLayout) itemView.findViewById(R.id.layout_chat_list);

            // linearLayout = (LinearLayout) itemView.findViewById(R.id.image_category_horizontal);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.inflate_chastlist, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;
        holder1.chatquestn.setText(arrCategoryModelList.get(position).getCht_topic());
        holder1.chat_des.setText(arrCategoryModelList.get(position).getCategory_name());

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
            List<ChatListModel> categoryModelArrayList1 = new ArrayList<>();
            text = text.toLowerCase();
            arrCategoryModelList.clear();
            arrCategoryModelList.addAll(categoryModelArrayList1);
        }
        notifyDataSetChanged();
    }

}
