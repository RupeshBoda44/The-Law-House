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
import com.thelawhouse.databinding.ActivityVerifyOtpBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class VerifyOtpActivity extends AppCompatActivity {
    VerifyOtpActivity mContext = VerifyOtpActivity.this;
    ActivityVerifyOtpBinding mBinding;
    ProgressHUD mProgressHUD;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_verify_otp);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("register");
            if (type.equalsIgnoreCase("loginAdmin")) {
                resendOtpAdmin();
            }
        }
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
        mBinding.tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("register")) {
                    resendOtpAdmin();
                } else if (type.equalsIgnoreCase("loginAdmin")) {
                    resendOtpAdmin();
                } else {
                    resendOtp();
                }
            }
        });
        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtOTP.getText().toString().equalsIgnoreCase("")) {
                    Utils.showToast(mContext, "Please Enter OTP");
                } else {
                    if (getIntent().getExtras().getString("class").equalsIgnoreCase("ForgotPwd"))
                        submitForgotOTP();
                    else if (type.equalsIgnoreCase("register"))
                        submitOTPAdmin();
                    else if (type.equalsIgnoreCase("loginAdmin"))
                        submitOTPAdmin();
                    else
                        submitOTP();
                }
            }
        });
    }

    private void submitForgotOTP() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().ForgotVeryOTP(paramSubmitOTP()).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().status == true) {
                            Intent intent = new Intent(mContext, NewPasswordActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
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

    private Map<String, String> paramSubmitOTP() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        params.put("otp", mBinding.edtOTP.getText().toString());
        return params;
    }

    private void submitOTP() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().VerifyOTP(paramSubmitOTP()).enqueue(new Callback<VerifyOTPModel>() {
                @Override
                public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().status == true) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private void submitOTPAdmin() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().VerifyOTPAdmin(paramSubmitOTPAdmin()).enqueue(new Callback<VerifyOTPModel>() {
                @Override
                public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().status == true) {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramSubmitOTPAdmin() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        params.put("otp", mBinding.edtOTP.getText().toString());
        return params;
    }

    private void resendOtp() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().ResendOtp(paramResendOTP()).enqueue(new Callback<VerifyOTPModel>() {
                @Override
                public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().status == true) {
                            Utils.showDialog(mContext, "Otp is Successfully Sent");
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
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
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        return params;
    }

    private void resendOtpAdmin() {
        if (isInternetAvailable(mContext)) {
            mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().ResendOtpAdmin(paramResendOTPAdmin()).enqueue(new Callback<VerifyOTPModel>() {
                @Override
                public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        if (response.body().status == true) {
                            Utils.showDialog(mContext, "Otp is Successfully Sent");
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramResendOTPAdmin() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        return params;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("register", "forgot");
        startActivity(intent);
        finish();
    }
}
