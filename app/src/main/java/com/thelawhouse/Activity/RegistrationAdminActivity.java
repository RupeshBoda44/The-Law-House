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
import com.thelawhouse.databinding.ActivityRegistrationAdminBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;
import static com.thelawhouse.Utils.Utils.showToast;

public class RegistrationAdminActivity extends AppCompatActivity {
    RegistrationAdminActivity mContext = RegistrationAdminActivity.this;
    ActivityRegistrationAdminBinding mBinding;
    ProgressHUD mProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_registration_admin);
        onClickListener();
    }

    private void onClickListener() {
        mBinding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if (mBinding.edtName.getText().toString().equalsIgnoreCase("")) {
                    showToast(mContext, "Please Enter your Name");
                } else if (!mBinding.edtEmail.getText().toString().matches(emailPattern)) {
                    showToast(mContext, "Please Enter valid Email Address");
                }  else if (mBinding.edtPassword.getText().toString().equalsIgnoreCase("")) {
                    showToast(mContext, "Please Enter your Password");
                } else {
                    registerUser();
                }
            }
        });
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().AdminRegister(paramRegister()).enqueue(new Callback<RegisterModel>() {
                @Override
                public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().message.equalsIgnoreCase("success")) {
                            PreferenceHelper.putString(Constants.USER_ID, response.body().user_id);
                            PreferenceHelper.putString(Constants.OTP, response.body().otp);
                            PreferenceHelper.putString(Constants.EMAIL, response.body().email);
                            Intent intent = new Intent(mContext, VerifyOtpActivity.class);
                            intent.putExtra("register", "register");
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
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

    private Map<String, String> paramRegister() {
        Map<String, String> params = new HashMap<>();
        params.put("name", mBinding.edtName.getText().toString());
        params.put("mobile_number", mBinding.edtMobile.getText().toString());
        params.put("email", mBinding.edtEmail.getText().toString());
        params.put("password", mBinding.edtPassword.getText().toString());
        return params;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
