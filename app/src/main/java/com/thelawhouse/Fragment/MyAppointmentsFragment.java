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
import androidx.fragment.app.FragmentManager;

import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Adapter.MyActivityPagerAdapter;
import com.thelawhouse.R;
import com.thelawhouse.databinding.FragmentMyAppointmentBinding;


public class MyAppointmentsFragment extends Fragment {
    FragmentMyAppointmentBinding mBinding;
    String index = "";
    private MainActivity mainActivity;

    public static MyAppointmentsFragment newInstance() {
        return new MyAppointmentsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_appointment, container, false);
        MyActivityPagerAdapter adapter = new MyActivityPagerAdapter(getChildFragmentManager());
        adapter.addFragment(FragTodayAppointment.newInstance(), "Today");
        adapter.addFragment(FragViewAllAppointMent.newInstance(), "All");
        adapter.addFragment(FragCompletedAppointment.newInstance(), "Completed");
        mBinding.vpMyActivity.setAdapter(adapter);
        mBinding.tabMyActivity.setupWithViewPager(mBinding.vpMyActivity);
        /*Bundle bundle = this.getArguments();
        if (bundle != null) {
            index = bundle.getString("index");
            if (index.equalsIgnoreCase("1")) {
                mBinding.vpMyActivity.setCurrentItem(1);
            }
        }*/
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        changeTabsFont();
        return mBinding.getRoot();
    }

    private void changeTabsFont() {
        Typeface font = ResourcesCompat.getFont(getActivity(), R.font.font_regular);
        ViewGroup vg = (ViewGroup) mBinding.tabMyActivity.getChildAt(0);
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

    public void selectIndex(int newIndex) {

        mBinding.vpMyActivity.setCurrentItem(newIndex);
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
