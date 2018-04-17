package com.meridian.dateout.explore.calendar;

import android.widget.TextView;

import com.meridian.dateout.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {
	private TextView monthTitleTextView;
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new CaldroidSampleCustomAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
	}
	@Override
	public void setMonthTitleTextView(TextView monthTitleTextView) {

		this.monthTitleTextView.setTextColor(getResources().getColor(R.color.button_color));
		this.monthTitleTextView=monthTitleTextView;
	}


//	@Override
//	public TextView getMonthTitleTextView() {
//		return monthTitleTextView;
//	}


}