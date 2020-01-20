package com.thelawhouse.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thelawhouse.Fragment.FragAboutUs;
import com.thelawhouse.Fragment.FragSegmentLawList;
import com.thelawhouse.Fragment.FragUnupdatedCases;
import com.thelawhouse.Fragment.FragUsefullLinkList;

import java.util.ArrayList;
import java.util.List;

public class ServicesPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public ServicesPagerAdapter(FragmentManager fm) {
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
                return new FragSegmentLawList();
            case 1:
                return new FragUsefullLinkList();
            case 2:
                return new FragAboutUs();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
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
