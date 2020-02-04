package com.thelawhouse.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thelawhouse.Fragment.FragAddNewCase;
import com.thelawhouse.Fragment.FragCaseCalenderList;
import com.thelawhouse.Fragment.FragCompletedCases;
import com.thelawhouse.Fragment.FragUnupdatedCases;
import com.thelawhouse.Fragment.FragViewAllCase;

import java.util.ArrayList;
import java.util.List;

public class CasePagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public CasePagerAdapter(FragmentManager fm) {
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
                return new FragCaseCalenderList();
            case 1:
                return new FragAddNewCase();
            case 2:
                return new FragViewAllCase();
            case 3:
                return new FragCompletedCases();
            case 4:
                return new FragUnupdatedCases();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
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
