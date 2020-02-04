package com.thelawhouse.Fragment;

import android.annotation.SuppressLint;
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

import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.CasePagerAdapter;
import com.thelawhouse.R;
import com.thelawhouse.databinding.FragCaseBinding;

public class FragCase extends Fragment {
    private FragCaseBinding mBinding;
    private MainActivity mainActivity;
    CasePagerAdapter adapter;

    public static FragCase newInstance() {
        return new FragCase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_case, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        mainActivity.caseTitle();
        adapter = new CasePagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(FragCaseCalenderList.newInstance(), "Cases Calender");
        adapter.addFragment(FragAddNewCase.newInstance(), "Add New Case");
        adapter.addFragment(FragViewAllCase.newInstance(), "All Cases");
        adapter.addFragment(FragCompletedCases.newInstance(), "Dispose Cases");
        adapter.addFragment(FragUnupdatedCases.newInstance(), "Unupdated Cases");
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        mBinding.vpCase.setAdapter(adapter);
        mBinding.vpCase.setOffscreenPageLimit(limit);
        setRetainInstance(true);
        mBinding.tabCase.setupWithViewPager(mBinding.vpCase);
        changeTabsFont();
        return mBinding.getRoot();
    }

    private void changeTabsFont() {
        Typeface font = ResourcesCompat.getFont(getActivity(), R.font.font_regular);
        ViewGroup vg = (ViewGroup) mBinding.tabCase.getChildAt(0);
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
