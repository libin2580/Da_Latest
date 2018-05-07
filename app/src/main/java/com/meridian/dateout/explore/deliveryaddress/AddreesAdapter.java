package com.meridian.dateout.explore.deliveryaddress;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.meridian.dateout.explore.address.Adddetails;

import java.util.List;

import kotlin.Pair;


/**
 * Created by libin on 3/17/2018.
 */

public class AddreesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<AddressModel> addressModels;

    View custompopup_view;
    PopupWindow address_popupwindow;
    LinearLayout coordinatorLayout;
    int selectedPosition = -1;// no selection by default
    public static String selected_id = "-1";
    List<Pair<String, String>> params;
   EditText addr_name, addr_phone, addr_city, addr_area, addrs_flat, addr_state, addr_pin, addr_email;
    CheckBox addr_work_edit, addr_home_edit;
    LinearLayout save_data;
String ad_type;
    public AddreesAdapter(Context context, List<AddressModel> addressModels) {
        this.context = context;
        this.addressModels = addressModels;


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name, flat_address, area, city, state, pin, work_home, adress_title, edit, email,phone;
        CheckBox address_check;
        public ItemViewHolder(View itemView) {
            super(itemView);

            adress_title = (TextView) itemView.findViewById(R.id.adress_title);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            flat_address = (TextView) itemView.findViewById(R.id.flat_address);
            area = (TextView) itemView.findViewById(R.id.area);
            city = (TextView) itemView.findViewById(R.id.city);
            state = (TextView) itemView.findViewById(R.id.state);
            pin = (TextView) itemView.findViewById(R.id.pin);
            work_home = (TextView) itemView.findViewById(R.id.work_home);
            phone = (TextView) itemView.findViewById(R.id.phone);
            edit = (TextView) itemView.findViewById(R.id.edit);
            address_check = (CheckBox) itemView.findViewById(R.id.address_work);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.inflate_address_item1, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder holder1 = (ItemViewHolder) holder;



        int s = position + 1;
        holder1.adress_title.setText("Address " + s);
        holder1.name.setText(addressModels.get(position).getName());
        holder1.email.setText(addressModels.get(position).getEmail());
        holder1.flat_address.setText(addressModels.get(position).getFlat_address());
        holder1.area.setText(addressModels.get(position).getArea());
        holder1.city.setText(addressModels.get(position).getCity());
        holder1.state.setText(addressModels.get(position).getState());
        holder1.pin.setText(addressModels.get(position).getPin());
        holder1.work_home.setText(addressModels.get(position).getPin());
        holder1.work_home.setText(addressModels.get(position).getWork_home());
        holder1.phone.setText(addressModels.get(position).getPhone());
        holder1.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(context, EdittestActivityDelivery.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id",addressModels.get(position).getId());
                i.putExtra("name",addressModels.get(position).getName());
                i.putExtra("email",addressModels.get(position).getEmail());
                i.putExtra("phone",addressModels.get(position).getPhone());
                i.putExtra("city",addressModels.get(position).getCity());
                i.putExtra("street",addressModels.get(position).getArea());
                i.putExtra("flat_no",addressModels.get(position).getFlat_address());
                i.putExtra("state",addressModels.get(position).getState());
                i.putExtra("pin",addressModels.get(position).getPin());
                i.putExtra("type",addressModels.get(position).getWork_home());

                context.startActivity(i);
            }
        });
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                /*AdddetailsDelivery.address_id=addressModels.get(position).getId();*/
                notifyDataSetChanged();
            }
        });
        holder1.address_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1.itemView.performClick();
                AdddetailsDelivery.address_id=addressModels.get(position).getId();
            }
        });
        if (selectedPosition == position) {
            holder1.address_check.setChecked(true);

        } else {
            holder1.address_check.setChecked(false);
        }
        if (holder1.address_check.isChecked()){
            AdddetailsDelivery.address_id=addressModels.get(position).getId();
        }
    }


    @Override
    public int getItemCount() {
        return addressModels.size();
    }



}