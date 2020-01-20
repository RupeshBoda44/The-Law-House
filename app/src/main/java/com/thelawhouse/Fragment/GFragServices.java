package com.thelawhouse.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.thelawhouse.Activity.GMainActivity;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.GServicesPagerAdapter;
import com.thelawhouse.Adapter.ServicesPagerAdapter;
import com.thelawhouse.R;
import com.thelawhouse.databinding.FragServicesBinding;

public class GFragServices extends Fragment {
    private FragServicesBinding mBinding;
    private GMainActivity mainActivity;
    GServicesPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_services, container, false);
        mainActivity = (GMainActivity) getActivity();
        mainActivity.ClickcableTrue();
        mainActivity.serviceTitle();
        adapter = new GServicesPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(GFragSegmentLawList.newInstance(), "Segments of law and Legal");
        adapter.addFragment(GFragUsefullLinkList.newInstance(), "Useful Link");
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        mBinding.vpServices.setAdapter(adapter);
        mBinding.vpServices.setOffscreenPageLimit(limit);
        setRetainInstance(true);
        mBinding.tabServices.setupWithViewPager(mBinding.vpServices);
        changeTabsFont();
        return mBinding.getRoot();
    }

    private void changeTabsFont() {
        Typeface font = ResourcesCompat.getFont(getActivity(), R.font.font_regular);
        ViewGroup vg = (ViewGroup) mBinding.tabServices.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font, Typeface.BOLD);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    mainActivity.hideSoftKeyboard(getActivity(), v);
                    onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        mainActivity.selectFirstItemAsDefault();
    }
}
