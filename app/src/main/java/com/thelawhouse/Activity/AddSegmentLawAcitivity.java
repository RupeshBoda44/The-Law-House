package com.thelawhouse.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.thelawhouse.Model.AddSegementLawModel;
import com.thelawhouse.Model.SegmentLawListModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityAddSegmentLawAcitivityBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class AddSegmentLawAcitivity extends AppCompatActivity {
    AddSegmentLawAcitivity mContext = AddSegmentLawAcitivity.this;
    ActivityAddSegmentLawAcitivityBinding mBinding;
    String linkId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_add_segment_law_acitivity);
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
            WebApiClient.getInstance().SegmentLawDetails(paramGetData(linkId)).enqueue(new Callback<SegmentLawListModel>() {
                @Override
                public void onResponse(Call<SegmentLawListModel> call, Response<SegmentLawListModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            mBinding.edtContent.setText(response.body().segment_of_law_data.get(0).content);
                            mBinding.edtTitle.setText(response.body().segment_of_law_data.get(0).title);

                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<SegmentLawListModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramGetData(String linkId) {
        Map<String, String> params = new HashMap<>();
        params.put("segment_id", linkId);
        return params;
    }

    private void onClickListener() {
        mBinding.toolbar.ivNavigation.setColorFilter(getResources().getColor(R.color.colorPrimary));
        mBinding.toolbar.ivNavigation.setEnabled(false);
        mBinding.toolbar.tvTitle.setText(getResources().getString(R.string.segments_of_law_and_legal));
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
                    Utils.showDialog(mContext, "Please enter Title");
                }
                if (mBinding.edtContent.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter Content");
                } else {
                    addUsefullLink();
                }
            }
        });
        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtTitle.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter Title");
                }
                if (mBinding.edtContent.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter Content");
                } else {
                    update(linkId);
                }
            }
        });
    }

    private void deleteSagementLaw(String linkId) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().DeleteSegmentLaw(paramGetData(linkId)).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
//                            onBackPressed();
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

    private void addUsefullLink() {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().AddSegemetnLaw(paramNewCase()).enqueue(new Callback<AddSegementLawModel>() {
                @Override
                public void onResponse(Call<AddSegementLawModel> call, Response<AddSegementLawModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            onBackPressed();
                        } else {
                            Utils.showDialog(mContext, response.body().message + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddSegementLawModel> call, Throwable t) {
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
            WebApiClient.getInstance().UpdateSegementLaw(paramUpdate(linkId)).enqueue(new Callback<VerifyModel>() {
                @Override
                public void onResponse(Call<VerifyModel> call, Response<VerifyModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("Response :", response.message() + "");
                    if (response.code() == 200) {
                        Log.e("response", response.body() + "");
                        assert response.body() != null;
                        if (response.body().message.equalsIgnoreCase("success")) {
                            onBackPressed();
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
        params.put("segment_id", linkId);
        params.put("title", mBinding.edtTitle.getText().toString());
        params.put("content", mBinding.edtContent.getText().toString());
        return params;
    }

    private Map<String, String> paramNewCase() {
        Map<String, String> params = new HashMap<>();
        params.put("title", mBinding.edtTitle.getText().toString());
        params.put("content", mBinding.edtContent.getText().toString());
        return params;
    }

}
