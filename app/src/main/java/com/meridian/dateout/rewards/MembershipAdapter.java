package com.meridian.dateout.rewards;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.model.MembershipModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user1 on 23-Oct-17.
 */

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.ViewHolder> {

    private List<MembershipModel> array = new ArrayList<>();

    Context context;

    public MembershipAdapter(Context context, ArrayList<MembershipModel> array) {
        this.array=array;
        this.context=context;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        WebView description;
        TextView away_from,stage_title;

        ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_first);
            away_from= (TextView) itemView.findViewById(R.id.stage_away);
         stage_title= (TextView) itemView.findViewById(R.id.stage_title);
            description= (WebView) itemView.findViewById(R.id.stage_points);





        }

    }


    @Override
    public int getItemCount() {

        return array.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inflate_member, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        final String s = array.get(i).getImage();
        Picasso.with(context).load(array.get(i).getImage()).into(holder.img);
        holder.away_from.setText(array.get(i).getPoint_stage());
        holder.stage_title.setText(array.get(i).getTitle());
        String str = "<div style=\'background-color:transparent;padding: 10px ;color:#000000'>" + array.get(i).getDiscription() + "</div";
        holder.description.setBackgroundColor(Color.TRANSPARENT);
        holder.description.getSettings().setJavaScriptEnabled(true);
        holder.description.loadDataWithBaseURL("",str,"text/html","UTF-8","");
        WebSettings settings_hig = holder.description.getSettings();
        settings_hig.setTextZoom(80);

    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }





}
