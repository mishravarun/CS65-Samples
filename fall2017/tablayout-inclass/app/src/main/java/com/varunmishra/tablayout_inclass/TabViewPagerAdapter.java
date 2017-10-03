package com.varunmishra.tablayout_inclass;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by varun on 10/3/17.
 */

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    public TabViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;

    }



    @Override
    public Fragment getItem(int position) {


        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "chat";
            case 1:
                return "find";
            case 2:
                return "meet";

            case 3:
                return "party";
            default:
                break;
        }
        return null;

    }
}
