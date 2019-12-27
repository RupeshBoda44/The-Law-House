package com.thelawhouse.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.thelawhouse.Adapter.LoginSignupPagerAdapter;
import com.thelawhouse.Fragment.EmployeeFragment;
import com.thelawhouse.Fragment.GuestFragment;
import com.thelawhouse.R;
import com.thelawhouse.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    LoginActivity mContext = LoginActivity.this;
    ActivityLoginBinding dataBinding;
    public static String register = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            register = bundle.getString("register");
        }
        LoginSignupPagerAdapter adapter = new LoginSignupPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(EmployeeFragment.newInstance(), "Admin");
        adapter.addFragment(GuestFragment.newInstance(), "Client");
        dataBinding.vpLoginSignUp.setAdapter(adapter);
        dataBinding.tabLoginSignUp.setupWithViewPager(dataBinding.vpLoginSignUp);
        if (register.equalsIgnoreCase("forgot")) {
            dataBinding.vpLoginSignUp.setCurrentItem(0);
        } else {
            dataBinding.vpLoginSignUp.setCurrentItem(1);
        }
        dataBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataBinding.swipeRefresh.setRefreshing(false);
                dataBinding.vpLoginSignUp.setCurrentItem(1);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
//        if (PreferenceHelper.getString(Constants.LoginTYPEFAndE, "").equals("F")) {
//            dataBinding.vpLoginSignUp.setCurrentItem(0);
//        } else {
//
//        }
        changeTabsFont();
//        onClickListener();
    }

    private void changeTabsFont() {
        Typeface font = ResourcesCompat.getFont(mContext, R.font.font_regular);
        ViewGroup vg = (ViewGroup) dataBinding.tabLoginSignUp.getChildAt(0);
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
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
        System.exit(0);
    }
}
