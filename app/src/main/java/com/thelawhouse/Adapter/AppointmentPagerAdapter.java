package com.thelawhouse.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thelawhouse.Fragment.FragAddAppointment;
import com.thelawhouse.Fragment.FragCompletedAppointment;
import com.thelawhouse.Fragment.FragTodayAppointment;
import com.thelawhouse.Fragment.FragViewAllAppointMent;

import java.util.ArrayList;
import java.util.List;

public class AppointmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public AppointmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragViewAllAppointMent();
            case 1:
                return new FragAddAppointment();
            case 2:
                return new FragTodayAppointment();
            case 3:
                return new FragCompletedAppointment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
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
