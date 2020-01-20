package com.thelawhouse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.thelawhouse.Model.AddUsefullLinkModel;
import com.thelawhouse.Model.UsefullLinkListModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityAddUsefullLinkBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class AddUsefullLinkActivity extends AppCompatActivity {
    AddUsefullLinkActivity mContext = AddUsefullLinkActivity.this;
    ActivityAddUsefullLinkBinding mBinding;
    String linkId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_add_usefull_link);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            linkId = bundle.getString("linkId");
            getData(linkId);
            mBinding.tvSubmit.setVisibility(View.GONE);
            mBinding.tvUpdate.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvSubmit.setVisibility(View.VISIBLE);
            mBinding.tvUpdate.setVisibility(View.GONE);
        }
        onClickListener();
    }

    private void getData(String linkId) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().UsefullLinkDetails(paramUpdate(linkId)).enqueue(new Callback<UsefullLinkListModel>() {
                @Override
                public void onResponse(Call<UsefullLinkListModel> call, Response<UsefullLinkListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.edtLink.setText(response.body().use_full_link_data.get(0).content);
                            mBinding.edtTitle.setText(response.body().use_full_link_data.get(0).status);
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<UsefullLinkListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private void update(String linkId) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().UpdateUsefullLink(paramUpdate(linkId)).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            Intent intent = new Intent();
                            setResult(103, intent);
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

    private Map<String, String> paramUpdate(String linkId) {
        Map<String, String> params = new HashMap<>();
        params.put("use_full_link_id", linkId);
        params.put("use_link", mBinding.edtLink.getText().toString());
        params.put("title", mBinding.edtTitle.getText().toString());
        return params;
    }

    private void onClickListener() {
        mBinding.toolbar.ivNavigation.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mBinding.toolbar.ivNavigation.setEnabled(false);
        mBinding.toolbar.tvTitle.setText("Usefull Link");
        mBinding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtTitle.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter title");
                } else if (mBinding.edtLink.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter your Usefull Link");
                } else {
                    addUsefullLink();
                }
            }
        });
        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtTitle.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter title");
                } else if (mBinding.edtLink.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter your Usefull Link");
                } else {
                    update(linkId);
                }
            }
        });
    }

    private void addUsefullLink() {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().AddUsefullLink(paramNewCase()).enqueue(new Callback<AddUsefullLinkModel>() {
                @Override
                public void onResponse(Call<AddUsefullLinkModel> call, Response<AddUsefullLinkModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            Intent intent = new Intent();
                            setResult(101, intent);
                            finish();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddUsefullLinkModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramNewCase() {
        Map<String, String> params = new HashMap<>();
        params.put("use_link", mBinding.edtLink.getText().toString());
        params.put("title", mBinding.edtTitle.getText().toString());
        return params;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
