package com.meridian.dateout.account.order;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by SIDDEEQ DESIGNER on 3/16/2018.
 */

public class OrderMain_Adapter extends FragmentStatePagerAdapter
{
    ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    ArrayList<String> stringArrayList=new ArrayList<>();

    public OrderMain_Adapter(FragmentManager fm) {
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
