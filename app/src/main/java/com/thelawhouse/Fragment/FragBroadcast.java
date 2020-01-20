package com.thelawhouse.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.thelawhouse.Activity.MainActivity;
import com.thelawhouse.Model.BroadcastModel;
import com.thelawhouse.Model.BrodcastDetailModel;
import com.thelawhouse.R;
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
        mainActivity.broadcastTitle();
        onClickListener();
        getBroadCastMessage();
        return mBinding.getRoot();
    }

    private void getBroadCastMessage() {
        if (isInternetAvailable(getActivity())) {
            ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), true, true, false, null);
            WebApiClient.getInstance().BroadcastDetails(paramGetBroadcast()).enqueue(new Callback<BrodcastDetailModel>() {
                @Override
                public void onResponse(Call<BrodcastDetailModel> call, Response<BrodcastDetailModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        mBinding.edtBroadcast.setText(response.body().broadcast_data.get(0).broadcast_message);
                    }
                }

                @Override
                public void onFailure(Call<BrodcastDetailModel> call, Throwable t) {
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
        params.put("broadcast_id", "1");
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
            WebApiClient.getInstance().UpdateBroadcastDetails(paramNewMember()).enqueue(new Callback<BroadcastModel>() {
                @Override
                public void onResponse(Call<BroadcastModel> call, Response<BroadcastModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        Toast.makeText(getActivity(), "Broadcast Message is updated.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<BroadcastModel> call, Throwable t) {
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
        params.put("broadcast_message", mBinding.edtBroadcast.getText().toString());
        params.put("broadcast_id", "1");
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
