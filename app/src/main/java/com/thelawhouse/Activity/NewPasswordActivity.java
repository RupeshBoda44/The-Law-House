package com.thelawhouse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.Model.VerifyOTPModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityNewPasswordBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;
import static com.thelawhouse.Utils.Utils.showToast;

public class NewPasswordActivity extends AppCompatActivity {
    NewPasswordActivity mContext = NewPasswordActivity.this;
    ActivityNewPasswordBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_new_password);
        onClickListener();
    }

    private void onClickListener() {
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("register", "forgot");
                startActivity(intent);
                finish();
            }
        });
        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtPassword.getText().toString().equalsIgnoreCase("")) {
                    showToast(mContext, "Please Enter your Password");
                } else if (mBinding.edtConfPassword.getText().toString().equalsIgnoreCase("")) {
                    showToast(mContext, "Please Enter your Confirm Password");
                } else if (!mBinding.edtPassword.getText().toString().equals(mBinding.edtConfPassword.getText().toString())) {
                    showToast(mContext, "Your Password did not Match");
                } else {
                    submitPassword();
                }
            }
        });
    }

    private void submitPassword() {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().ChangePassword(paramResendOTP()).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().message.equalsIgnoreCase("success")) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.putExtra("register", "forgot");
                            startActivity(intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
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
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("email", PreferenceHelper.getString(Constants.EMAIL, ""));
        params.put("password", mBinding.edtPassword.getText().toString());
        params.put("unique_code", PreferenceHelper.getString(Constants.UNIQUE_CODE, ""));
        return params;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, ForgotPWDActivity.class);
        startActivity(intent);
        finish();
    }
}
