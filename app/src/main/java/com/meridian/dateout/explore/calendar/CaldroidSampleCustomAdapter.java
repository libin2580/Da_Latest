package com.meridian.dateout.explore.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.meridian.dateout.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {
int flag=0;


	public int row_index=0;

	public CaldroidSampleCustomAdapter(Context context, int month, int year,
			Map<String, Object> caldroidData,
			Map<String, Object> extraData) {
		super(context, month, year, caldroidData, extraData);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;



		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		final TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);
		final FrameLayout click= (FrameLayout) cellView.findViewById(R.id.click);
		tv1.setTextColor(Color.BLACK);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		final Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != month) {
			tv1.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_lighter_gray));
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;
//		final View finalCellView = cellView;
		// Customize for disabled dates and date outside min/max dates

		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {
//	System.out.println("disabled datess"+disableDates+"...."+disableDates.indexOf(dateTime)+"...");

			tv1.setTextColor(CaldroidFragment.disabledTextColor);
//			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
//				cellView.setBackgroundResource(R.color.sliderdrawerclick);
//
//
				if (CaldroidFragment.disabledBackgroundDrawable == -1 ) {
					cellView.setBackgroundResource(R.color.white);

				}
				else {
					cellView.setBackgroundResource(R.color.white);
				}

//
//
//			}
//			else {
//
//			}
//			if(CaldroidFragment.disabledBackgroundDrawable == -1){
//				cellView.setBackgroundResource(R.color.white);
//
//
////				if (CaldroidFragment.disabledBackgroundDrawable == -1) {
////					cellView.setBackgroundResource(R.color.white);
////				}
////				else {
////					cellView.setBackgroundResource(R.color.sliderdrawerclick);
////				}
//				//cellView.setBackgroundResource(R.color.sliderdrawerclick);
//				//}
//
//			}
//			else {
//
////				else {
////					cellView.setBackgroundResource(R.color.white);
////				}
//
//			}


				//cellView.setBackgroundResource(R.color.white);
//			} else
//			{
//				cellView.setBackgroundResource(R.color.white);
//			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);

			}


		} else {
			shouldResetDiabledView = true;
		}




//		cellView.setOnClickListener(new View.OnClickListener() {
//	@Override
//	public void onClick(View v) {
		final View finalCellView = cellView;
		tv1.setVisibility(View.VISIBLE);
		tv2.setVisibility(View.INVISIBLE);

		if(row_index==position) {

			if(tv1.getCurrentTextColor()==CaldroidFragment.disabledTextColor)
			{

			}else
			{

				finalCellView.setBackgroundResource(R.color.sliderdrawerclick);
				tv1.setVisibility(View.INVISIBLE);
				tv2.setVisibility(View.VISIBLE);
				tv2.setTextColor(resources.getColor(R.color.white));


//
			}


//			if(tv1.getCurrentTextColor()== Integer.parseInt("-7829368"))
//			{
//				tv1.setTextColor(resources.getColor(R.color.white));
//			}


		}
		else {

			finalCellView.setBackgroundResource(R.color.white);
			tv1.setVisibility(View.VISIBLE);

		}



//	}
//});
		click.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(tv1.getCurrentTextColor()==CaldroidFragment.disabledTextColor)
				{
					finalCellView.setBackgroundResource(R.color.white);

				}
				else {
					//finalCellView.setBackgroundResource(R.color.sliderdrawerclick);
					changeIndex(position);


				}

				return false;
			}
		});


		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
//			cellView.setBackgroundColor(resources
//					.getColor(R.color.sliderdrawerclick));

			tv1.setTextColor(resources
					.getColor(R.color.sliderdrawerclick));


		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
			} else {
				cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
			}
		}

		tv1.setText("" + dateTime.getDay());
		tv2.setText(""+dateTime.getDay());

		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);

		// Set custom color if required
		setCustomResources(dateTime, cellView, tv1);

		return cellView;
	}
	public ArrayList<DateTime> getSelectedDates() {
		return selectedDates;
	}

	public void setSelectedDates(ArrayList<DateTime> selectedDates) {
		this.selectedDates = selectedDates;
	}


	public  void changeIndex(int position) {

		this.row_index = position;

		notifyDataSetChanged();
	}


}