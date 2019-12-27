package com.thelawhouse.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.thelawhouse.Activity.GMainActivity;
import com.thelawhouse.Activity.LoginActivity;
import com.thelawhouse.Activity.RegistrationActivity;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragmentGuestBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Activity.LoginActivity.register;
import static com.thelawhouse.Utils.Utils.isInternetAvailable;
import static com.thelawhouse.Utils.Utils.showToast;


public class GuestFragment extends Fragment {
    private FragmentGuestBinding mBinding;
    private ProgressHUD mProgressHUD;
    LoginActivity loginActivity;

    public static GuestFragment newInstance() {
        return new GuestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_guest, container, false);
        loginActivity = (LoginActivity) getActivity();
        if (register.equalsIgnoreCase("complete")) {
            mBinding.llMobile.setVisibility(View.GONE);
            mBinding.llOtp.setVisibility(View.VISIBLE);
            mBinding.tvSendOtp.setVisibility(View.GONE);
            mBinding.tvLogin.setVisibility(View.VISIBLE);
        }
        clickListener();
        return mBinding.getRoot();
    }

    private void clickListener() {
        mBinding.tvSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtMobile.getText().toString().equalsIgnoreCase("")) {
                    showToast(getActivity(), "Please Enter Phone Number");
                } else if (mBinding.edtMobile.getText().toString().length() < 10) {
                    showToast(getActivity(), "Please Enter Valid Phone Number");
                } else {
                    sendOtp();
                }
            }
        });
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtOtp.getText().toString().equalsIgnoreCase("")) {
                    showToast(getActivity(), "Please Enter OTP");
                } else {
                    submitOtp();
                }
            }
        });
        mBinding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendOtp() {
        if (isInternetAvailable(getActivity())) {
            mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().SendOtpLogin(paramSendOtp()).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            PreferenceHelper.putString(Constants.MOBILE_NUMBER, "+91" + mBinding.edtMobile.getText().toString());
                            mBinding.llMobile.setVisibility(View.GONE);
                            mBinding.llOtp.setVisibility(View.VISIBLE);
                            mBinding.tvSendOtp.setVisibility(View.GONE);
                            mBinding.tvLogin.setVisibility(View.VISIBLE);
                        } else {
                            Utils.showDialog(getActivity(), response.body().message + "");
                        }

                    } else {
                        Utils.showDialog(getActivity(), response.body().message + "");
                    }
                }

                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramSendOtp() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", "+91" + mBinding.edtMobile.getText().toString());
        return params;
    }

    private void submitOtp() {
        if (isInternetAvailable(getActivity())) {
            mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().VerifyOTPLogin(paramSubmitOtp()).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            PreferenceHelper.putBoolean(Constants.IS_LOGIN, true);
                            PreferenceHelper.putString(Constants.MOBILE_NUMBER, "+91" + mBinding.edtMobile.getText().toString());
                            PreferenceHelper.putString(Constants.LOGINTYPE, "guest");
                            Intent intent = new Intent(getActivity(), GMainActivity.class);
                            startActivity(intent);
                        } else {
                            Utils.showDialog(getActivity(), response.body().message + "");
                        }

                    } else {
                        Utils.showDialog(getActivity(), response.body().message + "");
                    }
                }

                @Override
                public void onFailure(Call<VerifyModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramSubmitOtp() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", PreferenceHelper.getString(Constants.MOBILE_NUMBER, ""));
        params.put("otp", mBinding.edtOtp.getText().toString());
        return params;
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
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finishAffinity();
        System.exit(0);
    }
}
