package com.thelawhouse.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.thelawhouse.Fragment.FragCompletedAppointment;
import com.thelawhouse.Fragment.FragTodayAppointment;
import com.thelawhouse.Fragment.FragViewAllAppointMent;

import java.util.ArrayList;
import java.util.List;

public class MyActivityPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public MyActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragTodayAppointment();
            case 1:
                return new FragViewAllAppointMent();
            case 2:
                return new FragCompletedAppointment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
