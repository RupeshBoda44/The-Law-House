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

import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.AppointmentPagerAdapter;
import com.thelawhouse.R;
import com.thelawhouse.databinding.FragAppointmentBinding;

public class FragAppointment extends Fragment {
    private FragAppointmentBinding mBinding;
    private MainActivity mainActivity;
    AppointmentPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_appointment, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        mainActivity.appointmentTitle();
        adapter = new AppointmentPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(FragViewAllAppointMent.newInstance(), "View All Appointment");
        adapter.addFragment(FragAddAppointment.newInstance(), "Add New Appointment");
        adapter.addFragment(FragTodayAppointment.newInstance(), "Today Appointment");
        adapter.addFragment(FragCompletedAppointment.newInstance(), "Completed Appointment");
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        mBinding.vpAppointment.setAdapter(adapter);
        mBinding.vpAppointment.setOffscreenPageLimit(limit);
        mBinding.tabAppointment.setupWithViewPager(mBinding.vpAppointment);
        changeTabsFont();
        onClickListener();
        return mBinding.getRoot();
    }

    private void changeTabsFont() {
        Typeface font = ResourcesCompat.getFont(getActivity(), R.font.font_regular);
        ViewGroup vg = (ViewGroup) mBinding.tabAppointment.getChildAt(0);
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

    private void onClickListener() {
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
