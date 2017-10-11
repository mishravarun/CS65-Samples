package com.varunmishra.tablayout_inclass;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.varunmishra.tablayout_inclass.view.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public TextView tv;
//Changes
    private SlidingTabLayout slidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private TabViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new FindFragment());
        fragments.add(new ChatFragment());
        fragments.add(new MeetFragment());
        fragments.add(new PartyFragment());

        mViewPagerAdapter = new TabViewPagerAdapter(getFragmentManager(),fragments);

        mViewPager.setAdapter(mViewPagerAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mViewPager);

    }
    public void testClick(View v){
        tv=findViewById(R.id.summary);
        tv.setText("text");
        Log.d("Test","tt");
    }
}
