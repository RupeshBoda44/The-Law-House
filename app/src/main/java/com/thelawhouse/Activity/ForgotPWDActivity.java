package com.thelawhouse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.thelawhouse.Model.RegisterModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityForgotPwdBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;
import static com.thelawhouse.Utils.Utils.showToast;

public class ForgotPWDActivity extends AppCompatActivity {
    ForgotPWDActivity mContext = ForgotPWDActivity.this;
    ActivityForgotPwdBinding mBinding;
    ProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_forgot_pwd);
        onClickListener();
    }

    private void onClickListener() {
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtMobile.getText().toString().equalsIgnoreCase("")) {
                    showToast(mContext, "Please Enter Phone Number");
                } else if (mBinding.edtMobile.getText().toString().length() < 10) {
                    showToast(mContext, "Please Enter Valid Phone Number");
                } else {
                    forgotPassword();
                }
            }
        });
    }

    private void forgotPassword() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().ForgotPassword(paramResendOTP()).enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().message.equalsIgnoreCase("success")) {
                            PreferenceHelper.putString(Constants.OTP, response.body().otp);
                            PreferenceHelper.putString(Constants.USER_ID, response.body().user_id);
                            PreferenceHelper.putString(Constants.MOBILE_NUMBER, response.body().mobile_number);
                            Intent intent = new Intent(mContext, VerifyOtpActivity.class);
                            intent.putExtra("class", "ForgotPwd");
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramResendOTP() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", mBinding.edtMobile.getText().toString());
        return params;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("register","forgot");
        startActivity(intent);
        finish();
    }
}
