package com.thelawhouse.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Model.AddAppointmentModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.Constants;
import com.thelawhouse.Utils.PreferenceHelper;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.FragBroadcastBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;
import static com.thelawhouse.Utils.Utils.showToast;

public class FragBroadcast extends Fragment {
    private FragBroadcastBinding mBinding;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frag_broadcast, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.ClickcableTrue();
        onClickListener();
        getBroadCastMessage();
//        setData();
        return mBinding.getRoot();
    }

    private void getBroadCastMessage() {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().AddAppointment(paramGetBroadcast()).enqueue(new Callback<AddAppointmentModel>() {
                @Override
                public void onResponse(Call<AddAppointmentModel> call, Response<AddAppointmentModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        showToast(getActivity(), "Broadcast Message is updated.");
                    }
                }

                @Override
                public void onFailure(Call<AddAppointmentModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramGetBroadcast() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", PreferenceHelper.getString(Constants.USER_ID, ""));
        return params;
    }

    private void onClickListener() {
        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtBroadcast.getText().toString().equalsIgnoreCase("")) {
                    showToast(getActivity(), "Please Enter Broadcast Message.");
                } else {
                    updateBroadcastMessage();
                }
            }
        });
    }

    private void updateBroadcastMessage() {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().AddAppointment(paramNewMember()).enqueue(new Callback<AddAppointmentModel>() {
                @Override
                public void onResponse(Call<AddAppointmentModel> call, Response<AddAppointmentModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        showToast(getActivity(), "Broadcast Message is updated.");
                    }
                }

                @Override
                public void onFailure(Call<AddAppointmentModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(getActivity(), "Check Your Internet.");
        }
    }

    private Map<String, String> paramNewMember() {
        Map<String, String> params = new HashMap<>();
        params.put("appointment_time", mBinding.edtBroadcast.getText().toString());
        return params;
    }

    private Map<String, String> paramUpdate(String linkId) {
        Map<String, String> params = new HashMap<>();
        params.put("use_full_id", linkId);
        return params;
    }

    private Map<String, String> paramMyBooking(String page) {
        Map<String, String> params = new HashMap<>();
        params.put("page_id", page);
        params.put("limit", "15");
        Log.d("param", params.toString());
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
