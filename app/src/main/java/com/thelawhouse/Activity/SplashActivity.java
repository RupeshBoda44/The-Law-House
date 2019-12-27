package com.thelawhouse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    SplashActivity mContext = SplashActivity.this;
    ActivitySplashBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_splash);
        splashMove();
    }

    public void splashMove() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceHelper.getBoolean(Constants.IS_LOGIN, false)) {
                    if (PreferenceHelper.getString(Constants.LOGINTYPE, "").equalsIgnoreCase("guest")) {
                        Intent intent = new Intent(mContext, GMainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                } else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
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
