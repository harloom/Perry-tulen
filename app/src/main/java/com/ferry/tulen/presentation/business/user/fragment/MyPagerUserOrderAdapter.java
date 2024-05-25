package com.ferry.tulen.presentation.business.user.fragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPagerUserOrderAdapter extends FragmentStateAdapter {

    public MyPagerUserOrderAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new Tab1MyOderFragment();
        } else {
            return new Tab2MyOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}