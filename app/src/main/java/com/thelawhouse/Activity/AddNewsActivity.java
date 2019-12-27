package com.thelawhouse.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.thelawhouse.Model.AddNewsModel;
import com.thelawhouse.Model.AddUsefullLinkModel;
import com.thelawhouse.Model.UsefullLinkListModel;
import com.thelawhouse.Model.VerifyModel;
import com.thelawhouse.R;
import com.thelawhouse.Utils.ProgressHUD;
import com.thelawhouse.Utils.TakePicture;
import com.thelawhouse.Utils.Utils;
import com.thelawhouse.Utils.WebApiClient;
import com.thelawhouse.databinding.ActivityAddNewsBinding;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.thelawhouse.Utils.Utils.isInternetAvailable;

public class AddNewsActivity extends AppCompatActivity {
    AddNewsActivity mContext = AddNewsActivity.this;
    ActivityAddNewsBinding mBinding;
    String linkId = "";
    private TakePicture mTakePicture;
    private String SELECTED_PATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.activity_add_news);
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
        mTakePicture = new TakePicture(mContext);
        onClickListener();
        checkPermission1();
    }

    private void checkPermission1() {
        Dexter.withActivity(mContext)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Utils.showSettingsDialog(mContext);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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
                            mBinding.edtNewsDetail.setText(response.body().use_full_link_data.get(0).content);
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
        params.put("use_full_link_id", linkId);
        params.put("use_link", mBinding.edtNewsDetail.getText().toString());
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
        mBinding.llPickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePic();
            }
        });
        mBinding.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtTitle.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter title");
                } else if (mBinding.edtNewsDetail.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter your Usefull Link");
                } else {
                    File file = new File(SELECTED_PATH);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("news_image", file.getName(), requestFile);
                    if (SELECTED_PATH.equals("")) {
                        editProfileWithoutImage(mBinding.edtTitle.getText().toString(), mBinding.edtNewsDetail.getText().toString());
                    } else {
                        editProfile(mBinding.edtTitle.getText().toString(), mBinding.edtNewsDetail.getText().toString(), body);
                    }
                }
            }
        });
        mBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtTitle.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter title");
                } else if (mBinding.edtNewsDetail.getText().toString().equalsIgnoreCase("")) {
                    Utils.showDialog(mContext, "Please enter your Usefull Link");
                } else {
                    update(linkId);
                }
            }
        });
    }

    private void editProfile(String title, String content, MultipartBody.Part body) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().AddNewsWithImg(title, content, body).enqueue(new Callback<AddNewsModel>() {
                @Override
                public void onResponse(Call<AddNewsModel> call, Response<AddNewsModel> response) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
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
                public void onFailure(Call<AddNewsModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private void editProfileWithoutImage(String title, String content) {
        if (isInternetAvailable(mContext)) {
            ProgressHUD mProgressHUD = ProgressHUD.show(mContext, true, true, false, null);
            WebApiClient.getInstance().AddNewsWithoutImg(paramWithoutImage(title, content)).enqueue(new Callback<AddNewsModel>() {
                @Override
                public void onResponse(Call<AddNewsModel> call, Response<AddNewsModel> response) {
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
                public void onFailure(Call<AddNewsModel> call, Throwable t) {
                    mProgressHUD.dismissProgressDialog(mProgressHUD);
                    Log.e("error", t.getMessage());
                }
            });
        } else {
            Utils.showDialog(mContext, "Check Your Internet.");
        }
    }

    private Map<String, String> paramWithoutImage(String title, String content) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("title", title);
        return params;
    }

    private void changeProfilePic() {
        Dexter.withActivity(mContext)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            selectProfilePic();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Utils.showSettingsDialog(mContext);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                SELECTED_PATH = mTakePicture.onActivityResult(requestCode, resultCode, data);
                Log.e("Tag", "Path: " + SELECTED_PATH);
                Glide.with(mContext).load(SELECTED_PATH).into(mBinding.ivNewsImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void selectProfilePic() {
        try {
            mTakePicture.selectImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
