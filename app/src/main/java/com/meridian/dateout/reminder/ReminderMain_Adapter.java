package com.meridian.dateout.reminder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by rishika on 11/27/2017.
 */

public class ReminderMain_Adapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    ArrayList<String> stringArrayList=new ArrayList<>();

    public ReminderMain_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
    public void addFragment(Fragment fragment, String title) {
        fragmentArrayList.add(fragment);

        stringArrayList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return   stringArrayList.get(position);
    }

}
