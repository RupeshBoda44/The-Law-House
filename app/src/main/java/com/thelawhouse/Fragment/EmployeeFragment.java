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

import com.thelawhouse.Activity.ForgotPWDActivity;
import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Model.LoginModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragmentEmployeeBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;
import static com.thelawhouse.Utils.Utils.showToast;


public class EmployeeFragment extends Fragment {
    private FragmentEmployeeBinding mBinding;
    private ProgressHUD mProgressHUD;

    public static EmployeeFragment newInstance() {
        return new EmployeeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_employee, container, false);
        onClickListener();
        return mBinding.getRoot();
    }

    private void onClickListener() {
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mBinding.tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForgotPWDActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        if (mBinding.edtMobile.getText().toString().equalsIgnoreCase("")) {
            showToast(getActivity(), "Please Enter Phone Number");
        } else if (mBinding.edtMobile.getText().toString().length() < 10) {
            showToast(getActivity(), "Please Enter Valid Phone Number");
        } else if (mBinding.edtPassword.getText().toString().equalsIgnoreCase("")) {
            showToast(getActivity(), "Please Enter your Password");
        } else {
            submitLogin();
        }
    }

    private void submitLogin() {
        if (isInternetAvailable(getActivity())) {
            mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().EmployeeLogin(paramLogin()).enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        PreferenceHelper.putBoolean(Constants.IS_LOGIN, true);
                        PreferenceHelper.putString(Constants.USER_ID, response.body().user.user_id);
                        PreferenceHelper.putString(Constants.NAME, response.body().user.name);
                        PreferenceHelper.putString(Constants.MOBILE_NUMBER, response.body().user.mobile_number);
                        PreferenceHelper.putString(Constants.EMAIL, response.body().user.email);
                        PreferenceHelper.putString(Constants.PASSWORD, response.body().user.password);
                        PreferenceHelper.putString(Constants.ORIGANAL_PASSWORD, response.body().user.origanal_password);
                        PreferenceHelper.putString(Constants.CREATED_DATE, response.body().user.created_date);
                        PreferenceHelper.putString(Constants.OTP, response.body().user.otp);
                        PreferenceHelper.putString(Constants.LOGINTYPE, "employee");
                        PreferenceHelper.putString(Constants.ADMIN_ACCESS, response.body().admin_access);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Utils.showDialog(getActivity(), response.body().message + "");
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramLogin() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", mBinding.edtMobile.getText().toString());
        params.put("password", mBinding.edtPassword.getText().toString());
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
